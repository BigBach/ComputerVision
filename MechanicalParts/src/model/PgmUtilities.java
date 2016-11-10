package model;

import java.io.*;
import java.util.StringTokenizer;

/**
 *
 * @author Markenos & BigBach
 */
public class PgmUtilities {

    static int numLines = 0;

    /**
     * 
     * @param width of the image
     * @param height of the image
     * @param max_val pixel of the image
     * @return a new empty pgm image
     */
    public PGM newPGM(int width, int height, int max_val) {
        
        return new PGM(width, height, max_val);
    }

    /**
     * 
     * @param br the buffered reader
     * @return the buffer wich contains the commented lines
     * @throws IOException 
     */
    public String skipComments(BufferedReader br) throws IOException {
        
        boolean loop = true;
        String buffer = br.readLine();

        while (loop) {
            if (buffer.charAt(0) != '#') {
                loop = false;
            } else {
                buffer = br.readLine();
                numLines++;
            }
        }

        return buffer;
    }

    /**
     * 
     * @param pgm the image in PGM format
     * 
     * This method set to zero all the pixels of a pgm image
     */
    public void resetPGM(PGM pgm) {
        
        int width = pgm.getWidth();
        int height = pgm.getHeight();
        int i;

        // set to zero all the pixels
        for (i = 0; i < width * height; i++) {
            pgm.getPixels()[i] = 0;
        }
    }
 
    /**
     * 
     * @param filename the path of the image that must be read
     * @return the PGM image
     * 
     * This method read pixels from different filetype
     */
    public PGM readPGM(String filename) {
        
        int width, height, max_val;
        boolean binary;

        PGM pgm;
        numLines = 3; // default number of lines of header

        try {
            FileInputStream fstream = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String buffer;
            // Read a line till \n or 64 char
            buffer = br.readLine();

            if ("P2".equals(buffer)) {
                binary = false;
                System.out.println("\nFORMAT: P2");
            } else if ("P5".equals(buffer)) {
                binary = true;
                System.out.println("\nFORMAT: P5");
            } else {
                System.err.println("ERROR: incorrect file format\n");
                in.close();
                return null;
            }

            // Jump commented lines
            buffer = skipComments(br);

            // Read width, height and max grayscale value
            StringTokenizer st = new StringTokenizer(buffer);
            width = Integer.parseInt(st.nextToken());
            height = Integer.parseInt(st.nextToken());

            buffer = br.readLine();
            max_val = Integer.parseInt(buffer);

            // Printing information on screen
            System.out.println("\nPGM Filename: " + filename + "\nPGM Width & Height: " + width + "," + height + 
                    "\nPGM Max Val & Type: " + max_val + "," + (binary ? "P5" : "P2") + "\n");

            // Initialize PGM
            pgm = newPGM(width, height, max_val);

            // Reading Pixels
            if (binary) // P5 case
            {

                br.close();
                fstream = new FileInputStream(filename);
                in = new DataInputStream(fstream);

                int numLinesToSkip = numLines;
                System.out.println(numLinesToSkip);
                while (numLinesToSkip > 0) {
                    char c;
                    do {
                        c = (char) (in.readUnsignedByte());
                    } while (c != '\n');
                    numLinesToSkip--;
                }

                int num;
                int x = 0;

                while ((num = in.read()) != -1) {
                    pgm.getPixels()[x] = num;
                    x++;
                }
            } else // P2 case
            {
                int i = 0;
                while ((buffer = br.readLine()) != null) {
                    st = new StringTokenizer(buffer);
                    while (st.hasMoreTokens()) {
                        pgm.getPixels()[i] = Integer.parseInt(st.nextToken());
                        i++;
                    }
                }
            }

            // Ok close the file
            in.close();

            System.out.println("\nImage correctly loaded");

            return pgm;
        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
            return null;
        } catch (IOException ex) {
            System.out.println("IOException. Please check file.");
            return null;
        }
    }

    /**
     * 
     * @param pgm the image in PGM format
     * @param filename the path where the image has to be saved
     * 
     * This method write pixels inside images for different filetype
     */
    public void writePGM(PGM pgm, String filename) {
        
        if (pgm == null) {
            System.err.println("Error! No data to write. Please Check.");
            return;
        }

        FileWriter fstream;
        try {
            fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write("P2\n" + pgm.getWidth() + " " + pgm.getHeight() + "\n" + pgm.getMax_val() + "\n");

            int i;
            int width = pgm.getWidth();
            int height = pgm.getHeight();

            // Write image
            for (i = 0; i < width * height; i++) {
                out.write(pgm.getPixels()[i] + "\n");
            }

            System.out.println("\nImage correctly writed");

            // Ok close the file
            out.close();

        } catch (IOException ex) {
            System.err.println("\nIOException. Check input Data.");
        }
    }

    /**
     * 
     * @param pgmIn the input PGM image
     * @return the output PGM image after the inversion
     * 
     * This method inverts pixels grayscale value inside images for different filetype
     */
    public PGM invertPGM(PGM pgmIn) {
        
        if (pgmIn == null) {
            System.err.println("Error! No input data. Please Check.");
            return null;
        }

        PGM pgmOut = new PGM(pgmIn.getWidth(), pgmIn.getHeight(), pgmIn.getMax_val());
        int i, inv;
        int max = pgmIn.getMax_val();
        int width = pgmIn.getWidth();
        int height = pgmIn.getHeight();

        // Writing Pixels
        for (i = 0; i < width * height; i++) {
            // Invert GrayScale Value
            inv = max - pgmIn.getPixels()[i];
            pgmOut.getPixels()[i] = inv;
        }

        return pgmOut;
    }

    /**
     * 
     * @param pgmIn the input PGM image
     * @return the output PGM image after the flip
     * 
     * This method flips image horizontally
     */
    public PGM hflipPGM(PGM pgmIn) {
        
        if (pgmIn == null) {
            System.err.println("Error! No input data. Please Check.");
            return null;
        }

        PGM pgmOut = new PGM(pgmIn.getWidth(), pgmIn.getHeight(), pgmIn.getMax_val());

        int i, j;
        int hfp;

        int width = pgmIn.getWidth();
        int height = pgmIn.getHeight();

        int[] inputPixels = pgmIn.getPixels();
        int[] flipPixels = new int[width * height];

        // Modify Pixels
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                // Flip GrayScale Value on width
                hfp = inputPixels[i * width + j];
                flipPixels[i * width + (width - j - 1)] = hfp;
            }
        }

        pgmOut.setPixels(flipPixels);

        return pgmOut;
    }

    /**
     * 
     * @param pgmIn the input PGM image
     * @return the output PGM image after the copy
     * 
     * This method copies a PGM image
     */
    public PGM copyPGM(PGM pgmIn) {
        
        if (pgmIn == null) {
            System.err.println("Error! No input data. Please Check.");
            return null;
        }
        PGM pgmOut = new PGM(pgmIn.getWidth(), pgmIn.getHeight(), pgmIn.getMax_val());

        int i;

        int width = pgmIn.getWidth();
        int height = pgmIn.getHeight();

        int[] inPixels = pgmIn.getPixels();
        int[] outPixels = new int[width * height];

        // Copy image
        for (i = 0; i < width * height; i++) {
            // Copy image
            outPixels[i] = inPixels[i];
        }

        pgmOut.setPixels(outPixels);

        return pgmOut;
    }

    /**
     * 
     * @param pgm the input PGM image
     * @return the histogram of the input PGM image
     * 
     * This method calculates the histogram
     */
    public int[] histogramPGM(PGM pgm) {
        
        if (pgm == null) {
            System.err.println("Error! No input data. Please Check.");
            return null;
        }

        int i, index;

        int[] inPixels = pgm.getPixels();
        int width = pgm.getWidth();
        int height = pgm.getHeight();
        int max_val = pgm.getMax_val();

        // if max_val is 255 each pixel of the image can have a value between [0;255]
        // so histogram have a dimension of 256
        int[] histogram = new int[max_val + 1];

        for (i = 0; i < width * height; i++) {
            index = inPixels[i];
            histogram[index]++;
        }

        return histogram;
    }

    public PGM filterConvolution(Filter filter) {

        return null;
    }
}

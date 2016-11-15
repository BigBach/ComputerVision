package model;

import java.io.*;
import java.util.StringTokenizer;
import model.exceptions.InvalidPixelMatrixSizeException;

/**
 *
 * @author Marco Robutti - Filippo Cipolla
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

        // set to the 0 all the pixels
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
            System.out.println("\nPGM Filename: " + filename + "\nPGM Width & Height: " + width + "," + height
                    + "\nPGM Max Val & Type: " + max_val + "," + (binary ? "P5" : "P2") + "\n");

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
                    pgm.setPixel(x, num);
                    x++;
                }
            } else // P2 case
            {
                int i = 0;
                while ((buffer = br.readLine()) != null) {
                    st = new StringTokenizer(buffer);
                    while (st.hasMoreTokens()) {
                        pgm.setPixel(i, Integer.parseInt(st.nextToken()));
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
     * This method inverts pixels grayscale value inside images for different
     * filetype
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
     * @throws model.exceptions.InvalidPixelMatrixSizeException
     *
     * This method flips image horizontally
     */
    public PGM hflipPGM(PGM pgmIn) throws InvalidPixelMatrixSizeException {

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
     * @throws model.exceptions.InvalidPixelMatrixSizeException
     *
     * This method copies a PGM image
     */
    public PGM copyPGM(PGM pgmIn) throws InvalidPixelMatrixSizeException {

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

    /**
     *
     * @param inputPGM the input PGM image
     * @param filter to apply for the output image
     * @return the PGM image with the application of convolution
     * @throws InvalidPixelMatrixSizeException
     */
    public PGM filterConvolution(PGM inputPGM, Filter filter) throws InvalidPixelMatrixSizeException {

        int[] outPixels = convolution(inputPGM, filter);
        int outputPgmWidth = inputPGM.getWidth() - filter.getSize() + 1;
        int outputPgmHeigth = inputPGM.getHeight() - filter.getSize() + 1;

        PGM outputPgm = new PGM(outputPgmWidth, outputPgmHeigth, inputPGM.getMax_val());
        outputPgm.setPixels(outPixels);

        return outputPgm;
    }

    /**
     *
     * @param inputPGM the input PGM image
     * @param moduleThreshold for thresholding the module of image after the
     * application of the isotropic filter
     * @return two images: one for the module of isotropic filter and the other
     * for the phase
     * @throws InvalidPixelMatrixSizeException
     */
    public PGM[] isotropicFilter(PGM inputPGM, int moduleThreshold, int phaseThreshold) throws InvalidPixelMatrixSizeException {

        Filter isotropicHorizontal = new Filter(new double[]{-1, 0, 1, -Math.sqrt(2), 0, Math.sqrt(2), -1, 0, 1});
        Filter isotropicVertical = new Filter(new double[]{1, Math.sqrt(2), 1, 0, 0, 0, -1, -Math.sqrt(2), -1});

        int outputPgmWidth = inputPGM.getWidth() - isotropicHorizontal.getSize() + 1;
        int outputPgmHeigth = inputPGM.getHeight() - isotropicHorizontal.getSize() + 1;
        int[] horizontalFilteredPixels = convolution(inputPGM, isotropicHorizontal);
        int[] verticalFilteredPixels = convolution(inputPGM, isotropicVertical);
        int[] modulePixels = new int[horizontalFilteredPixels.length];

        for (int i = 0; i < modulePixels.length; i++) {
            modulePixels[i] = (int) Math.sqrt(Math.pow(horizontalFilteredPixels[i], 2) + Math.pow(verticalFilteredPixels[i], 2));
        }

        modulePixels = rescalePixels(modulePixels, minPixel(modulePixels), maxPixel(modulePixels));
        modulePixels = thresholdPixels(modulePixels, moduleThreshold, 0, inputPGM.getMax_val());

        PGM modulePgm = new PGM(outputPgmWidth, outputPgmHeigth, inputPGM.getMax_val());
        modulePgm.setPixels(modulePixels);

        double[] phasePixels = new double[horizontalFilteredPixels.length];
        for (int i = 0; i < phasePixels.length; i++) {
            phasePixels[i] = (Math.atan2((double) verticalFilteredPixels[i], (double) horizontalFilteredPixels[i]));
        }
        double minPhasePixel = minPixel(phasePixels);
        double maxPhasePixel = maxPixel(phasePixels);
        phaseThreshold = rescalePixel(phaseThreshold, minPhasePixel, maxPhasePixel);
        int[] intPhasePixels = rescalePixels(phasePixels, minPhasePixel, maxPhasePixel);
        intPhasePixels = thresholdPixels(intPhasePixels, phaseThreshold, 0, inputPGM.getMax_val());
        PGM phasePgm = new PGM(outputPgmWidth, outputPgmHeigth, inputPGM.getMax_val());
        phasePgm.setPixels(intPhasePixels);

        PGM[] outputPgms = new PGM[2];
        outputPgms[0] = modulePgm;
        outputPgms[1] = phasePgm;

        return outputPgms;
    }

    /**
     *
     * @param inputPgm the input PGM image
     * @param filter to apply for the output image
     * @return the pixels of the image with the application of convolution
     * operator
     */
    private int[] convolution(PGM inputPgm, Filter filter) {
        int outputWidth = inputPgm.getWidth() - filter.getSize() + 1;
        int outputHeigth = inputPgm.getHeight() - filter.getSize() + 1;
        int[] outPixels = new int[outputWidth * outputHeigth];
        int x = 0;
        for (int i = 0; i < ((inputPgm.getPixels().length - 1) - (filter.getSize() * inputPgm.getWidth() - 1) - (filter.getSize() - 1)); i++) {
            int pixel = 0;
            for (int j = 0; j < filter.getValues().length; j++) {
                pixel += inputPgm.getPixels()[i] * filter.getValues()[j];
                if (((j + 1) % 3) == 0) {
                    i += inputPgm.getWidth() - (filter.getSize() - 1);
                } else {
                    i++;
                }
            }
            outPixels[x] = pixel;
            x++;
            i = i - (filter.getSize() - 1) - (filter.getSize() - 1) * inputPgm.getWidth();
            if ((((i + (filter.getSize() - 1)) + 1) % inputPgm.getWidth()) == 0) {
                i += (filter.getSize() - 1); //-1 because the for cycle will increment i of 1...
            } else {
                i++;
            }
        }
        return outPixels;
    }

    private int[] rescalePixels(int[] pixels, int min, int max) {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = (int) ((255 * (pixels[i] - min)) / (max - min));
        }

        return pixels;
    }

    private int[] rescalePixels(double[] pixels, double min, double max) {
        int[] intPixels = new int[pixels.length];
        for (int i = 0; i < intPixels.length; i++) {
            intPixels[i] = (int) ((255 * (pixels[i] - min)) / (max - min));
        }

        return intPixels;
    }

    private int rescalePixel(int pixel, int min, int max) {

        return (int) ((255 * (pixel - min)) / (max - min));
    }

    private int rescalePixel(int pixel, double min, double max) {

        return (int) ((255 * ((double) ((pixel * Math.PI) / 180) - min)) / (max - min));
    }

    private int[] thresholdPixels(int[] pixels, int threshold, int minPixel, int maxPixel) {

        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] > threshold) {
                pixels[i] = maxPixel;
            } else {
                pixels[i] = minPixel;
            }
        }

        return pixels;
    }

    private int maxPixel(int[] pixels) {

        int max = pixels[0];

        for (int i = 1; i < pixels.length; i++) {
            if (pixels[i] > max) {
                max = pixels[i];
            }
        }

        return max;
    }

    private double maxPixel(double[] pixels) {

        double max = pixels[0];

        for (int i = 1; i < pixels.length; i++) {
            if (pixels[i] > max) {
                max = pixels[i];
            }
        }

        return max;
    }

    private int minPixel(int[] pixels) {

        int min = pixels[0];

        for (int i = 1; i < pixels.length; i++) {
            if (pixels[i] < min) {
                min = pixels[i];
            }
        }

        return min;
    }

    private double minPixel(double[] pixels) {

        double min = pixels[0];

        for (int i = 1; i < pixels.length; i++) {
            if (pixels[i] < min) {
                min = pixels[i];
            }
        }

        return min;
    }

//    for (int i = ((filter.getSize() - 1) / 2); i < (inputPgm.getHeight() - ((filter.getSize() - 1) / 2)); i++) {
//            for (int j = ((filter.getSize() - 1) / 2); j < (inputPgm.getWidth() - ((filter.getSize() - 1) / 2)); j++) {
//                int pixel = 0;
//                for (int a = i - ((filter.getSize() - 1) / 2); a < (i - ((filter.getSize() - 1) / 2) + filter.getSize() - 1); a++) {
//                    for (int b = j - ((filter.getSize() - 1) / 2); b < (j - ((filter.getSize() - 1) / 2) + filter.getSize() - 1); b++) {
//                        pixel += outPixels[]
//                    }
//                }
//            }
//        }
}

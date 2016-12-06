package model;

import gui.MainFrame;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import model.exceptions.InexistingPointException;
import model.exceptions.InvalidPixelMatrixSizeException;

/**
 *
 * @author Markenos
 */
public class ImageProcessingUtilities extends Observable {

    /**
     * Performs the filter convolution on an input PGM image. You can decice
     * also the modality of applicazion of the filter setting the parameter
     * extendModality != null
     *
     * @param inputPGM the input PGM image
     * @param filter to apply for the output image
     * @param extendModality one of possibly modality used to apply the filter
     * to the image. All the possible values are defined in the
     * ConvolutionBorders class.
     * @return the PGM image with the application of convolution
     * @throws InvalidPixelMatrixSizeException
     * @see ConvolutionBorders
     */
    public PGM filterConvolution(PGM inputPGM, Filter filter, ConvolutionBorders extendModality) throws InvalidPixelMatrixSizeException {
        PGM outputPgm = null;
        //By default, if the extendModality method is null, it will be set to ConvolutionBorders.NONE.
        if (extendModality == null) {
            extendModality = ConvolutionBorders.NONE;
        }
        if (extendModality == ConvolutionBorders.NONE) {
            PixelMatrixInteger resultPixelMatrix = convolution(inputPGM.getPixelMatrix().toPixelMatrixDouble(), filter).toPixelMatrixInteger();
            outputPgm = new PGM(resultPixelMatrix.getWidth(), resultPixelMatrix.getHeigth(), inputPGM.getMax_val());
            outputPgm.setPixels(resultPixelMatrix);
        } else if (extendModality == ConvolutionBorders.EXTEND_WITH_ZERO) {
            PixelMatrixInteger resultPixelMatrix = convolution((PixelMatrixDouble) inputPGM.getPixelMatrix().toPixelMatrixDouble().extendsBordersWithZero((filter.getSize() - 1) / 2), filter).toPixelMatrixInteger();
            outputPgm = new PGM(resultPixelMatrix.getWidth(), resultPixelMatrix.getHeigth(), inputPGM.getMax_val());
            outputPgm.setPixels(resultPixelMatrix);
        }

        return outputPgm;
    }

    /**
     * Apply the isotropic filter to a PGM image giving to the method a
     * threshold for the module matrix and for the phase matrix resulted from
     * the application of the isotropic filter.
     *
     * @param inputPGM the input PGM image
     * @param moduleThreshold for thresholding the module of image after the
     * application of the isotropic filter
     * @param phaseThreshold the values of the gradient of the image (expressed
     * in radiants)
     * @return two images: one for the module of isotropic filter and the other
     * for the phase
     * @throws InvalidPixelMatrixSizeException
     */
    public PGM[] isotropicFilter(PGM inputPGM, int moduleThreshold, int phaseThreshold, ConvolutionBorders extendModality) throws InvalidPixelMatrixSizeException {
        
        //Obtains the module and phase pixel matrixes
        PixelMatrixDouble[] matrixes = isotropicFilterPixelMatrixes(inputPGM.getPixelMatrix().toPixelMatrixDouble(), extendModality);
        PixelMatrixDouble modulePixelMatrix = matrixes[0];
        PixelMatrixDouble phasePixelMatrix = matrixes[1];

        //We must rescale the module pixel matrix into a range of values from 0 to 255, and then tresholding it with moduleTreshold
        modulePixelMatrix.rescalePixels(modulePixelMatrix.getMinPixelValue(), modulePixelMatrix.getMaxPixelValue(), 0.0, 255.0);
        modulePixelMatrix.thresholdPixels((double) moduleThreshold, 0.0, 255.0);

        //We can then create the PGM image for the module
        PGM modulePgm = new PGM(modulePixelMatrix.getWidth(), modulePixelMatrix.getHeigth(), inputPGM.getMax_val());
        modulePgm.setPixels(modulePixelMatrix.toPixelMatrixInteger());

        //We must convert the input phaseTreshold (that is expressed in degrees) in radiants.
        //Then we must tresholds the phase pixel matrix with the phaseTreshold (notes that the values stored
        //in the phase pixel matrix are from -Math.PI to Math.PI...).
        //Then AFTER the treshold step we must rescale the phase pixel matrix into a range of values from 0 to 255.
        //The we convert the phase pixel matrix in a phasePixelMatrixInteger and we create the PGM image for the phase
        double phaseThresholdDouble = (phaseThreshold * Math.PI) / 180;
        phasePixelMatrix.thresholdPixels(phaseThresholdDouble, -Math.PI, Math.PI);
        phasePixelMatrix.rescalePixels(-Math.PI, Math.PI, 0.0, 255.0);
        PixelMatrixInteger phasePixelMatrixInteger = phasePixelMatrix.toPixelMatrixInteger();
        PGM phasePgm = new PGM(phasePixelMatrixInteger.getWidth(), phasePixelMatrixInteger.getHeigth(), inputPGM.getMax_val());
        phasePgm.setPixels(phasePixelMatrixInteger);

        PGM[] outputPgms = new PGM[2];
        outputPgms[0] = modulePgm;
        outputPgms[1] = phasePgm;

        return outputPgms;
    }

    /**
     * Apply the isotropic filter to a input pixel matrix. The resulting module
     * and phase matrix are not threshold
     *
     * @param inputPixelMatrix the input matrix of pixel of the image
     * @return two PixelMatrixDouble object: one for the module of the isotropic
     * filter and one for the phase.
     */
    public PixelMatrixDouble[] isotropicFilterPixelMatrixes(PixelMatrixDouble inputPixelMatrix, ConvolutionBorders extendModality) {

        //The two filter to apply to the input pixel matrix
        Filter isotropicHorizontal = new Filter(new double[]{-1, 0, 1, -Math.sqrt(2), 0, Math.sqrt(2), -1, 0, 1});
        Filter isotropicVertical = new Filter(new double[]{1, Math.sqrt(2), 1, 0, 0, 0, -1, -Math.sqrt(2), -1});
        
        //By default, if the extendModality method is null, it will be set to ConvolutionBorders.NONE.
        if (extendModality == null) {
            extendModality = ConvolutionBorders.NONE;
        }
        if (extendModality == ConvolutionBorders.NONE) {
            //Do nothing
        } else if (extendModality == ConvolutionBorders.EXTEND_WITH_ZERO) {
            inputPixelMatrix = (PixelMatrixDouble) inputPixelMatrix.extendsBordersWithZero((isotropicHorizontal.getSize()-1)/2);
        }
        
        //The resulting of the filtering: the horizontal and vertical gradient
        PixelMatrixDouble horizontalFilteredPixelMatrix = convolution(inputPixelMatrix, isotropicHorizontal);
        PixelMatrixDouble verticalFilteredPixelMatrix = convolution(inputPixelMatrix, isotropicVertical);
        
        //We determine the values for the module pixel matrix
        PixelMatrixDouble modulePixelMatrix = new PixelMatrixDouble(Double.class, horizontalFilteredPixelMatrix.getWidth(), horizontalFilteredPixelMatrix.getHeigth());
        for (int i = 0; i < modulePixelMatrix.getMatrix().length; i++) {
            modulePixelMatrix.getMatrix()[i] = Math.sqrt(Math.pow(horizontalFilteredPixelMatrix.getMatrix()[i], 2) + Math.pow(verticalFilteredPixelMatrix.getMatrix()[i], 2));
        }

        //We determine the values for the phase pixel matrix
        PixelMatrixDouble phasePixelMatrix = new PixelMatrixDouble(Double.class, modulePixelMatrix.getWidth(), modulePixelMatrix.getHeigth());
        for (int i = 0; i < phasePixelMatrix.getMatrix().length; i++) {
            phasePixelMatrix.getMatrix()[i] = (Math.atan2((double) verticalFilteredPixelMatrix.getMatrix()[i], (double) horizontalFilteredPixelMatrix.getMatrix()[i]));
        }

        //We return the two matrixes: module and phase
        PixelMatrixDouble[] matrixes = new PixelMatrixDouble[2];
        matrixes[0] = modulePixelMatrix;
        matrixes[1] = phasePixelMatrix;
        return matrixes;
    }

    /**
     * This method makes the circle Hough transform on a input PGM image in order to detect circles
     * into the selected image
     *
     * @param moduleThreshold the threshold that we want to apply to the module
     * pixel matrix
     * @param rayMin the minimum ray of the circle that we want to detect (expressed in pixel)
     * @param rayMax the maximum ray of the circle that we want to detect (expressed in pixel)
     * @param maxNumberOfCircle the maximum number of circle that we want to
     * detect
     * @param inputPgm the input image
     * @throws IOException
     * @throws InvalidPixelMatrixSizeException
     * @throws InexistingPointException
     */
    public HashMap<Point, Integer> houghTransformCircle(int moduleThreshold, double minRatio, int rayMin, int rayMax, int maxNumberOfCircle, PGM inputPgm, ConvolutionBorders extendModality) throws IOException, InvalidPixelMatrixSizeException, InexistingPointException {
        
        double milliseconds = System.currentTimeMillis();
        
        //We obtain the module and phase pixel matrix as the result of the applicazion of the isotropic filter on the pixel matrix of the original image.
        PixelMatrixDouble[] isotropicResultMatrixes = isotropicFilterPixelMatrixes(inputPgm.getPixelMatrix().toPixelMatrixDouble(),extendModality);
        PixelMatrixDouble modulePixelMatrix = isotropicResultMatrixes[0];
        PixelMatrixDouble phasePixelMatrix = isotropicResultMatrixes[1];

        //We must rescale the module pixel matrix into a range of values from 0 to 255, and then tresholding it with moduleTreshold
        modulePixelMatrix.rescalePixels(modulePixelMatrix.getMinPixelValue(), modulePixelMatrix.getMaxPixelValue(), 0.0, 255.0);
        modulePixelMatrix.thresholdPixels((double) moduleThreshold, 0.0, 255.0);
        
        
        //This block of code populate the accumulation matrix of the centers
        double rayStep = 0.05;
        int width = modulePixelMatrix.getWidth();
        int heigth = modulePixelMatrix.getHeigth();
        int[][] accumulationMatrix = new int[heigth][width];
        int xi, yi, xc1, yc1;
        //For every point of the module pixel matrix which value is > 0
        for (int i = 0; i < modulePixelMatrix.getMatrix().length; i++) {
            if (modulePixelMatrix.getMatrix()[i] > 0) {
                double theta = phasePixelMatrix.getMatrix()[i];
                xi = i - ((int) (i / modulePixelMatrix.getWidth())) * modulePixelMatrix.getWidth();
                yi = (int) (i / modulePixelMatrix.getWidth());
                //For every circle of ray "r" that pass from the point P(xi,yi) and that is in the gradient's direction...
                for (double r = rayMin; r < rayMax; r = r + rayStep) {
                    xc1 = (int) Math.round(xi - r * Math.cos(Math.PI - theta));
                    yc1 = (int) Math.round(yi - r * Math.sin(Math.PI - theta));
                    if ((xc1 >= 0) && (xc1 < phasePixelMatrix.getWidth()) && (yc1 >= 0) && (yc1 < phasePixelMatrix.getHeigth())) {
                        accumulationMatrix[(int) yc1][(int) xc1] += 1;
                    }
                }
            }
        }

        //This block of code converts the PGM image in a JPG in order to use it in a panel for 
        //drawing on it the circles that have been recognized.
//        PGM inputPGMResized = filterConvolution(inputPgm, new Filter(new double[]{0, 0, 0, 0, 1, 0, 0, 0, 0}), extendModality);
//        BufferedImage im = new BufferedImage(inputPGMResized.getWidth(), inputPGMResized.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//        WritableRaster raster = im.getRaster();
//        for (int h = 0; h < inputPGMResized.getHeight(); h++) {
//            for (int w = 0; w < inputPGMResized.getWidth(); w++) {
//                raster.setSample(w, h, 0, inputPGMResized.getPixels()[h * inputPGMResized.getWidth() + w]);
//            }
//        }
//        ByteArrayOutputStream inputImageJpg = new ByteArrayOutputStream();
//        javax.imageio.ImageIO.write(im, "jpg", inputImageJpg);
//        OutputStream outputStream = new FileOutputStream("./images/test.jpg");
//        inputImageJpg.writeTo(outputStream);
//        MainFrame mainFrame = new MainFrame("./images/test.jpg");
//        this.addObserver((Observer) mainFrame.getDrawingPanel());
        
        //This block of code extract the maximum points from the acccumulation matrix, keeping attention to the fact that
        //two maximum points cannot be nearest each others than a distance of "exclusionRay".
        //This last control is performed because without it the maximum points that are found are too closer each other (that if you have set
        //maxNumberOfCircle to 10, you don't obtain the centers of 10 circles, but only 10 points closer each other in which one of them is the 
        //center of the circle with the maximum ray).
        int exclusionRay = (int) Math.round(rayMax/2);
        ArrayList<Point> maxPoints = new ArrayList<Point>();
        int k = 0;
        //For each circle we want to detect
        while (k < maxNumberOfCircle) {
            int max = 0;
            Point maxPoint = null;
            //We cycle on all the accumulation matrix...
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < heigth; j++) {
                    if (accumulationMatrix[j][i] > max) {
                        boolean isNearFromMaxs = false;
                        //We must control that the point isn't closer to one of the 
                        //maximum point previously detected...
                        for (Point point : maxPoints) {
                            if ((point.getX() != i) && (point.getY() != j)) {
                                if (((Math.pow(i - point.getX(), 2)) + Math.pow(j - point.getY(), 2)) <= Math.pow(exclusionRay, 2)) {
                                    isNearFromMaxs = true;
                                }
                            } else {
                                isNearFromMaxs = true;
                            }
                        }
                        if (!isNearFromMaxs) {
                            max = accumulationMatrix[j][i];
                            maxPoint = new Point(i, j);
                        }
                    }
                }
            }
            maxPoints.add(maxPoint);
            k += 1;
        }

        //At the start of this step we have centers of circles but not the circles!
        //The following block of code determine which is the ray of the circle related to each center point.
        //It does this by fouding the first circle that matches a lot of its points on the module pixel matrix, and saving the ray.
        HashMap<Point, Integer> pointRayMap = new HashMap<Point, Integer>();
        for (Point maxPoint : maxPoints) {
            int ray = 0;
            for (int r = rayMin; r < rayMax; r++) {
                int xmin, xmax, matched, total;
                matched = 0;
                total = 0;
                if ((maxPoint.getX() - r) < 0) {
                    xmin = 0;
                } else {
                    xmin = maxPoint.getX() - r;
                }
                if ((maxPoint.getX() + r) >= width) {
                    xmax = width;
                } else {
                    xmax = maxPoint.getX() + r;
                }
                for (; xmin < xmax; xmin++) {
                    total += 2;
                    double b, c;
                    int y1, y2;
                    b = -2 * maxPoint.getY();
                    c = Math.pow(maxPoint.getY(), 2) + Math.pow(xmin - maxPoint.getX(), 2) - Math.pow(r, 2);
                    y1 = (int) Math.round((-b + Math.sqrt(Math.pow(b, 2) - 4 * c)) / 2);
                    y2 = (int) Math.round((-b - Math.sqrt(Math.pow(b, 2) - 4 * c)) / 2);
                    if (y1 < 0) {
                        y1 = 0;
                    }
                    if (y1 >= heigth) {
                        y1 = heigth - 1;
                    }
                    if (y2 < 0) {
                        y2 = 0;
                    }
                    if (y2 >= heigth) {
                        y2 = heigth - 1;
                    }
                    if (modulePixelMatrix.getMatrix()[y1 * modulePixelMatrix.getWidth() + xmin] > 0) {
                        matched += 1;
                    }
                    if (modulePixelMatrix.getMatrix()[y2 * modulePixelMatrix.getWidth() + xmin] > 0) {
                        matched += 1;
                    }
                }
                double ratio = 0;
                if (total != 0) {
                    ratio = (double) ((double) matched / (double) total);
                }
                if (ratio > minRatio) {
                    ray = r;
                    break;
                }
            }
            pointRayMap.put(maxPoint, ray);
        }
        milliseconds = System.currentTimeMillis() - milliseconds;
        System.out.println("Milliseconds = " + milliseconds);
        return pointRayMap;
    }

    
    /*This method calculate the convolution between an input pixel matrix and an input filter.
    */
    private PixelMatrixDouble convolution(PixelMatrixDouble inputPixelMatrix, Filter filter) {
        //The output pixel matrix will have a size of (inputPixelMatrix().getWidth/getHeigth - filter.getSize() + 1)
        //That is it is smaller than the input image depending from the filter size!
        PixelMatrixDouble outPixelMatrix = new PixelMatrixDouble(Double.class, inputPixelMatrix.getWidth() - filter.getSize() + 1,
                inputPixelMatrix.getHeigth() - filter.getSize() + 1);
        int x = 0;
        for (int i = 0; i < (inputPixelMatrix.getMatrix().length - (filter.getSize() - 1) * inputPixelMatrix.getWidth() - (filter.getSize() - 1)); i++) {
            double pixel = 0;
            for (int j = 0; j < filter.getValues().length; j++) {
                pixel += inputPixelMatrix.getMatrix()[i] * filter.getValues()[j];
                if (((j + 1) % 3) == 0) {
                    i += inputPixelMatrix.getWidth() - (filter.getSize() - 1);
                } else {
                    i++;
                }
            }
            outPixelMatrix.getMatrix()[x] = pixel;
            x++;
            i = i - filter.getSize() * inputPixelMatrix.getWidth(); //+1 is implicit because the for cycle will increment i of 1 at the and of the cycle...
            if ((((i + (filter.getSize() - 1)) + 1) % inputPixelMatrix.getWidth()) == 0) {
                i += (filter.getSize() - 1); //-1 because the for cycle will increment i of 1...
            }
        }
        return outPixelMatrix;
    }

    //This class is used within the container class in order to simplify managing of point in the Hough transform method.
    public class Point {

        private int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        //Is important to do the override of this "equals method" because it is used by the "contains method" of Lists and also by thei sortin method.
        //Using the defalut object implementation of equals method is wrong because it doesn't work as you want.
        @Override
        public boolean equals(Object object) {
            boolean sameSame = false;

            if ((object != null) && (((Point) object).getX() == this.getX()) && (((Point) object).getY() == this.getY())) {
                sameSame = true;
            }
            return sameSame;
        }

    }

}

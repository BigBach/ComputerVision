package model;

import model.exceptions.InvalidPixelMatrixSizeException;

/**
 *
 * @author Marco Robutti - Filippo Cipolla
 */
public class PGM {
    private int max_val;
    private PixelMatrixInteger pixelMatrix;

    /**
     *
     * @param width of image
     * @param height of image
     * @param max_val of the pixel
     */
    public PGM(int width, int height, int max_val) {
        if (max_val < 0) {
            this.max_val = 0;
        } else {
            this.max_val = max_val;
        }
        pixelMatrix = new PixelMatrixInteger(Integer.class, width, height);
    }

    /**
     *
     * @return the width of the image
     */
    public int getWidth() {

        return pixelMatrix.getWidth();
    }


    /**
     *
     * @return the height of the image
     */
    public int getHeight() {

        return pixelMatrix.getHeigth();
    }


    /**
     *
     * @return the max_val of the image
     */
    public int getMax_val() {
        
        return max_val;
    }

    /**
     *
     * @param max_val the max_val to set
     */
    public void setMax_val(int max_val) {
        if (max_val < 0) {
            this.max_val = 0;
        }
        if (this.max_val < max_val) {
            this.pixelMatrix.saturatePixels(0, max_val);
        }
        this.max_val = max_val;
    }

    /**
     *
     * @return the pixelMatrix of the image
     */
    public PixelMatrixInteger getPixelMatrix() {
        
        return this.pixelMatrix;
    }
    
    /**
     *
     * @return
     */
    public Integer[] getPixels() {
        
        return this.pixelMatrix.getMatrix();
//        int[] pixelsCopy = new int[pixelMatrix.length];
//        //The clone() method in this case works correctly (i.e it doesn't
//        //returns a shallow copy of the source array) because we are working with an array of primitives!
//        pixelsCopy = pixelMatrix.clone();
//        return pixelsCopy;
    }

    /**
     *
     * @param pixelMatrix
     * @throws model.exceptions.InvalidPixelMatrixSizeException
     */
    public void setPixels(PixelMatrixInteger pixelMatrix) throws InvalidPixelMatrixSizeException {
        this.pixelMatrix = pixelMatrix;
        this.pixelMatrix.saturatePixels(0, this.max_val);
    }

}

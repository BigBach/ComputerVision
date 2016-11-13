package model;

import model.exceptions.InvalidPixelMatrixSizeException;

/**
 *
 * @author Marco Robutti - Filippo Cipolla
 */
public class PGM {

    private int width;
    private int height;
    private int max_val;
    private int[] pixels;

    /**
     *
     * @param width of image
     * @param height of image
     * @param max_val of the pixel
     */
    public PGM(int width, int height, int max_val) {

        this.width = width;
        this.height = height;
        if (max_val < 0) {
            this.max_val = 0;
        } else {
            this.max_val = max_val;
        }
        this.pixels = new int[width * height];
    }

    /**
     *
     * @return the width of the image
     */
    public int getWidth() {

        return width;
    }

    /**
     *
     * @param width the width to set
     */
    public void setWidth(int width) {

        this.width = width;
    }

    /**
     *
     * @return the height of the image
     */
    public int getHeight() {

        return height;
    }

    /**
     *
     * @param height the height to set
     */
    public void setHeight(int height) {

        this.height = height;
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
        
        int previousMax_val = this.max_val;
        
        if (max_val < 0) {
            this.max_val = 0;
        } else {
            this.max_val = max_val;
        }
        if (this.max_val < previousMax_val) {
            for (int i = 0; i < this.pixels.length; i++) {
                this.setPixel(i, this.pixels[i]);
            }
        }
    }

    /**
     *
     * @return the pixels of the image
     */
    public int[] getPixels() {
        
        return this.pixels;
//        int[] pixelsCopy = new int[pixels.length];
//        //The clone() method in this case works correctly (i.e it doesn't
//        //returns a shallow copy of the source array) because we are working with an array of primitives!
//        pixelsCopy = pixels.clone();
//        return pixelsCopy;
    }

    /**
     *
     * @param pixels the pixels to set
     * @throws model.exceptions.InvalidPixelMatrixSizeException
     */
    public void setPixels(int[] pixels) throws InvalidPixelMatrixSizeException {
        
        if (pixels.length != this.pixels.length) {
            throw new InvalidPixelMatrixSizeException("The size of the matrix doesn't match the actual size of the PGM image");
        }
        for (int i = 0; i < this.pixels.length; i++) {
            this.setPixel(i, pixels[i]);
        }
    }

    /**
     * 
     * @param index of the pixels arrayd
     * @param value that has to be store in the array of pixels
     */
    public void setPixel(int index, int value) {
        
        if (value < 0) {
            value = 0;
        }
        if (value > this.max_val) {
            value = this.max_val;
        }
        this.pixels[index] = value;
    }

}

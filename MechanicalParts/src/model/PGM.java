package model;

/**
 *
 * @author Markenos & BigBach
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
        this.max_val = max_val;
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
        
        this.max_val = max_val;
    }

    /**
     * 
     * @return the pixels of the image
     */
    public int[] getPixels() {
        
        return pixels;
    }

    /**
     * 
     * @param pixels the pixels to set
     */
    public void setPixels(int[] pixels) {
        
        this.pixels = pixels;
    }
}

package model;

import java.lang.reflect.Array;
import model.exceptions.InvalidPixelMatrixSizeException;

/**
 *
 * @author Markenos
 * @param <T>
 */
public abstract class PixelMatrix<T extends Number> {
    private int width;
    private int heigth;
    private T[] matrix;

    /**
     *
     * @param c
     * @param width
     * @param heigth
     */
    public PixelMatrix(Class<T> c,int width, int heigth) {
        this.width = width;
        this.heigth = heigth;
        matrix = (T[]) Array.newInstance(c, width * heigth);
    }

    /**
     *
     * @param t
     * @throws InvalidPixelMatrixSizeException
     */
    public void setMatrix(T[] t) throws InvalidPixelMatrixSizeException{
        if (t.length != matrix.length){
            throw new InvalidPixelMatrixSizeException("The size of the matrix "
                    + "doesn't match the actual size of the PGM image");
        }
        this.matrix = t;
    }
    
    /**
     *
     * @return
     */
    public T[] getMatrix(){
        return matrix;
    }
    
    /**
     *
     * @param minValue
     * @param maxValue
     */
    public abstract void saturatePixels(T minValue, T maxValue);
    
    /**
     *
     * @param oldMinValue
     * @param oldMaxValue
     * @param newMinValue
     * @param newMaxValue
     */
    public abstract void rescalePixels(T oldMinValue, T oldMaxValue, T newMinValue, T newMaxValue);
    
    /**
     *
     * @param threshold
     * @param minValue
     * @param maxValue
     */
    public abstract void thresholdPixels(T threshold, T minValue, T maxValue);
    
    /**
     *
     * @return
     */
    public abstract T getMinPixelValue();
    
    /**
     *
     * @return
     */
    public abstract T getMaxPixelValue();
    
    /**
     *
     * @param borderSize
     * @return
     */
    public abstract PixelMatrix extendsBordersWithZero( int borderSize);

    /**
     *
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @return
     */
    public int getHeigth() {
        return heigth;
    }
    
    

    
    
    
    
    

}

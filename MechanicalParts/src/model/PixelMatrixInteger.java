package model;

/**
 *
 * @author Markenos
 */
public class PixelMatrixInteger extends PixelMatrix<Integer> {

    /**
     *
     * @param c
     * @param width
     * @param heigth
     */
    public PixelMatrixInteger(Class<Integer> c, int width, int heigth) {
        super(c, width, heigth);
    }

    /**
     *
     * @param threshold
     * @param minValue
     * @param maxValue
     */
    @Override
    public void thresholdPixels(Integer threshold, Integer minValue, Integer maxValue) {
        for (int i = 0; i < super.getMatrix().length; i++) {
            if (super.getMatrix()[i] > threshold) {
                super.getMatrix()[i] = maxValue;
            } else {
                super.getMatrix()[i] = minValue;
            }
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Integer getMinPixelValue() {
        Integer min = super.getMatrix()[0];

        for (int i = 1; i < super.getMatrix().length; i++) {
            if (super.getMatrix()[i] < min) {
                min = super.getMatrix()[i];
            }
        }
        return min;
    }

    /**
     *
     * @return
     */
    @Override
    public Integer getMaxPixelValue() {
        Integer max = super.getMatrix()[0];

        for (int i = 1; i < super.getMatrix().length; i++) {
            if (super.getMatrix()[i] > max) {
                max = super.getMatrix()[i];
            }
        }
        return max;
    }

    /**
     *
     * @param oldMinValue
     * @param oldMaxValue
     * @param newMinValue
     * @param newMaxValue
     */
    @Override
    public void rescalePixels(Integer oldMinValue, Integer oldMaxValue, Integer newMinValue, Integer newMaxValue) {
        for (int i = 0; i < super.getMatrix().length; i++) {
            super.getMatrix()[i] = rescalePixel(super.getMatrix()[i], oldMinValue, oldMaxValue, newMinValue, newMaxValue);
        }
    }

    private Integer rescalePixel(Integer pixel, Integer oldMin, Integer oldMax, Integer newMin, Integer newMax) {
        return (int) (((double)((newMax - newMin) * (pixel - oldMin))) / ((double)(oldMax - oldMin)));
    }

    /**
     *
     * @param minValue
     * @param maxValue
     */
    @Override
    public void saturatePixels(Integer minValue, Integer maxValue) {
        for (int i = 0; i < super.getMatrix().length; i++) {
            super.getMatrix()[i] = saturate(super.getMatrix()[i], minValue, maxValue);
        }
    }
    
    
    private Integer saturate(Integer value, Integer minValue, Integer maxValue){
        if (value < minValue) {
            value = minValue;
        }
        if (value > maxValue) {
            value = maxValue;
        }
        return value;
    }
    
    /**
     *
     * @return
     */
    public PixelMatrixDouble toPixelMatrixDouble(){
        PixelMatrixDouble  p = new PixelMatrixDouble(Double.class, super.getWidth(), super.getHeigth());
        for (int i = 0; i < super.getMatrix().length; i++) {
            p.getMatrix()[i] = super.getMatrix()[i].doubleValue();
        }
        return p;
    }

    /**
     *
     * @param borderSize
     * @return
     */
    public PixelMatrix extendsBordersWithZero(int borderSize){
         PixelMatrixInteger outputPixelMatrix = new PixelMatrixInteger(Integer.class, this.getWidth() + 2*borderSize, this.getHeigth() + 2*borderSize);
         for (int i = 0; i < outputPixelMatrix.getMatrix().length;i++){
                 outputPixelMatrix.getMatrix()[i] = 0;
         }
         int displacement = 0;
         for(int i = 0; i < this.getMatrix().length;i++){
             outputPixelMatrix.getMatrix()[i + borderSize * outputPixelMatrix.getWidth() + borderSize + displacement] = this.getMatrix()[i];
             if (((i + 1)% this.getWidth()) == 0){
                 displacement += (2*borderSize + 1) -1;
             }
         }
         return outputPixelMatrix;
     }
    
}

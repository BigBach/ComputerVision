package model;

/**
 *
 * @author Markenos
 */
public class PixelMatrixDouble extends PixelMatrix<Double> {

    /**
     *
     * @param c
     * @param width
     * @param heigth
     */
    public PixelMatrixDouble(Class<Double> c, int width, int heigth) {
        super(c, width, heigth);
    }

    /**
     *
     * @param threshold
     * @param minValue
     * @param maxValue
     */
    @Override
    public void thresholdPixels(Double threshold, Double minValue, Double maxValue) {
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
    public Double getMinPixelValue() {
        Double min = super.getMatrix()[0];

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
    public Double getMaxPixelValue() {
        Double max = super.getMatrix()[0];

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
    public void rescalePixels(Double oldMinValue, Double oldMaxValue, Double newMinValue, Double newMaxValue) {
        for (int i = 0; i < super.getMatrix().length; i++) {
            super.getMatrix()[i] = rescalePixel(super.getMatrix()[i], oldMinValue, oldMaxValue, newMinValue, newMaxValue);
        }
    }

    private Double rescalePixel(Double pixel, Double oldMin, Double oldMax, Double newMin, Double newMax) {
        return (((newMax - newMin) * (pixel - oldMin)) / (oldMax - oldMin));
    }
    
//    public void degreesToRadiantsConversion(){
//        for (int i = 0; i < super.getMatrix().length; i++) {
//            super.getMatrix()[i] = (super.getMatrix()[i] * Math.PI)/180;
//        }
//    }
    
    /**
     *
     * @param minValue
     * @param maxValue
     */
        
     @Override
    public void saturatePixels(Double minValue, Double maxValue) {
        for (int i = 0; i < super.getMatrix().length; i++) {
            super.getMatrix()[i] = saturate(super.getMatrix()[i], minValue, maxValue);
        }
    }
    
    
    private Double saturate(Double value, Double minValue, Double maxValue){
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
    public PixelMatrixInteger toPixelMatrixInteger(){
        PixelMatrixInteger  p = new PixelMatrixInteger(Integer.class, super.getWidth(), super.getHeigth());
        for (int i = 0; i < super.getMatrix().length; i++) {
            p.getMatrix()[i] = (int)Math.round(super.getMatrix()[i]);
        }
        return p;
    }
    
    /**
     *
     * @param borderSize
     * @return
     */
    public PixelMatrix extendsBordersWithZero(int borderSize){
         PixelMatrixDouble outputPixelMatrix = new PixelMatrixDouble(Double.class, this.getWidth() + 2*borderSize, this.getHeigth() + 2*borderSize);
         for (int i = 0; i < outputPixelMatrix.getMatrix().length;i++){
                 outputPixelMatrix.getMatrix()[i] = 0.0;
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

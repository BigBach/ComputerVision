package model;

/**
 * 
 * @author Markenos & BigBach
 */
public class Filter {

    private double[] values;

    /**
     * 
     * @param array which contains the value of each element of the filter
     */
    public Filter(double[] array) {
        if (((array.length % 2) != 0) && (Math.sqrt(array.length) == (int) Math.sqrt(array.length))) {
            this.values = array;
        } else {
            System.err.println("The dimension of filter is not valid!");
        }
    }
    
    public int getSize(){
        return (int) Math.sqrt(values.length);
    }

    public double[] getValues() {
        return values;
    }
    
    

}

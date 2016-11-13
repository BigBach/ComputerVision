package model;

/**
 * 
 * @author Marco Robutti - Filippo Cipolla
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
    
    /**
     * 
     * @return the dimension n of current filter (n x n) 
     */
    public int getSize(){
        
        return (int) Math.sqrt(values.length);
    }

    /**
     * 
     * @return the values of each content item in the filter
     */
    public double[] getValues() {
       
        return values;
    }
    
}

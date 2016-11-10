package model;

/**
 * 
 * @author Markenos & BigBach
 */
public class Filter {

    private int[] values;

    /**
     * 
     * @param array wich contains the value of each element of the filter
     */
    public Filter(int[] array) {
        if (((array.length % 2) != 0) && (Math.sqrt(array.length) == (int) Math.sqrt(array.length))) {
            this.values = array;
        } else {
            System.err.println("The dimension of filter is not valid!");
        }
    }

}

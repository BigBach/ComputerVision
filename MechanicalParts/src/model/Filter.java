package model;

public class Filter {

    private int[] values;

    public Filter(int[] array) {
        if (((array.length % 2) != 0) && (Math.sqrt(array.length) == (int) Math.sqrt(array.length))) {
            this.values = array;
        } else {
            //Throws an exception!!!
        }
    }
    
}

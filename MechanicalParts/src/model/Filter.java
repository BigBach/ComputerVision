package model;

public class Filter {

    private int size;
    private int[] values;

    public Filter(int[] array) {
        if ((array.length % 2) != 0) {
            this.values = array;
        } else {
            //Throws an exception!!!
        }
    }
//Creates a filter with dimension of rows and width equals to "size"
//(for example "size = 3" means a filter matrix of 3x3 dimension.

    public Filter(int size) {
        if (((size % 2) != 0) && (size < 10)) {
            this.values = new int[size * size];
        } else {
            //Throw an exception!!!
        }
    }
    
    
}

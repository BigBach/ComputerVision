package model.exceptions;

/**
 * 
 * @author Marco Robutti - Filippo Cipolla
 * 
 * This exception is throws when the size of matrix doesn't match the size of the image
 */
public class InvalidPixelMatrixSizeException extends Exception{

    /**
     * 
     * @param message of the exception 
     */
    public InvalidPixelMatrixSizeException(String message) {
        
        super(message);
    }
    
}

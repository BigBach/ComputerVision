package model.exceptions;

/**
 * 
 * @author Marco Robutti - Filippo Cipolla
 * 
 * This exception is throws when the size of matrix doesn't match the size of the image
 */
public class InexistingPointException extends Exception{

    /**
     * 
     * @param message of the exception 
     */
    public InexistingPointException(String message) {
        
        super(message);
    }
    
}

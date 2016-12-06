package tests;

import java.io.IOException;
import model.ConvolutionBorders;
import model.Filter;
import model.PGM;
import model.ImageProcessingUtilities;
import model.PgmUtilities;
import model.exceptions.InexistingPointException;
import model.exceptions.InvalidPixelMatrixSizeException;

/**
 * 
 * @author Marco Robutti - Filippo Cipolla
 */
public class ConvolutionTest {
    
public static final String IN_PATH = "./images/image_6.pgm";
public static final String MODULE_ISOTROPIC_PATH = "./images/image_6_MODULE_ISOTROPIC.pgm";
public static final String PHASE_ISOTROPIC_PATH = "./images/image_6_PHASE_ISOTROPIC.pgm";
    
    /**
     * 
     * @param args the command line arguments
     * @throws InvalidPixelMatrixSizeException 
     */
    public static void main(String[] args) throws InvalidPixelMatrixSizeException, IOException, InexistingPointException {
        
    
        ImageProcessingUtilities imgUtilities = new ImageProcessingUtilities();
    
    
    Filter filter = new Filter(new double[]{0,1,0,1,-5,1,0,1,0});
    
    
    }
    
}

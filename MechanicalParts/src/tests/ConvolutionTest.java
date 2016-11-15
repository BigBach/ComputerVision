package tests;

import model.Filter;
import model.PGM;
import model.PgmUtilities;
import model.exceptions.InvalidPixelMatrixSizeException;

/**
 * 
 * @author Marco Robutti - Filippo Cipolla
 */
public class ConvolutionTest {
    
public static final String IN_PATH = "./images/image_1.pgm";
public static final String OUTPUT_PATH = "./images/image_1_OUT.pgm";
public static final String MODULE_ISOTROPIC_PATH = "./images/image_1_MODULE_ISOTROPIC.pgm";
public static final String PHASE_ISOTROPIC_PATH = "./images/image_1_PHASE_ISOTROPIC.pgm";
    
    /**
     * 
     * @param args the command line arguments
     * @throws InvalidPixelMatrixSizeException 
     */
    public static void main(String[] args) throws InvalidPixelMatrixSizeException {
        
    PgmUtilities pgmUtilities = new PgmUtilities();
    
    PGM inputPgm = pgmUtilities.readPGM(IN_PATH);
    
    Filter filter = new Filter(new double[]{0,1,0,1,-5,1,0,1,0});
    
    PGM outputPgm = pgmUtilities.filterConvolution(inputPgm, filter);
    pgmUtilities.writePGM(outputPgm, OUTPUT_PATH);
    
    PGM[] isotropicPGMs = pgmUtilities.isotropicFilter(inputPgm, 100, -45);
    pgmUtilities.writePGM(isotropicPGMs[0], MODULE_ISOTROPIC_PATH);
    pgmUtilities.writePGM(isotropicPGMs[1], PHASE_ISOTROPIC_PATH);
    }
    
}

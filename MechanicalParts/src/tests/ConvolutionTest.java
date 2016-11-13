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
    
public static final String IN_PATH = "./images/1.-images-_1_.pgm";
public static final String OUTPUT_PATH = "./images/1.-images-_1_OUT.pgm";
public static final String MODULE_ISOTROPIC_PATH = "./images/1.-images-_1_MODULE_ISOTROPIC.pgm";
    
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
    pgmUtilities.writePGM(pgmUtilities.isotropicFilter(inputPgm,350)[0], MODULE_ISOTROPIC_PATH);
    
    }
    
}

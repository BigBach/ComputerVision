package tests;

import model.Filter;
import model.PGM;
import model.PgmUtilities;


public class ConvolutionTest {
    
public static final String IN_PATH = "./images/1.-images-_1_.pgm";
public static final String OUTPUT_PATH = "./images/1.-images-_1_OUT.pgm";
    
    public static void main(String[] args) {
        
    PgmUtilities pgmUtilities = new PgmUtilities();
    
    PGM inputPgm = pgmUtilities.readPGM(IN_PATH);
    
    Filter filter = new Filter(new int[]{1,2,1,0,0,0,-1,-2,-1});
    
    PGM outputPgm = pgmUtilities.filterConvolution(inputPgm, filter);
    
    pgmUtilities.writePGM(outputPgm, OUTPUT_PATH);
    
    }
    
}

package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import model.ConvolutionBorders;
import model.Filter;
import model.ImageProcessingUtilities;
import model.PGM;
import model.PgmUtilities;
import model.exceptions.InvalidPixelMatrixSizeException;

public class DrawingPanel extends JPanel {

    private DrawingPanelParameterObserver parameterObserverInnerClass;
    private DrawingPanelObserver observerInnerClass;
    private String path;
    private static final Color DRAWING_COLOR = Color.red;
    private Graphics g;
    Holder<BufferedImage> backgroundImage;
    
    public DrawingPanel() throws IOException {
        parameterObserverInnerClass = new DrawingPanelParameterObserver();
        observerInnerClass = new DrawingPanelObserver();
        backgroundImage = new Holder<BufferedImage>(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundImage != null) {
            super.paintComponent(g);
            g.drawImage(backgroundImage.getValue(), 0, 0, this);
        }
    }

    public DrawingPanelParameterObserver getParameterObserverInnerClass() {
        return parameterObserverInnerClass;
    }

    public DrawingPanelObserver getObserverInnerClass() {
        return observerInnerClass;
    }

    private class DrawingPanelObserver implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            double[] circleParameter = (double[]) arg;
            double xc = circleParameter[0];
            double yc = circleParameter[1];
            double ray = circleParameter[2];
            setG(backgroundImage.getValue().createGraphics());
            getG().setColor(DRAWING_COLOR);
            if (((xc - ray) >= 0) && ((yc - ray) >= 0)) {
                getG().drawOval((int) (xc - ray), (int) (yc - ray), (int) ray * 2, (int) ray * 2);
                repaint();
            }
//        g.drawLine((int)xc, (int)yc, (int)xc, (int)yc);
//        System.out.println("x = " + xc + ",y = " + yc + ", ray = " + ray);
        }

    }

    private class DrawingPanelParameterObserver implements Observer {

        private File file;

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        @Override
        public void update(Observable o, Object arg) {
            try {
                ArrayList<Object> items = (ArrayList<Object>) arg;
                File file = (File) items.get(0);
                PgmUtilities pgmUtilities = new PgmUtilities();
                ImageProcessingUtilities imgUtilities = new ImageProcessingUtilities();
                PGM inputPgm = pgmUtilities.readPGM(file.getPath());
                PGM inputPgmResized = imgUtilities.filterConvolution(inputPgm, new Filter(new double[]{0, 0, 0, 0, 1, 0, 0, 0, 0}), (ConvolutionBorders.valueOf((String) items.get(1))));
                BufferedImage im = new BufferedImage(inputPgmResized.getWidth(), inputPgmResized.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                WritableRaster raster = im.getRaster();
                for (int h = 0; h < inputPgmResized.getHeight(); h++) {
                    for (int w = 0; w < inputPgmResized.getWidth(); w++) {
                        raster.setSample(w, h, 0, inputPgmResized.getPixels()[h * inputPgmResized.getWidth() + w]);
                    }
                }
                BufferedImage imRgb = new BufferedImage(inputPgmResized.getWidth(), inputPgmResized.getHeight(), BufferedImage.TYPE_INT_RGB);
                for (int h = 0; h < inputPgmResized.getHeight(); h++) {
                    for (int w = 0; w < inputPgmResized.getWidth(); w++) {
                        Color grayColor = new Color(im.getRGB(w, h));
                        int gray = grayColor.getRed();

                        Color color = new Color(grayColor.getRed(), grayColor.getBlue(), grayColor.getGreen());
                        imRgb.setRGB(w, h, color.getRGB());
                    }
                }
                backgroundImage.setValue(imRgb);
                //Graphics g = backgroundImage.getValue().getGraphics();
                //g.drawImage(imRgb, 0, 0, DrawingPanel.this);
                repaint();
                //g.dispose();
            } catch (InvalidPixelMatrixSizeException ex) {
                Logger.getLogger(DrawingPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public Graphics getG() {
        return g;
    }

    public void setG(Graphics g) {
        this.g = g;
    }
    
    
}

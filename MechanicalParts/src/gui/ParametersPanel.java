package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import model.ConvolutionBorders;
import model.ImageProcessingUtilities;
import model.ImageProcessingUtilities.Point;
import model.PGM;
import model.PgmUtilities;
import model.exceptions.InexistingPointException;
import model.exceptions.InvalidPixelMatrixSizeException;

public class ParametersPanel extends JPanel {

    private ParametersPanelObservableInitializer observableInnerClassInitializer;
    private ParametersPanelObservableCircleDrawer observableInnerClassCircleDrawer;
    private int moduleTreshold;
    private double ratio;
    private double minRay;
    private double maxRay;
    private int circlesNumber;
    private ConvolutionBorders extensionModality;
    private String path;

    private JTextField moduleTresholdText;
    private JComboBox ratioCombo;
    private JTextField minRayText;
    private JTextField maxRayText;
    private JTextField circleNumberText;
    private JComboBox convolutionBordersCombo;

    private ImageProcessingUtilities imgUtilities = new ImageProcessingUtilities();
    private PGM inputPgm;
    private HashMap<Point, Integer> pointsRayMap;

    public ParametersPanel() throws IOException {
        this.observableInnerClassInitializer = new ParametersPanelObservableInitializer();
        this.observableInnerClassCircleDrawer = new ParametersPanelObservableCircleDrawer();
        setLayout(new GridLayout(7, 2, 5, 5));
        JLabel moduleTresholdLabel = new JLabel("Module treshold");
        JLabel ratioLabel = new JLabel("Ratio (%)");
        JLabel minRayLabel = new JLabel("Minimum ray");
        JLabel maxRayLabel = new JLabel("Maximum ray");
        JLabel circleNumberLabel = new JLabel("Number of circle");
        JLabel convolutionBordersLabel = new JLabel("Border extension modality");

        JTextField moduleTresholdText = new JTextField();
        JComboBox ratioCombo = new JComboBox();
        for (int i = 0; i <= 100; i++) {
            ratioCombo.addItem(i);
        }
        ratioCombo.setSelectedIndex(0);
        JTextField minRayText = new JTextField();
        JTextField maxRayText = new JTextField();
        JTextField circleNumberText = new JTextField();
        JComboBox convolutionBordersCombo = new JComboBox();
        for (ConvolutionBorders value : ConvolutionBorders.values()) {
            convolutionBordersCombo.addItem(value);
        }
        convolutionBordersCombo.setSelectedItem(ConvolutionBorders.NONE);
        JButton loadImageButton = new JButton("Load image");
        loadImageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

//                setModuleTreshold(Integer.valueOf(moduleTresholdText.getText()));
                setRatio(Integer.valueOf((int) (ratioCombo.getSelectedItem())).doubleValue() / 100);
//                setMinRay(Double.valueOf(minRayText.getText()));
//                setMaxRay(Double.valueOf(maxRayText.getText()));
//                setCirclesNumber(Integer.valueOf(circleNumberText.getText()));
                setExtensionModality((ConvolutionBorders) (convolutionBordersCombo.getSelectedItem()));

                if ((moduleTreshold < 0)
                        || (minRay < 0)
                        || (maxRay < 0)
                        || (circlesNumber < 0)
                        || (maxRay < minRay)) {
                    JOptionPane.showMessageDialog((MainFrame) SwingUtilities.getWindowAncestor(ParametersPanel.this),
                            "Hai inserito dei valori errati (valori negativi oppure raggio minimo maggiore di raggio massimo)",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);

                } else {
                    JFileChooser fc = new JFileChooser();
                    int returnVal = fc.showOpenDialog((MainFrame) SwingUtilities.getWindowAncestor(ParametersPanel.this));
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        if (file != null) {
                            ParametersPanel.this.path = file.getPath();
                            ArrayList<Object> items = new ArrayList<Object>();
                            items.add(file);
                            items.add(extensionModality.name());
                            observableInnerClassInitializer.notifyOtherPanel(items);
                        }
                    }
                }
            }
        });
        JButton calculateButton = new JButton("Calculate...");
        calculateButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    ArrayList<Object> items = new ArrayList<Object>();
                    items.add(new File(getPath()));
                    items.add(getExtensionModality().name());
                    getObservableInnerClassInitializer().notifyOtherPanel(items);

                    setInputPgm();
                    setImgUtilities();
//                PGM[] isotropicPGMs = imgUtilities.isotropicFilter(inputPgm, 100, 45, ConvolutionBorders.NONE);
//                pgmUtilities.writePGM(isotropicPGMs[0], MODULE_ISOTROPIC_PATH);
//                pgmUtilities.writePGM(isotropicPGMs[1], PHASE_ISOTROPIC_PATH);

                    setModuleTreshold(Integer.valueOf(moduleTresholdText.getText()));
                    setRatio(Integer.valueOf((int) (ratioCombo.getSelectedItem())).doubleValue() / 100);
                    setMinRay(Double.valueOf(minRayText.getText()));
                    setMaxRay(Double.valueOf(maxRayText.getText()));
                    setCirclesNumber(Integer.valueOf(circleNumberText.getText()));
                    setExtensionModality((ConvolutionBorders) (convolutionBordersCombo.getSelectedItem()));

                    if ((moduleTreshold < 0)
                            || (minRay < 0)
                            || (maxRay < 0)
                            || (circlesNumber < 0)
                            || (maxRay < minRay)) {
                        JOptionPane.showMessageDialog((MainFrame) SwingUtilities.getWindowAncestor(ParametersPanel.this),
                                "Hai inserito dei valori errati (valori negativi oppure raggio minimo maggiore di raggio massimo)",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        setPointsRayMap(getImgUtilities().houghTransformCircle(getModuleTreshold(), getRatio(), (int) Math.round(getMinRay() / 0.264583), (int) Math.round(getMaxRay() / 0.264583), getCirclesNumber(), inputPgm, getExtensionModality()));
                        //We draw the circles on the apposite panel to view them...
                        for (Map.Entry<ImageProcessingUtilities.Point, Integer> entrySet : getPointsRayMap().entrySet()) {
                            ImageProcessingUtilities.Point point = entrySet.getKey();
                            Integer ray = entrySet.getValue();
                            if (ray > 0) {
                                getObservableInnerClassCircleDrawer().notifyOtherPanel((double) point.getX(), (double) point.getY(), (double) ray);
                            }
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ParametersPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidPixelMatrixSizeException ex) {
                    Logger.getLogger(ParametersPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InexistingPointException ex) {
                    Logger.getLogger(ParametersPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        this.add(moduleTresholdLabel);
        this.add(moduleTresholdText);
        this.add(ratioLabel);
        this.add(ratioCombo);
        this.add(minRayLabel);
        this.add(minRayText);
        this.add(maxRayLabel);
        this.add(maxRayText);
        this.add(circleNumberLabel);
        this.add(circleNumberText);
        this.add(convolutionBordersLabel);
        this.add(convolutionBordersCombo);
        this.add(loadImageButton);
        this.add(calculateButton);
    }

    public ParametersPanelObservableInitializer getObservableInnerClassInitializer() {
        return observableInnerClassInitializer;
    }

    public ParametersPanelObservableCircleDrawer getObservableInnerClassCircleDrawer() {
        return observableInnerClassCircleDrawer;
    }

    public class ParametersPanelObservableInitializer extends Observable {

        public void notifyOtherPanel(ArrayList<Object> object) {
            this.setChanged();
            notifyObservers(object);
        }

    }

    public class ParametersPanelObservableCircleDrawer extends Observable {

        public void notifyOtherPanel(double x, double y, double ray) {
            this.setChanged();
            notifyObservers(new double[]{x, y, ray});
        }
    }

    public int getModuleTreshold() {
        return moduleTreshold;
    }

    public double getRatio() {
        return ratio;
    }

    public double getMinRay() {
        return minRay;
    }

    public double getMaxRay() {
        return maxRay;
    }

    public int getCirclesNumber() {
        return circlesNumber;
    }

    public ConvolutionBorders getExtensionModality() {
        return extensionModality;
    }

    public String getPath() {
        return path;
    }

    public void setObservableInnerClassInitializer(ParametersPanelObservableInitializer observableInnerClassInitializer) {
        this.observableInnerClassInitializer = observableInnerClassInitializer;
    }

    public void setObservableInnerClassCircleDrawer(ParametersPanelObservableCircleDrawer observableInnerClassCircleDrawer) {
        this.observableInnerClassCircleDrawer = observableInnerClassCircleDrawer;
    }

    public void setModuleTreshold(int moduleTreshold) {
        this.moduleTreshold = moduleTreshold;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public void setMinRay(double minRay) {
        this.minRay = minRay;
    }

    public void setCirclesNumber(int circlesNumber) {
        this.circlesNumber = circlesNumber;
    }

    public void setExtensionModality(ConvolutionBorders extensionModality) {
        this.extensionModality = extensionModality;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMaxRay(double maxRay) {
        this.maxRay = maxRay;
    }

    public ImageProcessingUtilities getImgUtilities() {
        return imgUtilities;
    }

    public void setImgUtilities() {
        this.imgUtilities = new ImageProcessingUtilities();
    }

    public PGM getInputPgm() {
        return inputPgm;
    }

    public void setInputPgm() {
        PgmUtilities pgmUtilities = new PgmUtilities();
        inputPgm = pgmUtilities.readPGM(path);
    }

    public HashMap<Point, Integer> getPointsRayMap() {
        return pointsRayMap;
    }

    public void setPointsRayMap(HashMap<Point, Integer> pointsRayMap) {
        this.pointsRayMap = pointsRayMap;
    }

}

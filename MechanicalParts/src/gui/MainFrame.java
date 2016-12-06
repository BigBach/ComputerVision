package gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.JFrame;

public class MainFrame extends JFrame {

    private ParametersPanel parametersPanel;
    private DrawingPanel drawingPanel;
    
    public MainFrame() throws IOException {
        this.setTitle("Hough transform of an image");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2);
        parametersPanel = new ParametersPanel();
        this.getContentPane().add(BorderLayout.EAST,parametersPanel);
        drawingPanel = new DrawingPanel();
        parametersPanel.getObservableInnerClassInitializer().addObserver(drawingPanel.getParameterObserverInnerClass());
        parametersPanel.getObservableInnerClassCircleDrawer().addObserver(drawingPanel.getObserverInnerClass());
        this.getContentPane().add(BorderLayout.CENTER,drawingPanel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public ParametersPanel getParametersPanel() {
        return parametersPanel;
    }

    public DrawingPanel getDrawingPanel() {
        return drawingPanel;
    }
    
    
    
}
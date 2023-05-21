package gui.mainmenu;

import javax.swing.*;
import java.awt.*;

public class SimulationScreen extends JPanel {
    private JFrame frame;
    private final int SCREEN_WIDTH = 1920;
    private final int SCREEN_HEIGHT = 1080;
    private PlanetList planetList;

    public SimulationScreen(PlanetList planetList) {
        frame = new JFrame();

        this.planetList = planetList;

        setBackground(Color.BLACK);

        frame.add(this, BorderLayout.CENTER);
        frame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        planetList.draw(g2d, getWidth(), getHeight());
        repaint();
    }
}

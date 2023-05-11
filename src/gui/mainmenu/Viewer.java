package gui.mainmenu;

import celestial_bodies.CelestialBody;
import gui.SolarSystemPanel;

import javax.swing.*;

public class Viewer {
    private JFrame frame;
    private final int SCREEN_WIDTH = 1920;
    private final int SCREEN_HEIGHT = 1080;
    public Viewer() {
        frame = new JFrame();

        SolarSystemPanel panel = new SolarSystemPanel();
        panel.setLayout(null);

        System.out.println(SCREEN_WIDTH);
        System.out.println(SCREEN_HEIGHT);

        frame.add(panel);
        frame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}

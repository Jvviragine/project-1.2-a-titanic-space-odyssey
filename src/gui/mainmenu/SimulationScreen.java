package gui.mainmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import celestial_bodies.SolarSystemPhysicsSimulation;
import physics.vectors.StateVector;
import solar_system_data.PlanetaryData;

public class SimulationScreen extends JPanel {
    private JFrame frame;
    private final int SCREEN_WIDTH = 1536;
    private final int SCREEN_HEIGHT = 801;

    private int[][] testOrbit = {{SCREEN_WIDTH/2, SCREEN_HEIGHT/2+100}, {SCREEN_WIDTH/2-100, SCREEN_HEIGHT/2}, {SCREEN_WIDTH/2, SCREEN_HEIGHT/2-100}, {SCREEN_WIDTH/2+100, SCREEN_HEIGHT/2}};
    private int currentIndex = 0;
    private Timer timer;
    private OrbitList orbitList = new OrbitList();
    private int[][] testPath1 = orbitList.getExamplePath1();
    private int[][] testPath2 = orbitList.getExamplePath2();
    private int[][] sunPath = orbitList.getSunPath();
    private int[][][] allPaths = {testPath1, testPath2, sunPath};

    public SimulationScreen() {
        frame = new JFrame("Simulation Screen");

        this.setLayout(null);
        this.setBackground(Color.BLACK);

        frame.add(this);
        frame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Create a timer with a delay of 0.5 second
        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iterateThroughOrbit();
            }
        });

        // Start the timer
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int xPath1 = testPath1[currentIndex][0];
        int yPath1 = testPath1[currentIndex][1];
        int xPath2 = testPath2[currentIndex][0];
        int yPath2 = testPath2[currentIndex][1];
        int xSunPath = sunPath[currentIndex][0];
        int ySunPath = sunPath[currentIndex][1];
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(xPath1, yPath1, 20, 20);
        g2d.fillOval(xPath2, yPath2, 20, 20);
        g2d.fillOval(xSunPath, ySunPath, 40, 40);
    }

    public void iterateThroughOrbit() {
        currentIndex++;

        for(int i = 0; i < allPaths.length; i++) {

            if (currentIndex >= testPath1.length) {
                currentIndex = 0;
            }

            repaint();
        }
    }
}

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
    private final int XCENTER = SCREEN_WIDTH/2;
    private final int YCENTER = SCREEN_HEIGHT/2;

    private int currentIndex = 0;
    private Timer timer;
    private OrbitList orbitList = new OrbitList();
    private int[][] sunPath = orbitList.getPath(0);
    private int[][] mercuryPath = orbitList.getPath(1);
    private int[][] venusPath = orbitList.getPath(2);
    private int[][] earthPath = orbitList.getPath(3);
    private int[][] marsPath = orbitList.getPath(4);
    private int[][] jupiterPath = orbitList.getPath(5);
    private int[][] saturnPath = orbitList.getPath(6);
    private int[][] uranusPath = orbitList.getPath(7);
    private int[][] neptunePath = orbitList.getPath(8);
    private int[][][] allPaths = {sunPath, mercuryPath, venusPath, marsPath, jupiterPath, saturnPath, uranusPath, neptunePath};

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

        // Create a timer with a delay of 1 millisecond
        timer = new Timer(20, new ActionListener() {
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

        g2d.setColor(Color.YELLOW);
        g2d.fillOval(XCENTER + sunPath[currentIndex][0] - 20, YCENTER + sunPath[currentIndex][1] - 20, 40, 40);
        g2d.setColor(Color.ORANGE);
        g2d.fillOval(XCENTER + venusPath[currentIndex][0] - 10,  YCENTER + venusPath[currentIndex][1] - 10, 20, 20);
        g2d.setColor(Color.BLUE);
        g2d.fillOval(XCENTER + earthPath[currentIndex][0] - 10,  YCENTER + earthPath[currentIndex][1] - 10, 20, 20);
        g2d.setColor(Color.RED);
        g2d.fillOval(XCENTER + marsPath[currentIndex][0] - 10,  YCENTER + marsPath[currentIndex][1] - 10, 20, 20);
        g2d.setColor(Color.ORANGE);
        g2d.fillOval(XCENTER + jupiterPath[currentIndex][0] - 10,  YCENTER + jupiterPath[currentIndex][1] - 10, 20, 20);
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(XCENTER + saturnPath[currentIndex][0] - 10,  YCENTER + saturnPath[currentIndex][1] - 10, 20, 20);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillOval(XCENTER + uranusPath[currentIndex][0] - 10,  YCENTER + uranusPath[currentIndex][1] - 10, 20, 20);
        g2d.setColor(Color.BLUE);
        g2d.fillOval(XCENTER + neptunePath[currentIndex][0] - 10,  YCENTER + neptunePath[currentIndex][1] - 10, 20, 20);
    }

    public void iterateThroughOrbit() {
        currentIndex++;

        for(int i = 0; i < allPaths.length; i++) {

            if (currentIndex >= mercuryPath.length) {
                currentIndex = 0;
            }
            //System.out.println(sunPath[currentIndex][0] + " " + sunPath[currentIndex][1]);
            //System.out.println(mercuryPath[currentIndex][0] + " " + mercuryPath[currentIndex][1]);
            repaint();
        }

    }
}

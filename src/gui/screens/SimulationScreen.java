package gui.screens;

import gui.data.OrbitList;
import gui.helper_classes.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * GUI class to show the solar system with relevant planets and the probe
 * Mercury, Uranus and Neptune have been omitted as they are not relevant to this project
 * The probe is launched from Earth and follows the simulation according to the user inputted values from the Start Screen
 */
public class SimulationScreen extends JPanel {
    //Static values, used in all screens that come after this one
    public static final int SCREEN_WIDTH = 1536;
    public static final int SCREEN_HEIGHT = 801;
    public static final int XCENTER = SCREEN_WIDTH/2;
    public static final int YCENTER = SCREEN_HEIGHT/2;

    //GUI
    private JFrame frame;

    //Images
    private ImageLoader imageLoader = new ImageLoader();
    //Assign the probe image to normandy
    private Image normandy = imageLoader.getImage("normandy");

    private ScheduledExecutorService executor;

    private int pathIndex = 0;

    //All planet and probe paths are initialised here
    private OrbitList orbitList = new OrbitList();
    private int[][] sunPath = orbitList.getPath(0);
    private int[][] venusPath = orbitList.getPath(2);
    private int[][] earthPath = orbitList.getPath(3);
    private int[][] moonPath = orbitList.getPath(4);
    private int[][] marsPath = orbitList.getPath(5);
    private int[][] jupiterPath = orbitList.getPath(6);
    private int[][] saturnPath = orbitList.getPath(7);
    private int[][] titanPath = orbitList.getPath(8);
    private int[][] probePath = orbitList.getPath(11);

    /**
     * Frame and executor get initialised here
     */
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

        //Update the screen with a delay in microseconds
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            if (!StartScreen.freezeSimulation) {
                iterateThroughOrbit();
            }
        }, 0, StartScreen.simulationInterval, TimeUnit.MICROSECONDS);
    }

    /**
     * Paint the planets and probe on the frame
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //Sun
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(XCENTER + sunPath[pathIndex][0] - 20, YCENTER + sunPath[pathIndex][1] - 20, 40, 40);
        g2d.drawString("Sun", XCENTER + sunPath[pathIndex][0] - 10, YCENTER + sunPath[pathIndex][1] + 30);

        //Venus
        g2d.setColor(Color.ORANGE);
        g2d.fillOval(XCENTER + venusPath[pathIndex][0] - 10,  YCENTER + venusPath[pathIndex][1] - 10, 13, 13);
        g2d.drawString("Venus", XCENTER + venusPath[pathIndex][0] - 18, YCENTER + venusPath[pathIndex][1] + 13);

        //Earth
        g2d.setColor(Color.BLUE);
        g2d.fillOval(XCENTER + earthPath[pathIndex][0] - 10,  YCENTER + earthPath[pathIndex][1] - 10, 15,15);
        g2d.drawString("Earth", XCENTER + earthPath[pathIndex][0] - 16, YCENTER + earthPath[pathIndex][1] + 17);

        //Moon
        g2d.setColor(Color.WHITE);
        g2d.fillOval(XCENTER + moonPath[pathIndex][0] - 8,  YCENTER + moonPath[pathIndex][1] - 8, 10, 10);
        g2d.drawString("Moon", XCENTER + moonPath[pathIndex][0] - 20, YCENTER + moonPath[pathIndex][1] + 10);

        //Mars
        g2d.setColor(Color.RED);
        g2d.fillOval(XCENTER + marsPath[pathIndex][0] - 10,  YCENTER + marsPath[pathIndex][1] - 10, 14, 14);
        g2d.drawString("Mars", XCENTER + marsPath[pathIndex][0] - 16, YCENTER + marsPath[pathIndex][1] + 15);

        //Jupiter
        g2d.setColor(Color.ORANGE);
        g2d.fillOval(XCENTER + jupiterPath[pathIndex][0] - 10,  YCENTER + jupiterPath[pathIndex][1] - 10, 25, 25);
        g2d.drawString("Jupiter", XCENTER + jupiterPath[pathIndex][0] - 14, YCENTER + jupiterPath[pathIndex][1] + 25);

        //Saturn
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(XCENTER + saturnPath[pathIndex][0] - 10,  YCENTER + saturnPath[pathIndex][1] - 10, 20, 20);
        g2d.drawString("Saturn", XCENTER + saturnPath[pathIndex][0] - 15, YCENTER + saturnPath[pathIndex][1] + 22);

        //Titan
        g2d.setColor(Color.WHITE);
        g2d.fillOval(XCENTER + titanPath[pathIndex][0] - 5,  YCENTER + titanPath[pathIndex][1] - 5, 10, 10);
        g2d.drawString("Titan", XCENTER + titanPath[pathIndex][0] - 20, YCENTER + titanPath[pathIndex][1] + 10);

        //Probe
        g2d.drawImage(normandy, XCENTER + probePath[pathIndex][0] - 20, YCENTER + probePath[pathIndex][1] - 20, 25, 25, null);
        g2d.drawString("Probe", XCENTER + probePath[pathIndex][0] - 22, YCENTER + probePath[pathIndex][1] + 7);
    }

    /**
     * Loops through the planet's and probe's paths
     * When the end of the path is reached: the simulation stops, the frame gets closed and the Orbit Screen gets launched
     */
    public void iterateThroughOrbit() {
        pathIndex++;

        if (pathIndex >= (earthPath.length - 1)) {
            OrbitScreen orbitScreen = new OrbitScreen();
            frame.dispose();
            executor.shutdown();        //keep this as last, stops the simulationScreen from doing anything
        }

        repaint();
    }
}

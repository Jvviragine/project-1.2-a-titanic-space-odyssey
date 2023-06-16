package gui.screens;

import gui.helper_classes.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static gui.screens.SimulationScreen.*;

public class OrbitScreen extends JPanel {

    private JFrame frame;

    private ImageLoader imageLoader = new ImageLoader();
    private Image normandy;
    private Image titan;

    private int pathIndex = 0;
    private int tempCounter = 0;

    private ScheduledExecutorService executor;

    private int[][] testPath = {{0, -200}, {-200, 0}, {0, 200}, {200, 0}};

    public OrbitScreen() {
        //Assign the probe and titan image to normandy and titan respectively
        normandy = imageLoader.getImage("normandy");
        titan = imageLoader.getImage("titan");

        frame = new JFrame("Orbit Screen");

        this.setLayout(null);
        this.setBackground(Color.BLACK);

        frame.add(this);
        frame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        System.out.println(XCENTER);
        System.out.println(YCENTER);
        System.out.println(getWidth());
        System.out.println(getHeight());

        // Schedule a task with a custom delay in microseconds
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            if (!StartScreen.freezeSimulation) {
                showOrbit();
            }
            repaint();
        }, 0, StartScreen.simulationSpeed * 10000, TimeUnit.MICROSECONDS);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //Titan
        g2d.drawImage(titan, XCENTER - 100, YCENTER - 100, 200, 200, null);

        //Probe
        g2d.drawImage(normandy, XCENTER + testPath[pathIndex][0] - 20, YCENTER + testPath[pathIndex][1] - 20, 25, 25, null);
    }

    public void showOrbit() {
        pathIndex++;

        //Stopping condition (loops for now)
        if (pathIndex >= testPath.length) {
            tempCounter++;
            pathIndex = 0;
//            executor.shutdown();        //keep this as last, stops the orbitScreen from doing anything
        }

        if(tempCounter > 5) {
            LandingScreen landingScreen = new LandingScreen();
            frame.dispose();
            executor.shutdown();
        }
        repaint();
    }
}

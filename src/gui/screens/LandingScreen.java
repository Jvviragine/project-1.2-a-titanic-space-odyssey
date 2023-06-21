package gui.screens;

import gui.helper_classes.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static gui.screens.SimulationScreen.*;

public class LandingScreen extends JPanel {

    private JFrame frame;

    private ImageLoader imageLoader = new ImageLoader();
    private Image normandy;

    private int pathIndex = 0;

    private ScheduledExecutorService executor;

    private int[][] testPath = {{0, -400}, {0, -350}, {0, -300}, {0, -250}, {0, -200}, {0, -150}, {0, -100}, {0, -50},
                                {0,0}, {0, 50}, {0, 100}, {0, 150}, {0, 200}, {0, 250}};

    public LandingScreen() {
        //Assign the probe image to normandy
        normandy = imageLoader.getAndRotateImage("normandy");

        frame = new JFrame("Orbit Screen");

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
                showLanding();
            }
            repaint();
        }, 0, StartScreen.simulationSpeed * 30000, TimeUnit.MICROSECONDS);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //Titan
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, YCENTER + 250, SCREEN_WIDTH, SCREEN_HEIGHT);

        //Probe
        g2d.drawImage(normandy, XCENTER + testPath[pathIndex][0] - 20, YCENTER + testPath[pathIndex][1] - 20, 60, 60, null);
    }

    public void showLanding() {
        pathIndex++;

        if(pathIndex > testPath.length - 1) {
            pathIndex = 0;
        }

        repaint();
    }
}

package gui.screens;

import gui.data.OrbitList;
import gui.helper_classes.ImageLoader;
import physics.vectors.Vector;
import stochastic_wind_simulation.WindModel;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static gui.screens.SimulationScreen.*;

/**
 * GUI class to show the probe landing on Titan
 * The probe slowly lowers, and eventually lands in the landing zone
 * Wind plays a role in this, displayed at the top of the frame
 */
public class LandingScreen extends JPanel {

    //GUI
    private JFrame frame;

    //The Y coordinate for the surface of Titan in the GUI
    private int titanSurfaceY = YCENTER + 250;

    //Images
    private ImageLoader imageLoader = new ImageLoader();
    private Image normandy = imageLoader.getAndRotateImage("normandy");

    private int pathIndex = 0;

    private ScheduledExecutorService executor;

    //The probe's landing path
    private int[][] landingPath = OrbitList.getLandingPath();

    //Wind initialisation
    private WindModel windModel = new WindModel();
    private Vector wind;

    /**
     * Frame and executor get initialised here
     */
    public LandingScreen() {
        frame = new JFrame("Landing Screen");

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
        }, 0, StartScreen.simulationInterval * 200, TimeUnit.MICROSECONDS);
    }

    /**
     * Shows Titan and the landing zone, as well as the probe landing
     * Also displays the wind speed at the top
     * @param g the <code>Graphics</code> object to protect
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //Adjust font sizes
        Font currentFont = g2d.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
        g2d.setFont(newFont);

        //Titan
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, titanSurfaceY, SCREEN_WIDTH, SCREEN_HEIGHT);

        //Landing zone
        g2d.setColor(Color.RED);
        g2d.fillRect(XCENTER - 25, titanSurfaceY, 50, 20);
        g2d.drawString("Landing zone", XCENTER - 50, titanSurfaceY + 40);

        //Probe
        g2d.drawImage(normandy, XCENTER + landingPath[pathIndex][0] - 30, titanSurfaceY - landingPath[pathIndex][1] - 35, 60, 60, null);

        //Wind
        g2d.drawString("Wind speed", XCENTER - 47, 25);
        g2d.setColor(Color.WHITE);
        g2d.drawString((wind.get(0) * 3.6) + " km/h", XCENTER - 95, 55);
    }

    /**
     * Loops through the probe's path
     * Updates the wind based on the probe's current height
     * Shuts down the program when the probe's path has been iterated through
     */
    public void showLanding() {
        pathIndex++;

        if(pathIndex >= landingPath.length - 1) {
            executor.shutdown();
        }

        wind = windModel.getWindSpeed((landingPath[pathIndex][1]) * 1000);

        repaint();
    }
}

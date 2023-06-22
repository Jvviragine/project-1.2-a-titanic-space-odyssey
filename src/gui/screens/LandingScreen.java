package gui.screens;

import gui.helper_classes.ImageLoader;
import landing.FeedbackController;
import landing.LanderState;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static gui.screens.SimulationScreen.*;
import static landing.FeedbackController.G;

public class LandingScreen extends JPanel {

    private JFrame frame;

    private ImageLoader imageLoader = new ImageLoader();
    private Image normandy;

    private int pathIndex = 0;

    StateVector s = new StateVector(new Vector[]{new Vector(new double[]{55, 20,0}), new Vector(new double[]{0,0,0})});
    LanderState l = new LanderState(s, 10*G, 0);
    FeedbackController controller = new FeedbackController(l);

    private ScheduledExecutorService executor;

    private double[][] landingPath = controller.getPath();

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
        }, 0, StartScreen.simulationSpeed * 5000, TimeUnit.MICROSECONDS);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //Titan
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, YCENTER + 250, SCREEN_WIDTH, SCREEN_HEIGHT);

        //Probe
        g2d.drawImage(normandy, (int) (XCENTER + landingPath[pathIndex][0] - 20), (int) (YCENTER + landingPath[pathIndex][1] - 20), 60, 60, null);
    }

    public void showLanding() {
        pathIndex++;

        if(pathIndex > landingPath.length - 1) {
            pathIndex = 0;
        }

        repaint();
    }
}

package gui.screens;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OrbitScreen extends JPanel {
    private JFrame frame;
    private final int SCREEN_WIDTH = 1540;
    private final int SCREEN_HEIGHT = 845;
    private final int XCENTER = SCREEN_WIDTH/2;
    private final int YCENTER = SCREEN_HEIGHT/2;
    private Image normandy;
    private Image titan;
    private int currentIndex = 0;
    private int testCounter = 0;
    private ScheduledExecutorService executor;
    private int[][] testPath = {{0, -200}, {-200, 0}, {0, 200}, {200, 0}};

    public OrbitScreen() {
        //Assign the images to the variables titan and normandy
        try {
            File pathToFileProbe = new File("src/gui/images/Normandy.png");
            normandy = ImageIO.read(pathToFileProbe);
            File pathToFileTitan = new File("src/gui/images/Titan.png");
            titan = ImageIO.read(pathToFileTitan);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

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

        // Schedule a task with a fixed delay of 1 millisecond
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
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
        g2d.drawImage(normandy, XCENTER + testPath[currentIndex][0] - 20, YCENTER + testPath[currentIndex][1] - 20, 25, 25, null);
    }

    public void showOrbit() {
        currentIndex++;

        if (currentIndex >= testPath.length) {
            testCounter++;
            currentIndex = 0;
//            executor.shutdown();        //keep this as last, stops the orbitScreen from doing anything
        }

        if(testCounter > 5) {
            LandingScreen landingScreen = new LandingScreen();
            frame.dispose();
            executor.shutdown();
        }
        repaint();
    }
}

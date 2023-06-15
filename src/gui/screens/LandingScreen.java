package gui.screens;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LandingScreen extends JPanel {
    private JFrame frame;
    private final int SCREEN_WIDTH = 1526;
    private final int SCREEN_HEIGHT = 808;
    private final int XCENTER = SCREEN_WIDTH/2;
    private final int YCENTER = SCREEN_HEIGHT/2;
    private Image normandy;
    private int currentIndex = 0;
    private ScheduledExecutorService executor;
    private int[][] testPath = {{0, -400}, {0, -350}, {0, -300}, {0, -250}, {0, -200}, {0, -150}, {0, -100}, {0, -50},
                                {0,0}, {0, 50}, {0, 100}, {0, 150}, {0, 200}, {0, 250}};

    public LandingScreen() {
        //Initialize the probe image
        assignAndRotateImage();

        frame = new JFrame("Orbit Screen");

        this.setLayout(null);
        this.setBackground(Color.BLACK);

        frame.add(this);
        frame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Schedule a task with a fixed delay of 1 millisecond
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            if (!StartScreen.freezeSimulation) {
                showOrbit();
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
        g2d.drawImage(normandy, XCENTER + testPath[currentIndex][0] - 20, YCENTER + testPath[currentIndex][1] - 20, 60, 60, null);
    }

    public void showOrbit() {
        currentIndex++;

        if(currentIndex > testPath.length - 1) {
            currentIndex = 0;
        }

        repaint();
    }

    //Initializes the normandy image and rotates it 45 degrees to the left
    public void assignAndRotateImage() {
        // Assign the images to the variables titan and normandy
        try {
            File pathToFileProbe = new File("src/gui/images/Normandy.png");
            BufferedImage originalImage = ImageIO.read(pathToFileProbe);

            // Calculate the dimensions for the rotated image
            int maxWidth = (int) Math.ceil(originalImage.getWidth() * Math.abs(Math.cos(Math.PI / 4)))
                    + (int) Math.ceil(originalImage.getHeight() * Math.abs(Math.sin(Math.PI / 4)));
            int maxHeight = (int) Math.ceil(originalImage.getWidth() * Math.abs(Math.sin(Math.PI / 4)))
                    + (int) Math.ceil(originalImage.getHeight() * Math.abs(Math.cos(Math.PI / 4)));

            // Create a new BufferedImage with the appropriate dimensions
            BufferedImage rotatedImage = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);

            // Get the graphics object of the rotated image
            Graphics2D g2d = rotatedImage.createGraphics();

            // Set the background color to transparent
            rotatedImage = g2d.getDeviceConfiguration().createCompatibleImage(maxWidth, maxHeight, Transparency.TRANSLUCENT);
            g2d.dispose();
            g2d = rotatedImage.createGraphics();

            // Rotate the image around the center
            AffineTransform transform = new AffineTransform();
            transform.translate((maxWidth - originalImage.getWidth()) / 2.0, (maxHeight - originalImage.getHeight()) / 2.0);
            transform.rotate(-Math.PI / 4, originalImage.getWidth() / 2.0, originalImage.getHeight() / 2.0);

            // Apply the rotation to the new image
            g2d.setTransform(transform);
            g2d.drawImage(originalImage, 0, 0, null);
            g2d.dispose();

            // Assign the rotated image to the variable
            normandy = rotatedImage;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

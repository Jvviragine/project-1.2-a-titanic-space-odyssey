package gui.screens;

import gui.helper_classes.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionListener;

import static gui.screens.SimulationScreen.*;

/**
 * GUI class to show the probe orbiting around Titan
 * The user can input x and y coordinates for the eventual landing of the probe on Titan
 * The probe keeps orbiting indefinitely until the user has pressed the "Initiate landing sequence!" button
 */
public class OrbitScreen extends JPanel implements ActionListener {
    //GUI
    private JFrame frame;
    private JPanel panel;
    private JLabel topText, xText, yText, errorText;
    private JTextField xTextInput, yTextInput;
    private JButton startButton;

    //Default values
    //Need to adjust these to be actual default values
    private double defaultXCoordinate = 500000.0;
    private double defaultYCoordinate = 300000.0;

    public static double landingX, landingY; //static variables, usable in other methods, important

    private boolean allInputsValid; //used later on to check if the user input is valid

    //Images
    private ImageLoader imageLoader = new ImageLoader();
    //Assign the probe and titan image to normandy and titan respectively
    private Image normandy = imageLoader.getImage("normandy");
    private Image titan = imageLoader.getImage("titan");

    private int pathIndex = 0;

    private ScheduledExecutorService executor;

    private int[][] testPath = {{0, -200}, {-200, 0}, {0, 200}, {200, 0}};

    /**
     * Frame and executor get initialised here
     * A small panel in the bottom right is added for the user to input the coordinates and to launch the landing sequence
     */
    public OrbitScreen() {
        frame = new JFrame("Orbit Screen");

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(1136, 631, 400, 170);

        this.setLayout(null);
        this.setBackground(Color.BLACK);

        //Describing text
        topText = new JLabel("Where would you like to land?");
        topText.setBounds(115, 10, 170, 25);
        panel.add(topText);

        xText = new JLabel("x: ");
        xText.setBounds(30, 40, 80, 25);
        panel.add(xText);

        //User x coordinate input
        xTextInput = new JTextField(30);
        xTextInput.setBounds(50, 40, 320, 25);
        panel.add(xTextInput);

        yText = new JLabel("y: ");
        yText.setBounds(30, 70, 80, 25);
        panel.add(yText);

        //User y coordinate input
        yTextInput = new JTextField(30);
        yTextInput.setBounds(50, 70, 320, 25);
        panel.add(yTextInput);

        //Error message to be printed when an invalid value is inputted for doubles
        errorText = new JLabel("");
        errorText.setBounds(10, 100, 380, 25);
        errorText.setForeground(Color.RED);
        errorText.setHorizontalAlignment(JLabel.CENTER);
        panel.add(errorText);

        //Start button to launch the landing
        startButton = new JButton("Initiate landing sequence!");
        startButton.setBounds(100, 130, 200, 25);
        startButton.addActionListener(this);
        panel.add(startButton);

        frame.add(panel);
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
                showOrbit();
            }
            repaint();
        }, 0, StartScreen.simulationInterval * 1000, TimeUnit.MICROSECONDS);
    }

    /**
     * Shows Titan in the middle, with the probe orbiting it
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        panel.updateUI(); //necessary to keep the panel showing

        //Titan
        g2d.drawImage(titan, XCENTER - 100, YCENTER - 100, 200, 200, null);

        //Probe
        g2d.drawImage(normandy, XCENTER + testPath[pathIndex][0] - 20, YCENTER + testPath[pathIndex][1] - 20, 25, 25, null);
    }

    /**
     * Loops through the probe's path
     * When the end of the path is reached, the index resets to 0, and it loops again.
     */
    public void showOrbit() {
        pathIndex++;

        //Making it loop for now
        if (pathIndex >= testPath.length) {
            pathIndex = 0;
        }

        repaint();
    }

    /**
     * What happens when the button is pressed
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //Base case
        allInputsValid = true;

        //Check the validity of the x and y user input
        checkInputValidity(xTextInput, defaultXCoordinate);
        checkInputValidity(yTextInput, defaultYCoordinate);

        /**
         * If all inputs are valid:
         * Assign the landing coordinates to static variables, usable everywhere;
         * Launch the landing screen and close the orbit screen.
         */
        if(allInputsValid) {
            errorText.setText("");
            landingX = Double.parseDouble(xTextInput.getText());
            landingY = Double.parseDouble(yTextInput.getText());

            LandingScreen landingScreen = new LandingScreen();
            frame.dispose();
            executor.shutdown();
        }
    }

    /**
     * Checks the validity of the user input for the coordinates
     * With this, any input from the user that is not in a double format, will be rejected
     * If the input box is left empty, a default value will be used
     * @param textInput The user text input to be verified
     * @param defaultCoordinate The default coordinate it should use when the text box is left empty
     */
    public void checkInputValidity(JTextField textInput, double defaultCoordinate) {

        //Fills in the default landing coordinate if the text box is left empty
        if(textInput.getText().isEmpty()) {
            textInput.setText(String.valueOf(defaultCoordinate));
        }

        //Checks if the input is valid. If not, displays an error message and resets the value.
        try {
            Double.parseDouble(textInput.getText());
        } catch (NumberFormatException exception) {
            errorText.setText("Please only use numbers, \".\" and \"-\" as inputs for the coordinates.");
            textInput.setText("");
            allInputsValid = false;
        }
    }
}

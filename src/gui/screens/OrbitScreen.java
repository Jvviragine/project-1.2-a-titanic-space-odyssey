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

public class OrbitScreen extends JPanel implements ActionListener {

    private JFrame frame;
    private JPanel panel;
    private JLabel topText, xText, yText, errorText;
    private JTextField xTextInput, yTextInput;
    private JButton startButton;

    //Need to adjust these to be actual default values
    private double defaultXCoordinate = 500000.0;
    private double defaultYCoordinate = 300000.0;

    public static double landingX, landingY;

    private boolean allInputsValid;

    private ImageLoader imageLoader = new ImageLoader();
    //Assign the probe and titan image to normandy and titan respectively
    private Image normandy = imageLoader.getImage("normandy");
    private Image titan = imageLoader.getImage("titan");

    private int pathIndex = 0;

    private ScheduledExecutorService executor;

    private int[][] testPath = {{0, -200}, {-200, 0}, {0, 200}, {200, 0}};

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
        }, 0, StartScreen.simulationSpeed * 10000, TimeUnit.MICROSECONDS);
    }

    @Override
    protected void paintComponent(Graphics g) {
        panel.updateUI();
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //Titan
        g2d.drawImage(titan, XCENTER - 100, YCENTER - 100, 200, 200, null);

        //Probe
        g2d.drawImage(normandy, XCENTER + testPath[pathIndex][0] - 20, YCENTER + testPath[pathIndex][1] - 20, 25, 25, null);
    }

    public void showOrbit() {
        pathIndex++;

        //Making it loop for now
        if (pathIndex >= testPath.length) {
            pathIndex = 0;
        }

        repaint();
    }

    //What happens when the button is pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        //Base case
        allInputsValid = true;

        //Check the validity of the x and y user input
        checkInputValidity(xTextInput, defaultXCoordinate);
        checkInputValidity(yTextInput, defaultYCoordinate);

        /**
         * If all inputs are valid:
         * assign the landing coordinates to a static variable, usable everywhere;
         * launch the landing screen and close the orbit screen
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

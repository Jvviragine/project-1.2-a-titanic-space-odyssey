package gui.mainmenu;

import physics.vectors.StateVector;
import physics.vectors.Vector;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

//Opening screen, allowing user input of initial velocities, positions and simulation speed.
//Also gives the option to freeze the simulation at the inputted time.
public class StartScreen extends JFrame implements ActionListener {
    private JFrame frame = new JFrame();
    private JLabel xText, yText, zText, v1Text, v2Text, v3Text, simulationSpeedText1, simulationSpeedText2, topText01, topText02, errorText;
    private JTextField xInput, yInput, zInput, v1Input, v2Input, v3Input, simulationSpeedInput;
    private double x, y, z, v1, v2, v3, simSpeed;
    //TODO: remove this defaultConditions variable and replace it with getDefaultConditions function from another class
    private double[] defaultConditions = {-148458048.395164, -27524868.1841142, 70233.6499287411, 42.42270135156, -43.62738201925, -3.1328169170, 5};
    private JButton startButton;
    private JCheckBox checkBox;
    private final int FRAME_WIDTH = 600;
    private final int FRAME_HEIGHT = 500;

    public static boolean freezeSimulation = false;       //boolean to see if the simulation should go
    public static int timerInterval = 5;
    public static StateVector initialProbeConditions;

    //The start screen, where the user can input custom values
    public StartScreen() {
        frame = new JFrame("Launch configuration");

        JPanel panel = new JPanel();
        panel.setLayout(null);

        topText01 = new JLabel("Please enter initial positions (x, y, z) in km and velocities (v1, v2, v3) in km/s.");
        topText01.setBounds(30, 10, 500, 25);
        topText01.setHorizontalAlignment(JLabel.CENTER);
        panel.add(topText01);

        topText02 = new JLabel("If all spaces are left empty, default values to reach Titan will be used.");
        topText02.setBounds(30, 30, 500, 25);
        topText02.setHorizontalAlignment(JLabel.CENTER);
        panel.add(topText02);

        //Error message to be printed when an invalid value inputted.
        errorText = new JLabel("");
        errorText.setBounds(30, 425, 500, 25);
        errorText.setForeground(Color.RED);
        errorText.setHorizontalAlignment(JLabel.CENTER);
        panel.add(errorText);

        xText = new JLabel("x: ");
        xText.setBounds(180, 80, 80, 25);
        panel.add(xText);

        xInput = new JTextField(30);
        xInput.setBounds(200, 80, 165, 25);
        panel.add(xInput);

        yText = new JLabel("y: ");
        yText.setBounds(180, 120, 80, 25);
        panel.add(yText);

        yInput = new JTextField(30);
        yInput.setBounds(200, 120, 165, 25);
        panel.add(yInput);

        zText = new JLabel("z: ");
        zText.setBounds(180, 160, 80, 25);
        panel.add(zText);

        zInput = new JTextField(30);
        zInput.setBounds(200, 160, 165, 25);
        panel.add(zInput);

        v1Text = new JLabel("v1: ");
        v1Text.setBounds(180, 200, 80, 25);
        panel.add(v1Text);

        v1Input = new JTextField(30);
        v1Input.setBounds(200, 200, 165, 25);
        panel.add(v1Input);

        v2Text = new JLabel("v2: ");
        v2Text.setBounds(180, 240, 80, 25);
        panel.add(v2Text);

        v2Input = new JTextField(30);
        v2Input.setBounds(200, 240, 165, 25);
        panel.add(v2Input);

        v3Text = new JLabel("v3: ");
        v3Text.setBounds(180, 280, 80, 25);
        panel.add(v3Text);

        v3Input = new JTextField(30);
        v3Input.setBounds(200, 280, 165, 25);
        panel.add(v3Input);

        simulationSpeedText1 = new JLabel("Simulation speed: ");
        simulationSpeedText1.setBounds(130, 310, 160, 25);
        panel.add(simulationSpeedText1);

        simulationSpeedText2 = new JLabel("(higher = slower)");
        simulationSpeedText2.setBounds(130, 325, 160, 25);
        panel.add(simulationSpeedText2);

        simulationSpeedInput = new JTextField(30);
        simulationSpeedInput.setBounds(235, 320, 165, 25);
        panel.add(simulationSpeedInput);

        checkBox = new JCheckBox("Freeze the simulation at the given time.");
        checkBox.setBounds(150, 360, 400, 20);
        panel.add(checkBox);

        startButton = new JButton("Launch!");
        startButton.setBounds(200, 400, 165, 25);
        startButton.addActionListener(this);
        panel.add(startButton);

        frame.add(panel);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //Closes and exits java when the window is closed with the "X" button
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();        // Close the start screen
                System.exit(0);   // Stop Java from running
            }
        });

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JTextField[] userInputs = {xInput, yInput, zInput, v1Input, v2Input, v3Input, simulationSpeedInput};
        double[] finalPositions = {x, y, z};
        double[] finalVelocities = {v1, v2, v3};
        boolean allInputsValid = true;

        if (checkBox.isSelected()) {
            freezeSimulation = true;
            //TODO: implement code to freeze the simulation at the time given in simulationSpeedText
            //Do this by setting a speed variable to 0?
            System.out.println("Jippie! Box is selected.");
        }

        /**
         * Goes through each textField and checks if it is empty.
         * If it is not, tries to convert the text in the textField to double.
         * If not successful, shows the error message and clears the value, else it proceeds.
         */
        for (int i = 0; i < userInputs.length; i++) {
            if (userInputs[i].getText().isEmpty()) {
                //TODO: implement code to use default value for the empty textField
                userInputs[i].setText(String.valueOf(defaultConditions[i]));
            }
            else {
                try {
                    Double.parseDouble(userInputs[i].getText());
                } catch (NumberFormatException exception) {
                    errorText.setText("Please only use numbers, \".\" and \"-\" as inputs.");
                    userInputs[i].setText("");
                    allInputsValid = false;
                }
            }
        }

        /**
         * If all input values are valid:
         * assigns all final values to variables, which are used to define the initialConditions;
         * removes the error text, closes the screen and opens the main GUI.
         */
        if (allInputsValid) {
            //Assign the inputted position to the final positions array
            for (int i = 0; i < finalPositions.length; i++) {
                finalPositions[i] = Double.parseDouble(userInputs[i].getText());
            }

            //Assign the inputted velocities to the final velocities array
            for (int i = 0; i < finalVelocities.length; i++) {
                finalVelocities[i] = Double.parseDouble(userInputs[i + 3].getText());
            }

            Vector initialPosition = new Vector(finalPositions);     //defines the initial positions vector
            Vector initialSpeed = new Vector(finalVelocities);    //defines the initial velocities vector

            //initiate all calculations with the inputted values
            initialProbeConditions = new StateVector(new Vector[]{initialPosition, initialSpeed});

//            //Test to see if the vector is working (it is!) (as far as i know)
//            for(int i = 0; i<2; i++) {
//                for(int j = 0; j<3; j++) {
//                    System.out.println(initialConditions.getVector(i).get(j));
//                }
//            }

            timerInterval = (int) Double.parseDouble(userInputs[6].getText());

            errorText.setText("");      //removes error message
            frame.dispose();        //closes the start screen
            SimulationScreen simulationScreen = new SimulationScreen();
        }
    }
}

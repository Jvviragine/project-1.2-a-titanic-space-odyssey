package gui.screens;

import physics.simulation.SolarSystemPhysicsSimulation;
import physics.simulation.TripSimulation;
import physics.solvers.*;
import physics.vectors.StateVector;
import physics.vectors.Vector;
import solar_system_data.InitialConditions;
import solar_system_data.PlanetaryData;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * Opening screen GUI, allowing user input of initial velocities and positions
 * Also allows the user to pick one of four ODE solvers (Euler's, RK2, RK3 and RK4) used to perform calculations
 * Moreover, the user can pick the step size used in the chosen solvers
 * A custom ending time for the launch simulation is also allowed
 * Finally, the GUI screens after this one all use an executor with an interval in microseconds to update the screen
 * The user is also allowed to alter this time interval
 */
public class StartScreen extends JFrame implements ActionListener {
    //GUI components
    private JFrame frame;
    private JLabel solverText, xText, yText, zText, v1Text, v2Text, v3Text,
                   stepSizeText, simulationEndTimeText, simulationSpeedText1, simulationSpeedText2,
                   topText01, topText02, errorText1, errorText2;
    private JTextField xInput, yInput, zInput, v1Input, v2Input, v3Input,
                   stepSizeInput, simulationEndTimeInput, simulationSpeedInput;
    private JComboBox solverChooser;
    private String[] solverOptions = {"Euler", "RK2", "RK3", "RK4"};
    private JButton startButton;
    private final int FRAME_WIDTH = 580;
    private final int FRAME_HEIGHT = 600;

    //
    private double x, y, z, v1, v2, v3, simSpeed;
    private StateVector probeInitialConditions = InitialConditions.getProbeInitialState();
    private double[] defaultConditions = {probeInitialConditions.getVector(0).get(0),       //x
                                          probeInitialConditions.getVector(0).get(1),       //y
                                          probeInitialConditions.getVector(0).get(2),       //z
                                          probeInitialConditions.getVector(1).get(0),       //v1
                                          probeInitialConditions.getVector(1).get(1),       //v2
                                          probeInitialConditions.getVector(1).get(2),       //v3
                                          1800, 63072000, 500};           //step size, end time and time interval

    //Values to be passed on and used in other functions
    public static boolean freezeSimulation = false;         //boolean that determines if the simulation should go
    public static StateVector initialProbeConditions;       //initial conditions for the probe, inputted by the user
    public static Solver finalSolver;                       //solver chosen by the user
    public static double h;                                 //step size in seconds
    public static int simulationEndTime, simulationInterval;   //end time in seconds, simulation interval in microseconds

    /**
     * Initialisation of the Start Screen GUI
      */
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

        //Error message to be printed when an invalid value is inputted for doubles
        errorText1 = new JLabel("");
        errorText1.setBounds(30, 510, 500, 25);
        errorText1.setForeground(Color.RED);
        errorText1.setHorizontalAlignment(JLabel.CENTER);
        panel.add(errorText1);

        //Error message to be printed when an invalid value is inputted for integers
        errorText2 = new JLabel("");
        errorText2.setBounds(30, 530, 500, 25);
        errorText2.setForeground(Color.RED);
        errorText2.setHorizontalAlignment(JLabel.CENTER);
        panel.add(errorText2);

        //Solver text
        solverText = new JLabel("Solver: ");
        solverText.setBounds(262, 58, 70, 25);
        panel.add(solverText);

        //Solver chooser
        solverChooser = new JComboBox(solverOptions);
        solverChooser.setBounds(182, 78, 200, 25);
        panel.add(solverChooser);

        //Coordinates texts and input fields
        xText = new JLabel("x: ");
        xText.setBounds(185, 120, 80, 25);
        panel.add(xText);

        xInput = new JTextField(30);
        xInput.setBounds(200, 120, 165, 25);
        panel.add(xInput);

        yText = new JLabel("y: ");
        yText.setBounds(185, 160, 80, 25);
        panel.add(yText);

        yInput = new JTextField(30);
        yInput.setBounds(200, 160, 165, 25);
        panel.add(yInput);

        zText = new JLabel("z: ");
        zText.setBounds(185, 200, 80, 25);
        panel.add(zText);

        zInput = new JTextField(30);
        zInput.setBounds(200, 200, 165, 25);
        panel.add(zInput);

        //Velocities texts and input fields
        v1Text = new JLabel("v1: ");
        v1Text.setBounds(180, 240, 80, 25);
        panel.add(v1Text);

        v1Input = new JTextField(30);
        v1Input.setBounds(200, 240, 165, 25);
        panel.add(v1Input);

        v2Text = new JLabel("v2: ");
        v2Text.setBounds(180, 280, 80, 25);
        panel.add(v2Text);

        v2Input = new JTextField(30);
        v2Input.setBounds(200, 280, 165, 25);
        panel.add(v2Input);

        v3Text = new JLabel("v3: ");
        v3Text.setBounds(180, 320, 80, 25);
        panel.add(v3Text);

        v3Input = new JTextField(30);
        v3Input.setBounds(200, 320, 165, 25);
        panel.add(v3Input);

        stepSizeText = new JLabel("Step size (s): ");
        stepSizeText.setBounds(123, 360, 160, 25);
        panel.add(stepSizeText);

        stepSizeInput = new JTextField(30);
        stepSizeInput.setBounds(200, 360, 165, 25);
        panel.add(stepSizeInput);

        simulationEndTimeText = new JLabel("Simulation end time (s): ");
        simulationEndTimeText.setBounds(65, 400, 160, 25);
        panel.add(simulationEndTimeText);

        simulationEndTimeInput = new JTextField(30);
        simulationEndTimeInput.setBounds(200, 400, 165, 25);
        panel.add(simulationEndTimeInput);

        simulationSpeedText1 = new JLabel("Interval between frames: ");
        simulationSpeedText1.setBounds(57, 440, 160, 25);
        panel.add(simulationSpeedText1);

        simulationSpeedText2 = new JLabel("(microseconds)");
        simulationSpeedText2.setBounds(370, 440, 160, 25);
        panel.add(simulationSpeedText2);

        simulationSpeedInput = new JTextField(30);
        simulationSpeedInput.setBounds(200, 440, 165, 25);
        panel.add(simulationSpeedInput);

        //Simulation launch button
        startButton = new JButton("Launch!");
        startButton.setBounds(200, 480, 165, 25);
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
        JTextField[] userInputs = {xInput, yInput, zInput, v1Input, v2Input, v3Input, stepSizeInput, simulationEndTimeInput, simulationSpeedInput};
        double[] finalPositions = {x, y, z};
        double[] finalVelocities = {v1, v2, v3};
        boolean allInputsValid = true;

        /**
         * Goes through each coordinate and velocity textField and checks if it is empty.
         * If it is not, tries to convert the text in the textField to double.
         * If not successful, shows the error message and clears the value, else it proceeds.
         */
        for (int i = 0; i < userInputs.length - 3; i++) {
            if (userInputs[i].getText().isEmpty()) {
                userInputs[i].setText(String.valueOf(defaultConditions[i]));        //uses default values if the box is left empty
            }
            else {
                try {
                    Double.parseDouble(userInputs[i].getText());
                } catch (NumberFormatException exception) {
                    errorText1.setText("Please only use numbers, \".\" and \"-\" as inputs for coordinates and velocities.");
                    userInputs[i].setText("");
                    allInputsValid = false;
                }
            }
            if(allInputsValid) {
                errorText1.setText("");
            }
        }

        //Separate checks for the step size, end time and simulation interval, as they can only be positive integers
        for (int i = 6; i < userInputs.length; i++) {
            if(userInputs[i].getText().isEmpty()) {
                userInputs[i].setText(String.valueOf((int) defaultConditions[i]));        //uses default values if the box is left empty
            }

            else {
                try {
                    Integer.parseInt(userInputs[i].getText());
                    if (Integer.parseInt(userInputs[i].getText()) <= 0) {
                        throw new NumberFormatException();
                    }

                    if((i == 7) && (Integer.parseInt(userInputs[7].getText()) % Integer.parseInt(userInputs[6].getText()) != 0)) {
                        errorText2.setText("Please choose an end time such that it is divisible by the step size.");
                        userInputs[7].setText("");
                        allInputsValid = false;
                    }

                } catch (NumberFormatException exception) {
                    errorText2.setText("Please only use positive integers for the step size, end time and simulation speed.");
                    userInputs[i].setText("");
                    allInputsValid = false;
                }
            }
            if(allInputsValid) {
                errorText2.setText("");
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
            Vector initialSpeed = new Vector(finalVelocities);       //defines the initial velocities vector

            //Initiate probe path calculations with the inputted values
            initialProbeConditions = new StateVector(new Vector[]{initialPosition, initialSpeed});
            InitialConditions.setProbeInitialPosition(initialPosition);
            InitialConditions.setProbeInitialVelocity(initialSpeed);

            //Sets the solver
            int selectedSolverOption = solverChooser.getSelectedIndex();
            switch(selectedSolverOption) {
                case 0:
                    finalSolver = new EulerSolver();
                    break;
                case 1:
                    finalSolver = new RK2Solver();
                    break;
                case 2:
                    finalSolver = new RK3Solver();
                    break;
                case 3:
                    finalSolver = new RK4Solver();
                    break;
            }

            //Sets the step size
            h = Math.ceil(Double.parseDouble(userInputs[6].getText()));

            //Sets the simulation end time
            simulationEndTime = (int) Math.ceil(Double.parseDouble(userInputs[7].getText()));

            //Sets the simulation interval
            simulationInterval = (int) Math.ceil(Double.parseDouble(userInputs[8].getText()));

            //Initialise the physics simulation with the user inputted vectors
            SolarSystemPhysicsSimulation system = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames(),finalSolver);

            TripSimulation tripSimulation = new TripSimulation();
            errorText1.setText("");      //removes error messages
            errorText2.setText("");
            frame.dispose();        //closes the start screen
            SimulationScreen simulationScreen = new SimulationScreen();     //starts the simulation screen
        }
    }
}
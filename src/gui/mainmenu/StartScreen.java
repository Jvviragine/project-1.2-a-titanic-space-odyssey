package gui.mainmenu;

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
    private JLabel xText, yText, zText, v1Text, v2Text, v3Text, simulationSpeedText, topText01, topText02, errorText;
    private JTextField xInput, yInput, zInput, v1Input, v2Input, v3Input, simulationSpeedInput;
    private JButton startButton;
    private JCheckBox checkBox;
    private final int FRAME_WIDTH = 600;
    private final int FRAME_HEIGHT = 500;

    //The start screen, where the user can input custom values
    public StartScreen() {
        frame = new JFrame();

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
        xText.setBounds(180,80,80,25);
        panel.add(xText);

        xInput = new JTextField(30);
        xInput.setBounds(200,80,165,25);
        panel.add(xInput);

        yText = new JLabel("y: ");
        yText.setBounds(180,120,80,25);
        panel.add(yText);

        yInput = new JTextField(30);
        yInput.setBounds(200,120,165,25);
        panel.add(yInput);

        zText = new JLabel("z: ");
        zText.setBounds(180,160,80,25);
        panel.add(zText);

        zInput = new JTextField(30);
        zInput.setBounds(200,160,165,25);
        panel.add(zInput);

        v1Text = new JLabel("v1: ");
        v1Text.setBounds(180,200,80,25);
        panel.add(v1Text);

        v1Input = new JTextField(30);
        v1Input.setBounds(200,200,165,25);
        panel.add(v1Input);

        v2Text = new JLabel("v2: ");
        v2Text.setBounds(180,240,80,25);
        panel.add(v2Text);

        v2Input = new JTextField(30);
        v2Input.setBounds(200,240,165,25);
        panel.add(v2Input);

        v3Text = new JLabel("v3: ");
        v3Text.setBounds(180,280,80,25);
        panel.add(v3Text);

        v3Input = new JTextField(30);
        v3Input.setBounds(200,280,165,25);
        panel.add(v3Input);

        simulationSpeedText = new JLabel("Simulation speed: ");
        simulationSpeedText.setBounds(130,320,160,25);
        panel.add(simulationSpeedText);

        simulationSpeedInput = new JTextField(30);
        simulationSpeedInput.setBounds(235,320,165,25);
        panel.add(simulationSpeedInput);

        checkBox = new JCheckBox("Freeze the simulation at the given time.");
        checkBox.setBounds(150, 360, 400, 20);
        panel.add(checkBox);

        startButton = new JButton("Go to Titan!");
        startButton.setBounds(200, 400, 165, 25);
        startButton.addActionListener(this);
        panel.add(startButton);

        frame.add(panel);
        frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //Closes and stops java from running when the window is closed with the "X" button
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose(); // Close the start screen
                System.exit(0); // Stop Java from running
            }
        });

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JTextField[] userInputs = {xInput, yInput, zInput, v1Input, v2Input, v3Input, simulationSpeedInput};
        boolean allInputsValid = true;

        if(checkBox.isSelected()) {
            //TODO: implement code to freeze the simulation at the time given in simulationSpeedText
            //Do this by setting a speed variable to 0?
            System.out.println("Jippie! Box is selected.");
        }

        //Goes through each textField and checks if it is empty.
        //If it is not, tries to convert the text in the textField to double.
        //If not successful, shows the error message and clears the value, else it proceeds.
        for(JTextField i : userInputs) {
            if (i.getText().isEmpty()) {
                //TODO: implement code to use default value for the empty textField
                //i.setText(actualValue);
                System.out.println("Empty test successful");    //testing purposes
            } else {
                try {
                    Double.parseDouble(i.getText());
                } catch (NumberFormatException exception) {
                    errorText.setText("Please only use numbers, \".\" and \"-\" as inputs.");
                    i.setText("");
                    allInputsValid = false;
                }
                //TODO: implement values from the physics team
                System.out.println(i.getText());                //testing purposes
            }
        }

        //If all input values are valid, removes the error text, closes the screen and opens the main GUI.
        if(allInputsValid) {
            errorText.setText("");      //removes error message
            frame.dispose();        //closes the start screen
            Viewer mainGUI = new Viewer();        //opens the main GUI
        }
    }
}

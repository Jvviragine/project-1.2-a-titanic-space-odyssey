package gui.mainmenu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class StartScreen extends JFrame implements ActionListener {
    //opening screen allowing user input of initial velocities and positions
    JFrame frame = new JFrame();
    JLabel x, y, z, v1, v2, v3, simulationSpeed, topText01, topText02, errorText;
    JTextField xText, yText, zText, v1Text, v2Text, v3Text, simulationSpeedText;
    JButton startButton;
    JCheckBox checkBox;
    final int FRAME_WIDTH = 600;
    final int FRAME_HEIGHT = 500;

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

        x = new JLabel("x: ");
        x.setBounds(180,80,80,25);
        panel.add(x);

        xText = new JTextField(30);
        xText.setBounds(200,80,165,25);
        panel.add(xText);

        y = new JLabel("y: ");
        y.setBounds(180,120,80,25);
        panel.add(y);

        yText = new JTextField(30);
        yText.setBounds(200,120,165,25);
        panel.add(yText);

        z = new JLabel("z: ");
        z.setBounds(180,160,80,25);
        panel.add(z);

        zText = new JTextField(30);
        zText.setBounds(200,160,165,25);
        panel.add(zText);

        v1 = new JLabel("v1: ");
        v1.setBounds(180,200,80,25);
        panel.add(v1);

        v1Text = new JTextField(30);
        v1Text.setBounds(200,200,165,25);
        panel.add(v1Text);

        v2 = new JLabel("v2: ");
        v2.setBounds(180,240,80,25);
        panel.add(v2);

        v2Text = new JTextField(30);
        v2Text.setBounds(200,240,165,25);
        panel.add(v2Text);

        v3 = new JLabel("v3: ");
        v3.setBounds(180,280,80,25);
        panel.add(v3);

        v3Text = new JTextField(30);
        v3Text.setBounds(200,280,165,25);
        panel.add(v3Text);

        simulationSpeed = new JLabel("Simulation speed: ");
        simulationSpeed.setBounds(130,320,160,25);
        panel.add(simulationSpeed);

        simulationSpeedText = new JTextField(30);
        simulationSpeedText.setBounds(235,320,165,25);
        panel.add(simulationSpeedText);

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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JTextField[] userInputs = {xText, yText, zText, v1Text, v2Text, v3Text, simulationSpeedText};
        boolean allInputsValid = true;

        if(checkBox.isSelected()) {
            //TODO: implement code to freeze the simulation at the time given in simulationSpeedText
            //Do this by setting a speed variable to 0?
        }

        //Goes through each textField and checks if it is empty.
        //If it is not, tries to convert the text in the textField to double.
        //If not successful, shows the error message and clears the value, else it proceeds.
        for(JTextField i : userInputs) {
            if (i.getText().isEmpty()) {
                //TODO: implement code to use default value for the empty textField
                //i.setText(actualValue);
                System.out.println("Empty test successful");
            } else {
                try {
                    Double.parseDouble(i.getText());
                } catch(NumberFormatException exception) {
                    errorText.setText("Please only use numbers, \".\" and \"-\" as inputs.");
                    i.setText("");
                    allInputsValid = false;
                }

                //If all input values are valid, remove the error text
                //Maybe not necessary if we eventually just want to remove the startScreen
                if(allInputsValid) {
                    errorText.setText("");
                }

                System.out.println(i.getText());
            }
        }
    }
}

package gui.mainmenu;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    private JFrame frame;
    private final int SCREEN_WIDTH = 1920;
    private final int SCREEN_HEIGHT = 1080;
    public MainGUI() {
        frame = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(null);

        System.out.println(SCREEN_WIDTH);
        System.out.println(SCREEN_HEIGHT);

        frame.add(panel);
        frame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}

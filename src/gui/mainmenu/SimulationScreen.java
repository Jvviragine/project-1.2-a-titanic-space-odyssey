package gui.mainmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationScreen extends JPanel {
    private JFrame frame;
    private final int SCREEN_WIDTH = 1536;
    private final int SCREEN_HEIGHT = 801;
    private final int XCENTER = SCREEN_WIDTH/2;
    private final int YCENTER = SCREEN_HEIGHT/2;

    private int currentIndex = 0;
    private Timer timer;

    private OrbitList orbitList = new OrbitList();
    private int[][] sunPath = orbitList.getPath(0);
    private int[][] mercuryPath = orbitList.getPath(1);
    private int[][] venusPath = orbitList.getPath(2);
    private int[][] earthPath = orbitList.getPath(3);
    private int[][] moonPath = orbitList.getPath(4);
    private int[][] marsPath = orbitList.getPath(5);
    private int[][] jupiterPath = orbitList.getPath(6);
    private int[][] saturnPath = orbitList.getPath(7);
    private int[][] titanPath = orbitList.getPath(8);
    private int[][] uranusPath = orbitList.getPath(9);
    private int[][] neptunePath = orbitList.getPath(10);
    private int[][][] allPaths = {sunPath, mercuryPath, venusPath, earthPath, moonPath, marsPath, jupiterPath, saturnPath, titanPath, uranusPath, neptunePath};

    public SimulationScreen() {
        frame = new JFrame("Simulation Screen");

        this.setLayout(null);
        this.setBackground(Color.BLACK);

        frame.add(this);
        frame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Create a timer with a delay of 1 millisecond
        timer = new Timer(StartScreen.timerInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!StartScreen.freezeSimulation) {
                    iterateThroughOrbit();
                }
            }
        });

        // Start the timer
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //Sun
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(XCENTER + sunPath[currentIndex][0] - 20, YCENTER + sunPath[currentIndex][1] - 20, 40, 40);
        g2d.drawString("Sun", XCENTER + sunPath[currentIndex][0] - 10, YCENTER + sunPath[currentIndex][1] + 30);

        //Mercury
        g2d.setColor(Color.GRAY);
        g2d.fillOval(XCENTER + mercuryPath[currentIndex][0] - 10,  YCENTER + mercuryPath[currentIndex][1] - 10, 20, 20);
        g2d.drawString("Mercury", XCENTER + mercuryPath[currentIndex][0] - 20, YCENTER + mercuryPath[currentIndex][1] + 20);

        //Venus
        g2d.setColor(Color.ORANGE);
        g2d.fillOval(XCENTER + venusPath[currentIndex][0] - 10,  YCENTER + venusPath[currentIndex][1] - 10, 20, 20);
        g2d.drawString("Venus", XCENTER + venusPath[currentIndex][0] - 16, YCENTER + venusPath[currentIndex][1] + 20);

        //Earth
        g2d.setColor(Color.BLUE);
        g2d.fillOval(XCENTER + earthPath[currentIndex][0] - 10,  YCENTER + earthPath[currentIndex][1] - 10, 20, 20);
        g2d.drawString("Earth", XCENTER + earthPath[currentIndex][0] - 14, YCENTER + earthPath[currentIndex][1] + 20);

        //Moon
        g2d.setColor(Color.WHITE);
        g2d.fillOval(XCENTER + moonPath[currentIndex][0] - 10,  YCENTER + moonPath[currentIndex][1] - 10, 10, 10);
        g2d.drawString("Moon", XCENTER + moonPath[currentIndex][0] - 20, YCENTER + moonPath[currentIndex][1] + 10);

        //Mars
        g2d.setColor(Color.RED);
        g2d.fillOval(XCENTER + marsPath[currentIndex][0] - 10,  YCENTER + marsPath[currentIndex][1] - 10, 20, 20);
        g2d.drawString("Mars", XCENTER + marsPath[currentIndex][0] - 12, YCENTER + marsPath[currentIndex][1] + 20);

        //Jupiter
        g2d.setColor(Color.ORANGE);
        g2d.fillOval(XCENTER + jupiterPath[currentIndex][0] - 10,  YCENTER + jupiterPath[currentIndex][1] - 10, 20, 20);
        g2d.drawString("Jupiter", XCENTER + jupiterPath[currentIndex][0] - 16, YCENTER + jupiterPath[currentIndex][1] + 20);

        //Saturn
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(XCENTER + saturnPath[currentIndex][0] - 10,  YCENTER + saturnPath[currentIndex][1] - 10, 20, 20);
        g2d.drawString("Saturn", XCENTER + saturnPath[currentIndex][0] - 13, YCENTER + saturnPath[currentIndex][1] + 20);

        //Titan
        g2d.setColor(Color.WHITE);
        g2d.fillOval(XCENTER + titanPath[currentIndex][0] - 10,  YCENTER + titanPath[currentIndex][1] - 10, 10, 10);
        g2d.drawString("Titan", XCENTER + titanPath[currentIndex][0] - 20, YCENTER + titanPath[currentIndex][1] + 10);

        //Uranus (doesn't show up because the GUI was scaled to Saturn's max distance)
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillOval(XCENTER + uranusPath[currentIndex][0] - 10,  YCENTER + uranusPath[currentIndex][1] - 10, 20, 20);
        g2d.drawString("Uranus", XCENTER + uranusPath[currentIndex][0] - 13, YCENTER + uranusPath[currentIndex][1] + 20);

        //Neptune (doesn't show up because the GUI was scaled to Saturn's max distance)
        g2d.setColor(Color.BLUE);
        g2d.fillOval(XCENTER + neptunePath[currentIndex][0] - 10,  YCENTER + neptunePath[currentIndex][1] - 10, 20, 20);
        g2d.drawString("Neptune", XCENTER + neptunePath[currentIndex][0] - 13, YCENTER + neptunePath[currentIndex][1] + 20);
    }

    public void iterateThroughOrbit() {
        currentIndex++;

        for(int i = 0; i < allPaths.length; i++) {

            if (currentIndex >= mercuryPath.length) {
                currentIndex = 0;
            }

            repaint();
        }

    }
}

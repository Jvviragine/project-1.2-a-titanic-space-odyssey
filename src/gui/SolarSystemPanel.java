package gui;

import celestial_bodies.CelestialBody;
import physics.vectors.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;

public class SolarSystemPanel extends JPanel {

    public SolarSystemPanel() {

        for (CelestialBody body : CelestialBody.bodies) {
            add(body.getCelestialBodyLabel());
        }

    }
}

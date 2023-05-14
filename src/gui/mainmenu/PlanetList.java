package gui.mainmenu;

import java.awt.*;
import java.util.ArrayList;

public class PlanetList {
    static class Planet {
        public int x, y, z, diameter;   //planetary data
        public int centerX, centerY, radius;    //center point and radius of the orbit
        public double angle;    //angle of the planet

        public Planet(int centerX, int centerY, int radius, int diameter, double angle) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
            this.diameter = diameter;
            this.angle = angle;
        }
    }

    ArrayList<Planet> planets;

    public PlanetList() {
        planets = new ArrayList<Planet>();
    }

    public void addPlanet(Planet planet) {
        planets.add(planet);
    }

    public void draw(Graphics2D g) {
        for(Planet p : planets) {
            p.x = p.centerX + (int) (p.radius * Math.cos(p.angle));
            p.y = p.centerY + (int) (p.radius * Math.sin(p.angle));

            g.fillOval(p.x, p.y, p.diameter, p.diameter);

            p.angle += 0.05;
        }
    }
}

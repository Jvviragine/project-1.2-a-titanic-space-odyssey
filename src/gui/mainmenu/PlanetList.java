package gui.mainmenu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlanetList {
    private ArrayList<Planet> planets;
    private ArrayList<Planet> moons;
    private final int SCREEN_WIDTH = 1536;
    private final int SCREEN_HEIGHT = 801;
    private Image[] planetImages = {
                                    ImageIO.read(new File("src/gui/mainmenu/images/Sun.png")),
                                    ImageIO.read(new File("src/gui/mainmenu/images/Venus.png")),
                                    ImageIO.read(new File("src/gui/mainmenu/images/Earth.png")),
                                    ImageIO.read(new File("src/gui/mainmenu/images/Mars.png")),
                                    ImageIO.read(new File("src/gui/mainmenu/images/Jupiter.png")),
                                    ImageIO.read(new File("src/gui/mainmenu/images/Saturn.png")),
                                    ImageIO.read(new File("src/gui/mainmenu/images/Titan.png")),
                                    ImageIO.read(new File("src/gui/mainmenu/images/Moon.png")),
                                    };
    static class Planet {
        public int x, y, z, diameter;           //planetary data
        public int centerX, centerY, radius;    //center point and radius of the orbit
        public double angle;                    //angle of the planet
        public String planetName;               //name of the planet

        public Planet(int centerX, int centerY, int radius, int diameter, double angle, String planetName) throws IOException {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
            this.diameter = diameter;
            this.angle = angle;
            this.planetName = planetName;
        }
    }

    public PlanetList() throws IOException {
        planets = new ArrayList<Planet>();
    }

    public void addPlanet(Planet planet) {
        planets.add(planet);
    }

    public void addMoon(Planet moon) {
        moons.add(moon);
    }

    public void draw(Graphics2D g, int screenWidth, int screenHeight) {
        int counter = 1;

        g.setColor(Color.YELLOW);
        g.drawImage(planetImages[0], screenWidth/2-15, screenHeight/2-15, 30, 30, null);
        g.drawString("Sun", screenWidth/2 - 10, screenHeight/2 + 23);

        g.setColor(Color.CYAN);

        for(Planet p : planets) {
            p.x = p.centerX + (int) (p.radius * Math.cos(p.angle));
            p.y = p.centerY + (int) (p.radius * Math.sin(p.angle));

            g.drawImage(planetImages[counter], p.x, p.y, p.diameter, p.diameter, null);
            g.drawString(p.planetName, p.x-5, p.y + p.diameter + 10);

            counter++;
            p.angle += 0.01;
        }

        for(Planet m : moons) {

        }
    }
}

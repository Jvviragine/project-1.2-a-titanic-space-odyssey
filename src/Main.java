import celestial_bodies.CelestialBody;
import celestial_bodies.SolarSystemPhysicsSimulation;
import gui.mainmenu.StartScreen;
import physics.vectors.StateVector;
import physics.vectors.Vector;
import solar_system_data.PlanetaryData;

import java.io.IOException;

// Press ⇧ twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException {

        StartScreen startScreen = new StartScreen();

        SolarSystemPhysicsSimulation system = new SolarSystemPhysicsSimulation(PlanetaryData.getCCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames());

        //Based on input from GUI (how many steps to take)
        //system.updateState();

    }
}
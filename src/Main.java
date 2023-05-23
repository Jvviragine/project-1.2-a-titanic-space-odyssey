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
    public static void main(String[] args) {

        SolarSystemPhysicsSimulation system = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames());

        //Use system.simulateCelestialBodiesOrbit(tf,h) to get list of lists of paths
        //System.out.println(system.simulateCelestialBodiesOrbit(31536000,360).toString());

        StartScreen startScreen = new StartScreen();



    }
}
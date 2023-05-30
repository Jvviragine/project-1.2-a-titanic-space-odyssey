import physics.simulation.SolarSystemPhysicsSimulation;
import gui.screens.StartScreen;
import physics.simulation.TripSimulation;
import solar_system_data.PlanetaryData;

// Press ⇧ twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        //SolarSystemPhysicsSimulation system = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames());
        TripSimulation sim = new TripSimulation();

        StartScreen startScreen = new StartScreen();

    }
}
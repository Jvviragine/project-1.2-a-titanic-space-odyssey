import celestial_bodies.CelestialBody;
import gui.mainmenu.StartScreen;
import solar_system_data.PlanetaryData;

// Press ⇧ twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        StartScreen startScreen = new StartScreen();

        //initialize the celestial bodies with PlanetaryData
        CelestialBody venus = new CelestialBody(null, null, 0, null);
        CelestialBody earth = new CelestialBody(null, null, 0, null);
        CelestialBody moon = new CelestialBody(null, null, 0, null);
        CelestialBody mars = new CelestialBody(null, null, 0, null);
        CelestialBody jupiter = new CelestialBody(null, null, 0, null);
        CelestialBody saturn = new CelestialBody(null, null, 0, null);
        CelestialBody titan = new CelestialBody(null, null, 0, null);

    }
}
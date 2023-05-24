package gui.mainmenu;

import celestial_bodies.SolarSystemPhysicsSimulation;
import physics.vectors.StateVector;
import solar_system_data.PlanetaryData;

import java.util.List;

/**
 * Separates and assigns all orbits to the different planets
 * These will then be passed on to the simulationScreen class
 * They can then be displayed without calculating anything more.
 */
public class OrbitList {
    private static final int SCREEN_WIDTH = 1536;
    private static final int SCREEN_HEIGHT = 801;

    private static SolarSystemPhysicsSimulation simulation = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames());
    private static List<List<StateVector>> planetPaths = simulation.simulateCelestialBodiesOrbit(31536000,3600);

    private static double saturnMaxDistance = getMaxDistanceFromSun(1253801723.95465, -760453007.810989);
    final private static double scale = Math.min(SCREEN_WIDTH, SCREEN_HEIGHT) / (2 * saturnMaxDistance);

    public static int[][] getPath(int index){

        int[][] path = new int[planetPaths.get(index).size()][2];

        for(int i = 0; i < planetPaths.get(index).size(); i++){
            path[i][0] = (int) (scale * planetPaths.get(index).get(i).getVector(0).get(0));
            path[i][1] = (int) (scale * planetPaths.get(index).get(i).getVector(0).get(1));
        }
        return path;
    }

    public static double getMaxDistanceFromSun(double x, double y) {
        double maxDistance = Math.hypot(x, y);
        return maxDistance;
    }
}


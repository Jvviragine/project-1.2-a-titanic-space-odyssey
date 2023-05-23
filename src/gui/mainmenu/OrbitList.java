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
    private static SolarSystemPhysicsSimulation simulation = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames());
    private static List<List<StateVector>> planetPaths = simulation.simulateCelestialBodiesOrbit(31536000,360);
    private static StateVector sunPath = planetPaths.get(0).get(0);       //gets the first x coordinate for venus
    private static int[][] examplePath1 = {{400, 400}, {300, 300}, {400, 200}, {500, 300}};
    private static int[][] examplePath2 = {{300, 400}, {200, 300}, {300, 200}, {400, 300}};

    public static int[][] convertStateVectorToArray(StateVector stateVector) {
        int[][] planetPath = new int[stateVector.getNumberOfVectors()][2];
        for(int i = 0; i < planetPath.length; i++) {
            for(int j = 0; j < planetPath[i].length; j++) {
                if(j == 0) {
                    planetPath[i][j] = (int) stateVector.getVector(i).get(0);       //assigns x coordinate as an int
                    System.out.println(planetPath[i][j]);
                }
                else {
                    planetPath[i][j] = (int) stateVector.getVector(i).get(1);       //assigns y coordinate as an int
                    System.out.println(planetPath[i][j]);
                }
            }
        }
        return planetPath;
    }

    public static int[][] getPath(int index){
        for(int i = 0; i < planetPaths.get(0).size(); i++){
            
        }
    }
    public static int[][] getExamplePath1() {
        return examplePath1;
    }
    public static int[][] getExamplePath2() {
        return examplePath2;
    }
    public static int[][] getSunPath() {
        return convertStateVectorToArray(sunPath);
    }
}


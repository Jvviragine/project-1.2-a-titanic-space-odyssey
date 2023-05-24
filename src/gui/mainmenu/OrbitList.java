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
    private static StateVector sunPath = planetPaths.get(0).get(0);             //gets the sun state vector
    private static StateVector mercuryPath = planetPaths.get(1).get(0);         //gets the mercury state vector
    private static int[][] examplePath1 = {{400, 400}, {300, 300}, {400, 200}, {500, 300}};
    private static int[][] examplePath2 = {{300, 400}, {200, 300}, {300, 200}, {400, 300}};

    //Converts the state vector of a planet to an array
    public static int[][] convertStateVectorToArray(StateVector stateVector, int indexSize) {
        //TODO: this method gives me the first two values of each of the chosen planet's vectors within its state vector;
        // -thus also gives velocities which i don't need
        // -figure out how to get all the x and y values so that nothing goes out of bounds anymore

        int planetPathSize = planetPaths.get(indexSize).size();     //defines the size of the full stateVector and sets it equal to the planets stateVector size

        int[][] planetPath = new int[planetPathSize][2];

        for(int i = 0; i < planetPath.length; i++) {
            for(int j = 0; j < planetPath[i].length; j++) {
                if(j == 0) {
                    planetPath[i][j] = (int) stateVector.getVector(i).get(0);       //assigns x coordinate as an int
                    System.out.println(planetPath[i][j]);
                    System.out.println(planetPath.length);
                    System.out.println(planetPathSize);
                }
                if(j == 1) {
                    planetPath[i][j] = (int) stateVector.getVector(i).get(1);       //assigns y coordinate as an int
                    System.out.println(planetPath[i][j]);
                }
            }
        }
        return planetPath;
    }

//    public static int[][] getPath(int index){
//        for(int i = 0; i < planetPaths.size(); i++){        //removed the .get(0), should be good like this right?
//
//        }
//    }

    public static double[][] getPath(int index){

        double[][] path = new double[planetPaths.get(index).size()][2];

        for(int i = 0; i < planetPaths.get(index).size(); i++){
            path[i][0] = planetPaths.get(index).get(i).getVector(0).get(0);
            path[i][1] = planetPaths.get(index).get(i).getVector(0).get(1);
        }

        return path;
    }

    public static int[][] getExamplePath1() {
        return examplePath1;
    }

    public static int[][] getExamplePath2() {
        return examplePath2;
    }

    public static int[][] getSunPath() {
        return convertStateVectorToArray(sunPath, 0);
    }

    public static int[][] getMercuryPath() {
        return convertStateVectorToArray(mercuryPath, 1);
    }


}


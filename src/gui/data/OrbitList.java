package gui.data;

import physics.controllers.FeedbackController;
import physics.controllers.LanderState;
import physics.simulation.TripSimulation;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import java.util.List;

import static physics.controllers.FeedbackController.G;

/**
 * Separates, scales and assigns all orbits to the different planets
 * These will then be called in the simulationScreen class
 * They can then be displayed without calculating anything more.
 */
public class OrbitList {
    private static final int SCREEN_WIDTH = 1536;
    private static final int SCREEN_HEIGHT = 801;

    private static TripSimulation sim = new TripSimulation();
    private static List<List<StateVector>> planetPaths = sim.simulateTrip();

    private static StateVector s = new StateVector(new Vector[]{new Vector(new double[]{-100, 50,0}), new Vector(new double[]{0,0,0})});
    private static LanderState l = new LanderState(s, 10*G, 0);
    private static FeedbackController controller = new FeedbackController(l);
    //private static OpenLoopController c = new OpenLoopController(s, new Vector(new double[]{0,0}),0,50, 1);


    private static double[][] initialLandingPath = controller.getPath();
    //private static double[][] initialLandingPath = c.getPath();

    private static double saturnMaxDistance = getDistanceFromSun(1253801723.95465, -760453007.810989);
    final private static double scale = Math.min(SCREEN_WIDTH, SCREEN_HEIGHT) / (1.5 * saturnMaxDistance);
    final private static double landingScale = 6.0;

    /**
     * Gets the path of the planet with index "index"
     * @param index: the index of the planet we want the path from
     * @return a 2d integer array, where each index contains scaled x and y coordinates
     */
    public static int[][] getPath(int index){

        int[][] path = new int[planetPaths.get(index).size()][2];

        for(int i = 0; i < planetPaths.get(index).size(); i++){
            path[i][0] = (int) (scale * planetPaths.get(index).get(i).getVector(0).get(0));
            path[i][1] = (int) (scale * planetPaths.get(index).get(i).getVector(0).get(1));
        }
        return path;
    }

    /**
     * Gets the landing path of the probe
     * @return
     */
    public static int[][] getLandingPath() {
        int[][] scaledLandingPath = new int[initialLandingPath.length][2];

        for(int i = 0; i < initialLandingPath.length; i++) {
            scaledLandingPath[i][0] = (int) (landingScale * initialLandingPath[i][0]);
            scaledLandingPath[i][1] = (int) (landingScale * initialLandingPath[i][1]);
        }

        return scaledLandingPath;
    }

    /**
     * Gets the distance from the sun for given coordinates
     * @param x: inputted x coordinate
     * @param y: inputted y coordinate
     * @return the distance from the sun to the inputted (x,y) coordinates
     */
    public static double getDistanceFromSun(double x, double y) {
        double maxDistance = Math.hypot(x, y);
        return maxDistance;
    }
}


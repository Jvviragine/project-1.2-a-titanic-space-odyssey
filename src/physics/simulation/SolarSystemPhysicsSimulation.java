package physics.simulation;

import physics.functions.DerivativeFunction;
import physics.solvers.*;
import physics.vectors.StateVector;
import physics.vectors.Vector;
import solar_system_data.InitialConditions;
import solar_system_data.PlanetaryData;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for performing the Physics simulation of our Trip to Titan
 * The GUI will know nothing about how the Physics works; it will only receive the paths of each object and display it
 */
public class SolarSystemPhysicsSimulation {

    // Instance Fields
    private double[] masses; // Masses of each Celestial Body
    private StateVector[] stateVectors;

    private StateVector[] initialStates;

    private List<List<StateVector>> allStates = new ArrayList<>();

    private String[] names;

    public final double G = 6.6743*Math.pow(10,-20);

    private double t;

    private double h;

    private DerivativeFunction df;

    private Solver solver;

    public SolarSystemPhysicsSimulation(StateVector[] stateVectors, double[] masses, String[] names){
        this.stateVectors = stateVectors;
        this.masses = masses;
        this.names = names;
        this.df = new DerivativeFunction(this);
        this.solver = new RK4Solver();
        t = 0;
        initialiseStates();
    }

    public SolarSystemPhysicsSimulation(StateVector[] stateVectors, double[] masses, String[] names,Solver solver){
        this.stateVectors = stateVectors;
        this.masses = masses;
        this.names = names;
        this.df = new DerivativeFunction(this);
        this.solver = solver;
        t = 0;
        initialiseStates();
    }

    public double[] getMasses(){
        return masses;
    }

    /**
     * Initialises the allStates array that holds all current and past positions of the bodies in the system
     */
    public void initialiseStates(){
        initialStates = new StateVector[stateVectors.length];

        for(int i = 0; i < stateVectors.length; i++){
            List <StateVector> state = new ArrayList<>();
            state.add(stateVectors[i]);
            allStates.add(state);
            initialStates[i] = stateVectors[i];
        }
    }

    /**
     * (Re)Sets new state vectors
     * @param stateVectors
     */
    public void setStateVectors(StateVector [] stateVectors){
        this.stateVectors = stateVectors;
    }

    /**
     * Returns all current state vectors (at time of request)
     * @return stateVectors array
     */
    public StateVector[] getStateVectors(){
        return stateVectors;
    }

    /**
     * Updates the state of the system (only celestial bodies) given a final time, and step size,h
     * @param tf the final time after all updates
     * @param h the time difference between each update
     * @return
     */
    public List<List<StateVector>> simulateCelestialBodiesOrbit(double tf,double h){

        //List containing all orbits of all planets
        List<List<StateVector>> orbits = new ArrayList<>();

        solver.solve(df,stateVectors,0,tf,h);

        for(int i = 0; i< stateVectors.length; i++){
            orbits.add(solver.getAllStates(i));
        }

        this.t = tf;
        this.h = h;

        allStates = orbits;

        return orbits;
    }

    public List<List<StateVector>> simulateOrbitsWithProbe(StateVector initialProbeState, double tf,double h){
        StateVector [] vectorsWithProbe = new StateVector[stateVectors.length+1];

        for(int i = 0; i < stateVectors.length; i++){
            vectorsWithProbe[i] = stateVectors[i];
        }
        vectorsWithProbe[vectorsWithProbe.length -1] = initialProbeState;

        stateVectors = vectorsWithProbe;

        //List containing all orbits of all planets
        List<List<StateVector>> orbits = new ArrayList<>();

        solver.solve(df,stateVectors,0,tf,h);

        for(int i = 0; i< stateVectors.length; i++){
            orbits.add(solver.getAllStates(i));
        }

        StateVector [] restoreStateVectors = new StateVector[vectorsWithProbe.length-1];

        for(int i = 0; i < restoreStateVectors.length;i++){
            restoreStateVectors[i] = stateVectors[i];
        }

        stateVectors = restoreStateVectors;

        this.t = tf;
        this.h = h;
        allStates = orbits;

        return orbits;

    }


    /**
     * List containing the List of StateVectors for each of the bodies in the system
     * @return StateVector 2-D list
     */
    public List<List<StateVector>> getPath(){
        return allStates;
    }

    /**
     *Gets the index of a particular StateVector
     * @param v the StateVector of which index is to be found
     * @return integer index of StateVector v in the stateVectors array
     */
    public int getIndex(StateVector v){

        for(int i = 0; i < stateVectors.length; i++){
            if(v.isEqual(stateVectors[i])){
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns names of all bodies in the system
     * @return string array containing all body names
     */
    public String[] getBodyNames(){
        return names;
    }

    /**
     * Returns the total number of bodies in the system
     * @return integer number of bodies
     */
    public int totalBodies(){
        return names.length;
    }

    // MAIN for Testing
    public static void main(String[] args) {
        /*
        double [] vector1 = new double[]{1,2,3};
        double [] vector2 = new double[]{4,5,6};
        Vector u = new Vector(vector1);
        Vector v = new Vector(vector2);
        Vector[] vectors1 = new Vector[]{u,v};
        StateVector sv1 = new StateVector(vectors1);

        double [] vector3 = new double[]{7,8,9};
        double [] vector4 = new double[]{10,11,12};
        Vector s = new Vector(vector3);
        Vector t = new Vector(vector4);
        Vector[] vectors2 = new Vector[]{s,t};
        StateVector sv2 = new StateVector(vectors2);

        StateVector[] svs = new StateVector[]{sv1,sv2};

        double[] masses = new double[]{50,100};

        String [] planets = new String[]{"planet1","planet2"};

        SolarSystemPhysicsSimulation mySystem = new SolarSystemPhysicsSimulation(svs,masses,planets);

         */

        // Simulate Celestial Orbits for RunTime Experiment

        // Instance of the Solvers
        Solver euler = new EulerSolver();
        Solver rk2 = new RK2Solver();
        Solver rk3 = new RK3Solver();
        Solver rk4 = new RK4Solver();

        // Instace of the Solar Systems
        SolarSystemPhysicsSimulation mySolarSystem_Euler = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(), PlanetaryData.getCelestialBodiesMasses(), PlanetaryData.getCelestialBodyNames(), euler);
        SolarSystemPhysicsSimulation mySolarSystem_RK2 = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(), PlanetaryData.getCelestialBodiesMasses(), PlanetaryData.getCelestialBodyNames(), rk2);
        SolarSystemPhysicsSimulation mySolarSystem_RK3 = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(), PlanetaryData.getCelestialBodiesMasses(), PlanetaryData.getCelestialBodyNames(), rk3);
        SolarSystemPhysicsSimulation mySolarSystem_Rk4 = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(), PlanetaryData.getCelestialBodiesMasses(), PlanetaryData.getCelestialBodyNames(), rk4);


    }
}

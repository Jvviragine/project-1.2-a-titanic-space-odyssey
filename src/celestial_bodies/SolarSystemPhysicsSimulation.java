package celestial_bodies;

import physics.functions.DerivativeFunction;
import physics.solvers.RK4Solver;
import physics.solvers.Solver;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import java.util.ArrayList;
import java.util.List;

public class SolarSystemPhysicsSimulation {
    private double[] masses;
    private StateVector[] stateVectors;

    private List<List<StateVector>> allStates = new ArrayList<>();;

    //Possible ordering : Sun,Venus,Earth,Moon,Mars,Jupiter,Saturn,Titan,Probe
    private String[] names;

    public final double G = 6.6743*Math.pow(10,-20);

    private DerivativeFunction df;

    private Solver solver;

    private double t;
    public SolarSystemPhysicsSimulation(StateVector[] stateVectors, double[] masses, String[] names){
        this.stateVectors = stateVectors;
        this.masses = masses;
        this.names = names;
        this.df = new DerivativeFunction(this);
        this.solver = new RK4Solver();
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
        for(int i = 0; i < stateVectors.length; i++){
            List <StateVector> state = new ArrayList<>();
            state.add(stateVectors[i]);
            allStates.add(state);
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
     * Updates the state of the system given a given number of steps and step size,h
     * @param tf final time after all updates
     * @param h the time difference between each update
     */
    public void updateState(double tf,double h){

        //Update all objects in the Solar System
        for(int i = 0; i < stateVectors.length; i++){
            StateVector newState = solver.solve(df,stateVectors[i],t,tf,h);
            allStates.get(i).add(newState);
            //Update current state
            stateVectors[i] = newState;
        }

        //Update the time of the system
        t = tf;
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
            if(v == stateVectors[i]){
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

        System.out.println(mySystem.getPath().toString());
    }
}

package physics.solvers;

import physics.functions.Function;
import physics.functions.TestODEDerivativeFunction;
import physics.vectors.Vector;
import physics.vectors.StateVector;

import java.util.ArrayList;
import java.util.List;

public class EulerSolver implements Solver{
    static double t;

    ArrayList<StateVector> allStates;

    List<List<StateVector>> allPlanetStates;

    public EulerSolver(){

    }

    /**
     * Applies Euler's method to the current state vector
     *      yn+1 = yn + hf(t,y)
     * @param function the derivative function of the equation
     * @param initialCondition the position and velocity at t0
     * @param t0 the time from which the Euler steps are calculated
     * @param tf the time until which the Euler steps are calculated
     * @param stepSize the time interval at which evaluations of the derivative are made
     * @return the state vector at the end of the time period tf - t0
     */
    public StateVector solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize) {

        //List of states at each step
        ArrayList<StateVector> stateVectors = new ArrayList<>();

        StateVector currentState = initialCondition;

        //Solve Euler for the time period tf-t0
        for(double t=t0; t<tf; t+=stepSize){
            //Get derivative, f(t,y) of the function
            StateVector derivative = function.applyFunction(currentState,t);

            //h * f(t,y)
            StateVector hfty = derivative.multiply(stepSize);

            //yn + hf(t,y)
            StateVector y1 = currentState.add(hfty);

            //Set y1 to y0 for next iteration
            currentState = y1;

            //Update solver time
            this.t +=stepSize;

            //Add current state to all existing states
            stateVectors.add(currentState);
        }

        allStates = stateVectors;

        return currentState;

    }

    /**
     * Solves Euler for an array of StateVectors
     * @param function the derivative function to be applied to each y
     * @param initialConditions array of all initial conditions for the system
     * @param t0 initial time
     * @param tf final time
     * @param stepSize interval between each evaluation of the gradient
     * @return StateVector array with states at the end of the time period
     */
    public StateVector[] solve(Function function, StateVector[] initialConditions, double t0, double tf, double stepSize) {

        //List of states at each step
        ArrayList<StateVector> stateVectors = new ArrayList<>();
        List<List<StateVector>> allPlanetStateVectors = new ArrayList<>(initialConditions.length);

        //Initialise state lists
        for(int i=0; i< initialConditions.length;i++){
            ArrayList<StateVector> planetStates = new ArrayList<>();
            planetStates.add(initialConditions[i]);
            allPlanetStateVectors.add(planetStates);
        }

        StateVector currentStates[] = initialConditions;
        StateVector nextStates[] = new StateVector[initialConditions.length];

        //Solve Euler for the time period tf-t0
        for(double t=t0; t<tf; t+=stepSize){

            for(int i = 0; i < initialConditions.length; i++){

                StateVector currentState = currentStates[i];

                //Get derivative, f(t,y) of the function
                StateVector derivative = function.applyFunction(currentState,t);

                //h * f(t,y)
                StateVector hfty = derivative.multiply(stepSize);

                //yn + hf(t,y)
                StateVector y1 = currentState.add(hfty);

                //Set y1 to y0 for next iteration
                nextStates[i] = y1;

                //Add current state to all existing states
                allPlanetStateVectors.get(i).add(y1);

            }
            currentStates = nextStates;
            function.resetState(currentStates);
        }

        allPlanetStates = allPlanetStateVectors;
        allStates = stateVectors;

        return currentStates;
    }

    /**
     * Returns all states of a particular body
     * @param index the index of the body
     * @return Arraylist of all states in the solve period
     */
    public ArrayList<StateVector> getAllStates(int index){
        ArrayList<StateVector> states = new ArrayList<>();
        for(int i=0; i<allPlanetStates.get(index).size();i++){
            states.add(allPlanetStates.get(index).get(i));
        }
        return states;
    }

    /**
     * Gets state in case only one StateVector is passed into the function
     * @return
     */
    public ArrayList<StateVector> getAllStates(){
        ArrayList<StateVector> states = new ArrayList<>();

        return states;
    }


    // Testing the Euler Solver for y'=y, and y(0)=1, for which we know the exact solution y=eˆt
    public static void main(String[] args) {

        // Defining and Getting the f' = f(t,y) = dy/dt
        Function dydt = new TestODEDerivativeFunction();

        // Defining the Initial Condition y(0)=1
        double t0 = 0, tf = 1;
        Vector y0 = new Vector(new double[]{1}); // Vector that represents y(o)=1
        StateVector svY0 = new StateVector(new Vector[]{y0}); // Embeds this Vector into a State Vector

        // Defines the step Size
        double h = 0.000001;

        // Calls the Euler Solver to Numerically solve y'=t with y(0)=1 and h=0.1
        Solver eulerSolver = new EulerSolver();
        StateVector svYf = eulerSolver.solve(dydt, svY0, t0, tf, h);

        // Prints wt at tf
        System.out.println(svYf.getVector(0).get(0));

    }

}

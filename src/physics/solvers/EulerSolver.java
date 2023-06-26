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

        //get the number of iterations from t0, tf and the stepSize
        int n = getStepNumber(t0, tf, stepSize);

        //Solve Euler for the time period tf-t0
        for(int i = 0; i < n; i++){
            //Get derivative, f(t,y) of the function
            StateVector derivative = function.applyFunction(currentState,t0 + i*stepSize);

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

        //get the number of iterations from t0, tf and the stepSize
        int n = getStepNumber(t0, tf, stepSize);

        //Solve Euler for the time period tf-t0
        for(int i = 0; i < n; i++){

            for(int j = 0; j < initialConditions.length; j++){

                StateVector currentState = currentStates[j];

                //Get derivative, f(t,y) of the function
                StateVector derivative = function.applyFunction(currentState,t0 + i*stepSize);

                //h * f(t,y)
                StateVector hfty = derivative.multiply(stepSize);

                //yn + hf(t,y)
                StateVector y1 = currentState.add(hfty);

                //Set y1 to y0 for next iteration
                nextStates[j] = y1;

                //Add current state to all existing states
                allPlanetStateVectors.get(j).add(y1);

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

    /**
     * This method computes the number of iterations needed to reach tf
     * starting from t0 by adding one step size.
     * It is used to stop the solver at the correct step which sometimes
     * failed because of the double inaccuracy caused when we used the
     * actual time interval.
     * @param t0
     * @param tf
     * @param stepSize
     * @return n = (tf - t0) / stepSize
     */
    @Override
    public int getStepNumber(double t0, double tf, double stepSize) {
        //initialize the variables for t0, tf and the step size
        double scaled_t0 = t0;
        double scaled_tf = tf;
        double scaledStepSize = stepSize;

        //multiply by 10 the step size along with t0 and tf until the step size has no decimal values
        while ((int) scaledStepSize != scaledStepSize) {
            scaled_t0 *= 10;
            scaled_tf *= 10;
            scaledStepSize *= 10;
        }

        //cast the doubles to int because their decimal part is now null
        int intScaled_t0 = (int) scaled_t0;
        int intScaled_tf = (int) scaled_tf;
        int intScaledStepSize = (int) scaledStepSize;

        //check if the t0 is smaller or equal to tf
        if (intScaled_tf < intScaled_t0) {
            throw new IllegalArgumentException("tf must be bigger or equal to t0");

        //check if the step size is bigger than 0
        } else if (intScaledStepSize <= 0) {
            throw new IllegalArgumentException("Step size must be positive");
        }
        //check if tf is reachable from t0 by only adding hole step sizes
//        } else if ((intScaled_tf - intScaled_t0) % intScaledStepSize != 0) {
//            System.out.println((intScaled_tf - intScaled_t0) % intScaledStepSize);
//            throw new IllegalArgumentException("The range from t0 to tf must be dividable by the step size without any remainder");
//
//        }
        else {
            //calculate n or the number of iterations needed to reach tf from t0 by adding one step size
            int n = (intScaled_tf - intScaled_t0)/intScaledStepSize;
            return n;
        }
    }

    // Testing the Euler Solver for y'=y, and y(0)=1, for which we know the exact solution y=eˆt
    public static void main(String[] args) {

        // Defining and Getting the f' = f(t,y) = dy/dt
        Function dydt = new TestODEDerivativeFunction();

        // Defining the Initial Condition y(0)=1
        double t0 = 0, tf = 6;
        Vector y0 = new Vector(new double[]{1}); // Vector that represents y(o)=1
        StateVector svY0 = new StateVector(new Vector[]{y0}); // Embeds this Vector into a State Vector

        // Defines the step Size
        double h = 0.1;

        // Calls the Euler Solver to Numerically solve y'=t with y(0)=1 and h=0.1
        Solver eulerSolver = new EulerSolver();
        StateVector svYf = eulerSolver.solve(dydt, svY0, t0, tf, h);

        // Prints wt at tf
        System.out.println(svYf.getVector(0).get(0));

    }

}

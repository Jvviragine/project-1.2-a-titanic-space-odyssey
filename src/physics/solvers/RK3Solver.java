package physics.solvers;

import physics.functions.Function;
import physics.vectors.StateVector;

import java.util.ArrayList;
import java.util.List;

public class RK3Solver implements Solver{

    ArrayList<StateVector> allStates;

    List<List<StateVector>> allPlanetStates;

    public RK3Solver(){
    }

    /**
     * Heun's method (third order Runge-Kutta)
     * @param function the derivative function
     * @param initialCondition the state of the vector at t0
     * @param t0 beginning of the time period
     * @param tf end of the time period
     * @param stepSize step size which determinates the difference in time periods after each step
     * @return the state of a vector at the period tf, which is the end of a given time period
     */
    public StateVector solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize) {
        StateVector currentState = initialCondition;

        //List of states at each step
        ArrayList<StateVector> stateVectors = new ArrayList<>();

        //get the number of iterations from t0, tf and the stepSize
        int n = getStepNumber(t0, tf, stepSize);

        for(int i = 0; i < n; i++) {

            //derivative, f(t,y) in next calculation step
            StateVector fty = function.applyFunction(currentState, t0 + i*stepSize);

            //k1 = h*f(t,y)
            StateVector k1 = fty.multiply(stepSize);

            //k2 = h*f(t + 1/3*h,y + 1/3*k1), getting the derivative than calculating k2
            StateVector fty2 = function.applyFunction(currentState.add(k1.multiply(0.333333333333334)), t0 + i*stepSize + 0.333333333333333*stepSize);
            StateVector k2 = fty2.multiply(stepSize);

            //k3 = h*f(t + 2/3*h, y + 2/3*k2), getting the derivative than calculating k3
            StateVector fty3 = function.applyFunction(currentState.add(k2.multiply(0.666666666666667)), t0 + i*stepSize + 0.666666666666667*stepSize);
            StateVector k3 = fty3.multiply(stepSize);

            //yn+1 = y + 1/4*(k1 + 3*k3), calculating new y and updating the current state
            StateVector add = k3.multiply(3.0).add(k1).multiply(0.25);
            currentState = currentState.add(add);

            //Add current state to all existing states
            stateVectors.add(currentState);
        }

        allStates = stateVectors;

        return currentState;
    }

    /**
     * Solves Heun's RK3 for an array of StateVectors
     * @param function the derivative function to be applied to each y
     * @param initialConditions array of all initial conditions for the system
     * @param t0 initial time
     * @param tf final time
     * @param stepSize interval between each evaluation of the gradient
     * @return StateVector array with states at the end of the time period
     */
    public StateVector[] solve(Function function, StateVector[] initialConditions, double t0, double tf, double stepSize){
        //List of states at each step
        ArrayList<StateVector> stateVectors = new ArrayList<>();
        List<List<StateVector>> allPlanetStateVectors = new ArrayList<>(initialConditions.length);
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

                //derivative, f(t,y) in next calculation step
                StateVector fty = function.applyFunction(currentState, t0 + i*stepSize);

                //k1 = h*f(t,y)
                StateVector k1 = fty.multiply(stepSize);

                //k2 = h*f(t + 1/3*h,y + 1/3*k1), getting the derivative than calculating k2
                StateVector fty2 = function.applyFunction(currentState.add(k1.multiply(0.333333333333334)), t0 + i*stepSize + 0.333333333333333*stepSize);
                StateVector k2 = fty2.multiply(stepSize);

                //k3 = h*f(t + 2/3*h, y + 2/3*k2), getting the derivative than calculating k3
                StateVector fty3 = function.applyFunction(currentState.add(k2.multiply(0.666666666666667)), t0 + i*stepSize + 0.666666666666667*stepSize);
                StateVector k3 = fty3.multiply(stepSize);

                //yn+1 = y + 1/4*(k1 + 3*k3), calculating new y and updating the current state
                StateVector add = k3.multiply(3.0).add(k1).multiply(0.25);
                currentState = currentState.add(add);

                //Add current state to all existing states
                stateVectors.add(currentState);

                //Set y1 to y0 for next iteration
                nextStates[j] = currentState;

                //Add current state to all existing states
                allPlanetStateVectors.get(j).add(nextStates[j]);

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
     * @return Arraylist of all states in the period
     */
    public ArrayList<StateVector> getAllStates(){
        return allStates;
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

        //check if tf is reachable from t0 by only adding hole step sizes
        } else if ((intScaled_tf - intScaled_t0) % intScaledStepSize != 0) {
            throw new IllegalArgumentException("The rang from t0 to tf must be dividable by the step size without any remainder");

        } else {
            //calculate n or the number of iterations needed to reach tf from t0 by adding one step size
            int n = (intScaled_tf - intScaled_t0)/intScaledStepSize;
            return n;
        }
    }
}

package physics.solvers;
import physics.functions.Function;
import physics.vectors.Vector;
import physics.vectors.StateVector;

import java.util.ArrayList;
import java.util.List;

public class RK4Solver implements Solver{

    ArrayList<StateVector> allStates;

    List<List<StateVector>> allPlanetStates;

    /**
     * Classical (Fourth-order Runge-Kutta)
     * @param function the derivative function of the equation
     * @param initialCondition the position and velocity at t0
     * @param t0 the time from which the Ralston's steps are calculated
     * @param tf the time until which the Ralston's steps are calculated
     * @param stepSize the time interval at which evaluations of the derivative are made
     * @return the state vector at the end of the time period tf - t0
     */
    public StateVector solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize) {

        StateVector currentState = initialCondition; // y(0) - Exact Value

        //List of states at each step
        ArrayList<StateVector> stateVectors = new ArrayList<>();

        //get the number of iterations from t0, tf and the stepSize
        int n = getStepNumber(t0, tf, stepSize);

        // Iterate through the Step Sizes until we reach the Final one
        for(int i = 0; i < n; i++){

            // Euler Step - K1
            StateVector k1 = function.applyFunction(currentState, t0 + i*stepSize).multiply(stepSize);

            // Second Approximation - K2
            double k2Time = t0 + i*stepSize + (0.5)*stepSize;
            StateVector k2FunctionStateVector = currentState.add(k1.multiply(0.5));
            StateVector k2 = function.applyFunction(k2FunctionStateVector, k2Time).multiply(stepSize);
            StateVector scalledK2 = k2.multiply(2.0);

            // Third Approximation - K3
            double k3Time = t0 + i*stepSize + (0.5 * stepSize);
            StateVector k3FunctionStateVector = currentState.add(k2.multiply(0.5));
            StateVector k3 = function.applyFunction(k3FunctionStateVector, k3Time).multiply(stepSize);
            StateVector scaledK3 = k3.multiply(2.0);

            // Fourth Approximation - K4
            double k4Time = t0 + i*stepSize + stepSize;
            StateVector k4FunctionStateVector = currentState.add(k3);
            StateVector k4 = function.applyFunction(k4FunctionStateVector, k4Time).multiply(stepSize);

            // Sum of Scalled Ks
            StateVector scaledSumOfK1AndK4 = k1.add(k4);
            StateVector scaledSumOfK2AndK3 = scalledK2.add(scaledK3);
            StateVector scaledSumOfKs = scaledSumOfK1AndK4.add(scaledSumOfK2AndK3);

            // Diving Scalled Sum by 4
            StateVector averagedScaledStateVector = scaledSumOfKs.multiply(0.166666666666667);

            currentState = currentState.add(averagedScaledStateVector);

            //Add current state to all existing states
            stateVectors.add(currentState);
        }

        allStates = stateVectors;

        return currentState;
    }

    /**
     * Solves Classical RK4 for an array of StateVectors
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

                // Euler Step - K1
                StateVector k1 = function.applyFunction(currentState, t0 + i*stepSize).multiply(stepSize);

                // Second Approximation - K2
                double k2Time = t0 + i*stepSize + (0.5)*stepSize;
                StateVector k2FunctionStateVector = currentState.add(k1.multiply(0.5));
                StateVector k2 = function.applyFunction(k2FunctionStateVector, k2Time).multiply(stepSize);
                StateVector scalledK2 = k2.multiply(2.0);

                // Third Approximation - K3
                double k3Time = t0 + i*stepSize + (0.5 * stepSize);
                StateVector k3FunctionStateVector = currentState.add(k2.multiply(0.5));
                StateVector k3 = function.applyFunction(k3FunctionStateVector, k3Time).multiply(stepSize);
                StateVector scaledK3 = k3.multiply(2.0);

                // Fourth Approximation - K4
                double k4Time = t0 + i*stepSize + stepSize;
                StateVector k4FunctionStateVector = currentState.add(k3);
                StateVector k4 = function.applyFunction(k4FunctionStateVector, k4Time).multiply(stepSize);

                // Sum of Scalled Ks
                StateVector scaledSumOfK1AndK4 = k1.add(k4);
                StateVector scaledSumOfK2AndK3 = scalledK2.add(scaledK3);
                StateVector scaledSumOfKs = scaledSumOfK1AndK4.add(scaledSumOfK2AndK3);

                // Diving Scalled Sum by 4
                StateVector averagedScaledStateVector = scaledSumOfKs.multiply(0.166666666666667);

                //Set y1 to y0 for next iteration
                nextStates[j] = currentState.add(averagedScaledStateVector);

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
     * @return
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
            throw new IllegalArgumentException("The range from t0 to tf must be dividable by the step size without any remainder");

        } else {
            //calculate n or the number of iterations needed to reach tf from t0 by adding one step size
            int n = (intScaled_tf - intScaled_t0)/intScaledStepSize;
            return n;
        }
    }

}

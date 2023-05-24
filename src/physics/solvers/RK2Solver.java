package physics.solvers;
import physics.functions.Function;
import physics.vectors.Vector;
import physics.vectors.StateVector;

import java.util.ArrayList;
import java.util.List;

public class RK2Solver implements Solver {

    ArrayList<StateVector> allStates;

    List<List<StateVector>> allPlanetStates;

    public RK2Solver(){

    }

    /**
     *Ralston's Method (Second-order Runge-Kutta)
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

        // Iterate through the Step Sizes until the final one, tf
        for(double t=t0; t<tf; t+=stepSize){

            // Euler Step - K1
            StateVector k1 = function.applyFunction(currentState, t).multiply(stepSize);

            // Second Approximation - K2
            double k2Time = t + (0.333333333333333)*stepSize;
            StateVector k2FunctionStateVector = currentState.add(k1.multiply(0.333333333333333));
            StateVector k2 = function.applyFunction(k2FunctionStateVector, k2Time).multiply(stepSize);
            StateVector scaledK2 = k2.multiply(3.0);

            // Sum of Scaled Ks
            StateVector scaledSumOfKs = k1.add(scaledK2);

            // Diving Scaled Sum by 4
            StateVector averagedScaledStateVector = scaledSumOfKs.multiply(0.25);

            currentState = currentState.add(averagedScaledStateVector);

            //Add current state to all existing states
            stateVectors.add(currentState);
        }

        allStates = stateVectors;

        return currentState;
    }

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

        //Solve Euler for the time period tf-t0
        for(double t=t0; t<tf; t+=stepSize){

            for(int i = 0; i < initialConditions.length; i++){

                StateVector currentState = currentStates[i];

                // Euler Step - K1
                StateVector k1 = function.applyFunction(currentState, t).multiply(stepSize);

                // Second Approximation - K2
                double k2Time = t + (0.333333333333333)*stepSize;
                StateVector k2FunctionStateVector = currentState.add(k1.multiply(0.333333333333333));
                StateVector k2 = function.applyFunction(k2FunctionStateVector, k2Time).multiply(stepSize);
                StateVector scaledK2 = k2.multiply(3.0);

                // Sum of Scaled Ks
                StateVector scaledSumOfKs = k1.add(scaledK2);

                // Diving Scaled Sum by 4
                StateVector averagedScaledStateVector = scaledSumOfKs.multiply(0.25);

                currentState = currentState.add(averagedScaledStateVector);

                //Add current state to all existing states
                stateVectors.add(currentState);

                //Set y1 to y0 for next iteration
                nextStates[i] = currentState;

                //Add current state to all existing states
                allPlanetStateVectors.get(i).add(nextStates[i]);

            }
            currentStates = nextStates;
            function.resetState(currentStates);
        }

        allPlanetStates = allPlanetStateVectors;
        allStates = stateVectors;

        return currentStates;
    }

    public ArrayList<StateVector> getAllStates(){
        return allStates;
    }

    public ArrayList<StateVector> getAllStates(int index){
        ArrayList<StateVector> states = new ArrayList<>();
        for(int i=0; i<allPlanetStates.get(index).size();i++){
            states.add(allPlanetStates.get(index).get(i));
        }
        return states;
    }

}

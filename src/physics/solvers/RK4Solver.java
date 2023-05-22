package physics.solvers;
import physics.functions.Function;
import physics.vectors.Vector;
import physics.vectors.StateVector;

import java.util.ArrayList;

public class RK4Solver implements Solver{

    ArrayList<StateVector> allStates;

    public StateVector solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize) {

        StateVector currentState = initialCondition; // y(0) - Exact Value

        //List of states at each step
        ArrayList<StateVector> stateVectors = new ArrayList<>();

        // Iterate through the Step Sizes until we reach the Final one
        for(double t=t0; t<tf; t+=stepSize){

            // Euler Step - K1
            StateVector k1 = function.applyFunction(currentState, t).multiply(stepSize);

            // Second Approximation - K2
            double k2Time = t + (0.5)*stepSize;
            StateVector k2FunctionStateVector = currentState.add(k1.multiply(0.5));
            StateVector k2 = function.applyFunction(k2FunctionStateVector, k2Time).multiply(stepSize);
            StateVector scalledK2 = k2.multiply(2.0);

            // Third Approximation - K3
            double k3Time = t + (0.5 * stepSize);
            StateVector k3FunctionStateVector = currentState.add(k2.multiply(0.5));
            StateVector k3 = function.applyFunction(k3FunctionStateVector, k3Time).multiply(stepSize);
            StateVector scaledK3 = k3.multiply(2.0);

            // Fourth Approximation - K4
            double k4Time = t + stepSize;
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

    public ArrayList<StateVector> getAllStates(){
        return allStates;
    }

}

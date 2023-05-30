package physics.solvers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.functions.Function;
import physics.functions.TestODEDerivativeFunction;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RK4SolverTest {

    private RK4Solver solver;

    @BeforeEach
    //initializes an RK4Solver before each test
    void setUp() {
        solver = new RK4Solver();
    }

    @AfterEach
    //deletes the RK4Solver before resetting it
    void tearDown() {
        solver = null;
    }

    @Test
    //covers solve for a singular stateVector taken as parameter
    void testSolveWithSingularStateVector() {
        Function dydt = new TestODEDerivativeFunction();
        double t0 = 0;
        Vector vector = new Vector(new double[]{1});
        StateVector stateVector = new StateVector(new Vector[]{vector});
        double tf = 6.0;
        double h = 0.1;
        StateVector output = solver.solve(dydt, stateVector, t0, tf, h);

        for(double t = t0; t<tf; t += h) {
            StateVector k1 = dydt.applyFunction(stateVector, t).multiply(h);
            double k2Time = t + (0.5)*h;
            StateVector k2FunctionStateVector = stateVector.add(k1.multiply(0.5));
            StateVector k2 = dydt.applyFunction(k2FunctionStateVector, k2Time).multiply(h);
            StateVector scalledK2 = k2.multiply(2.0);
            double k3Time = t + (0.5 * h);
            StateVector k3FunctionStateVector = stateVector.add(k2.multiply(0.5));
            StateVector k3 = dydt.applyFunction(k3FunctionStateVector, k3Time).multiply(h);
            StateVector scaledK3 = k3.multiply(2.0);
            double k4Time = t + h;
            StateVector k4FunctionStateVector = stateVector.add(k3);
            StateVector k4 = dydt.applyFunction(k4FunctionStateVector, k4Time).multiply(h);
            StateVector scaledSumOfK1AndK4 = k1.add(k4);
            StateVector scaledSumOfK2AndK3 = scalledK2.add(scaledK3);
            StateVector scaledSumOfKs = scaledSumOfK1AndK4.add(scaledSumOfK2AndK3);
            StateVector averagedScaledStateVector = scaledSumOfKs.multiply(0.166666666666667);
            stateVector = stateVector.add(averagedScaledStateVector);
        }
        StateVector expected = stateVector;

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers solve for an array of stateVector taken as parameter
    void testSolveWithArrayOfStateVector() {
        Function dydt = new TestODEDerivativeFunction();
        double t0 = 0;
        Vector vector1 = new Vector(new double[]{1});
        Vector vector2 = new Vector(new double[]{2});
        StateVector stateVector1 = new StateVector(new Vector[]{vector1});
        StateVector stateVector2 = new StateVector(new Vector[]{vector2});
        StateVector[] currentStates = new StateVector[]{stateVector1, stateVector2};
        double tf = 6.0;
        double h = 0.1;
        StateVector[] output = solver.solve(dydt, currentStates, t0, tf, h);

        StateVector nextStates[] = new StateVector[currentStates.length];
        for(double t=t0; t<tf; t+=h){
            for(int i = 0; i < currentStates.length; i++){
                StateVector currentState = currentStates[i];
                StateVector k1 = dydt.applyFunction(currentState, t).multiply(h);
                double k2Time = t + (0.5)*h;
                StateVector k2FunctionStateVector = currentState.add(k1.multiply(0.5));
                StateVector k2 = dydt.applyFunction(k2FunctionStateVector, k2Time).multiply(h);
                StateVector scalledK2 = k2.multiply(2.0);
                double k3Time = t + (0.5 * h);
                StateVector k3FunctionStateVector = currentState.add(k2.multiply(0.5));
                StateVector k3 = dydt.applyFunction(k3FunctionStateVector, k3Time).multiply(h);
                StateVector scaledK3 = k3.multiply(2.0);
                double k4Time = t + h;
                StateVector k4FunctionStateVector = currentState.add(k3);
                StateVector k4 = dydt.applyFunction(k4FunctionStateVector, k4Time).multiply(h);
                StateVector scaledSumOfK1AndK4 = k1.add(k4);
                StateVector scaledSumOfK2AndK3 = scalledK2.add(scaledK3);
                StateVector scaledSumOfKs = scaledSumOfK1AndK4.add(scaledSumOfK2AndK3);
                StateVector averagedScaledStateVector = scaledSumOfKs.multiply(0.166666666666667);
                currentState = currentState.add(averagedScaledStateVector);
                nextStates[i] = currentState;
            }
            currentStates = nextStates;
        }
        StateVector expected[] = currentStates;

        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].getNumberOfVectors(); j++) {
                for (int k = 0; k < expected[i].getVector(j).getDimension(); k++) {
                    assertEquals(expected[i].getVector(j).get(k), output[i].getVector(j).get(k));
                }
            }
        }
    }

    @Test
    //covers getAllStates for index < allPlanetStates.length
    void testGetAllStatesWithIndexInBounds() {
        Function dydt = new TestODEDerivativeFunction();
        double t0 = 0;
        Vector vector1 = new Vector(new double[]{1});
        Vector vector2 = new Vector(new double[]{2});
        StateVector stateVector1 = new StateVector(new Vector[]{vector1});
        StateVector stateVector2 = new StateVector(new Vector[]{vector2});
        StateVector[] currentStates = {stateVector1, stateVector2};
        double tf = 6.0;
        double h = 0.1;

        List<List<StateVector>> expected = new ArrayList<>();

        //manual initialisation
        for (int i = 0; i < currentStates.length; i++) {
            ArrayList<StateVector> planetStates = new ArrayList<>();
            planetStates.add(currentStates[i]);
            expected.add(planetStates);
        }

        solver.solve(dydt, currentStates, t0, tf, h);  //runs solve to fill the solver.allPlanetStates

        //manually running the solve and adding each stateVector
        StateVector nextStates[] = new StateVector[currentStates.length];

        for(double t=t0; t<tf; t+=h){
            for(int i = 0; i < currentStates.length; i++){
                StateVector currentState = currentStates[i];
                StateVector k1 = dydt.applyFunction(currentState, t).multiply(h);
                double k2Time = t + (0.5)*h;
                StateVector k2FunctionStateVector = currentState.add(k1.multiply(0.5));
                StateVector k2 = dydt.applyFunction(k2FunctionStateVector, k2Time).multiply(h);
                StateVector scalledK2 = k2.multiply(2.0);
                double k3Time = t + (0.5 * h);
                StateVector k3FunctionStateVector = currentState.add(k2.multiply(0.5));
                StateVector k3 = dydt.applyFunction(k3FunctionStateVector, k3Time).multiply(h);
                StateVector scaledK3 = k3.multiply(2.0);
                double k4Time = t + h;
                StateVector k4FunctionStateVector = currentState.add(k3);
                StateVector k4 = dydt.applyFunction(k4FunctionStateVector, k4Time).multiply(h);
                StateVector scaledSumOfK1AndK4 = k1.add(k4);
                StateVector scaledSumOfK2AndK3 = scalledK2.add(scaledK3);
                StateVector scaledSumOfKs = scaledSumOfK1AndK4.add(scaledSumOfK2AndK3);
                StateVector averagedScaledStateVector = scaledSumOfKs.multiply(0.166666666666667);
                currentState = currentState.add(averagedScaledStateVector);
                nextStates[i] = currentState;
            }
            currentStates = nextStates;
            dydt.resetState(currentStates);
        }

        //checking between expected and output values
        for (int i = 0; i < expected.size(); i++) {
            ArrayList<StateVector> output = solver.getAllStates(i);
            for (int j = 0; j < expected.get(i).size(); j++) {
                assertEquals(expected.get(i).get(j).getVector(0).get(0), output.get(j).getVector(0).get(0));
            }
        }
    }

    @Test
    //covers getAllStates for index = null;
    void testGetAllStatesWithNullIndex() {
        Function dydt = new TestODEDerivativeFunction();
        double t0 = 0;
        Vector vector1 = new Vector(new double[]{1});
        Vector vector2 = new Vector(new double[]{2});
        StateVector stateVector1 = new StateVector(new Vector[]{vector1});
        StateVector stateVector2 = new StateVector(new Vector[]{vector2});
        StateVector[] currentStates = {stateVector1, stateVector2};
        double tf = 6.0;
        double h = 0.1;

        ArrayList<StateVector> expected = new ArrayList<>();

        solver.solve(dydt, currentStates, t0, tf, h);  //runs solve to fill the solver.allStates

        //manually running the solve and adding each stateVector
        StateVector nextStates[] = new StateVector[currentStates.length];
        for(double t=t0; t<tf; t+=h){
            for(int i = 0; i < currentStates.length; i++){
                StateVector currentState = currentStates[i];
                StateVector k1 = dydt.applyFunction(currentState, t).multiply(h);
                double k2Time = t + (0.5)*h;
                StateVector k2FunctionStateVector = currentState.add(k1.multiply(0.5));
                StateVector k2 = dydt.applyFunction(k2FunctionStateVector, k2Time).multiply(h);
                StateVector scalledK2 = k2.multiply(2.0);
                double k3Time = t + (0.5 * h);
                StateVector k3FunctionStateVector = currentState.add(k2.multiply(0.5));
                StateVector k3 = dydt.applyFunction(k3FunctionStateVector, k3Time).multiply(h);
                StateVector scaledK3 = k3.multiply(2.0);
                double k4Time = t + h;
                StateVector k4FunctionStateVector = currentState.add(k3);
                StateVector k4 = dydt.applyFunction(k4FunctionStateVector, k4Time).multiply(h);
                StateVector scaledSumOfK1AndK4 = k1.add(k4);
                StateVector scaledSumOfK2AndK3 = scalledK2.add(scaledK3);
                StateVector scaledSumOfKs = scaledSumOfK1AndK4.add(scaledSumOfK2AndK3);
                StateVector averagedScaledStateVector = scaledSumOfKs.multiply(0.166666666666667);
                currentState = currentState.add(averagedScaledStateVector);
                nextStates[i] = currentState;
            }
            currentStates = nextStates;
            dydt.resetState(currentStates);
        }

        //checking between expected and output values
        ArrayList<StateVector> output = solver.getAllStates();
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getVector(0).get(0), output.get(i).getVector(0).get(0));
        }
    }

    @Test
    //covers getAllStates for index >= allPlanetStates.length
    void testGetAllStatesWithIndexOutOfBounds() {
        Function dydt = new TestODEDerivativeFunction();
        double t0 = 0;
        Vector vector1 = new Vector(new double[]{1});
        Vector vector2 = new Vector(new double[]{2});
        StateVector stateVector1 = new StateVector(new Vector[]{vector1});
        StateVector stateVector2 = new StateVector(new Vector[]{vector2});
        StateVector[] currentStates = {stateVector1, stateVector2};
        double tf = 6.0;
        double h = 0.1;
        solver.solve(dydt, currentStates, t0, tf, h);

        int index = currentStates.length;
        assertThrows(IndexOutOfBoundsException.class, () -> solver.getAllStates(index));
    }
}
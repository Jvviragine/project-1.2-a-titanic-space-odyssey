package physics.solvers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.functions.Function;
import physics.functions.TestODEDerivativeFunction;
import physics.vectors.StateVector;
import physics.vectors.Vector;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EulerSolverTest {

    private Solver eulerSolver;

    @BeforeEach
    //initializes an euleurSolver before each test
    void setUp() {
        eulerSolver = new EulerSolver();
    }

    @AfterEach
    //deletes the euleurSolver before resetting it
    void tearDown() {
        eulerSolver = null;
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
        StateVector output = eulerSolver.solve(dydt, stateVector, t0, tf, h);

        for(double t=t0; t<tf; t+=h){
            StateVector derivative = dydt.applyFunction(stateVector,t);
            StateVector hfty = derivative.multiply(h);
            StateVector y1 = stateVector.add(hfty);
            stateVector = y1;
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
        StateVector[] output = eulerSolver.solve(dydt, currentStates, t0, tf, h);

        for(double t=t0; t<tf; t+=h){
            for(int i = 0; i < currentStates.length; i++){
                StateVector currentState = currentStates[i];
                StateVector derivative = dydt.applyFunction(currentState,t);
                StateVector hfty = derivative.multiply(h);
                StateVector y1 = currentState.add(hfty);
                currentStates[i] = y1;
            }
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

        eulerSolver.solve(dydt, currentStates, t0, tf, h);  //runs solve to fill the euleurSolver.allPlanetStates

        //manually running the solve and adding each stateVector
        StateVector nextStates[] = new StateVector[currentStates.length];
        for(double t=t0; t<tf; t+=h) {
            for (int i = 0; i < currentStates.length; i++) {
                StateVector currentState = currentStates[i];
                StateVector derivative = dydt.applyFunction(currentState, t);
                StateVector hfty = derivative.multiply(h);
                StateVector y1 = currentState.add(hfty);
                nextStates[i] = y1;
                expected.get(i).add(y1);
            }
            currentStates = nextStates;
            dydt.resetState(currentStates);
        }

        //checking between expected and output values
        for (int i = 0; i < expected.size(); i++) {
            ArrayList<StateVector> output = eulerSolver.getAllStates(i);
            for (int j = 0; j < expected.get(i).size(); j++) {
                assertEquals(expected.get(i).get(j).getVector(0).get(0), output.get(j).getVector(0).get(0));
            }
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
        eulerSolver.solve(dydt, currentStates, t0, tf, h);

        int index = currentStates.length;
        assertThrows(IndexOutOfBoundsException.class, () -> eulerSolver.getAllStates(index));
    }
}
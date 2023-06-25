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

/*
This class is used to test the RK3Solver class.
Before each test a new solver is initialized and deleted after use.
This step is executed for each test case. This class also has a tolerance
attribute which determines the error margin for the solve methods.

Find below the input partitioning for each method :

solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize) :
    - initialCondition : initialCondition < 0, initialCondition > 0

solve(Function function, StateVector[] initialConditions, double t0, double tf, double stepSize) :
    - initialConditions : initialConditions.length == 0, initialCondition.length > 0

getAllStates(int index) :
    - index >= allPlanetStates.length
    - index < allPlanetStates.length

getStepNumber(double t0, double tf, double stepSize) :
    - t0 <= tf, t0 > tf
    - stepSize : stepSize <= 0, stepSize > 0
 */

class RK3SolverTest {

    private RK3Solver solver;

    private double tolerance = 0.00001;

    @BeforeEach
    //initializes an RK3Solver before each test
    void setUp() {
        solver = new RK3Solver();
    }

    @AfterEach
    //deletes the RK3Solver before resetting it
    void tearDown() {
        solver = null;
    }

    @Test
    //covers solve for a singular stateVector taken as parameter for initialCondition > 0
    void testSolveWithSingularStateVectorAndPositiveInitialCondition() {
        Vector expectedValue = new Vector(new double[]{1210.00706593404});
        StateVector expected = new StateVector(new Vector[]{expectedValue});

        Function dydt = new TestODEDerivativeFunction();
        double t0 = 0;
        Vector vector = new Vector(new double[]{3});
        StateVector stateVector = new StateVector(new Vector[]{vector});
        double tf = 6.0;
        double h = 0.1;
        StateVector output = solver.solve(dydt, stateVector, t0, tf, h);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j), tolerance);
            }
        }
    }

    @Test
    //covers solve for a singular stateVector taken as parameter for initialCondition < 0
    void testSolveWithSingularStateVectorAndNegativeInitialCondition() {
        Vector expectedValue = new Vector(new double[]{-1210.00706593404});
        StateVector expected = new StateVector(new Vector[]{expectedValue});

        Function dydt = new TestODEDerivativeFunction();
        double t0 = 0;
        Vector vector = new Vector(new double[]{-3});
        StateVector stateVector = new StateVector(new Vector[]{vector});
        double tf = 6.0;
        double h = 0.1;
        StateVector output = solver.solve(dydt, stateVector, t0, tf, h);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j), tolerance);
            }
        }
    }

    @Test
    //covers solve for an array of stateVector taken as parameter for initialConditions.length > 0
    void testSolveWithArrayOfStateVectorAndTestODEFunctionAndNonEmptyInitialConditions() {
        Vector expectedValue1 = new Vector(new double[]{-1210.00706593404});
        Vector expectedValue2 = new Vector(new double[]{0});
        Vector expectedValue3 = new Vector(new double[]{2823.34982051276});
        StateVector expectedStateVector1 = new StateVector(new Vector[]{expectedValue1});
        StateVector expectedStateVector2 = new StateVector(new Vector[]{expectedValue2});
        StateVector expectedStateVector3 = new StateVector(new Vector[]{expectedValue3});
        StateVector[] expected = new StateVector[]{expectedStateVector1, expectedStateVector2, expectedStateVector3};

        Function dydt = new TestODEDerivativeFunction();
        double t0 = 0;
        Vector vector1 = new Vector(new double[]{-3});
        StateVector stateVector1 = new StateVector(new Vector[]{vector1});
        Vector vector2 = new Vector(new double[]{0});
        StateVector stateVector2 = new StateVector(new Vector[]{vector2});
        Vector vector3 = new Vector(new double[]{7});
        StateVector stateVector3 = new StateVector(new Vector[]{vector3});
        StateVector[] stateVectorArray = {stateVector1, stateVector2, stateVector3};
        double tf = 6.0;
        double h = 0.1;
        StateVector[] output = solver.solve(dydt, stateVectorArray, t0, tf, h);

        for (int i = 0; i < stateVectorArray.length; i++) {
            for (int j = 0; j < stateVectorArray[i].getNumberOfVectors(); j++) {
                for (int k = 0; k < stateVectorArray[i].getVector(j).getDimension(); k++) {
                    assertEquals(expected[i].getVector(j).get(k), output[i].getVector(j).get(k), tolerance);
                }
            }
        }
    }

    @Test
    //covers solve for an array of stateVector taken as parameter for initialConditions.length == 0
    void testSolveWithArrayOfStateVectorAndTestODEFunctionAndEmptyInitialConditions() {
        Class expected = IllegalArgumentException.class;

        Function dydt = new TestODEDerivativeFunction();
        double t0 = 0;
        Vector vector = new Vector(new double[]{-3});
        StateVector stateVector = new StateVector(new Vector[]{vector});
        double tf = 6.0;
        double h = 0.1;

        assertThrows(expected, () -> solver.solve(dydt, stateVector, t0, tf, h));
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
                StateVector fty = dydt.applyFunction(currentState, t);
                StateVector k1 = fty.multiply(h);
                StateVector fty2 = dydt.applyFunction(currentState.add(k1.multiply(0.333333333333334)), t + 0.333333333333333*h);
                StateVector k2 = fty2.multiply(h);
                StateVector fty3 = dydt.applyFunction(currentState.add(k2.multiply(0.666666666666667)), t + 0.666666666666667*h);
                StateVector k3 = fty3.multiply(h);
                StateVector add = k3.multiply(3.0).add(k1).multiply(0.25);
                currentState = currentState.add(add);
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
                StateVector fty = dydt.applyFunction(currentState, t);
                StateVector k1 = fty.multiply(h);
                StateVector fty2 = dydt.applyFunction(currentState.add(k1.multiply(0.333333333333334)), t + 0.333333333333333*h);
                StateVector k2 = fty2.multiply(h);
                StateVector fty3 = dydt.applyFunction(currentState.add(k2.multiply(0.666666666666667)), t + 0.666666666666667*h);
                StateVector k3 = fty3.multiply(h);
                StateVector add = k3.multiply(3.0).add(k1).multiply(0.25);
                currentState = currentState.add(add);
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

    @Test
    //covers getStepNumber for t0 <= tf ; stepSize > 0
    void testGetStepNumberWith_t0SmallerOrEqualTo_tfAndPositiveStepSize() {
        int expected = 15;

        double t0 = -2.5;
        double tf = 5;
        double stepSize = 0.5;
        int output = solver.getStepNumber(t0, tf, stepSize);

        assertEquals(expected, output);
    }

    @Test
    //covers getStepNumber for t0 > tf ; stepSize > 0
    void testGetStepNumberWith_t0BiggerThan_tfAndPositiveStepSize() {
        Class expected = IllegalArgumentException.class;

        double t0 = 5;
        double tf = -5;
        double stepSize = 0.5;

        assertThrows(expected, () -> solver.getStepNumber(t0, tf, stepSize));
    }

    @Test
    //covers getStepNumber for t0 <= tf ; stepSize <= 0
    void testGetStepNumberWith_t0SmallerOrEqualTo_tfAndNonPositiveStepSize() {
        Class expected = IllegalArgumentException.class;

        double t0 = 5;
        double tf = 5;
        double stepSize = 0;

        assertThrows(expected, () -> solver.getStepNumber(t0, tf, stepSize));
    }

    @Test
    //covers getStepNumber for t0 > tf ; stepSize <= 0
    void testGetStepNumberWith_t0BiggerThan_tfAndNonPositiveStepSize() {
        Class expected = IllegalArgumentException.class;

        double t0 = 9;
        double tf = 5;
        double stepSize = 0;

        assertThrows(expected, () -> solver.getStepNumber(t0, tf, stepSize));
    }
}
package physics.functions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import static org.junit.jupiter.api.Assertions.*;

/*
This class is responsible for testing the TestODEFunction class
which is a class that represents a basic function.
It is used to perform tests on the solvers.

Find the input partitioning below :

applyFunction(StateVector y, double t) :
    - y : y < 0, y == 0, y > 0
    - t : t < 0, t == 0, t > 0
 */

class TestODEFunctionTest {

    private Function ft;

    @BeforeEach
    //initializes the ODEFunction before each test
    void setUp() {
        ft = new TestODEFunction();
    }

    @AfterEach
    //deletes the ODEFunction before resetting it
    void tearDown() {
        ft = null;
    }

    @Test
    //covers applyFunction for y.getNumberOfVectors() == 1 and y.getNumberOfDimensions() == 1
    void testApplyFunctionWithOneUnidimensionalVector() {
        double t = 6.0;
        double e = Math.exp(1);
        double value = Math.pow(e, t);
        Vector vector = new Vector(new double[]{value});
        StateVector expected = new StateVector(new Vector[]{vector});

        Vector initialValueVector = new Vector(new double[]{1});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = ft.applyFunction(initialValue, t);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers applyFunction for y.getNumberOfVectors() == 1 and y.getNumberOfDimensions() > 1
    void testApplyFunctionWithOneMultidimensionalVector() {
        double t = 6.0;
        Vector initialValueVector = new Vector(new double[]{1, 2});
        StateVector expected = new StateVector(new Vector[]{initialValueVector});

        StateVector output = ft.applyFunction(expected, t);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers applyFunction for y.getNumberOfVectors() > 1 and y.getNumberOfDimensions() == 1
    void testApplyFunctionWithSeveralUnidimensionalVector() {
        double t = 6.0;
        Vector initialValueVector1 = new Vector(new double[]{1});
        Vector initialValueVector2 = new Vector(new double[]{2});
        StateVector expected = new StateVector(new Vector[]{initialValueVector1, initialValueVector2});

        StateVector output = ft.applyFunction(expected, t);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers applyFunction for y.getNumberOfVectors() > 1 and y.getNumberOfDimensions() > 1
    void testApplyFunctionWithSeveralMultidimensionalVector() {
        double t = 6.0;
        Vector initialValueVector1 = new Vector(new double[]{1, 3});
        Vector initialValueVector2 = new Vector(new double[]{2, 4});
        StateVector expected = new StateVector(new Vector[]{initialValueVector1, initialValueVector2});

        StateVector output = ft.applyFunction(expected, t);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers resetState
    void testResetState() {
        //does nothing
        ft.resetState(null);
    }
}
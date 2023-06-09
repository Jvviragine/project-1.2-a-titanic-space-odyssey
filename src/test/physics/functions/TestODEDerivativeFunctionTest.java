package physics.functions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import javax.swing.plaf.nimbus.State;

import static org.junit.jupiter.api.Assertions.*;

/*
This class is responsible for testing the TestODEDerivativeFunction class
which is a class that represents a basic function.
It is used to perform tests on the solvers.

Find the input partitioning below :

applyFunction(StateVector y, double t) :
    - y : y < 0, y == 0, y > 0
    - t : t < 0, t == 0, t > 0
 */

class TestODEDerivativeFunctionTest {

    private Function dydt;

    @BeforeEach
    //initializes the  before each test
    void setUp() {
        dydt = new TestODEDerivativeFunction();
    }

    @AfterEach
    //deletes the ODEDerivativeFunction before resetting it
    void tearDown() {
        dydt = null;
    }

    @Test
    //covers applyFunction for t < 0 and y.getVector(0).get(0) < 0
    void testApplyFunctionWithNegative_tAndNegativeInitialValue() {
        double t = -6.3;
        double valueAtY = -3;
        double expected = valueAtY;

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = dydt.applyFunction(initialValue, t);

        for (int i = 0; i < output.getNumberOfVectors(); i++) {
            for (int j = 0; j < output.getVector(i).getDimension(); j++) {
                assertEquals(expected, output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers applyFunction for t < 0 and y.getVector(0).get(0) == 0
    void testApplyFunctionWithNegative_tAndNullInitialValue() {
        double t = -6.3;
        double valueAtY = 0;
        double expected = valueAtY;

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = dydt.applyFunction(initialValue, t);

        for (int i = 0; i < output.getNumberOfVectors(); i++) {
            for (int j = 0; j < output.getVector(i).getDimension(); j++) {
                assertEquals(expected, output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers applyFunction for t < 0 and y.getVector(0).get(0) > 0
    void testApplyFunctionWithNegative_tAndPositiveInitialValue() {
        double t = -6.3;
        double valueAtY = 3.32;
        double expected = valueAtY;

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = dydt.applyFunction(initialValue, t);

        for (int i = 0; i < output.getNumberOfVectors(); i++) {
            for (int j = 0; j < output.getVector(i).getDimension(); j++) {
                assertEquals(expected, output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers applyFunction for t == 0 and y.getVector(0).get(0) < 0
    void testApplyFunctionWithNull_tAndNegativeInitialValue() {
        double t = 0;
        double valueAtY = -3;
        double expected = valueAtY;

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = dydt.applyFunction(initialValue, t);

        for (int i = 0; i < output.getNumberOfVectors(); i++) {
            for (int j = 0; j < output.getVector(i).getDimension(); j++) {
                assertEquals(expected, output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers applyFunction for t == 0 and y.getVector(0).get(0) == 0
    void testApplyFunctionWithNull_tAndNullInitialValue() {
        double t = 0;
        double valueAtY = 0;
        double expected = valueAtY;

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = dydt.applyFunction(initialValue, t);

        for (int i = 0; i < output.getNumberOfVectors(); i++) {
            for (int j = 0; j < output.getVector(i).getDimension(); j++) {
                assertEquals(expected, output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers applyFunction for t == 0 and y.getVector(0).get(0) > 0
    void testApplyFunctionWithNull_tAndPositiveInitialValue() {
        double t = 0;
        double valueAtY = 3.32;
        double expected = valueAtY;

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = dydt.applyFunction(initialValue, t);

        for (int i = 0; i < output.getNumberOfVectors(); i++) {
            for (int j = 0; j < output.getVector(i).getDimension(); j++) {
                assertEquals(expected, output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers applyFunction for t > 0 and y.getVector(0).get(0) < 0
    void testApplyFunctionWithPositive_tAndNegativeInitialValue() {
        double t = 14.2;
        double valueAtY = -3;
        double expected = valueAtY;

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = dydt.applyFunction(initialValue, t);

        for (int i = 0; i < output.getNumberOfVectors(); i++) {
            for (int j = 0; j < output.getVector(i).getDimension(); j++) {
                assertEquals(expected, output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers applyFunction for t > 0 and y.getVector(0).get(0) == 0
    void testApplyFunctionWithPositive_tAndNullInitialValue() {
        double t = 14.2;
        double valueAtY = 0;
        double expected = valueAtY;

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = dydt.applyFunction(initialValue, t);

        for (int i = 0; i < output.getNumberOfVectors(); i++) {
            for (int j = 0; j < output.getVector(i).getDimension(); j++) {
                assertEquals(expected, output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers applyFunction for t > 0 and y.getVector(0).get(0) > 0
    void testApplyFunctionWithPositive_tAndPositiveInitialValue() {
        double t = 14.2;
        double valueAtY = 3.32;
        double expected = valueAtY;

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = dydt.applyFunction(initialValue, t);

        for (int i = 0; i < output.getNumberOfVectors(); i++) {
            for (int j = 0; j < output.getVector(i).getDimension(); j++) {
                assertEquals(expected, output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers resetState
    void testResetState() {
        //does nothing
        dydt.resetState(null);
    }
}
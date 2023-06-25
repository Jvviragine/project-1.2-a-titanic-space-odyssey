package physics.functions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import static org.junit.jupiter.api.Assertions.*;

/*
This class is responsible for testing the Test2ODEFunction class
which is a class that represents a basic function.
It is used to perform tests on the solvers.

Find the input partitioning below :

applyFunction(StateVector y, double t) :
    - y : y < 0, y == 0, y > 0
    - t : t < 0, t == 0, t > 0
 */

class Test2ODEFunctionTest {

    private Function ft;

    @BeforeEach
    //initializes the ODEFunction before each test
    void setUp() {
        ft = new Test2ODEFunction();
    }

    @AfterEach
    //deletes the ODEFunction before resetting it
    void tearDown() {
        ft = null;
    }

    @Test
    //covers applyFunction for t < 0 and y.getVector(0).get(0) < 0
    void testApplyFunctionWithNegative_tAndNegativeInitialValue() {
        double t = -6.3;
        double valueAtY = -3;
        double expected = -2 + 2*Math.pow(Math.E, (0.5*Math.pow(t, 2) + t -4));

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = ft.applyFunction(initialValue, t);

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
        double expected = -2 + 2*Math.pow(Math.E, (0.5*Math.pow(t, 2) + t -4));

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = ft.applyFunction(initialValue, t);

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
        double expected = -2 + 2*Math.pow(Math.E, (0.5*Math.pow(t, 2) + t -4));

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = ft.applyFunction(initialValue, t);

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
        double expected = -2 + 2*Math.pow(Math.E, (0.5*Math.pow(t, 2) + t -4));

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = ft.applyFunction(initialValue, t);

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
        double expected = -2 + 2*Math.pow(Math.E, (0.5*Math.pow(t, 2) + t -4));

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = ft.applyFunction(initialValue, t);

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
        double expected = -2 + 2*Math.pow(Math.E, (0.5*Math.pow(t, 2) + t -4));

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = ft.applyFunction(initialValue, t);

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
        double expected = -2 + 2*Math.pow(Math.E, (0.5*Math.pow(t, 2) + t -4));

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = ft.applyFunction(initialValue, t);

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
        double expected = -2 + 2*Math.pow(Math.E, (0.5*Math.pow(t, 2) + t -4));

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = ft.applyFunction(initialValue, t);

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
        double expected = -2 + 2*Math.pow(Math.E, (0.5*Math.pow(t, 2) + t -4));

        Vector initialValueVector = new Vector(new double[]{valueAtY});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = ft.applyFunction(initialValue, t);

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
        ft.resetState(null);
    }
}
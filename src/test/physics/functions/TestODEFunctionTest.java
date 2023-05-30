package physics.functions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TestODEFunctionTest {

    private Function ft;

    @BeforeEach
    //initializes th ODEFunction before each test
    void setUp() {
        ft = new TestODEFunction();
    }

    @AfterEach
    //deletes the ODEFunction before resetting it
    void tearDown() {
        ft = null;
    }

    @Test
    //covers applyFunction for one 1-dimensional stateVector
    void testApplyFunctionWithOneUnidimensionalVector() {
        double tf = 6.0;
        double e = Math.exp(1);
        double value = Math.pow(e, tf);
        Vector vector = new Vector(new double[]{value});
        StateVector expected = new StateVector(new Vector[]{vector});

        Vector initialValueVector = new Vector(new double[]{1});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});
        StateVector output = ft.applyFunction(initialValue, tf);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers applyFunction for one multidimensional stateVector
    void testApplyFunctionWithOneMultidimensionalVector() {
        double tf = 6.0;
        Vector initialValueVector = new Vector(new double[]{1, 2});
        StateVector expected = new StateVector(new Vector[]{initialValueVector});

        StateVector output = ft.applyFunction(expected, tf);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers applyFunction for several 1-dimensional stateVector
    void testApplyFunctionWithSeveralUnidimensionalVector() {
        double tf = 6.0;
        Vector initialValueVector1 = new Vector(new double[]{1});
        Vector initialValueVector2 = new Vector(new double[]{2});
        StateVector expected = new StateVector(new Vector[]{initialValueVector1, initialValueVector2});

        StateVector output = ft.applyFunction(expected, tf);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
        //covers applyFunction for several multidimensional stateVector
    void testApplyFunctionWithSeveralMultidimensionalVector() {
        double tf = 6.0;
        Vector initialValueVector1 = new Vector(new double[]{1, 3});
        Vector initialValueVector2 = new Vector(new double[]{2, 4});
        StateVector expected = new StateVector(new Vector[]{initialValueVector1, initialValueVector2});

        StateVector output = ft.applyFunction(expected, tf);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    void testResetState() {
        //does nothing
        ft.resetState(null);
    }
}
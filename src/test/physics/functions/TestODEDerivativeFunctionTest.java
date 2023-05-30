package physics.functions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import javax.swing.plaf.nimbus.State;

import static org.junit.jupiter.api.Assertions.*;

class TestODEDerivativeFunctionTest {

    private Function dydt;

    @BeforeEach
    //initializes th  before each test
    void setUp() {
        dydt = new TestODEFunction();
    }

    @AfterEach
    //deletes the ODEDerivativeFunction before resetting it
    void tearDown() {
        dydt = null;
    }

    @Test
    //covers testApplyFunction
    void testApplyFunction() {
        Vector vector1 = new Vector(new double[]{1,20,300});
        Vector vector2 = new Vector(new double[]{4,50,600});
        StateVector expected = new StateVector(new Vector[]{vector1, vector2});

        double time = 0.0;
        StateVector output = dydt.applyFunction(expected, time);

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
        dydt.resetState(null);
    }
}
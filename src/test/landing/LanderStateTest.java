package landing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.controllers.LanderState;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import static org.junit.jupiter.api.Assertions.*;

/*
This class is used to test the features of the LanderState class.
To do so, there is a local LanderState variable that stores a landerState
instance to runs the test on.

Each method will be called from this local landerState instance which is
deleted and reconstructed after each test. This is done with the help of
the setUp method which reinitialize the local landerState and the tearDown
method which erases the used landerState instance. The setUp method is called
before each test whereas the tearUp method is called after each test is done
so that if any test changes the local landerState those changes will be undone.
This assures the fact that each test will be run on the same conditions.
 */

class LanderStateTest {

    private LanderState landerState;

    @BeforeEach
    void setUp() {
        Vector vector1 = new Vector(new double[]{0, 1.5, -4});
        Vector vector2 = new Vector(new double[]{-1.5, 3, 4.76});
        StateVector state = new StateVector(new Vector[]{vector1, vector2});
        double acceleration = 3.33;
        double torque = 10.1;
        landerState = new LanderState(state, acceleration, torque);
    }

    @AfterEach
    void tearDown() {
        landerState = null;
    }

    @Test
    //covers getState
    void testGetState() {
        Vector vector1 = new Vector(new double[]{0, 1.5, -4});
        Vector vector2 = new Vector(new double[]{-1.5, 3, 4.76});
        StateVector expected = new StateVector(new Vector[]{vector1, vector2});

        StateVector output = landerState.getState();

        for (int i = 0; i < output.getNumberOfVectors(); i++) {
            for (int j = 0; j < output.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers setState
    void testSetState() {
        Vector vector1 = new Vector(new double[]{0, 1, 4});
        Vector vector2 = new Vector(new double[]{1.5, 3, 5});
        StateVector stateVector = new StateVector(new Vector[]{vector1, vector2});
        StateVector expected = stateVector;

        landerState.setState(stateVector);
        StateVector output = landerState.getState();

        for (int i = 0; i < output.getNumberOfVectors(); i++) {
            for (int j = 0; j < output.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers getThetaPos
    void testGetThetaPos() {
        double expected = -4;
        double output = landerState.getThetaPos();

        assertEquals(expected, output);
    }

    @Test
    //covers setThetaPos
    void testSetThetaPos() {
        double thetaPos = 22.02;
        double expected = thetaPos;

        landerState.setThetaPos(thetaPos);
        double output = landerState.getThetaPos();

        assertEquals(expected, output);
    }

    @Test
    //covers getThetaVel
    void testGetThetaVel() {
        double expected = 4.76;
        double output = landerState.getThetaVel();

        assertEquals(expected, output);
    }

    @Test
    //covers getU
    void testGetU() {
        double expected = 3.33;
        double output = landerState.getU();

        assertEquals(expected, output);
    }

    @Test
    //covers setU
    void testSetU() {
        double u = -0.3;
        double expected = u;

        landerState.setU(u);
        double output = landerState.getU();

        assertEquals(expected, output);
    }

    @Test
    //covers getTorque
    void testGetTorque() {
        double expected = 10.1;
        double output = landerState.getTorque();

        assertEquals(expected, output);
    }

    @Test
    //covers getStateToString
    void testGetStateToString() {
        String expected = "0.0, 1.5, -4.0\n-1.5, 3.0, 4.76";
        String output = landerState.getStateToString();

        assertEquals(expected, output);
    }

    @Test
    //covers getPos
    void testGetPos() {
        Vector expected = new Vector(new double[]{0.0, 1.5, -4.0});
        Vector output = landerState.getPos();

        for (int i = 0; i < output.getDimension(); i++) {
            assertEquals(expected.get(i), output.get(i));
        }
    }

    @Test
    //covers getVel
    void testGetVel() {
        Vector expected = new Vector(new double[]{-1.5, 3.0, 4.76});
        Vector output = landerState.getVel();

        for (int i = 0; i < output.getDimension(); i++) {
            assertEquals(expected.get(i), output.get(i));
        }
    }

    @Test
    //covers setVel
    void testSetVel() {
        double[] vel = {-0.1, -0.0, 4.76};
        Vector expected = new Vector(vel);
        landerState.setVel(new Vector(vel));
        Vector output = landerState.getVel();

        for (int i = 0; i < output.getDimension(); i++) {
            assertEquals(expected.get(i), output.get(i));
        }
    }

    @Test
    //covers getMass
    void testGetMass() {
        double expected = 50000;
        double output = landerState.getMass();

        assertEquals(expected, output);
    }

    @Test
    //covers getTotalState
    void testGetTotalState() {
        String expected = "Position: 0.0, 1.5, -4.0\nVelocity: -1.5, 3.0, 4.76";
        String output = landerState.getTotalState();

        assertEquals(expected, output);
    }
}
package physics.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import static org.junit.jupiter.api.Assertions.*;

/*
This class is used to test the features of the LandingFunction class.
To do so, there is a local LandingFunction variable that stores a landingFunction
instance to runs the test on.

Each method will be called from this local landingFunction instance which is
deleted and reconstructed after each test. This is done with the help of
the setUp method which reinitialize the local landingFunction and the tearDown
method which erases the used landingFunction instance. The setUp method is called
before each test whereas the tearUp method is called after each test is done
so that if any test changes the local landingFunction those changes will be undone.
This assures the fact that each test will be run on the same conditions.
 */

class LandingFunctionTest {

    private LandingFunction landingFunction;
    private double G = -1.352;

    @BeforeEach
    void setUp() {
        landingFunction = new LandingFunction();
    }

    @AfterEach
    void tearDown() {
        landingFunction = null;
    }

    @Test
    //covers landerStep
    void testLanderStep() {
        Vector vector1 = new Vector(new double[]{0, 1.5, -4});
        Vector vector2 = new Vector(new double[]{-1.5, 3, 4.76});
        StateVector state = new StateVector(new Vector[]{vector1, vector2});
        double acceleration = 3.33;
        double torque = 10.1;
        LanderState landerState = new LanderState(state, acceleration, torque);
        double h = 0.2;

        Vector newPos = landingFunction.calculateNewPos(landerState, h);
        Vector newVel = landingFunction.calculateNewVel(landerState, h);
        StateVector expected = new StateVector(new Vector[]{newPos, newVel});

        LanderState output = landingFunction.LanderStep(landerState, h);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getState().getVector(i).get(j));
            }
        }
    }

    @Test
    //covers calculateNewPos
    void testCalculateNewPos() {
        Vector vector1 = new Vector(new double[]{0, 1.5, -4});
        Vector vector2 = new Vector(new double[]{-1.5, 3, 4.76});
        StateVector state = new StateVector(new Vector[]{vector1, vector2});
        double acceleration = 3.33;
        double torque = 10.1;
        LanderState landerState = new LanderState(state, acceleration, torque);
        double tempTheta = landerState.getThetaPos();
        double h = 0.2;

        double theta = landingFunction.calculateTheta(landerState);
        landerState.setThetaPos(theta);
        Vector expected = landerState.getPos().add(landerState.getVel().multiply(h));
        landerState.setThetaPos(tempTheta);

        Vector output = landingFunction.calculateNewPos(landerState, h);

        for (int i = 0; i < output.getDimension(); i++) {
            assertEquals(expected.get(i), output.get(i));
        }
    }

    @Test
    //covers calculateNewVel
    void testCalculateNewVel() {
        Vector vector1 = new Vector(new double[]{0, 1.5, -4});
        Vector vector2 = new Vector(new double[]{-1.5, 3, 4.76});
        StateVector state = new StateVector(new Vector[]{vector1, vector2});
        double acceleration = 3.33;
        double torque = 10.1;
        LanderState landerState = new LanderState(state, acceleration, torque);
        double h = 0.2;

        Vector acc = landingFunction.getAcceleration(landerState);
        Vector expected = landerState.getVel().add(acc.multiply(h));

        Vector output = landingFunction.calculateNewVel(landerState, h);
    }

    @Test
    //covers getAcceleration
    void testGetAcceleration() {
        Vector vector1 = new Vector(new double[]{0, 1.5, -4});
        Vector vector2 = new Vector(new double[]{-1.5, 3, 4.76});
        StateVector state = new StateVector(new Vector[]{vector1, vector2});
        double acceleration = 3.33;
        double torque = 10.1;
        LanderState landerState = new LanderState(state, acceleration, torque);

        Vector acc = new Vector(new double[3]);
        double x = landerState.getU()*Math.sin(landerState.getThetaPos());
        double y = landerState.getU()*Math.cos(landerState.getThetaPos()) + G;
        acc.set(0, x);
        acc.set(1, y);
        acc.set(2, landerState.getTorque());
        Vector expected = acc;

        Vector output = landingFunction.getAcceleration(landerState);
    }

    @Test
    //covers calculateTheta
    void testCalculateTheta() {
        Vector vector1 = new Vector(new double[]{0, 1.5, -4});
        Vector vector2 = new Vector(new double[]{-1.5, 3, 4.76});
        StateVector state = new StateVector(new Vector[]{vector1, vector2});
        double acceleration = 3.33;
        double torque = 10.1;
        LanderState landerState = new LanderState(state, acceleration, torque);

        double theta = Math.atan((landerState.getPos().get(1)-G)/landerState.getPos().get(0));
        double expected = theta;

        double output = landingFunction.calculateTheta(landerState);

        assertEquals(expected, output);
    }

    @Test
    //covers calculateU
    void testCalculateU() {
        Vector vector1 = new Vector(new double[]{0, 1.5, -4});
        Vector vector2 = new Vector(new double[]{-1.5, 3, 4.76});
        StateVector state = new StateVector(new Vector[]{vector1, vector2});
        double acceleration = 3.33;
        double torque = 10.1;
        LanderState landerState = new LanderState(state, acceleration, torque);
        double theta = -2.3;
        double expected = -landerState.getPos().get(0)/Math.sin(theta);

        double output = landingFunction.calculateU(landerState, theta);

        assertEquals(expected, output);
    }
}
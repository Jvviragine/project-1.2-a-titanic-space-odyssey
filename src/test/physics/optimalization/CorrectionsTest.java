package physics.optimalization;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import javax.swing.plaf.nimbus.State;

import static org.junit.jupiter.api.Assertions.*;

/*
This class is used to test the features of the Corrections class.
To do so, there is a local Corrections variable that stores a corrections
instance to runs the test on.

Each method will be called from this local corrections instance which is
deleted and reconstructed after each test. This is done with the help of
the setUp method which reinitialize the local corrections and the tearDown
method which erases the used corrections instance. The setUp method is called
before each test whereas the tearUp method is called after each test is done
so that if any test changes the local corrections those changes will be undone.
This assures the fact that each test will be run on the same conditions.
 */

class CorrectionsTest {

    private Corrections corrections;

    @BeforeEach
    void setUp() {
        corrections = new Corrections();
    }

    @AfterEach
    void tearDown() {
        corrections = null;
    }

    @Test
    //covers adjust
    void testAdjust() {
        Vector u1 = new Vector(new double[]{1, 2, 3});
        Vector u2 = new Vector(new double[]{-1, -2, -3});
        Vector u3 = new Vector(new double[]{0, 0, 0});
        StateVector stateVector1 = new StateVector(new Vector[]{u1, u2, u3, u1, u2, u3});

        Vector v1 = new Vector(new double[]{0.1, 0.2, 0.3});
        Vector v2 = new Vector(new double[]{-0.1, -0.2, -0.3});
        Vector v3 = new Vector(new double[]{-0.0, -0.0, -0.0});
        StateVector stateVector2 = new StateVector(new Vector[]{v1, v2, v3, v1, v2, v3});

        double eta = 200000;
        double timePassed = 100000;

        double vectorChanges = 0;
        double temp = 0;
        double timeLeft = eta - timePassed;

        for(int i = 0; i < 3; i++){
            temp = stateVector1.getVector(1).get(i);
            stateVector1.getVector(1).set(i,(stateVector2.getVector(0).get(i) - stateVector1.getVector(0).get(i)) * (1/timeLeft));
            vectorChanges += FuelUsage.fuel(stateVector1.getVector(1).get(i), temp, 1);
        }
        StateVector expected = stateVector1;

        StateVector outputProbeVector = (StateVector) corrections.adjust(stateVector1, stateVector2, timePassed,eta)[0];

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), outputProbeVector.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers orbitEntry
    void testOrbitEntry() {
        Vector u1 = new Vector(new double[]{1, 2, 3});
        Vector u2 = new Vector(new double[]{-1, -2, -3});
        Vector u3 = new Vector(new double[]{0, 0, 0});
        StateVector stateVector1 = new StateVector(new Vector[]{u1, u2, u3, u1, u2, u3});

        Vector v1 = new Vector(new double[]{0.1, 0.2, 0.3});
        Vector v2 = new Vector(new double[]{-0.1, -0.2, -0.3});
        Vector v3 = new Vector(new double[]{-0.0, -0.0, -0.0});
        StateVector stateVector2 = new StateVector(new Vector[]{v1, v2, v3, v1, v2, v3});

        double eta = 200000;
        double timePassed = 100000;

        double goalVelocity = 500.5;

        double timeLeft = eta - timePassed;
        double[] allV = new double[3];
        double totalV = 0;
        int count = 0;
        do{
            for(int i = 0; i < 3; i++){
                stateVector1.getVector(1).set(i,(stateVector2.getVector(0).get(i) - stateVector1.getVector(0).get(i)) * (1/timeLeft) - count);
                allV[i] = stateVector1.getVector(1).get(i);
            }
            count++;
            totalV = Math.sqrt(Math.pow(allV[0], 2) + Math.pow(allV[1], 2) + Math.pow(allV[2], 2));
        }
        while (totalV > goalVelocity);
        StateVector expected = stateVector1;

        StateVector output = corrections.orbitEntry(stateVector1, stateVector2, timePassed, eta, goalVelocity);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }
}
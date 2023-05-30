package physics.optimalization;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import javax.swing.plaf.nimbus.State;

import static org.junit.jupiter.api.Assertions.*;

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
    //covers adjust for timePassed < year
    void testAdjustWithTimePassedLessThenOneYear() {
        Vector u1 = new Vector(new double[]{1, 2, 3});
        Vector u2 = new Vector(new double[]{-1, -2, -3});
        Vector u3 = new Vector(new double[]{0, 0, 0});
        StateVector stateVector1 = new StateVector(new Vector[]{u1, u2, u3, u1, u2, u3});

        Vector v1 = new Vector(new double[]{0.1, 0.2, 0.3});
        Vector v2 = new Vector(new double[]{-0.1, -0.2, -0.3});
        Vector v3 = new Vector(new double[]{-0.0, -0.0, -0.0});
        StateVector stateVector2 = new StateVector(new Vector[]{v1, v2, v3, v1, v2, v3});

        double timeLeft = 10000;
        double timePassed = 31536000 - timeLeft;

        for(int i = 0; i < 3; i++){
            stateVector1.getStateVector()[i+3] = stateVector1.getStateVector()[i].subtract(stateVector2.getStateVector()[i]).multiply(1/timeLeft);
        }
        StateVector expected = stateVector1;

        StateVector output = corrections.adjust(stateVector1, stateVector2, timePassed);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers adjust for timePassed >= year
    void testAdjustWithTimePassedMoreThenOneYear() {
        Vector u1 = new Vector(new double[]{1, 2, 3});
        Vector u2 = new Vector(new double[]{-1, -2, -3});
        Vector u3 = new Vector(new double[]{0, 0, 0});
        StateVector stateVector1 = new StateVector(new Vector[]{u1, u2, u3, u1, u2, u3});

        Vector v1 = new Vector(new double[]{0.1, 0.2, 0.3});
        Vector v2 = new Vector(new double[]{-0.1, -0.2, -0.3});
        Vector v3 = new Vector(new double[]{-0.0, -0.0, -0.0});
        StateVector stateVector2 = new StateVector(new Vector[]{v1, v2, v3, v1, v2, v3});

        double timeLeft = 10000;
        double timePassed = 2*31536000 - timeLeft;

        for(int i = 0; i < 3; i++){
            stateVector1.getStateVector()[i+3] = stateVector1.getStateVector()[i].subtract(stateVector2.getStateVector()[i]).multiply(1/timeLeft);
        }
        StateVector expected = stateVector1;

        StateVector output = corrections.adjust(stateVector1, stateVector2, timePassed);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }
}
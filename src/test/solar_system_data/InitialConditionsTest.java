package solar_system_data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import static org.junit.jupiter.api.Assertions.*;

class InitialConditionsTest {

    private static Vector initialProbePosition;
    private static Vector initialProbeVelocity;
    private static double mass;

    @BeforeAll
    //Gets the static attribute values from the InitialConditions class
    static void init() {
        initialProbePosition = InitialConditions.getInitialProbePosition();
        initialProbeVelocity = InitialConditions.getInitialProbeVelocity();
        mass = InitialConditions.getProbeMass();
    }

    @BeforeEach
    //Resets the correct static attribute values to the InitialConditions class
    void setUp() {
        InitialConditions.setProbeInitialPosition(initialProbePosition);
        InitialConditions.setProbeInitialVelocity(initialProbeVelocity);
    }

    @Test
    //cover getInitialProbePosition
    void testGetInitialProbePosition() {
        Vector expected = initialProbePosition;
        Vector output = InitialConditions.getInitialProbePosition();
        for (int i = 0; i < expected.getDimension(); i++) {
            assertEquals(expected.get(i), output.get(i));
        }
    }

    @Test
    //cover getInitialProbeVelocity
    void testGetInitialProbeVelocity() {
        Vector expected = initialProbeVelocity;
        Vector output = InitialConditions.getInitialProbeVelocity();
        for (int i = 0; i < expected.getDimension(); i++) {
            assertEquals(expected.get(i), output.get(i));
        }
    }

    @Test
    //cover setInitialProbePosition
    void testSetProbeInitialPosition() {
        double[] expected = new double[]{0, -1, 2, 3.5};
        InitialConditions.setProbeInitialPosition(new Vector(expected));
        Vector output = InitialConditions.getInitialProbePosition();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], output.get(i));
        }
    }

    @Test
    void testSetProbeInitialVelocity() {
        double[] expected = new double[]{0, -1, 2, 3.5};
        InitialConditions.setProbeInitialVelocity(new Vector(expected));
        Vector output = InitialConditions.getInitialProbeVelocity();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], output.get(i));
        }
    }

    @Test
    void testGetProbeInitialState() {
        Vector[] expected = new Vector[]{initialProbePosition, initialProbeVelocity};
        StateVector output = InitialConditions.getProbeInitialState();
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].getDimension(); j++) {
                assertEquals(expected[i].get(j), output.getVector(i).get(j));
            }

        }
    }

    @Test
    void testGetProbeMass() {
        double expected = this.mass;
        double output = InitialConditions.getProbeMass();
        assertEquals(expected, output);
    }
}
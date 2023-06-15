package solar_system_data;

import org.junit.jupiter.api.*;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import static org.junit.jupiter.api.Assertions.*;

/*
This class saves the initial condition state vectors in static attributes.
To test the methods that apply changes on those static fields, this test class
will recover each static values and will save them to recover any changes made

This is how it works :

1- init() : saves the static attributes of InitialConditions in local variables

2- setUp() : set the static attributes of the InitialConditions class to their
    initial values from the local variables.
    Run a test and repeat this second test until all tests are covered

3- cleanUp() : reset the InitialConditions values one last time so that no static
    values are changed from the tested class
 */

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

    @AfterAll
    //Restores the correct values to the static fields after all the tests
    static void cleanUp() {
        InitialConditions.setProbeInitialPosition(initialProbePosition);
        InitialConditions.setProbeInitialVelocity(initialProbeVelocity);
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
    //cover setProbeInitialVelocity
    void testSetProbeInitialVelocity() {
        double[] expected = new double[]{0, -1, 2, 3.5};
        InitialConditions.setProbeInitialVelocity(new Vector(expected));
        Vector output = InitialConditions.getInitialProbeVelocity();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], output.get(i));
        }
    }

    @Test
    //cover getProbeInitialState
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
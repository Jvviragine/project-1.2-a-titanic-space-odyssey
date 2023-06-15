package celestial_bodies;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import static org.junit.jupiter.api.Assertions.*;

/*
This class tests the Probe class by constructing a probe instance before each test.
This step happens with the setUp method where an arbitrary probe is initiated.
After each test is done the changes done on the testing instance are unmade.
To do so, the instance is set to null after each test with the tearDown method before
rebuilding it with the same constructor parameters using setUp.
 */

class ProbeTest {

    private Probe probe;

    @BeforeEach
    //Sets up a probe before each test
    void setUp() {
        String name = "probe";
        Vector v1 = new Vector(new double[]{0, -1, 2});
        Vector v2 = new Vector(new double[]{0.5, -3, 10});
        StateVector stateVector = new StateVector(new Vector[]{v1, v2});
        double mass = Probe.getProbeMass();

        probe = new Probe(name, mass, stateVector);
    }

    @AfterEach
    //Deletes the probe before resetting it to its initial state
    void tearDown() {
        probe = null;
    }

    @Test
    void testProbe() {
        String nameExpected = "probe";
        Vector v1 = new Vector(new double[]{0, -1, 2});
        Vector v2 = new Vector(new double[]{0.5, -3, 10});
        StateVector stateVectorExpected = new StateVector(new Vector[]{v1, v2});
        double massExpected = Probe.getProbeMass();
        Probe output = new Probe(nameExpected, massExpected, stateVectorExpected);
        assertEquals(nameExpected, output.getProbeName());
        assertEquals(massExpected, Probe.getProbeMass());
    }

    @Test
    //covers getProbeName
    void testGetProbeName() {
        String expected = "probe";
        String output = probe.getProbeName();
        assertEquals(expected, output);
    }

    @Test
    //covers getProbeMass
    void testGetProbeMass() {
        double expected = Probe.getProbeMass();
        double output = Probe.getProbeMass();
        assertEquals(expected, output);
    }
}
package celestial_bodies;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import static org.junit.jupiter.api.Assertions.*;

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
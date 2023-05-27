package celestial_bodies;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import static org.junit.jupiter.api.Assertions.*;

class CelestialBodyTest {

    private CelestialBody celestialBody;

    @BeforeEach
    //Sets up a celestialBody before each test
    void setUp() {
        String name = "test";
        Vector v1 = new Vector(new double[]{0, -1, 2});
        Vector v2 = new Vector(new double[]{0.5, -3, 10});
        StateVector stateVector = new StateVector(new Vector[]{v1, v2});
        double mass = 500.1;

        celestialBody = new CelestialBody("test", stateVector, mass);
    }

    @AfterEach
    //Deletes the celestialBody before resetting it to its initial state
    void tearDown() {
        celestialBody = null;
    }

    @Test
    //covers getName
    void testGetName() {
        String expected = "test";
        String output = celestialBody.getName();
        assertEquals(expected, output);
    }

    @Test
    //covers getInitialState
    void testGetInitialState() {
        Vector vector1 = new Vector(new double[]{0, -1, 2});
        Vector vector2 = new Vector(new double[]{0.5, -3, 10});
        StateVector expected = new StateVector(new Vector[]{vector1, vector2});
        StateVector output = celestialBody.getInitialState();
        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers getMass
    void testGetMass() {
        double expected = 500.1;
        double output = celestialBody.getMass();
        assertEquals(expected, output);
    }
}
package solar_system_data;

import celestial_bodies.CelestialBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.vectors.StateVector;

import static org.junit.jupiter.api.Assertions.*;

/*
This class tests getter methods on a static attribute.
To do so, the static attribute of the PlanetaryData class is saved in a local variable


This class also tests the indexOf method which returns the index of the given String
representing the name of a celestialBody contained in the array of celestial bodies.
Two input partitions can be identified :

1- The given String is the name of a celestialBody : should return the correct index

2- The given String is not the name of a celestialBody : should return an exception

 */

class PlanetaryDataTest {

    private static CelestialBody[] celestialBodies;

    @BeforeAll
    //Gets the celestialBodies attribute from the InitialConditions class
    static void init() {
        celestialBodies = PlanetaryData.getCelestialBodies();
    }

    @Test
    //covers getCelestialBodies
    void testGetCelestialBodies() {
        CelestialBody[] expected = celestialBodies;
        CelestialBody[] output = PlanetaryData.getCelestialBodies();
        assertEquals(expected, output);
    }

    @Test
    //covers getCelestialBodiesStateVector
    void testGetCelestialBodiesStateVector() {
        StateVector[] expected = new StateVector[celestialBodies.length];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = celestialBodies[i].getInitialState();
        }
        StateVector[] output = PlanetaryData.getCelestialBodiesStateVector();
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].getNumberOfVectors(); j++) {
                for (int k = 0; k < expected[i].getVector(j).getDimension(); k++) {
                    assertEquals(expected[i].getVector(j).get(k), output[i].getVector(j).get(k));
                }
            }
        }
    }

    @Test
    //covers getCelestialBodiesMasses
    void testGetCelestialBodiesMasses() {
        double[] expected = new double[celestialBodies.length];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = celestialBodies[i].getMass();
        }
        double[] output = PlanetaryData.getCelestialBodiesMasses();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], output[i]);
        }
    }

    @Test
    //covers getCelestialBodyNames
    void testGetCelestialBodyNames() {
        String[] expected = new String[celestialBodies.length];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = celestialBodies[i].getName();
        }
        String[] output = PlanetaryData.getCelestialBodyNames();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], output[i]);
        }
    }

    @Test
    //covers indexOf for an existing celestialBody name in celestialBodies
    void testIndexOfWithNameInCelestialBodies() {
        String name = celestialBodies[0].getName();
        int expected = 0;
        int output = PlanetaryData.indexOf(name);
        assertEquals(expected, output);
    }

    @Test
    //covers indexOf for an unknown celestialBody name in celestialBodies
    void testIndexOfWithNameNotInCelestialBodies() {
        String name = "";
        int expected = -1;
        int output = PlanetaryData.indexOf(name);
        assertEquals(expected, output);
    }
}
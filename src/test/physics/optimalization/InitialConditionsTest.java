package physics.optimalization;

import org.junit.jupiter.api.*;
import physics.simulation.SolarSystemPhysicsSimulation;
import physics.vectors.StateVector;
import physics.vectors.Vector;
import solar_system_data.PlanetaryData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
This class is used to test the features of the InitialConditions class.
To do so, there is a local InitialConditions variable that stores a initialConditions
instance to runs the test on.

Each method will be called from this local initialConditions instance which is
deleted and reconstructed after each test. This is done with the help of
the setUp method which reinitialize the local initialConditions and the tearDown
method which erases the used initialConditions instance. The setUp method is called
before each test whereas the tearUp method is called after each test is done
so that if any test changes the local initialConditions those changes will be undone.
This assures the fact that each test will be run on the same conditions.
 */

class InitialConditionsTest {

    private static Vector position;

    private static Vector current;

    private static Vector velocity;

    @BeforeAll
    //Gets the static attribute values from the InitialConditions class
    static void init() {
        position = InitialConditions.pos;

        current = InitialConditions.current;

        velocity = InitialConditions.velocity;
    }

    @BeforeEach
    //Resets the correct static attribute values to the InitialConditions class
    void setUp() {
        InitialConditions.pos = position;
        InitialConditions.current = current;
        InitialConditions.velocity = velocity;
    }


    @Test
    //covers findPosOnSurface
    void testFindPosOnSurface() {
        Vector u = new Vector(new double[]{1, 2, 3});
        Vector v = new Vector(new double[]{0, 0.2, -3});
        double radius = 9.9;

        double dist = v.distance(u);
        Vector unit = new Vector(new double[3]);
        for(int i = 0; i<unit.getDimension(); i++){
            unit.set(i,u.get(i)-v.get(i)/dist);
        }
        Vector point = new Vector(new double[3]);
        for(int i = 0; i<point.getDimension(); i++){
            point.set(i,v.get(i) + radius*unit.get(i));
        }
        Vector expected = point;

        Vector output = InitialConditions.findPosOnSurface(u, v, radius);

        for (int i = 0; i < u.getDimension(); i++) {
            assertEquals(expected.get(i), output.get(i));
        }
    }

    @Disabled("Take too much time !!")
    @Test
    //covers findVel
    void testFindVel() {
        int iter = 500;
        StateVector iniState = new StateVector(new Vector[]{position, current});
        SolarSystemPhysicsSimulation system = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames());
        List<List<StateVector>> paths = system.simulateOrbitsWithProbe(iniState,31536000,23200);
        List<StateVector> probePath = paths.get(11);
        List<StateVector> titanPath = paths.get(8);
        double bestDistance = InitialConditions.closest(probePath, titanPath);
        while(iter>0){
            iter--;
            Vector[] neighbours = InitialConditions.generateNeighbours(current);
            for (Vector neighbour : neighbours) {
                iniState.getStateVector()[1] = neighbour;
                system = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames());
                paths = system.simulateOrbitsWithProbe(iniState,31536000,23200);
                probePath = paths.get(11);
                titanPath = paths.get(8);
                double distance = InitialConditions.closest(probePath,titanPath);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    current = neighbour;
                }
            }
        }
        Vector expected = current;

        Vector output = InitialConditions.findVel(current);

        for (int i = 0; i < expected.getDimension(); i++) {
            assertEquals(expected.get(i), output.get(i));
        }
    }

    @Test
    //covers closest
    void testClosest() {
        StateVector iniState = new StateVector(new Vector[]{position, current});
        SolarSystemPhysicsSimulation system = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames());
        List<List<StateVector>> paths = system.simulateOrbitsWithProbe(iniState,23200*10,23200);
        List<StateVector> pathProbe = paths.get(11);
        List<StateVector> pathTitan = paths.get(8);
        double dist = pathProbe.get(0).getVector(0).distance(pathTitan.get(0).getVector(0));
        for(int i = 0;i<pathProbe.size();i++){
            double ndist = pathProbe.get(i).getVector(0).distance(pathTitan.get(i).getVector(0));
            if(ndist<dist){
                dist = ndist;
            }
        }
        double expected = dist;

        double output = InitialConditions.closest(pathProbe, pathTitan);

        assertEquals(expected, output);
    }
}
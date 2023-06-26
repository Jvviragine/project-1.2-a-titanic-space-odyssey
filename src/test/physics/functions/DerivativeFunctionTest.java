package physics.functions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.simulation.SolarSystemPhysicsSimulation;
import physics.vectors.StateVector;
import physics.vectors.Vector;
import solar_system_data.InitialConditions;

import static org.junit.jupiter.api.Assertions.*;

/*
This class is responsible for testing the DerivativeFunction class
which is a class that represents a basic function.
It is used to perform tests on the solvers.

Find the input partitioning below :

applyFunction(StateVector y, double t) :
    - this.system == null
    - this.system != null

getAcceleration(int index) :
- index < system.stateVectors.length
- index = system.totalBodies() - 1
- index >= system.stateVectors.length


 */

class DerivativeFunctionTest {

    private SolarSystemPhysicsSimulation system;
    private DerivativeFunction derivativeFunction;

    @BeforeEach
    //initializes a derivativeFunction to test on
    void setUp() {
        Vector vector1 = new Vector(new double[]{1,20,300});
        Vector vector2 = new Vector(new double[]{4,50,600});
        StateVector stateVector1 = new StateVector(new Vector[]{vector1, vector2});

        Vector vector3 = new Vector(new double[]{700,80,9});
        Vector vector4 = new Vector(new double[]{1000,110,12});
        StateVector stateVector2 = new StateVector(new Vector[]{vector1, vector2});

        StateVector[] stateVectors = new StateVector[]{stateVector1, stateVector2};

        double[] masses = new double[]{50,100};

        String [] planets = new String[]{"planet1","planet2"};

        system = new SolarSystemPhysicsSimulation(stateVectors,masses,planets);

        derivativeFunction = new DerivativeFunction(system);
    }

    @AfterEach
    //deletes the derivativeFunction before resetting it
    void tearDown() {
        derivativeFunction = null;
        system = null;
    }

    @Test
    //covers applyFunction for a stateVector in system.stateVectors
    public void testApplyFunctionWithStateFunctionKnownToSystem() {
        Vector vector1 = new Vector(new double[]{1,20,300});
        Vector vector2 = new Vector(new double[]{4,50,600});
        StateVector stateVector = new StateVector(new Vector[]{vector1, vector2});
        StateVector expected = new StateVector(new Vector[]{vector2, derivativeFunction.getAcceleration(system.getIndex(stateVector))});

        double time = 0.0;
        StateVector output = derivativeFunction.applyFunction(stateVector, time);

        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        };
    }

    @Test
    //covers applyFunction for a stateVector not in system.stateVectors
    public void testApplyFunctionWithStateFunctionUnknownToSystem() {
        double time = 0.0;
        Vector vector1 = new Vector(new double[]{0.01, 0.02});
        Vector vector2 = new Vector(new double[]{0.01, 0.02});
        StateVector stateVector = new StateVector(new Vector[]{vector1, vector2});

        assertThrows(IllegalArgumentException.class, () -> derivativeFunction.applyFunction(stateVector, time));
    }

    @Test
    //covers resetState
    void testResetState() {
        Vector vector1 = new Vector(new double[]{0.01, 0.02});
        Vector vector2 = new Vector(new double[]{0.01, 0.02});
        StateVector[] expected = new StateVector[]{new StateVector(new Vector[]{vector1, vector2})};
        derivativeFunction.resetState(expected);

        StateVector[] output = system.getStateVectors();

        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].getNumberOfVectors(); j++) {
                for (int k = 0; k < expected[i].getVector(j).getDimension(); k++) {
                    assertEquals(expected[i].getVector(j).get(k), output[i].getVector(j).get(k));
                }
            }
        }
    }

    @Test
    //covers getAcceleration for an index < system.stateVectors.length
    public void testGetAccelerationWithIndexInBounds() {
        int index = 0;

        Vector force = new Vector(new double[3]);
        double mass = system.getMasses()[index];

        Vector position = system.getStateVectors()[index].getVector(0).copyOf();

        for(int i = 0; i < system.totalBodies(); i++) {
            if(i != index){
                Vector thisVector = system.getStateVectors()[i].getVector(0).copyOf();
                double weight = system.G * system.getMasses()[i] * mass;
                Vector diff = position.subtract(thisVector);
                double distance = Math.pow(position.distance(thisVector),3);
                Vector div = diff.multiply(1/distance);
                force = force.add(div.multiply(weight));
            }
        }
        force = force.multiply(-1);
        Vector expected = force.multiply(1/mass);

        Vector output = derivativeFunction.getAcceleration(index);

        for (int i = 0; i < expected.getDimension(); i++) {
            assertEquals(expected.get(i), output.get(i));
        }
    }

    @Test
    //covers getAcceleration for the probe index = system.totalBodies() - 1
    public void testGetAccelerationWithProbeIndex() {
        int index = system.totalBodies() - 1;

        Vector force = new Vector(new double[3]);
        double mass = InitialConditions.getProbeMass();

        Vector position = system.getStateVectors()[index].getVector(0).copyOf();

        for(int i = 0; i < system.totalBodies(); i++) {
            if(i != index){
                Vector thisVector = system.getStateVectors()[i].getVector(0).copyOf();
                double weight = system.G * system.getMasses()[i] * mass;
                Vector diff = position.subtract(thisVector);
                double distance = Math.pow(position.distance(thisVector),3);
                Vector div = diff.multiply(1/distance);
                force = force.add(div.multiply(weight));
            }
        }
        force = force.multiply(-1);
        Vector expected = force.multiply(1/mass);

        Vector output = derivativeFunction.getAcceleration(index);

        for (int i = 0; i < expected.getDimension(); i++) {
            assertEquals(expected.get(i), output.get(i));
        }
    }

    @Test
    //covers getAcceleration for an index >= system.stateVectors.length
    public void testGetAccelerationWithIndexOutOfBounds() {
        int index = system.totalBodies();
        assertThrows(IndexOutOfBoundsException.class, () -> derivativeFunction.getAcceleration(index));
    }
}
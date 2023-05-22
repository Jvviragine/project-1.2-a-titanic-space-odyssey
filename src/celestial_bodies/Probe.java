package celestial_bodies;
import physics.vectors.StateVector;
import physics.vectors.Vector;

/**
 * This class represents the Probe in which the trip to Mars is going to take place
 * In case a different Spacecraft is used, just construct the object probe with different Parameters
 */
public class Probe {

    // Instance Fields
    private String name;

    private Vector initialPosition;
    private Vector initialVelocity;

    private StateVector initialConditions;

    private double mass;

    public Probe(String name, double mass, StateVector initialConditions) {
        this.name = name;
        this.mass = mass;

        this.initialConditions = initialConditions;
        this.initialPosition = initialConditions.getVector(0);
        this.initialVelocity = initialConditions.getVector(1);
    }
}

package physics.functions;

import celestial_bodies.SolarSystem;
import physics.vectors.Vector;
import physics.vectors.StateVector;

public class DerivativeFunction implements Function{
    private SolarSystem system;

    public DerivativeFunction(SolarSystem system){
        this.system = system;
    }

    @Override
    public StateVector applyFunction(StateVector y, double t) {
        Vector[] vectors = new Vector[y.getNumberOfVectors()];
        double[] state = new double[y.getNumberOfDimensions()];

        //New position vector is the velocity vector from the original vector
        for(int i = 0; i < y.getNumberOfDimensions(); i++){
           state[i] = y.getVector(0).get(i);
        }

        //Place new position vector in new state array
        Vector vector1 = new Vector(state);
        vectors[0] = vector1;

        //Calculate acceleration and place new velocity in new state array
        vectors[1] = getAcceleration(system.getIndex(y));

        return new StateVector(vectors);
    }

    public Vector getAcceleration(int index){
        Vector force = new Vector(new double[2]);
        double mass = system.getMasses()[index];
        Vector position = system.getStateVectors()[index].getVector(0);

        for(int i = 0; i < system.totalBodies(); i++) {
            if(i != index){
                Vector thisVector = system.getStateVectors()[i].getVector(0);

                //G * MiMj
                double weight = system.G * system.getMasses()[i] * mass;

                //Xi - Xj
                Vector diff = position.subtract(thisVector);

                //||Xi - Xj||^3
                double distance = Math.pow(position.distance(thisVector),3);

                //Xi - Xj / ||Xi - Xj||^3
                Vector div = diff.multiply(1/distance);

                //G * MiMj * Xi - Xj / ||Xi - Xj||^3
                force = force.add(div.multiply(weight));
            }
        }
        force = force.multiply(-1);
        Vector acceleration = force.multiply(1/mass);

        return acceleration;
    }

}

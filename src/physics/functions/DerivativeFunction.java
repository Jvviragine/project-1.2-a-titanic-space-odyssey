package physics.functions;

import celestial_bodies.SolarSystem;
import physics.vectors.Vector;
import physics.vectors.StateVector;

public class DerivativeFunction implements Function{
    private SolarSystem system;

    public DerivativeFunction(SolarSystem system){
        this.system = system;
    }

    public static void main(String[] args) {
        double [] vector1 = new double[]{1,2,3};
        double [] vector2 = new double[]{4,5,6};
        Vector u = new Vector(vector1);
        Vector v = new Vector(vector2);
        Vector[] vectors1 = new Vector[]{u,v};
        StateVector sv1 = new StateVector(vectors1);

        double [] vector3 = new double[]{7,8,9};
        double [] vector4 = new double[]{10,11,12};
        Vector s = new Vector(vector3);
        Vector t = new Vector(vector4);
        Vector[] vectors2 = new Vector[]{s,t};
        StateVector sv2 = new StateVector(vectors2);

        StateVector[] svs = new StateVector[]{sv1,sv2};

        double[] masses = new double[]{50,100};

        String [] planets = new String[]{"planet1","planet2"};

        SolarSystem mySystem = new SolarSystem(svs,masses,planets);

        DerivativeFunction df = new DerivativeFunction(mySystem);

        StateVector n = df.applyFunction(sv1,0);

        for(int i=0;i<n.getNumberOfVectors();i++){
            for(int j=0;j<n.getVector(i).getDimension();j++){
                System.out.println(n.getVector(i).get(j));
            }
        }
    }

    @Override
    public StateVector applyFunction(StateVector y, double t) {
        Vector[] vectors = new Vector[y.getNumberOfVectors()];

        //Place new position vector (old velocity vector) in new state array
        Vector vector1 = y.getVector(1).copyOf();
        vectors[0] = vector1;

        //Calculate acceleration and place new velocity in new state array
        vectors[1] = getAcceleration(system.getIndex(y));

        return new StateVector(vectors);
    }

    public Vector getAcceleration(int index){
        Vector force = new Vector(new double[3]);
        double mass = system.getMasses()[index];
        Vector position = system.getStateVectors()[index].getVector(0).copyOf();

        for(int i = 0; i < system.totalBodies(); i++) {
            if(i != index){
                Vector thisVector = system.getStateVectors()[i].getVector(0).copyOf();

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

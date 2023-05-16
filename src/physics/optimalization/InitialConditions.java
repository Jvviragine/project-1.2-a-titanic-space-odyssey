package physics.optimalization;

import celestial_bodies.SolarSystem;
import physics.functions.DerivativeFunction;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import physics.functions.Function;

public class InitialConditions {

    /**
     * Finds point on the surface of a planet that is the closest to the desired target,
     * if we want to find the point on the orbit we increase the radius
     * https://math.stackexchange.com/questions/1784106/how-do-i-compute-the-closest-points-on-a-sphere-given-a-point-outside-the-sphere
     * @param target target point
     * @param center center of a planet
     * @param radius radius of the planet
     * @return point on the surface closest to the target
     */
    public Vector findPosOnSurface(Vector target, Vector center, double radius){
        double dist = center.distance(target);
        Vector unit = new Vector(new double[]{3});
        for(int i = 0; i<unit.getDimension(); i++){
            unit.set(i,target.get(i)-center.get(i)/dist);
        }
        Vector point = new Vector(new double[]{3});
        for(int i = 0; i<point.getDimension(); i++){
            point.set(i,center.get(i) + radius*unit.get(i));
        }
        return point;
    }

    public Vector findVelocity(Vector current, Vector probe, Vector titan){
        double dist = 0;
        int iter = 1000;
        while(iter>0){
            iter--;
            //SolarSystem system = new SolarSystem();
            //Vector possibleVel = gradietDecsent(current, 0.01, new DerivativeFunction(system));

            //here simulation of the trip with the possible velocity

            //checking if the simulation with the velocity got us to titan
            if(closeEnough(system.getStateVectors()[10].getVector(0), system.getStateVectors()[9].getVector(0), dist)){
                return possibleVel;
            }
            //updating the vectors
            current = possibleVel;
        }
        return current;
    }

    public Vector gradietDecsent(Vector current, double learningRate, DerivativeFunction function){

        //calculate the derivative (acceleration) for the probe
        Vector acc = function.getAcceleration(10);

        //calculating the error which is the expected output - current output,
        // in our case the derivative of the velocity (acceleration) - current velocity
        Vector error = error(current, acc);

        //y_new = currentVel - learningRate*error
        Vector newVel = current.subtract(error.multiply(learningRate));

        return newVel;
    }

    public Vector error(Vector initial, Vector expected){
        return expected.subtract(initial);
    }

    public boolean closeEnough(Vector probe, Vector titan, double dist){
        return probe.distance(titan)<dist;
    }
}

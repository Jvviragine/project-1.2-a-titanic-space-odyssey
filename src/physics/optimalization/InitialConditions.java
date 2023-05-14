package physics.optimalization;

import physics.vectors.StateVector;
import physics.vectors.Vector;

import physics.functions.Function;

public class InitialConditions {

    /**
     * Finds point on the surface of a planet that is the closest to the desired target,
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

//    public void findVelocity(Vector current, Vector probe, Vector titan){
//        double dist = 0;
//        int iter = 1000;
//        while(!closeEnough(probe, titan, dist) && iter>0){
//            iter--;
//            gradietDecsent(current, 0.01, );
//        }
//    }
//
//    public void gradietDecsent(Vector current, double learningRate, Function function){
//
//        //calculate the derivative (acceleration)
//        Vector[] vec = new Vector[1];
//        vec[0] = current;
//        StateVector sVec = new StateVector(vec);
//        StateVector sAcc = function.applyFunction(sVec, 0);
//        Vector acc = sAcc.getVector(0);
//
//        //calculating the error which is the expected output - current output,
//        // in our case the derivative of the velocity (acceleration) - current velocity
//        Vector error = error(current, acc);
//
//        //y_new = currentVel - learningRate*error
//        Vector newVel = current.subtract(error.multiply(learningRate));
//
//        //checking whether the new velocity gets us close enough to Titan by simulating the trip
//        //TODO simulation of the trip and checking closeEnough
//
//
//    }
//
//    public Vector error(Vector initial, Vector expected){
//        return expected.subtract(initial);
//    }
//
//    public boolean closeEnough(Vector probe, Vector titan, double dist){
//        return probe.distance(titan)<dist;
//    }
}

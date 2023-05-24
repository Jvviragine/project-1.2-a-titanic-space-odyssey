package physics.optimalization;

import celestial_bodies.SolarSystemPhysicsSimulation;
import physics.functions.DerivativeFunction;
import physics.vectors.StateVector;
import physics.vectors.Vector;
import solar_system_data.PlanetaryData;
import celestial_bodies.Probe;

import java.util.Arrays;
import java.util.List;

public class InitialConditions {
    //static Vector pos = findPosOnSurface(new Vector(new double[]{1254501624.95946, -761340299.067828, -36309613.8378104}), new Vector(new double[]{148186906.893642, -27823158.5715694, 33746.8987977113}), 6400);
    static Vector pos = new Vector(new double[]{-148458048.395164,-27524868.1841142,70233.6499287411});
    static Vector current = new Vector(new double[]{42.42270135156,-43.62738201925,-3.1328169170});
    double value = 0.5;

    /**
     * Finds point on the surface of a planet that is the closest to the desired target,
     * if we want to find the point on the orbit we increase the radius
     * https://math.stackexchange.com/questions/1784106/how-do-i-compute-the-closest-points-on-a-sphere-given-a-point-outside-the-sphere
     * @param target target point
     * @param center center of a planet
     * @param radius radius of the planet
     * @return point on the surface closest to the target
     */
    public static Vector findPosOnSurface(Vector target, Vector center, double radius){
        double dist = center.distance(target);
        Vector unit = new Vector(new double[3]);
        for(int i = 0; i<unit.getDimension(); i++){
            unit.set(i,target.get(i)-center.get(i)/dist);
        }
        Vector point = new Vector(new double[3]);
        for(int i = 0; i<point.getDimension(); i++){
            point.set(i,center.get(i) + radius*unit.get(i));
        }
        return point;
    }

//    public static Vector findVelocity(Vector current){
//        double dist = 350;
//        int iter = 1000;
//        Vector[] stateP = new Vector[2];
//        stateP[0] = pos;
//        while(iter>0){
//            iter--;
//            StateVector[] states = new StateVector[PlanetaryData.getCelestialBodiesStateVector().length];
//            stateP[1] = current;
//            states[0] = new StateVector(stateP);
//            for(int i = 1; i<PlanetaryData.getCelestialBodiesStateVector().length;i++){
//                states[i] = PlanetaryData.getCelestialBodiesStateVector()[i];
//            }
//            double[] masses = new double[PlanetaryData.getCelestialBodiesMasses().length];
//            masses[0] = 50000;
//            for(int i = 1; i<PlanetaryData.getCelestialBodiesMasses().length;i++){
//                masses[i] = PlanetaryData.getCelestialBodiesMasses()[i];
//            }
//            SolarSystemPhysicsSimulation system = new SolarSystemPhysicsSimulation(states,masses,PlanetaryData.getCelestialBodyNames());
//            Vector possibleVel = gradietDecsent(current, 0.01, new DerivativeFunction(system));
//
//
//            //here simulation of the trip with the possible velocity
//            stateP[1] = possibleVel;
//            states[0] = new StateVector(stateP);
//            system.simulateCelestialBodiesOrbit(31536000,5800);
//
//            if(closeEnough(system.getPath().get(0).get(system.getPath().get(0).size()-1).getVector(0), system.getPath().get(9).get(system.getPath().get(9).size()-1).getVector(0), dist)){
//                return possibleVel;
//            }
//
//            stateP[1] = possibleVel.multiply(-1);
//            states[0] = new StateVector(stateP);
//            system.simulateCelestialBodiesOrbit(31536000,5800);
//
//            System.out.println(iter);
//            System.out.println(current.get(0));
//            System.out.println(current.get(1));
//            System.out.println(current.get(2));
//
//            //checking if the simulation with the velocity got us to titan
//           if(closeEnough(system.getPath().get(0).get(system.getPath().get(0).size()-1).getVector(0), system.getPath().get(9).get(system.getPath().get(9).size()-1).getVector(0), dist)){
//                return possibleVel;
//            }
//            //updating the vectors
//            current = possibleVel;
//        }
//        return current;
//    }
//
//    public static Vector gradietDecsent(Vector current, double learningRate, DerivativeFunction function){
//
//        //calculate the derivative (acceleration) for the probe
//        Vector acc = function.getAcceleration(0);
//
//        //calculating the error which is the expected output - current output,
//        // in our case the derivative of the velocity (acceleration) - current velocity
//        Vector error = error(current, acc);
//
//        //y_new = currentVel - learningRate*error
//        Vector newVel = current.subtract(error.multiply(learningRate));
//
//        return newVel;
//    }

    public static Vector findVel(Vector current){
        int iter = 1000;
        StateVector[] states = setStates(current);
        double[] masses = setMasses();
        SolarSystemPhysicsSimulation system = new SolarSystemPhysicsSimulation(states,masses,PlanetaryData.getCelestialBodyNames());
        system.simulateCelestialBodiesOrbit(31536000,23200);
        List<StateVector> probePath = system.getPath().get(0);
        List<StateVector> titanPath = system.getPath().get(9);
        double bestDistance = closest(probePath,titanPath);
        System.out.println(bestDistance);
        while(iter>0){
            iter--;
            System.out.println(iter);
            Vector[] neighbours = generateNeighbours(current);
            for (Vector neighbour : neighbours) {
                states[0].getStateVector()[1] = neighbour;
                system = new SolarSystemPhysicsSimulation(states, masses, PlanetaryData.getCelestialBodyNames());
                system.simulateCelestialBodiesOrbit(31536000, 23200);
                probePath = system.getPath().get(0);
                titanPath = system.getPath().get(9);
                double distance = closest(probePath,titanPath);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    current = neighbour;
                    System.out.println(bestDistance);
                }
            }
        }
        return current;
    }

    public static Vector[] generateNeighbours(Vector current){
        Vector[] neighbours = new Vector[20];
        for(int i = 0; i<neighbours.length;i++){
            Vector n = current.add(randomVector(0.1));
            neighbours[i] = n;
            //System.out.print(n.get(0) + " ");
            //System.out.print(n.get(1) + " ");
            //System.out.println(n.get(2));
        }
        return neighbours;
    }

    public static Vector randomVector(double max){
        Vector rand = new Vector(new double[]{0,0,0});
        double min = max*(-1);
        int n = (int) (Math.random() * 3);
        double r = Math.random() * (max - min + 1) + min;
        rand.set(n, r);
        return rand;
    }

    public static StateVector[] setStates(Vector vel){
        Vector[] stateP = new Vector[2];
        stateP[0] = pos;
        StateVector[] states = new StateVector[PlanetaryData.getCelestialBodiesStateVector().length];
        stateP[1] = vel;
        states[0] = new StateVector(stateP);
        for(int i = 1; i<PlanetaryData.getCelestialBodiesStateVector().length;i++){
            states[i] = PlanetaryData.getCelestialBodiesStateVector()[i-1];
        }
        return states;
    }

    public static double[] setMasses(){
        double[] masses = new double[PlanetaryData.getCelestialBodiesMasses().length];
        masses[0] = 50000;
        for(int i = 1; i<PlanetaryData.getCelestialBodiesMasses().length;i++){
            masses[i] = PlanetaryData.getCelestialBodiesMasses()[i-1];
        }
        return masses;
    }

    public static double closest(List<StateVector> pathProbe, List<StateVector> pathTitan){
        //distance at the beggining
        double dist = pathProbe.get(0).getVector(0).distance(pathTitan.get(0).getVector(0));
        for(int i = 0;i<pathProbe.size();i++){
            double ndist = pathProbe.get(i).getVector(0).distance(pathTitan.get(i).getVector(0));
            if(ndist<dist){
                dist = ndist;
            }
        }
        return dist;
    }



//    public static Vector error(Vector initial, Vector expected){
//        return expected.subtract(initial);
//    }
//
//    public static boolean closeEnough(Vector probe, Vector titan, double dist){
//        return probe.distance(titan)<dist;
//   }

    public static void main(String[] args) {
        //pos = findPosOnSurface(new Vector(new double[]{1254501624.95946, -761340299.067828, -36309613.8378104}), new Vector(new double[]{148186906.893642, -27823158.5715694, 33746.8987977113}), 6400);
        Vector vel = findVel(current);
        System.out.println(vel.get(0));
        System.out.println(vel.get(1));
        System.out.println(vel.get(2));

    }
}

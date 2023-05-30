package physics.optimalization;

import gui.screens.StartScreen;
import physics.simulation.SolarSystemPhysicsSimulation;
import physics.vectors.StateVector;
import physics.vectors.Vector;
import solar_system_data.PlanetaryData;

import java.util.List;

public class InitialConditions {

    static Vector pos = new Vector(new double[]{-1.48e08,-2.78e07,40070});
    //static Vector pos = findPosOnSurface(PlanetaryData.getCelestialBodiesStateVector()[3].getVector(0), new Vector(new double[]{1.25450162495946E9, -7.61340299067828E8, -3.63096138378104E7}), 6400);
    static Vector current = new Vector(new double[]{43.70346586, -44.30558291, -1.3464296});

    static Vector velocity = new Vector(new double[]{0,0,0});


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

    /**
     * Method that finds the best initial velocity by generating neighbours for the initial guess
     * and simulation the trip with all generated neighbours. Next it takes the best found velocity
     * form the neighbours and repeats the whole process.
     * @param current, inital guess for the velocity
     * @return Vector representing the new velocity
     */
    public static Vector findVel(Vector current){
        int iter = 100;
        StateVector iniState = new StateVector(new Vector[]{pos, current});
        SolarSystemPhysicsSimulation system = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames(), StartScreen.finalSolver);
        List<List<StateVector>> paths = system.simulateOrbitsWithProbe(iniState,31536000,1800);
        List<StateVector> probePath = paths.get(11);
        List<StateVector> titanPath = paths.get(8);
        double bestDistance = closest(probePath, titanPath);
        while(iter>0){
            iter--;
            Vector[] neighbours = generateNeighbours(current);
            for (Vector neighbour : neighbours) {
                iniState.getStateVector()[1] = neighbour;
                system = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames(),StartScreen.finalSolver);
                paths = system.simulateOrbitsWithProbe(iniState,31536000,1800);
                probePath = paths.get(11);
                titanPath = paths.get(8);
                double distance = closest(probePath,titanPath);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    current = neighbour;
                }
            }
        }
        return current;
    }

    /**
     * Generating random neighbours for a given vector
     * @param current, vector that is going to be a bit changed
     * @return array of neighbours
     */
    public static Vector[] generateNeighbours(Vector current){
        Vector[] neighbours = new Vector[20];
        for(int i = 0; i<neighbours.length;i++){
            Vector r = randomVector(0.4);
            Vector n = current.add(r);
            neighbours[i] = n;
        }
        return neighbours;
    }

    /**
     * Method generating random vector with a given maximum change
     * @param max, the max and min (-max) value that can change the vector
     * @return new random Vector
     */
    public static Vector randomVector(double max){
        Vector rand = new Vector(new double[]{0,0,0});
        double min = max*(-1);
        //int n = (int) (Math.random() * 3);
        for(int i = 0; i<3;i++) {
            double r = Math.random() * (max - min + 1) + min;
            rand.set(i, r);
        }
        return rand;
    }

    /**
     * Method finding the closest distance form probe to titan at any point of the trip
     * @param pathProbe
     * @param pathTitan
     * @return the closest distance
     */
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


    public static void main(String[] args) {

        Vector velocity = findVel(current);
        System.out.println(velocity.get(0));
        System.out.println(velocity.get(1));
        System.out.println(velocity.get(2));

    }
}

package physics.optimalization;

import celestial_bodies.SolarSystemPhysicsSimulation;
import physics.vectors.StateVector;
import physics.vectors.Vector;
import solar_system_data.PlanetaryData;

import java.util.List;

public class InitialConditions {

    static Vector pos = new Vector(new double[]{-1.48e08,-2.78e07,40070});
    static Vector current = new Vector(new double[]{0.07019906402937609, 1.942998691375609, 0.06026274884923538});

    static Vector velocity = new Vector(new double[]{0,0,0});

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
        SolarSystemPhysicsSimulation system = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames());
        List<List<StateVector>> paths = system.simulateOrbitsWithProbe(iniState,31536000,23200);
        List<StateVector> probePath = paths.get(11);
        List<StateVector> titanPath = paths.get(8);
        double bestDistance = closest(probePath, titanPath);
        //System.out.println(probePath.get(probePath.size()-1).getVector(0).distance(titanPath.get(titanPath.size()-1).getVector(0)));
        System.out.println(bestDistance);
        while(iter>0){
            iter--;
            System.out.println(iter);
            Vector[] neighbours = generateNeighbours(current);
            for (Vector neighbour : neighbours) {
                iniState.getStateVector()[1] = neighbour;
                //System.out.println(bestDistance);
                system = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames());
                paths = system.simulateOrbitsWithProbe(iniState,31536000,23200);
                probePath = paths.get(11);
                titanPath = paths.get(8);
                double distance = closest(probePath,titanPath);
                //System.out.println(distance);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    current = neighbour;
                    System.out.println(distance);
                    System.out.print(current.get(0)+ " ");
                    System.out.print(current.get(1)+ " ");
                    System.out.println(current.get(2));
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
            Vector n = current.add(randomVector(0.8));
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
            //System.out.println(r);
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
                //System.out.println(dist);
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

package physics.simulation;

import gui.screens.StartScreen;
import physics.optimalization.Corrections;
import physics.solvers.RK4Solver;
import physics.solvers.Solver;
import physics.vectors.StateVector;
import physics.vectors.Vector;
import solar_system_data.InitialConditions;
import solar_system_data.PlanetaryData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrbitSimulation {

    private final double GRAVITATIONAL_CONSTANT = 6.6743*Math.pow(10,-20);
    private StateVector titanAfterYear = new StateVector(new Vector[]{new Vector(new double[]{1.2546766357749035E9,-7.611165385234222E8,-3.6355814709236585E7}), new Vector(new double[]{8.675963426322431,11.489686682061745,-2.412597372409073})});

    public List<List<StateVector>> orbitSimulation() {

        List<List<StateVector>> orbits;
        List<List<StateVector>> finalOrbits = new ArrayList<>();
        double h = 1800;
        int tf = 31536000;
        double sec = 0;
        Solver solver = new RK4Solver();

        Corrections correctVel = new Corrections();
        Object arr[] = correctVel.adjust(InitialConditions.getProbeInitialState(), titanAfterYear, sec, h*2);
        StateVector newProbeState = (StateVector)arr[0];
        InitialConditions.setProbeInitialVelocity(newProbeState.getVector(1));
        InitialConditions.setProbeInitialPosition(newProbeState.getVector(0));
        System.out.println(newProbeState.getVector(1).get(0));
        System.out.println(newProbeState.getVector(1).get(1));
        System.out.println(newProbeState.getVector(1).get(2));


        SolarSystemPhysicsSimulation simulation = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(), PlanetaryData.getCelestialBodiesMasses(), PlanetaryData.getCelestialBodyNames(), solver);
        orbits = simulation.simulateOrbitsWithProbe(InitialConditions.getProbeInitialState(), tf, h);

        //System.out.println(orbits.get(8).get(orbits.size()-1).getVector(0).get(0));
        //System.out.println(orbits.get(8).get(orbits.size()-1).getVector(0).get(1));
        //System.out.println(orbits.get(8).get(orbits.size()-1).getVector(0).get(2));

        double leftOverTime = 0;


        for (int i = 0; i < orbits.get(0).size(); i++) {
            //distance from the probe to titan
            double dist = orbits.get(11).get(i).getVector(0).distance(orbits.get(8).get(i).getVector(0));
            System.out.println(dist);
            //checking if got to titan if so than setting up the simulation to get back, we never do??;(((((
            if (dist < 3300) {
                leftOverTime = (orbits.get(0).size() - i) * h;

                int length = orbits.get(0).size() - 1;
                for (int j = 0; j < orbits.size(); j++) {
                    for (int k = length; k > i; k--) {
                        orbits.get(j).remove(length);
                        length--;
                    }
                    length = orbits.get(orbits.size() - 1).size() - 1;
                }
                sec = i * h;
                System.out.println(sec);

                //slowing down to orbital velocity
                double orbitalSpeed = getOrbitalSpeed(PlanetaryData.getCelestialBodiesMasses()[8], dist);
                Vector relativeVelocity = orbits.get(8).get(orbits.get(8).size() - 1).getVector(1).subtract(orbits.get(11).get(orbits.get(11).size() - 1).getVector(1));
                Vector zSpeed = new Vector(new double[]{0, 0, orbitalSpeed});
                Vector finalVelocity = relativeVelocity.add(zSpeed);

                //setting the new velocity
                newProbeState = new StateVector(new Vector[]{orbits.get(11).get(orbits.get(11).size() - 1).getVector(0), finalVelocity});

                System.out.print(newProbeState.getVector(1).get(0) + " ");
                System.out.print(newProbeState.getVector(1).get(1) + " ");
                System.out.println(newProbeState.getVector(1).get(2) + " ");

                //making new state vectors for new simulation
                StateVector[] newStateVectors = new StateVector[simulation.getStateVectors().length];
                for (int j = 0; j < newStateVectors.length; j++) {
                    newStateVectors[j] = orbits.get(j).get(i);
                }

                //new simulation with probe orbiting titan
                SolarSystemPhysicsSimulation orbitalSimulation = new SolarSystemPhysicsSimulation(newStateVectors, PlanetaryData.getCelestialBodiesMasses(), PlanetaryData.getCelestialBodyNames(), solver);
                List<List<StateVector>> orbitingTitan = new ArrayList<>();
                orbitingTitan = orbitalSimulation.simulateOrbitsWithProbe(newProbeState, tf + leftOverTime, h);

                //creating the final orbits list
                for (int j = 0; j < orbits.size(); j++) {
                    finalOrbits.add(new ArrayList<>());
                    for (int k = 0; k < i + (tf / h); k++) {
                        Vector v = new Vector(new double[]{0, 0, 0});
                        StateVector sv = new StateVector(new Vector[]{v, v});
                        if (k <= i) {
                            sv = orbits.get(j).get(k);
                        } else {
                            sv = orbitingTitan.get(j).get(k-i);
                        }
                        finalOrbits.get(j).add(sv);

                    }
                }
                break;
            }
        }
        return finalOrbits;
    }

    public double getOrbitalSpeed(double mass, double radius){
        return Math.sqrt((GRAVITATIONAL_CONSTANT*mass)/radius);
    }

    public static void main(String[] args) {
        OrbitSimulation sim = new OrbitSimulation();
        List<List<StateVector>> orb = sim.orbitSimulation();
        System.out.println(orb.get(11).size());
        for(int i = 16157; i<16159; i++){
            System.out.print("Probe position: ");
            System.out.print(orb.get(11).get(i).getVector(0).get(0) + " ");
            System.out.print(orb.get(11).get(i).getVector(0).get(1) + " ");
            System.out.println(orb.get(11).get(i).getVector(0).get(2) + " ");
            System.out.print("Titan position: ");
            System.out.print(orb.get(8).get(i).getVector(0).get(0) + " ");
            System.out.print(orb.get(8).get(i).getVector(0).get(1) + " ");
            System.out.println(orb.get(8).get(i).getVector(0).get(2) + " ");
            System.out.println("distance: " + orb.get(8).get(i).getVector(0).distance(orb.get(11).get(i).getVector(0)));
        }

    }

}

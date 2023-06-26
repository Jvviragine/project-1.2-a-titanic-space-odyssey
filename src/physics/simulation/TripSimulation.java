package physics.simulation;

import gui.screens.StartScreen;
import physics.optimalization.Corrections;
import physics.vectors.StateVector;
import physics.vectors.Vector;
import solar_system_data.InitialConditions;
import solar_system_data.PlanetaryData;
import physics.optimalization.FuelUsage;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TripSimulation {


    public TripSimulation() {

    }

    double usedFuel = 0;

    /**
     * calculates the trip from Earth to Titan
     * @return a list containing all stateVectors at all times for the GUI to visualize
     */
    public List<List<StateVector>> simulateTrip() {

        List<List<StateVector>> orbits;
        List<List<StateVector>> finalOrbits = new ArrayList<>();
        int tf = StartScreen.simulationEndTime / 2; //31536000
        double h = StartScreen.h; //1800
        int tI = 500; //time-interval: 110 is optimized, 3403 is as well under the consideration of computation time, 500 is a good median value
        int count = 0;
        double dist = 0;
        double leftOverTime = 0;
        double sec = 0;
        Object[] arr = new Object[2];
        double[] orbitCoord = new double[3];
        orbitCoord[0] = 1365410325.77402;
        orbitCoord[1] = -487853739.0631;
        orbitCoord[2] = -4.501235837440703E7;
        Vector orbit = new Vector(orbitCoord);  //determined coordinates through finding perpendicular points to the vector from the spaceship to Titan
                                                //the z-coordinate was assumed to stay the same so that there would only be two resulting points, one of which I chose
        double[] orbitVel = new double[3];
        orbitVel[0] = 1.743907161704536 + 5.30970899548503;
        orbitVel[1] = -0.28002820540898765 + 6.675603842323675;
        orbitVel[2] = -0.04563866373189404 + 0.6929143808519659; //determined by calculating the exact speed to stay in orbit though a formula from the FAQ
                                                                 //used the orbitEntry method to incrementally adjust the velocity vectors until it reached the required speed
                                                                 //then added on the velocity vectors of Titan at the moment of arrival, since the calculated speed was in relation to that moon

        //simulation used to calculate the fuel for exiting earth and also to get earths position after a year
        SolarSystemPhysicsSimulation simulationForSec = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(), PlanetaryData.getCelestialBodiesMasses(), PlanetaryData.getCelestialBodyNames(), StartScreen.finalSolver);
        simulationForSec.simulateOrbitsWithProbe(InitialConditions.getProbeInitialState(), tf, h);

        //calculating the time needed to exit earth to know how much fuel we use
        for (int i = 0; i < simulationForSec.getPath().get(0).size(); i++) {
            dist = simulationForSec.getPath().get(4).get(i).getVector(0).distance(simulationForSec.getPath().get(11).get(i).getVector(0));
            if (dist > 6563000) {
                sec = i * h;
                break;
            }
        }

        //calculating used fuel and setting new velocity
        Vector velF = new Vector(new double[]{46.18781669742928, -44.638735761874045, -2.9953741584880706});
        double sub = FuelUsage.fuelTakeoffLanding(InitialConditions.getInitialProbeVelocity().getMagnitude(), velF.getMagnitude(), sec);
        usedFuel = usedFuel + sub;

        InitialConditions.setProbeInitialVelocity(velF);

        //start of the simulation to get to Titan
        SolarSystemPhysicsSimulation simulation = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(), PlanetaryData.getCelestialBodiesMasses(), PlanetaryData.getCelestialBodyNames(), StartScreen.finalSolver);
        orbits = simulation.simulateOrbitsWithProbe(InitialConditions.getProbeInitialState(), tf, h);

        //making new state vectors for new simulation
        StateVector[] newStateVectors = new StateVector[simulation.getStateVectors().length];


        //getting new velocity for the probe
        Corrections correct = new Corrections();


        for (int i = 0; i < orbits.get(orbits.size()-1).size(); i++) {

            leftOverTime = (orbits.get(orbits.size()-1).size() - i) * h;

            //distance from the probe to titan
            if (i != 0) //finalOrbits would not yet have values for i=0 -> error
                dist = finalOrbits.get(11).get(finalOrbits.get(11).size()-1).getVector(0).distance(finalOrbits.get(8).get(finalOrbits.get(8).size()-1).getVector(0));

            //stops simulation once we are in orbit
            if (dist < 2875) //300km away from Titan's surface
                break;


            //Adjust probe's velocity every time-interval
            if (tI * count < orbits.get(orbits.size()-1).size() && i % tI == 0 || i > orbits.get(orbits.size()-1).size()-5) { // checks that we do not use time-intervals past 1 year of flight
                count++;

                for (int j = 0; j < newStateVectors.length; j++) {
                    newStateVectors[j] = orbits.get(j).get(i);
                }

                //Adjust new coordinates, dependant on how far we are in the journey
                if (i == 0)
                    arr = correct.adjust(orbits.get(11).get(i), orbits.get(8).get(i).getVector(0), i * h, tf*0.9);
                else if (i < orbits.get(11).size() / 2) //adjusts towards Titan's current position for the first half-year; avoids larger effects of Sun's gravitational pull
                    arr = correct.adjust(finalOrbits.get(11).get(i - 1), orbits.get(8).get(i).getVector(0), i * h, tf*0.99);
                else if(i < orbits.get(11).size()-1) //adjusts towards Titan's position after a year during the second half of the flight; reduces fuel consumption from "chasing" the planet
                    arr = correct.adjust(finalOrbits.get(11).get(i - 1), orbit, i * h, tf);
                else
                    arr = correct.orbitEntry(finalOrbits.get(11).get(i - 1), orbit, 1.767166079); //goalVelocity was calculated separately given the formula from the FAQ


                StateVector newProbeState = (StateVector) arr[0];

                //calculating fuel to adjust the new velocity
                sub = (double) arr[1];
                usedFuel = usedFuel + sub;

                //setting up new simulation and running it with new State for probe
                SolarSystemPhysicsSimulation adjustedSimulation = new SolarSystemPhysicsSimulation(newStateVectors, PlanetaryData.getCelestialBodiesMasses(), PlanetaryData.getCelestialBodyNames(), StartScreen.finalSolver);
                List<List<StateVector>> adjustedOrbits = new ArrayList<>();
                adjustedOrbits = adjustedSimulation.simulateOrbitsWithProbe(newProbeState, leftOverTime, h);

                //creating the final list of all stateVectors; the true path of the flight
                for (int j = 0; j < orbits.size(); j++) {
                    if (finalOrbits.size() <= 11)
                        finalOrbits.add(new ArrayList<>());

                    //adds states from the final time-interval until the full year has passed
                    if (tI * count > orbits.get(orbits.size()-1).size()) {
                        for (int k = 0; k + i < orbits.get(orbits.size()-1).size(); k++) {
                            finalOrbits.get(j).add(adjustedOrbits.get(j).get(k));
                        }
                    }
                    //adds states up to the next time-interval
                    else{
                        for (int k = 0; k + i < tI * count; k++) {
                            finalOrbits.get(j).add(adjustedOrbits.get(j).get(k));
                        }
                    }
                }
            }
        }

        System.out.println("Probe coordinates: " + finalOrbits.get(11).get(finalOrbits.get(11).size()-1));
        System.out.println("Titan coordinates: " + finalOrbits.get(8).get(finalOrbits.get(11).size()-1));
        System.out.println("Proximity to Titan: " + dist + "km");
        System.out.println("Total fuel consumed is: " + usedFuel + "kg");

        //visualizes the continued journey from afar. Through this one can see that the probe does stay near Titan
//        Vector velP = new Vector(new double[]{orbitCoord[0], orbitCoord[1], orbitCoord[2]});
//        Vector velV = new Vector(new double[]{orbitVel[0], orbitVel[1], orbitVel[2]});
//
//        InitialConditions.setProbeInitialPosition(velP);
//        InitialConditions.setProbeInitialVelocity(velV);
//
//        SolarSystemPhysicsSimulation adjustedSimulation = new SolarSystemPhysicsSimulation(newStateVectors, PlanetaryData.getCelestialBodiesMasses(), PlanetaryData.getCelestialBodyNames(), StartScreen.finalSolver);
//        List<List<StateVector>> adjustedOrbits = new ArrayList<>();
//        adjustedOrbits = adjustedSimulation.simulateOrbitsWithProbe(InitialConditions.getProbeInitialState(), tf, h);
//        for (int j = 0; j < orbits.size(); j++) {
//            for (int k = 0; k + orbits.get(11).size() < orbits.get(11).size()*2; k++) {
//                finalOrbits.get(j).add(adjustedOrbits.get(j).get(k));
//            }
//        }
        return finalOrbits;
    }
}

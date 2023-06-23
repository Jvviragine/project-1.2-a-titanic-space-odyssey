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

    public List<List<StateVector>> simulateTrip() {

        List<List<StateVector>> orbits;
        List<List<StateVector>> finalOrbits = new ArrayList<>();
        int tf = StartScreen.simulationEndTime / 2; //31536000
        double h = StartScreen.h; //1800
        int tI = 500; //time-interval
        int count = 0;
        double dist = 0;
        double leftOverTime = 0;
        double sec = 0;
        Object[] arr = new Object[2];

        //simulation used to calculate the fuel for exiting earth and also to get earths position after two years
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


        for (int i = 0; i < orbits.get(11).size(); i++) {

            leftOverTime = (orbits.get(11).size() - i) * h;

            //distance from the probe to titan
            if (i != 0) //finalOrbits would not yet have values for i=0
                dist = finalOrbits.get(11).get(finalOrbits.get(11).size()-1).getVector(0).distance(finalOrbits.get(8).get(finalOrbits.get(8).size()-1).getVector(0));

            //stops simulation once we are in orbit
            if (dist < 2874) //300km away from Titan's surface
                break;

            //Adjust probe's velocity every time-interval
            if (tI * count < orbits.get(11).size() && i % tI == 0) { // checks that we do not use time-intervals past 1 year of flight
                count++;

                for (int j = 0; j < newStateVectors.length; j++) {
                    newStateVectors[j] = orbits.get(j).get(i);
                }

                //Adjust new coordinates
                if (i == 0)
                    arr = correct.adjust(orbits.get(11).get(i), orbits.get(8).get(i), i * h, tf);
                else if (i < orbits.get(11).size() / 2) //adjusts towards Titan's current position for the first half-year; avoids larger effects of Sun's gravitational pull
                    arr = correct.adjust(finalOrbits.get(11).get(i - 1), orbits.get(8).get(i), i * h, tf);
                else //adjusts towards Titan's position after a year during the second half of the flight; reduces fuel consumption from "chasing" the planet
                    arr = correct.adjust(finalOrbits.get(11).get(i - 1), orbits.get(8).get(orbits.get(8).size()-1), i * h, tf);

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
                    if (tI * count > orbits.get(11).size()) {
                        for (int k = 0; k + i < orbits.get(11).size(); k++) {
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
        System.out.println("Proximity to Titan: " + dist + "km");
        System.out.println("Total fuel consumed is: " + usedFuel + "kg");
        return finalOrbits;
    }
}

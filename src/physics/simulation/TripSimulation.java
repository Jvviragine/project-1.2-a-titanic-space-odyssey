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
import java.util.List;


public class TripSimulation {

//    SolarSystemPhysicsSimulation system;
//
//    public TripSimulation(SolarSystemPhysicsSimulation system){
//        this.system = system;
//    }
    double usedFuel = 0;
    public void simulate(){
        //Create a new simulation
        SolarSystemPhysicsSimulation simulation = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames());
        simulation.simulateOrbitsWithProbe(InitialConditions.getProbeInitialState(), StartScreen.simulationEndTime,StartScreen.h);

        //Go through path and find the point at which the probe and titan are < 300km away from one another
        //  Maybe make a greater distance and when it gets there start adjusting velocity ?
        for(int i = 0; i < simulation.getPath().get(0).size(); i++){

            //Get distance between probe and titan
            double distance = (simulation.getPath().get(PlanetaryData.indexOf("Titan")).get(i).getVector(0)).distance(simulation.getPath().get(simulation.getPath().size()-1).get(i).getVector(0));

            //Check if close enough to enter orbit
            if(distance <= 300) {

                //Use titan and probe states when they are <=300km apart to adjust the velocity to get into orbit
                StateVector titanState = simulation.getPath().get(PlanetaryData.indexOf("Titan")).get(i);
                StateVector probeState = simulation.getPath().get(simulation.getPath().size()-1).get(i);

                //Initialise velocity correction
                Corrections correct = new Corrections();

                //Adjust new coordinates
                StateVector newProbeState = correct.adjust(probeState,titanState,i*StartScreen.h, 31536000);

                //Recalculate second part of path using current probe velocities
                simulation.adjustPath(newProbeState,i*StartScreen.h, StartScreen.simulationEndTime,StartScreen.h);

                //Have to have some condition to keep evaluating and changing direction (adjusting path)
                
                break;
            }
        }
    }

     public List<List<StateVector>> simulateTrip(){

         List<List<StateVector>> orbits;
         List<List<StateVector>> finalOrbits = new ArrayList<>();
         int tf = StartScreen.simulationEndTime;
         double h = StartScreen.h;

         SolarSystemPhysicsSimulation simulationForSec = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames());
         simulationForSec.simulateOrbitsWithProbe(InitialConditions.getProbeInitialState(), 31536000,360);

         //calculating the time needed to exit earth to know how much fuel we use
         double sec = 0;
         for(int i = 0; i<simulationForSec.getPath().get(0).size();i++){
             double dist = simulationForSec.getPath().get(4).get(i).getVector(0).distance(simulationForSec.getPath().get(11).get(i).getVector(0));
             if(dist>6563000){
                 sec = i*360;
                 break;
             }
         }

         //calculating used fuel and setting new velocity
         Vector velF  = new Vector(new double[]{46.18781669742928, -44.638735761874045, -2.9953741584880706});
         double sub = FuelUsage.fuelTakeoffLanding(InitialConditions.getInitialProbeVelocity().getMagnitude(), velF.getMagnitude(), sec);
         usedFuel = usedFuel + sub;

         InitialConditions.setProbeInitialVelocity(velF);

         //start of the simulation
         SolarSystemPhysicsSimulation simulation = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames());
         orbits = simulation.simulateOrbitsWithProbe(InitialConditions.getProbeInitialState(), 31536000,360);
         System.out.println(orbits.get(0).size());




         for(int i = 0;i<orbits.get(0).size();i++){
            //distance from the probe to titan
            double dist = orbits.get(11).get(i).getVector(0).distance(orbits.get(8).get(i).getVector(0));
            //checinkg if got to titan if so than setting up the simulation to get back, we never do??;(((((
            if(dist<3000000){
                sec = i*360;
                System.out.println(sec);
                //making new state vectors for new simulation
                StateVector[] newStateVectors = new StateVector[simulation.getStateVectors().length];
                for(int j = 0; j<newStateVectors.length;j++){
                    newStateVectors[j] = orbits.get(j).get(i);
                }

                //stopping the probe, calculating fuel
                velF  = new Vector(new double[]{0,0,0});
                sub = FuelUsage.fuelTakeoffLanding(simulation.getPath().get(11).get(i).getVector(1).getMagnitude(), velF.getMagnitude(), 0);
                usedFuel = usedFuel + sub;

                //getting new velocity for the probe
                Corrections correct = new Corrections();

                //Adjust new coordinates
                StateVector newProbeState = correct.adjust(simulation.getPath().get(11).get(i),simulation.getPath().get(8).get(i),sec,31536000);

                //setting up new simulation and running it with new State for probe (on titan)
                double newSimulationTime = 31536000-sec;
                SolarSystemPhysicsSimulation adjustedSimulation = new SolarSystemPhysicsSimulation(newStateVectors,PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames());
                List<List<StateVector>> adjustedOrbits = new ArrayList<>();
                adjustedOrbits = adjustedSimulation.simulateOrbitsWithProbe(newProbeState,newSimulationTime,360);
                System.out.println(adjustedOrbits.size());

                //creating the final orbits list
                for(int j=0; j<orbits.size();j++){
                    finalOrbits.add(new ArrayList<>());
                    int diff = orbits.size()-adjustedOrbits.size();
                    for(int k=0; k<orbits.get(j).size();k++){
                        StateVector sv = orbits.get(j).get(k);
                        if(k>=diff && k-diff<adjustedOrbits.size()){
                            sv = adjustedOrbits.get(j).get(k-diff);
                        }
                        finalOrbits.get(j).add(sv);
                    }
                }
                break;
            }
         }
         return finalOrbits;
     }

    public static void main(String[] args) {
        TripSimulation sim = new TripSimulation();
        List<List<StateVector>> orb = sim.simulateTrip();
        System.out.println(orb.get(11).get(orb.get(4).size()-1).getVector(0).get(0));
    }
}

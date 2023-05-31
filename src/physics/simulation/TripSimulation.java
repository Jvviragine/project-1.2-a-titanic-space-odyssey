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


    public TripSimulation(){

    }
    double usedFuel = 0;

     public List<List<StateVector>> simulateTrip(){

         List<List<StateVector>> orbits;
         List<List<StateVector>> finalOrbits = new ArrayList<>();
         int tf = StartScreen.simulationEndTime;
         double h = StartScreen.h;
//         double h = 1800;
//         int tf = 31536000;

         //simulation used to calculate the fuel for exiting earth and also to get earths position after two years
         SolarSystemPhysicsSimulation simulationForSec = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames(),StartScreen.finalSolver);
         simulationForSec.simulateOrbitsWithProbe(InitialConditions.getProbeInitialState(), tf*2,h);

         //calculating the time needed to exit earth to know how much fuel we use
         double sec = 0;
         for(int i = 0; i<simulationForSec.getPath().get(0).size();i++){
             double dist = simulationForSec.getPath().get(4).get(i).getVector(0).distance(simulationForSec.getPath().get(11).get(i).getVector(0));
             if(dist>6563000){
                 sec = i*h;
                 break;
             }
         }

         //calculating used fuel and setting new velocity
         Vector velF  = new Vector(new double[]{46.18781669742928, -44.638735761874045, -2.9953741584880706});
         double sub = FuelUsage.fuelTakeoffLanding(InitialConditions.getInitialProbeVelocity().getMagnitude(), velF.getMagnitude(), sec);
         usedFuel = usedFuel + sub;

         InitialConditions.setProbeInitialVelocity(velF);

         //start of the simulation to get to titan
         SolarSystemPhysicsSimulation simulation = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames(),StartScreen.finalSolver);
         orbits = simulation.simulateOrbitsWithProbe(InitialConditions.getProbeInitialState(), tf,h);

         double leftOverTime = 0;

         for(int i = 0;i<orbits.get(0).size();i++){
            //distance from the probe to titan
            double dist = orbits.get(11).get(i).getVector(0).distance(orbits.get(8).get(i).getVector(0));
            //checking if got to titan if so than setting up the simulation to get back, we never do??;(((((
            if(dist<3000000){
                leftOverTime = (orbits.get(0).size() - i)*h;

                int length = orbits.get(0).size()-1;
                for(int j = 0; j < orbits.size(); j++){
                    for(int k = length; k > i; k--){
                        orbits.get(j).remove(length);
                        length--;
                    }
                    length = orbits.get(orbits.size()-1).size()-1;
                }

                System.out.println(sec);
                sec = i*h;


                //making new state vectors for new simulation
                StateVector[] newStateVectors = new StateVector[simulation.getStateVectors().length];
                for(int j = 0; j<newStateVectors.length;j++){
                    newStateVectors[j] = orbits.get(j).get(i);
                }


                //getting new velocity for the probe
                Corrections correct = new Corrections();

                //Adjust new coordinates
                Object arr[] = correct.adjust(simulation.getPath().get(11).get(i),simulationForSec.getPath().get(4).get(simulationForSec.getPath().get(4).size()-1),sec,tf*0.85);
                StateVector newProbeState = (StateVector)arr[0];

                //calculating fuel to adjust the new velocity
                sub = (double)arr[1];
                usedFuel = usedFuel + sub;

                System.out.print(newProbeState.getVector(1).get(0)+ " ");
                System.out.print(newProbeState.getVector(1).get(1)+ " ");
                System.out.println(newProbeState.getVector(1).get(2)+ " ");

                //setting up new simulation and running it with new State for probe (on titan)
                SolarSystemPhysicsSimulation adjustedSimulation = new SolarSystemPhysicsSimulation(newStateVectors,PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames(),StartScreen.finalSolver);
                List<List<StateVector>> adjustedOrbits = new ArrayList<>();
                adjustedOrbits = adjustedSimulation.simulateOrbitsWithProbe(newProbeState,tf+leftOverTime,h);

                
                //creating the final orbits list
                for(int j=0; j<orbits.size();j++){
                    finalOrbits.add(new ArrayList<>());
                    for(int k=0; k<i+(tf/h);k++){
                        Vector v = new Vector(new double[]{0,0,0});
                        StateVector sv = new StateVector(new Vector[]{v,v});
                        if(k<i) {
                            sv = orbits.get(j).get(k);
                        }
                        else{
                            if(j==11){
                                sv = (StateVector)correct.adjust(adjustedOrbits.get(j).get(k-i), adjustedOrbits.get(4).get(k-i), k*h, tf)[0];
                            }else {
                                sv = adjustedOrbits.get(j).get(k - i);
                            }
                            //sv = adjustedOrbits.get(j).get(k - orbits.get(j).size());
                        }
                        finalOrbits.get(j).add(sv);

                    }
                }
                break;
            }
         }
         System.out.println("Total fuel consumed is: " + usedFuel + "kg");
         return finalOrbits;
     }

    public static void main(String[] args) {
        TripSimulation sim = new TripSimulation();
        List<List<StateVector>> orb = sim.simulateTrip();
        System.out.println(orb.get(11).get(orb.get(4).size()-1).getVector(0).get(0));
        System.out.println(orb.get(11).get(orb.get(4).size()-1).getVector(0).get(1));
        System.out.println(orb.get(11).get(orb.get(4).size()-1).getVector(0).get(2));
        System.out.println(orb.get(4).get(orb.get(4).size()-1).getVector(0).get(0));
        System.out.println(orb.get(4).get(orb.get(4).size()-1).getVector(0).get(1));
        System.out.println(orb.get(4).get(orb.get(4).size()-1).getVector(0).get(2));
        System.out.println(orb.get(4).get(orb.get(4).size()-1).getVector(0).distance(orb.get(11).get(orb.get(4).size()-1).getVector(0)));
    }
}

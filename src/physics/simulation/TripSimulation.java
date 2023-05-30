package physics.simulation;

import gui.screens.StartScreen;
import physics.optimalization.Corrections;
import physics.vectors.StateVector;
import solar_system_data.InitialConditions;
import solar_system_data.PlanetaryData;

public class TripSimulation {
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
                StateVector newProbeState = correct.Adjust(probeState,titanState,i*StartScreen.h);

                //Recalculate second part of path using current probe velocities
                simulation.adjustPath(newProbeState,i*StartScreen.h, StartScreen.simulationEndTime,StartScreen.h);

                //Have to have some condition to keep evaluating and changing direction (adjusting path)
                
                break;
            }
        }
    }

     public void simulateTrip(SolarSystemPhysicsSimulation system){
        //first thrust, change of velocoty from 0 to initial

        //start of the sinulation until 365 days with the starting state vectors the positions after probe exits earth

        //when entered titans orbit change velocity to 0(secind thrust)

        //find the direction vector to earth

        //last thrust, change velocoty from 0 to direction of the earth

        //sinulate for kne year

     }
}

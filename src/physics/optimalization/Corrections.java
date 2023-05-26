package physics.optimalization;

import physics.vectors.StateVector;
import physics.vectors.Vector;

public class Corrections {
    //Using the coordinates of the probe and titan and the trajectory of the probe,
    // we calculate the vector that would connect the two and adjust the probe's velocity vector accordingly
    public StateVector Adjust(StateVector probePos, StateVector titanPos, StateVector probeTraj, int timeElapsed){
        double vectorChanges = 0;
        StateVector diff = probePos.subtract(titanPos);
        for(int i = 0; i < diff.getNumberOfVectors(); i++){
            diff.getVector(i).multiply((double) 1 / timeElapsed);
            vectorChanges += FuelUsage.Takeoff(probeTraj.getVector(i).getMagnitude(), diff.getVector(i).getMagnitude(), 6.6743*Math.pow(10, -20));
        }
        return diff;
    }
}

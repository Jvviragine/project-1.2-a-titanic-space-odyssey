package physics.optimalization;

import physics.vectors.StateVector;
import physics.vectors.Vector;

public class Corrections {
    /**
     * Using the coordinates of the probe and titan and the trajectory of the probe,
     * we calculate the vector that would connect the two and adjust the probe's velocity vector accordingly* @param probeVec StateVector of the probe
     * @param probeVec StateVector of the probe
     * @param titanVec StateVector of Titan
     * @param timePassed How much time has passed in the journey in seconds
     * @return StateVector of the probe with updated velocity values that aim it towards Titan
     */

    public StateVector Adjust(StateVector probeVec, StateVector titanVec, double timePassed){
        //double vectorChanges = 0;
        int year = 31536000;
        double timeLeft;
        if(timePassed < year){
            timeLeft = year - timePassed;
        }
        else{
            timeLeft = 2*year - timePassed;
        }

        for(int i = 0; i < 3; i++){
            probeVec.getStateVector()[i+3] = probeVec.getStateVector()[i].subtract(titanVec.getStateVector()[i]).multiply(1/timeLeft);
            //vectorChanges += FuelUsage.Takeoff(probeTraj.getVector(i).getMagnitude(), diff.getVector(i).getMagnitude(), 6.6743*Math.pow(10, -20));
        }
        return probeVec;
    }
}

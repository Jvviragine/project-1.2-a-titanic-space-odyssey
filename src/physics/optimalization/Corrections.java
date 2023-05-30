package physics.optimalization;

import physics.vectors.StateVector;
import physics.vectors.Vector;

public class Corrections {
    /**
     * Using the coordinates of the probe, our destination and the trajectory of the probe,
     * we calculate the vector that would connect the two and adjust the probe's velocity vector accordingly
     * @param probeVec StateVector of the probe
     * @param destinationVec StateVector of Titan/Earth
     * @param timePassed How much time has passed in the journey in seconds
     * @param eta Estimated time of arrival
     * @return StateVector of the probe with updated velocity values that aim it towards Titan
     */

    public StateVector adjust(StateVector probeVec, StateVector destinationVec, double timePassed, double eta){
        //double vectorChanges = 0;
//        int year = 31536000;
//        double timeLeft;
//        if(timePassed < year){
//            timeLeft = year - timePassed;
//        }
//        else{
//            timeLeft = 2*year - timePassed;
//        }
        double timeLeft = eta - timePassed;

        for(int i = 0; i < 3; i++){
            probeVec.getVector(1).set(i,(destinationVec.getVector(0).get(i) - probeVec.getVector(0).get(i)) * (1/timeLeft));
            //vectorChanges += FuelUsage.Takeoff(probeTraj.getVector(i).getMagnitude(), diff.getVector(i).getMagnitude(), 6.6743*Math.pow(10, -20));
        }
        return probeVec;
    }

    /**
     * Using the coordinates of the probe, the orbit entry point and the trajectory of the probe,
     * we calculate the vector that would connect the two and adjust the probe's velocity vector accordingly.
     * Additionally, we reduce the speed bit by bit until we reach the desired velocity to successfully orbit
     * @param probeVec StateVector of the probe
     * @param destinationVec StateVector of Titan/Earth
     * @param timePassed How much time has passed in the journey in seconds
     * @param eta Estimated time of arrival
     * @param goalVelocity speed required to orbit around the given planet
     * @return StateVector of the probe with updated velocity values that aim it towards Titan
     */
    public StateVector orbitEntry(StateVector probeVec, StateVector destinationVec, double timePassed, double eta, double goalVelocity){
        double timeLeft = eta - timePassed;
        double[] allV = new double[3];
        double totalV = 0;
        int count = 0;
            do{
                for(int i = 0; i < 3; i++){
                    probeVec.getVector(1).set(i,(destinationVec.getVector(0).get(i) - probeVec.getVector(0).get(i)) * (1/timeLeft) - count);
                    allV[i] = probeVec.getVector(1).get(i);
                }
                count++;
                totalV = Math.sqrt(Math.pow(allV[0], 2) + Math.pow(allV[1], 2) + Math.pow(allV[2], 2));
            }
            while (totalV > goalVelocity);
        return probeVec;
    }
}

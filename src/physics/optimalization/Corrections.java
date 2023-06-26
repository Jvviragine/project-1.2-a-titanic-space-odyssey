package physics.optimalization;

import physics.vectors.StateVector;
import physics.vectors.Vector;

import java.util.Arrays;

public class Corrections {
    /**
     * Using the coordinates of the probe, our destination and the trajectory of the probe,
     * we calculate the vector that would connect the two and adjust the probe's velocity vector accordingly
     * @param probeVec StateVector of the probe
     * @param destinationVec positional vector of Titan/Earth
     * @param timePassed How much time has passed in the journey in seconds
     * @param eta Estimated time of arrival
     * @return StateVector of the probe with updated velocity values that aim it towards Titan
     */

    public Object[] adjust(StateVector probeVec, Vector destinationVec, double timePassed, double eta){
        double vectorChanges = 0;
        Object arr[] = new Object[2];
        double temp = 0;
        double timeLeft = eta - timePassed;

        for(int i = 0; i < 3; i++){
            temp = probeVec.getVector(1).get(i);
            probeVec.getVector(1).set(i,(destinationVec.get(i) - probeVec.getVector(0).get(i)) * (1/timeLeft));
            vectorChanges += FuelUsage.fuel(probeVec.getVector(1).get(i), temp, 1);
        }
        arr[0] = probeVec;
        arr[1] = vectorChanges;
        return arr;
    }

    /**
     * Using the coordinates of the probe, the orbit entry point and the trajectory of the probe,
     * we calculate the vector that would connect the two and adjust the probe's velocity vector accordingly.
     * Additionally, we reduce the speed bit by bit until we reach the desired velocity to successfully orbit
     * @param probeVec StateVector of the probe
     * @param destinationVec positional vector of Titan/Earth
     * @param goalVelocity speed required to orbit around the given planet
     * @return array containing StateVector of the probe with updated velocity values that aim it towards Titan, as well as the consumed fuel
     */
    public Object[] orbitEntry(StateVector probeVec, Vector destinationVec, double goalVelocity){
        Object[] arr = new Object[2];
        double[] allV = new double[3];
        double totalV = 0;
        double count = 0;
        double fuel = 0;
        double[] temporary = new double[3];
        for(int i = 0; i < probeVec.getVector(1).getDimension(); i++){
            temporary[i] = probeVec.getVector(1).get(i);
        }
            do{
                for(int i = 0; i < probeVec.getVector(1).getDimension(); i++){
                    probeVec.getVector(1).set(i, temporary[i]);
                    double temp = destinationVec.get(i) - probeVec.getVector(0).get(i);
                    probeVec.getVector(1).set(i,temp/(count*10));
                    allV[i] = probeVec.getVector(1).get(i);
                }
                count++;
                totalV = Math.sqrt(Math.pow(allV[0], 2) + Math.pow(allV[1], 2) + Math.pow(allV[2], 2));
            }
            while (totalV > goalVelocity);
        probeVec.getVector(1).set(0, probeVec.getVector(1).get(0) + 5.309617504160241);
        probeVec.getVector(1).set(1, probeVec.getVector(1).get(1) + 6.6755817140768166);
        probeVec.getVector(1).set(2, probeVec.getVector(1).get(2) + 0.6929673080598575);
            arr[0] = probeVec;
            for(int i = 0; i < temporary.length; i++) {
                fuel += FuelUsage.fuel(temporary[i], probeVec.getVector(1).get(i), 1800);
            }
            arr[1] = fuel;
        return arr;
    }
}

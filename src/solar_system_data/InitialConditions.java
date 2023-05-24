package solar_system_data;
import physics.vectors.Vector;
import physics.vectors.StateVector;


/**
 * Stores the Initial Conditions of the Probe Launch
 * If nothing is specified, it uses the Initial Conditions given in the Manual
 */
public class InitialConditions {

    // Instance Fields - By default the probe will start at the Initial Conditions found on Phase 1
    private static Vector initialProbePosition = new Vector(new double[]{-148458048.395164+6370, -27524868.1841142, 70233.6499287411});
    private static Vector initialProbeVelocity = new Vector(new double[]{42.42270135156, -43.62738201925, -3.1328169170});

    private static final double mass = 50000;
    // Getter Methods
    public static Vector getInitialProbePosition() {
        return initialProbePosition;
    }

    public static Vector getInitialProbeVelocity() {
        return initialProbeVelocity;
    }

    // Setter Methods - The Values passed by the User in the GUI
    public static void setProbeInitialPosition(Vector initialProbePositionVector) {
        initialProbePosition = initialProbePositionVector;
    }
    public static void setProbeInitialVelocity(Vector initialProbeVelocityVector) {
        initialProbeVelocity = initialProbeVelocityVector;
    }

    public static StateVector getProbeInitialState(){
        return new StateVector(new Vector[]{initialProbePosition,initialProbeVelocity});
    }

    public static double getProbeMass(){ return mass;}

}

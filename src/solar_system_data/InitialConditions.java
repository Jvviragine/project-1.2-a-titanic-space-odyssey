package solar_system_data;
import physics.vectors.Vector;
import physics.vectors.StateVector;


/**
 * Stores the Initial Conditions of the Probe Launch
 * If nothing is specified, it uses the Initial Conditions given in the Manual
 */
public class InitialConditions {

    // Instance Fields - By default the probe will start at the Initial Conditions
    private static Vector initialProbePosition = new Vector(new double[]{-1.48e08,-2.78e07,40070});
    //velocity obtained from optimalization
    private static Vector initialProbeVelocity = new Vector((new double[]{44.51165027941231, -44.06386148177975, -3.180851149434365}));
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

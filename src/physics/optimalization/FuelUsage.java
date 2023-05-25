package physics.optimalization;

public class FuelUsage {
    private static final int ISP = 280; //Specific Impulse taken from a rocket with similar mass (Atlas V)
    private static final int MASS = 50000; //Mass of our rocket in kilograms

    /**
     * Calculates the fuel consumption in kilograms for any constant maneuver. No code optimization,
     * since by design only using the rocket thrusters for takeoff, turnaround and landing is optimized.
     * https://pressbooks.online.ucf.edu/osuniversityphysics/chapter/9-7-rocket-propulsion/
     * @param startVelocity velocity in m/s when activating thrusters
     * @param endVelocity velocity in m/s when deactivating thrusters
     * @param gravity gravitational force of the planet in m/s^2
     * @param timeframe time in seconds it takes from start to end of the maneuver
     * @return point on the surface closest to the target
     */
    public double CalculateFuel(double startVelocity, double endVelocity, double gravity, double timeframe){
        double momentum = MASS * Math.abs(endVelocity - startVelocity);
        double thrust = (momentum / timeframe) - (gravity * MASS);
        return (thrust / (gravity * ISP)) * timeframe;
    }
    public static double Alternative(double startVelocity, double endVelocity, double gravity){
        double e = 2.718;
        double wetMass = MASS * Math.pow (e, Math.abs(startVelocity - endVelocity) / (ISP * gravity));
        return wetMass - MASS;
    }
}

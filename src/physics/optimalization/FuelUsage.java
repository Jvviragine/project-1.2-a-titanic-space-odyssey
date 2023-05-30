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
    public static double calculateFuel(double startVelocity, double endVelocity, double gravity, double timeframe){
        double momentum = MASS * Math.abs(endVelocity - startVelocity);
        double thrust = (momentum / timeframe) - (gravity * MASS);
        return (thrust / (gravity * ISP)) * timeframe;
    }
    public static double takeoff(double startVelocity, double endVelocity, double gravity){
        double e = 2.718;
        double wetMass = MASS * Math.pow (e, Math.abs(startVelocity - endVelocity) / (ISP * gravity));
        return wetMass - MASS;
    }

    public static double landing(double startVelocity, double endVelocity, double gravity, double distance){
        double acc = (Math.pow(startVelocity, 2) - Math.pow(endVelocity, 2)) / (2* distance);
        //double thrust = MASS * (acc + gravity);
        double time = (startVelocity - endVelocity) / acc;
        return calculateFuel(startVelocity, endVelocity, gravity, time);
    }

    public double fuel(double startVelocity, double endVelocity, double timeframe){
        return Math.abs(impulse(startVelocity, endVelocity, timeframe)) * 0.001; //1m/s is 0.001km/s, fuel consumption is proportional to impulse
    }
    public double fuelTakeoffLanding(double startVelocity, double endVelocity, double timeframe){
        return Math.abs(impulseTakeoffLanding(startVelocity, endVelocity, timeframe)) * 0.001;
    }

    public double impulse(double startVelocity, double endVelocity, double timeframe){
        return force(startVelocity, endVelocity, timeframe) * timeframe; //For a constant force over time, one can choose to solve the integral graphically (form of a rectangle)
    }
    public double impulseTakeoffLanding(double startVelocity, double endVelocity, double timeframe){
        return forceTakeoffLanding(startVelocity, endVelocity, timeframe) * timeframe;
    }

    public double force(double startVelocity, double endVelocity, double timeframe){
        return MASS * acceleration(startVelocity, endVelocity, timeframe); //F=m*a
    }
    public double forceTakeoffLanding(double startVelocity, double endVelocity, double timeframe){
        return MASS * acceleration(startVelocity, endVelocity, timeframe) + 9.81; //F=m*a+g
    }

    public double acceleration(double startVelocity, double endVelocity, double timeframe){
        return Math.abs(startVelocity - endVelocity) / timeframe; //Change in speed over a timeframe; assumes a constant acceleration
    }

    public double trap(double a, double b, int n, double h, double startVelocity, double endVelocity, double timeframe){
        double result;
        double x;
        int i;

        result = (f(a, startVelocity, endVelocity, timeframe) + f(b, startVelocity, endVelocity, timeframe))/2.0;
        for (i = 1; i <= n-1; i++) {
            x = a + i*h;
            result = result + f(x, startVelocity, endVelocity, timeframe);
        }
        result = result*h;

        return result;
    }
    public double f(double x, double startVelocity, double endVelocity, double timeframe) {
        double result;

        result = force(startVelocity, endVelocity, timeframe);
        return result;
    }
}

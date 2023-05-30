package physics.optimalization;

public class FuelUsage {
    //private static final int ISP = 280; //Specific Impulse taken from a rocket with similar mass (Atlas V)
    private final int MASS = 50000; //Mass of our rocket in kilograms

//    public static double calculateFuel(double startVelocity, double endVelocity, double gravity, double timeframe){
//        double momentum = MASS * Math.abs(endVelocity - startVelocity);
//        double thrust = (momentum / timeframe) - (gravity * MASS);
//        return (thrust / (gravity * ISP)) * timeframe;
//    }
//    public static double takeoff(double startVelocity, double endVelocity, double gravity){
//        double e = 2.718;
//        double wetMass = MASS * Math.pow (e, Math.abs(startVelocity - endVelocity) / (ISP * gravity));
//        return wetMass - MASS;
//    }
//
//    public static double landing(double startVelocity, double endVelocity, double gravity, double distance){
//        double acc = (Math.pow(startVelocity, 2) - Math.pow(endVelocity, 2)) / (2* distance);
//        //double thrust = MASS * (acc + gravity);
//        double time = (startVelocity - endVelocity) / acc;
//        return calculateFuel(startVelocity, endVelocity, gravity, time);
//    }

    /**
     * Calculates the fuel consumption in kg for any change in speed over a time
     * @param startVelocity velocity in km/s when activating thrusters
     * @param endVelocity velocity in km/s when deactivating thrusters
     * @param timeframe time in seconds it takes from start to end of the maneuver
     * @return consumed fuel
     */
    public double fuel(double startVelocity, double endVelocity, double timeframe){
        return Math.abs(impulse(startVelocity, endVelocity, timeframe)) * 0.001; //1m/s is 0.001km/s, fuel consumption is proportional to impulse
    }
    public double fuelTakeoffLanding(double startVelocity, double endVelocity, double timeframe){
        return Math.abs(impulseTakeoffLanding(startVelocity, endVelocity, timeframe)) * 0.001;
    }

    /**
     * Calculates the impulse in Newton-seconds for a constant force applied over a timeframe
     * @param startVelocity velocity in km/s when activating thrusters
     * @param endVelocity velocity in km/s when deactivating thrusters
     * @param timeframe time in seconds it takes from start to end of the maneuver
     * @return impulse of the maneuver
     */
    public double impulse(double startVelocity, double endVelocity, double timeframe){
        return force(startVelocity, endVelocity, timeframe) * timeframe; //For a constant force over time, one can choose to solve the integral graphically (form of a rectangle)
    }
    public double impulseTakeoffLanding(double startVelocity, double endVelocity, double timeframe){
        return forceTakeoffLanding(startVelocity, endVelocity, timeframe) * timeframe;
    }

    /**
     * Calculates the force in Newton for an acceleration from one speed to another over a timeframe
     * @param startVelocity velocity in km/s when activating thrusters
     * @param endVelocity velocity in km/s when deactivating thrusters
     * @param timeframe time in seconds it takes from start to end of the maneuver
     * @return force required from the thrusters
     */
    public double force(double startVelocity, double endVelocity, double timeframe){
        return MASS * acceleration(startVelocity, endVelocity, timeframe); //F=m*a
    }
    public double forceTakeoffLanding(double startVelocity, double endVelocity, double timeframe){
        return MASS * acceleration(startVelocity, endVelocity, timeframe) + 9.81; //F=m*a+g
    }

    /**
     * Calculates the acceleration in km/s^2 needed to get from one speed to another over a timeframe
     * @param startVelocity velocity in km/s when activating thrusters
     * @param endVelocity velocity in km/s when deactivating thrusters
     * @param timeframe time in seconds it takes from start to end of the maneuver
     * @return constant acceleration
     */
    public double acceleration(double startVelocity, double endVelocity, double timeframe){
        return Math.abs(startVelocity - endVelocity) / timeframe; //Change in speed over a timeframe; assumes a constant acceleration
    }

    /**
     * Trapezoid method to calculate an integral
     * @param a left endpoint
     * @param b right endpoint
     * @param n total amount of trapezoids
     * @param h step-size
     * @param startVelocity speed at the beginning in km/s
     * @param endVelocity speed at the end in km/s
     * @return the integral
     */
    public double trap(double a, double b, int n, double h, double startVelocity, double endVelocity){
        double result;
        double x;
        int i;

        result = (f(a, startVelocity, endVelocity, b - a) + f(b, startVelocity, endVelocity, b - a))/2.0;
        for (i = 1; i <= n-1; i++) {
            x = a + i*h;
            result = result + f(x, startVelocity, endVelocity, b - a);
        }
        result = result*h;

        return result;
    }

    /**
     * For use of a function
     * @param x variable
     * @param startVelocity velocity in km/s when activating thrusters
     * @param endVelocity velocity in km/s when deactivating thrusters
     * @param timeframe time in seconds it takes from start to end of the maneuver
     * @return a function value at point x
     */
    public double f(double x, double startVelocity, double endVelocity, double timeframe) {
        double function;
        function = force(startVelocity, endVelocity, timeframe); //Temporary; need a proper function here
        return function * x;
    }
}

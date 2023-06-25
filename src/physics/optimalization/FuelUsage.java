package physics.optimalization;

import physics.simulation.TripSimulation;

public class FuelUsage {
    //private static final int ISP = 280; //Specific Impulse taken from a rocket with similar mass (Atlas V)
    private static final int MASS = 50000; //Mass of our rocket in kilograms

        //with the following method I went through time-interval sizes iteratively (starting from step sizes of 100) to ultimately get to the following local optima
        //changes were made in TripSimulator to accommodate this, that were later reverted
        //interval size of 110 if computation time can be longer (ca. one minute)
        //interval size of 3403 if not (ca. 3 seconds)
//    public void bruteForce(){
//
//        TripSimulation test = new TripSimulation();
//        int minValue = 101;
//        int maxValue = 129; //dips: 3403 ain't bad (4494km/7730kg), 110 (3322km/7578kg)
//        double lowestValue = 8000;
//        int optimalVariable = -1;
//        for (int variable = minValue; variable <= maxValue; variable+=1) {
//            System.out.println("Iteration: "+variable);
//            double returnValue = (double)test.simulateTrip(variable)[1];
//            if (returnValue < lowestValue) {
//                lowestValue = returnValue;
//                optimalVariable = variable;
//            }
//        }
//
//        System.out.println("Optimal Variable: " + optimalVariable);
//        System.out.println("Lowest Return Value: " + lowestValue);
//
//    }

    /**
     * Calculates the fuel consumption in kg for any change in speed over a time
     * @param startVelocity velocity in km/s when activating thrusters
     * @param endVelocity velocity in km/s when deactivating thrusters
     * @param timeframe time in seconds it takes from start to end of the maneuver
     * @return consumed fuel
     */
    public static double fuel(double startVelocity, double endVelocity, double timeframe){
        return Math.abs(impulse(startVelocity, endVelocity, timeframe)) * 0.001; //1m/s is 0.001km/s, fuel consumption is proportional to impulse
    }
    public static double fuelTakeoffLanding(double startVelocity, double endVelocity, double timeframe){
        return Math.abs(impulseTakeoffLanding(startVelocity, endVelocity, timeframe)) * 0.001;
    }

    /**
     * Calculates the impulse in Newton-seconds for a constant force applied over a timeframe
     * @param startVelocity velocity in km/s when activating thrusters
     * @param endVelocity velocity in km/s when deactivating thrusters
     * @param timeframe time in seconds it takes from start to end of the maneuver
     * @return impulse of the maneuver
     */
    public static double impulse(double startVelocity, double endVelocity, double timeframe){
        return force(startVelocity, endVelocity, timeframe) * timeframe; //For a constant force over time, one can choose to solve the integral graphically (form of a rectangle)
    }
    public static double impulseTakeoffLanding(double startVelocity, double endVelocity, double timeframe){
        return forceTakeoffLanding(startVelocity, endVelocity, timeframe) * timeframe;
    }

    /**
     * Calculates the force in Newton for an acceleration from one speed to another over a timeframe
     * @param startVelocity velocity in km/s when activating thrusters
     * @param endVelocity velocity in km/s when deactivating thrusters
     * @param timeframe time in seconds it takes from start to end of the maneuver
     * @return force required from the thrusters
     */
    public static double force(double startVelocity, double endVelocity, double timeframe){
        return MASS * acceleration(startVelocity, endVelocity, timeframe); //F=m*a
    }
    public static double forceTakeoffLanding(double startVelocity, double endVelocity, double timeframe){
        return MASS * acceleration(startVelocity, endVelocity, timeframe) - 9.81; //F=m*a-g
    }

    /**
     * Calculates the acceleration in km/s^2 needed to get from one speed to another over a timeframe
     * @param startVelocity velocity in km/s when activating thrusters
     * @param endVelocity velocity in km/s when deactivating thrusters
     * @param timeframe time in seconds it takes from start to end of the maneuver
     * @return constant acceleration
     */
    public static double acceleration(double startVelocity, double endVelocity, double timeframe){
        return Math.abs(startVelocity - endVelocity) / timeframe; //Change in speed over a timeframe; assumes a constant acceleration
    }

    // The following methods would give a more accurate estimate of the fuel consumption, however they aren't necessary
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

//        public static double calculateFuel(double startVelocity, double endVelocity, double gravity, double timeframe){
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
}

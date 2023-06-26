package physics.landing;

import physics.vectors.Vector;

import java.util.ArrayList;
import java.util.List;

public class OpenLoopLanding {
    private static double h;
    private final static double g = 1.352;
    private final static double umax = 10 * g;
    public OpenLoopLanding(double h){
        this.h = h;
    }

    /**
     * Simulates movement across the x-axis.
     *      x" = u * sin(theta)
     *      theta = pi/2 radians
     *      therefore x" = u
     * @param initialCoordinates the initial x coordinates of the landing module
     * @param landingCoordinates the x coordinates of the landing point
     * @param tf the final time
     * @param t0 the start time
     * @param h the step size
     * @return list of all steps taken across the x-axis
     */
    public static List<Vector> moveAcrossXAxis(double initialCoordinates, double landingCoordinates, double tf, double t0, double h){
        List<Vector> thrusts = new ArrayList<>();
        double landerXCoordinate = initialCoordinates;

        double landingXCoordinate = landingCoordinates;

        double displacement = landingXCoordinate - landerXCoordinate;

        //Assume initial velocity to be zero after stabilising
        double initialVelocity = 0;

        //Calculate number of steps, and displacement covered by each step
        int numberOfSteps = (int)(((tf-t0)/h));
        double displacementPerStep = displacement/numberOfSteps;
        double displacementCovered = 0;

        for(int i = 0; i < numberOfSteps;i++) {

            double displacementPerSubStep = calculateDisplacementPerStepXDirection(displacementPerStep, initialVelocity);

            double steps = Math.ceil(displacementPerStep / displacementPerSubStep);
            int numberOfSubsteps = (int) steps;

            for (int j = 0; j < numberOfSubsteps; j++) {
                double acceleration = getUniformAcceleration(displacementPerSubStep, initialVelocity, h);
                if (j == (numberOfSubsteps - 1) && i == (numberOfSteps - 1)) {
                    thrusts.add(new Vector(new double[]{acceleration, 0, 0}));
                    acceleration = -initialVelocity;
                    thrusts.add(new Vector(new double[]{acceleration, 0, 0}));
                    displacement += initialVelocity * h;
                } else if (j == 0 && i == 0) {
                    acceleration /= (acceleration / displacementPerSubStep);
                    Vector thrust = new Vector(new double[]{acceleration, 0, 0});
                    thrusts.add(thrust);
                    initialVelocity = (acceleration * h) + initialVelocity; //reorganise a = (v-u)/t -> v = at + u
                    displacementCovered += initialVelocity * h;
                } else if (j == 0) {
                    if (initialVelocity < displacementPerSubStep) {
                        double difference = initialVelocity - displacementPerSubStep;
                        acceleration = Math.abs(difference);
                    } else {
                        double difference = initialVelocity - displacementPerSubStep;
                        acceleration = -Math.abs(difference);
                    }

                    Vector thrust = new Vector(new double[]{acceleration, 0, 0});
                    thrusts.add(thrust);
                    initialVelocity = (acceleration * h) + initialVelocity; //reorganise a = (v-u)/t -> v = at + u
                    displacementCovered += initialVelocity * h;
                } else {
                    acceleration = 0;
                    Vector thrust = new Vector(new double[]{acceleration, 0, 0});
                    thrusts.add(thrust);
                    displacementCovered += initialVelocity * h;
                }
            }
        }
        return thrusts;
    }

    /**
     * Simulates movement across the y-axis.
     *      y" = u * cos(theta) - g
     *      theta = pi radians
     *      therefore y" = (-1 * u) - g
     * @param initialCoordinates the initial y coordinates of the landing module
     * @param landingCoordinates the y coordinates of the landing point
     * @param tf the final time
     * @param t0 the start time
     * @param h the step size
     * @return list of all steps taken across the y-axis
     */
    public static List<Vector> moveAcrossYAxis(double initialCoordinates, double landingCoordinates, double tf, double t0, double h){
        List<Vector> thrusts = new ArrayList<>();
        double landerYCoordinate = initialCoordinates;

        double landingYCoordinate = landingCoordinates;

        double displacement = landingYCoordinate - landerYCoordinate;

        //Assume initial velocity to be zero after stabilising
        double initialVelocity = 0;

        int numberOfSteps = (int)(((tf-t0)/h));
        double displacementPerStep = displacement/numberOfSteps;
        double displacementCovered = 0;

        for(int i = 0; i < numberOfSteps;i++){

            double displacementPerSubStep = OpenLoopLanding.calculateDisplacementPerStepYDirection(displacementPerStep);

            double steps = Math.ceil(displacementPerStep/displacementPerSubStep);
            int numberOfSubsteps = (int)steps;

            for(int j = 0; j < numberOfSubsteps; j++){
                double acceleration = getUniformAcceleration(displacementPerSubStep,initialVelocity,h);
                acceleration = getAcceleration(acceleration,0,Math.PI).get(1);
                if (j == (numberOfSubsteps-1) && i == (numberOfSteps-1) && (numberOfSubsteps != 1 && numberOfSteps != 1)){
                    thrusts.add(new Vector(new double[]{0,0,0}));
                    acceleration = -1 * initialVelocity;
                    thrusts.add(new Vector(new double[]{0,acceleration,0}));
                    displacement += initialVelocity * h;
                }
                else if(j == 0 && i == 0) {
                    acceleration /= (acceleration/displacementPerSubStep);
                    Vector thrust = new Vector(new double[]{0,acceleration,0});
                    thrusts.add(thrust);
                    initialVelocity = (acceleration * h) + initialVelocity; //reorganise a = (v-u)/t -> v = at + u
                    displacementCovered += initialVelocity * h;
                }
                else if(j == 0){
                    if(initialVelocity < displacementPerSubStep){
                        double difference = initialVelocity-displacementPerSubStep;
                        acceleration = Math.abs(difference);
                    }
                    else{
                        double difference = initialVelocity-displacementPerSubStep;
                        acceleration = -Math.abs(difference);
                    }

                    Vector thrust = new Vector(new double[]{0,acceleration,0});
                    thrusts.add(thrust);
                    initialVelocity = (acceleration * h) + initialVelocity; //reorganise a = (v-u)/t -> v = at + u
                    displacementCovered += initialVelocity * h;
                }
                else{
                    acceleration = 0;
                    Vector thrust = new Vector(new double[]{0,acceleration,0});
                    thrusts.add(thrust);
                    displacementCovered += initialVelocity * h;
                }
            }
        }
        return thrusts;
    }

    /**
     * Splits up a given time step into multiple substeps to ensure acceleration does not exceed umax
     * @param displacementForStep the current displacement for the step
     * @return displacement for each substep
     */
    public static double calculateDisplacementPerStepXDirection(double displacementForStep, double initialVelocity){
        boolean validAcceleration = false;
        int originalSteps = 1;
        int currentNumberOfSteps;
        double originalDisplacement = displacementForStep;
        initialVelocity = 0;
        double newDisplacement = originalDisplacement;
        int n = 1;

        while(!validAcceleration){
            double acceleration = getUniformAcceleration(newDisplacement, initialVelocity,1);
            if(!validateAcceleration(acceleration)){
                currentNumberOfSteps = originalSteps * (n+1);
                n++;
                newDisplacement = originalDisplacement/currentNumberOfSteps;
            }
            else{
                return newDisplacement;
            }
        }
        return newDisplacement;
    }

    /**
     * Splits up a given time step into multiple substeps to ensure acceleration does not exceed umax
     * @param displacementForStep the current displacement for the step
     * @return displacement for each substep
     */
    public static double calculateDisplacementPerStepYDirection(double displacementForStep){
        boolean validAcceleration = false;
        int originalSteps = 1;
        int currentNumberOfSteps;
        double originalDisplacement = displacementForStep;
        double initialVelocity = 0;
        double newDisplacement = originalDisplacement;
        int n = 1;

        while(!validAcceleration){
            double acceleration = getUniformAcceleration(newDisplacement,initialVelocity,1);
            acceleration = getAcceleration(acceleration,0,Math.PI).get(1);
            if(!validateAcceleration(acceleration)){
                currentNumberOfSteps = originalSteps * (n+1);
                n++;
                newDisplacement = originalDisplacement/currentNumberOfSteps;
            }
            else{
                return newDisplacement;
            }
        }
        return newDisplacement;
    }

    /**
     * Validates whether the acceleration is possible for the main thruster (acceleration does not exceed umax)
     * @param acceleration the acceleration to be validated
     * @return true if the acceleration falls within the allowed thrust of the main thruster, false otherwise.
     */
    public static boolean validateAcceleration(double acceleration){
        acceleration = Math.abs(acceleration);
        if(acceleration < umax) {
            return true;
        }
        return false;
    }

    /**
     * Calculates the acceleration over a specified distance for a specified period of time
     *      s = ut + 1/2at^2
     *      ->  2(s - ut) = at^2
     *      ->  a = 2(s-ut)/t^2
     * @param displacement the distance over which acceleration is to be calculated
     * @param initialVelocity the velocity at the beginning of the period
     * @param time the length of the period
     * @return the acceleration for the period
     */
    public static double getUniformAcceleration(double displacement,double initialVelocity,double time){
        double acceleration  = 2 * (displacement - (initialVelocity * time) ) / Math.pow(time,2);
        return acceleration;
    }

    /**
     * Applies differential equations to get the acceleration
     * @param u the acceleration from the main thruster
     * @param v the torque from the side thrusters
     * @param theta the angle of rotation of the landing module
     * @return acceleration according to differential equations of motion
     */
    public static Vector getAcceleration(double u, double v, double theta){
        double accelerationX =  u * Math.sin(theta);
        double accelerationY = u * Math.cos(theta) - g;
        double accelerationTheta = v;
        Vector acceleration = new Vector(new double[]{accelerationX,accelerationY,accelerationTheta});
        return acceleration;
    }

    /**
     * Finds the angle between two vectors
     * @param u vector one
     * @param v vector two
     * @return angle between u and v
     */
    public double angleBetweenVectors(Vector u, Vector v){
        double uX = u.get(0);
        double uY = u.get(1);
        double vX = v.get(0);
        double vY = v.get(1);

        u = new Vector(new double[]{uX,uY});
        v = new Vector(new double[]{vX,vY});

        double dotProduct = u.dotProduct(v);
        double magnitudeU = u.getMagnitude();
        double magnitudeV = v.getMagnitude();

        //Answer in radians - check if radians or degrees
        return Math.acos(dotProduct/(magnitudeU * magnitudeV));
    }
}

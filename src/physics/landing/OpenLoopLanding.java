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

    public static List<Vector> moveAcrossXAxis(double initialCoordinates, double landingCoordinates, double tf, double t0, double h){
        List<Vector> thrusts = new ArrayList<>();
        double landerXCoordinate = initialCoordinates;

        double landingXCoordinate = landingCoordinates;

        double displacement = landingXCoordinate - landerXCoordinate;

        //Assume initial velocity to be zero after stabilising
        double initialVelocity = 0;

        //Calculate number of steps, and displacement covered by each step
        int numberOfSteps = (int)(((tf-t0)/h));
        System.out.println("Number of steps: "+numberOfSteps);
        double displacementPerStep = displacement/numberOfSteps;
        System.out.println("Displacement per step: "+displacementPerStep);
        System.out.println("Displacement: "+displacement);
        double displacementCovered = 0;

        for(int i = 0; i < numberOfSteps;i++) {

            double displacementPerSubStep = calculateDisplacementPerStepXDirection(displacementPerStep, initialVelocity);
            System.out.println("Displacement per step: " + displacementPerStep);
            System.out.println("Displacement per substep: " + displacementPerSubStep);

            double steps = Math.ceil(displacementPerStep / displacementPerSubStep);
            int numberOfSubsteps = (int) steps;
            System.out.println("Calculated number of substeps: " + numberOfSubsteps);

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
                    System.out.println("Acceleration: " + acceleration);
                    thrusts.add(thrust);
                    initialVelocity = (acceleration * h) + initialVelocity; //reorganise a = (v-u)/t -> v = at + u
                    displacementCovered += initialVelocity * h;
                    System.out.println("Distance covered: " + displacementCovered);
                } else if (j == 0) {
                    if (initialVelocity < displacementPerSubStep) {
                        double difference = initialVelocity - displacementPerSubStep;
                        acceleration = Math.abs(difference);
                        System.out.println("New Acceleration: " + acceleration);
                    } else {
                        double difference = initialVelocity - displacementPerSubStep;
                        acceleration = -Math.abs(difference);
                        System.out.println("New Acceleration: " + acceleration);
                    }

                    Vector thrust = new Vector(new double[]{acceleration, 0, 0});
                    System.out.println("Acceleration: " + acceleration);
                    thrusts.add(thrust);
                    initialVelocity = (acceleration * h) + initialVelocity; //reorganise a = (v-u)/t -> v = at + u
                    System.out.println("New velocity: " + initialVelocity);
                    displacementCovered += initialVelocity * h;
                    System.out.println("Distance covered: " + displacementCovered);
                } else {
                    acceleration = 0;
                    Vector thrust = new Vector(new double[]{acceleration, 0, 0});
                    System.out.println("Acceleration: " + acceleration);
                    thrusts.add(thrust);
                    System.out.println("Initial velocity: " + initialVelocity);
                    displacementCovered += initialVelocity * h;
                    System.out.println("Distance covered: " + displacementCovered);
                }

//                stabiliseWind();
            }
        }
        return thrusts;
    }

    public static List<Vector> moveAcrossYAxis(double initialCoordinates, double landingCoordinates, double tf, double t0, double h){
        List<Vector> thrusts = new ArrayList<>();
        double landerYCoordinate = initialCoordinates;

        double landingYCoordinate = landingCoordinates;

        double displacement = landingYCoordinate - landerYCoordinate;

        //Assume initial velocity to be zero after stabilising
        double initialVelocity = 0;

        //Calculate number of steps, and displacement covered by each step
        int numberOfSteps = (int)(((tf-t0)/h));
        //System.out.println("Number of steps: "+numberOfSteps);
        double displacementPerStep = displacement/numberOfSteps;
        //System.out.println("Displacement per step: "+displacementPerStep);
        //System.out.println("Displacement: "+displacement);
        double displacementCovered = 0;

        for(int i = 0; i < numberOfSteps;i++){

            double displacementPerSubStep = OpenLoopLanding.calculateDisplacementPerStepYDirection(displacementPerStep,initialVelocity);
            //System.out.println("Displacement per step: "+displacementPerStep);
            //System.out.println("Displacement per substep: "+displacementPerSubStep);

            double steps = Math.ceil(displacementPerStep/displacementPerSubStep);
            int numberOfSubsteps = (int)steps;
            //System.out.println("Calculated number of substeps: "+numberOfSubsteps);

            for(int j = 0; j < numberOfSubsteps; j++){
                double acceleration = getUniformAcceleration(displacementPerSubStep,initialVelocity,h);
                //System.out.println("First Acceleration: "+acceleration);
                acceleration = getAcceleration(acceleration,0,Math.PI).get(1);
                //System.out.println("Altered Acceleration: "+acceleration);
                if (j == (numberOfSubsteps-1) && i == (numberOfSteps-1) && (numberOfSubsteps != 1 && numberOfSteps != 1)){
                    thrusts.add(new Vector(new double[]{0,0,0}));
                    acceleration = -1 * initialVelocity;
                    thrusts.add(new Vector(new double[]{0,acceleration,0}));
                    displacement += initialVelocity * h;
                }
                else if(j == 0 && i == 0) {
                    acceleration /= (acceleration/displacementPerSubStep);
                    Vector thrust = new Vector(new double[]{0,acceleration,0});
                    //System.out.println("Acceleration: "+acceleration);
                    thrusts.add(thrust);
                    initialVelocity = (acceleration * h) + initialVelocity; //reorganise a = (v-u)/t -> v = at + u
                    displacementCovered += initialVelocity * h;
                    //System.out.println("Distance covered: "+displacementCovered);
                }
                else if(j == 0){
                    if(initialVelocity < displacementPerSubStep){
                        double difference = initialVelocity-displacementPerSubStep;
                        acceleration = Math.abs(difference);
                        //System.out.println("New Acceleration: "+acceleration);
                    }
                    else{
                        double difference = initialVelocity-displacementPerSubStep;
                        acceleration = -Math.abs(difference);
                        //System.out.println("New Acceleration: "+acceleration);
                    }

                    Vector thrust = new Vector(new double[]{0,acceleration,0});
                    //System.out.println("Acceleration: "+acceleration);
                    thrusts.add(thrust);
                    initialVelocity = (acceleration * h) + initialVelocity; //reorganise a = (v-u)/t -> v = at + u
                    //System.out.println("New velocity: "+initialVelocity);
                    displacementCovered += initialVelocity * h;
                    //System.out.println("Distance covered: "+displacementCovered);
                }
                else{
                    acceleration = 0;
                    Vector thrust = new Vector(new double[]{0,acceleration,0});
                    //System.out.println("Acceleration: "+acceleration);
                    thrusts.add(thrust);
                    //System.out.println("Initial velocity: "+initialVelocity);
                    displacementCovered += initialVelocity * h;
                    //System.out.println("Distance covered: "+displacementCovered);
                }

//                stabiliseWind();
            }
        }
        return thrusts;
    }

//    public static int calculateAccelerationSteps(double currentAcceleration, double desiredAcceleration){
//
//    }



    /**
     * Splits up a given time step into multiple substeps to ensure acceleration does not exceed umax
     *
     * @param displacementForStep the current displacement for the step
     * @return displacement for each substep
     */
    public static double calculateDisplacementPerStepXDirection(double displacementForStep, double initialVelocity){
        boolean validAcceleration = false;
        int originalSteps = 1;
        int currentNumberOfSteps;
        double originalDisplacement = displacementForStep;
        initialVelocity = 0;
        System.out.println("Original displacement: "+originalDisplacement);
        double newDisplacement = originalDisplacement;
        int n = 1;

        while(!validAcceleration){
            double acceleration = getUniformAcceleration(newDisplacement, initialVelocity,1);
            System.out.println("Acceleration: " + acceleration);
            //System.out.println("Validating acceleration...");
            if(!validateAcceleration(acceleration)){
                currentNumberOfSteps = originalSteps * (n+1);
                //System.out.println(currentNumberOfSteps);
                n++;
                newDisplacement = originalDisplacement/currentNumberOfSteps;
                //System.out.println("New displacement: "+newDisplacement);
                //System.out.println("Displacement per step: "+displacementForStep);
                //System.out.println("Number of adjusted steps:"+ currentNumberOfSteps);
            }
            else{
                return newDisplacement;
            }
        }
        return newDisplacement;
    }

    public static double calculateDisplacementPerStepYDirection(double displacementForStep, double initialVelocity){
        boolean validAcceleration = false;
        int originalSteps = 1;
        int currentNumberOfSteps;
        double originalDisplacement = displacementForStep;
        initialVelocity = 0;
        //System.out.println("Original displacement: "+originalDisplacement);
        double newDisplacement = originalDisplacement;
        int n = 1;

        while(!validAcceleration){
            double acceleration = getUniformAcceleration(newDisplacement,initialVelocity,1);
            //System.out.println("Acceleration before: " + acceleration);
            acceleration = getAcceleration(acceleration,0,Math.PI).get(1);
            //System.out.println("Acceleration after: " + acceleration);
            if(!validateAcceleration(acceleration)){
                currentNumberOfSteps = originalSteps * (n+1);
                //System.out.println(currentNumberOfSteps);
                n++;
                newDisplacement = originalDisplacement/currentNumberOfSteps;
                //System.out.println("New displacement: "+newDisplacement);
                //System.out.println("Displacement per step: "+displacementForStep);
                //System.out.println("Number of adjusted steps:"+ currentNumberOfSteps);
            }
            else{
                double velocity = (acceleration * h) + initialVelocity;
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

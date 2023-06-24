package physics.controllers;

import physics.vectors.StateVector;
import physics.vectors.Vector;

import java.util.ArrayList;
import java.util.List;

public class OpenLoopController implements Controller{

    private StateVector initialState;
    private Vector landingPosition;

    private final double g = 1.352;
    private final double umax = 10 * g;
    private double t0;
    private double tf;
    private double h;
    private List<Vector>thrusts = new ArrayList<>();
    private List<Vector>torques = new ArrayList<>();

    //Starting point for each controller
    @Override
    public void startControl() {

    }
    public OpenLoopController(StateVector initialState, Vector landingPosition, double t0, double tf, double h){
        this.initialState = initialState;
        this.landingPosition = landingPosition;
        this.t0 = t0;
        this.tf = tf;
        this.h = h;
    }

    /**
     *
     * @param initialState initial position and velocity of the probe: x, y, and theta
     * @param landingPosition two-dimensional vector representing the landing position
     */
    public void OpenLoop(StateVector initialState, Vector landingPosition, double t0, double tf, double h) {
        this.initialState = initialState;
        this.landingPosition = landingPosition;
        this.t0 = t0;
        this.tf = tf;
        this.h = h;
    }

    public void simulateLanding(){
        stabiliseLandingModule();
        stabiliseWind();
        rotateModuleHorizontally();
        approachXCoordinate();
        rotateModuleVertically();
        descendToLandingPoint();
    }

    /**
     * Stabilises the landing module at the initial conditions so that a clear path to the landing point
     * can be made without the landing module moving further away/closer to the spot in the process.
     */
    public void stabiliseLandingModule(){
        Vector stabilisingVelocity = initialState.getVector(1).multiply(-1);
        double stabilisingVelocityMagnitude = stabilisingVelocity.getMagnitude();
        if(stabilisingVelocityMagnitude < umax){
            thrusts.add(stabilisingVelocity);
        }
        else{
            double timeToStabilise = stabilisingVelocityMagnitude / umax;
            int roundStabilisingTime = (int)Math.round(timeToStabilise);
            double residual = timeToStabilise - roundStabilisingTime;
            if(residual == 0){
                Vector stableUnitVector = stabilisingVelocity.multiply(1/stabilisingVelocityMagnitude);
                for(int i = 0; i < roundStabilisingTime; i++){
                    Vector thrust = stableUnitVector.multiply(umax);
                    thrusts.add(thrust);
                }
            }
            else{
                Vector stableUnitVector = stabilisingVelocity.multiply(1/stabilisingVelocityMagnitude);
                roundStabilisingTime = (int)Math.floor(timeToStabilise);
                for(int i = 0; i < roundStabilisingTime; i++){
                    Vector thrust = stableUnitVector.multiply(umax);
                    thrusts.add(thrust);
                }
                double partialThrust = residual*umax;
                Vector residualThrust = stableUnitVector.multiply(partialThrust);
                thrusts.add(residualThrust);
            }
        }
    }

    public void stabiliseWind(){

    }

    public void rotateModuleHorizontally(){
        //Parallel to the x-axis
        Vector goalOrientation;
    }

    public void rotateModuleVertically(){
        //Perpendicular to the x-axis
        Vector goalOrientation;
    }

    /**
     * Simulates probe moving parallel to the surface of titan to hover above the landing point
     */
    public void approachXCoordinate(){
        double landerXCoordinate = initialState.getVector(0).get(0);

        double landingXCoordinate = landingPosition.get(0);

        double displacement = landingXCoordinate - landerXCoordinate;

        //Assume initial velocity to be zero after stabilising
        double initialVelocity = 0;

        int numberOfSteps = (int)((tf-t0)/h);
        System.out.println("Number of steps: "+numberOfSteps);
        double displacementPerStep = displacement/numberOfSteps;
        System.out.println("Displacement per step: "+displacementPerStep);
        System.out.println("Displacement: "+displacement);
        double displacementCovered = 0;

        for(int i =0; i < numberOfSteps; i++){
            double acceleration = getUniformAcceleration(displacementPerStep,initialVelocity,1);
            System.out.println("Acceleration: "+acceleration);
            Vector thrust = new Vector(new double[]{acceleration,0,0});
            thrusts.add(thrust);
            initialVelocity = (acceleration * 1) + initialVelocity; //reorganise a = (v-u)/t -> v = at + u
            System.out.println("Initial velocity: "+initialVelocity);
            displacementCovered += initialVelocity * 1;
            System.out.println("Distance covered: "+displacementCovered);
            stabiliseWind();
        }
    }

    /**
     * Simulates probe descent in the y-direction given initial y conditions and landing y position
     */
    public void descendToLandingPoint(){ //works with large step sizes (small tf)
        double landerYCoordinate = initialState.getVector(0).get(1);

        double landingYCoordinate = landingPosition.get(1);

        double displacement = landingYCoordinate - landerYCoordinate;

        //Assume initial velocity to be zero after stabilising
        double initialVelocity = 0;

        int numberOfSteps = (int)((tf-t0)/h);
        System.out.println("Number of steps: "+numberOfSteps);
        double displacementPerStep = displacement/numberOfSteps;
        System.out.println("Displacement per step: "+displacementPerStep);
        System.out.println("Displacement: "+displacement);
        double displacementCovered = 0;

        for(int i = 0; i <  numberOfSteps; i++){
            System.out.println("HI");
            double uniformAcceleration = -getUniformAcceleration(displacementPerStep,initialVelocity,1);
            System.out.println("Uniform acceleration: "+uniformAcceleration);
            double acceleration = getAcceleration(uniformAcceleration,initialVelocity,Math.PI).get(1);
            if(Math.abs(acceleration) > umax){
                double sign = Math.signum(acceleration);
                acceleration = sign * umax;
            }
            System.out.println("Acceleration: " + acceleration);
            Vector thrust = new Vector(new double[]{0,acceleration,0});
            thrusts.add(thrust);
            initialVelocity = (acceleration * 1) + initialVelocity; //reorganise a = (v-u)/t -> v = at + u
            System.out.println("Initial velocity: "+initialVelocity);
            displacementCovered += initialVelocity * 1;
            System.out.println("Distance covered: "+displacementCovered);
            stabiliseWind();
        }

    }

    /**
     * Applies all thrusts for the period to an initial state vector
     * @param initialState state vector at time t0 (initial position & velocity)
     * @return list of all state vectors for the period after thrust is applied
     */
    public List <StateVector> applyThrust(StateVector initialState){
        Vector initialPosition = initialState.getVector(0);
        Vector initialVelocity = initialState.getVector(1);
        List <StateVector> allStates = new ArrayList<>();

        for(int i =0; i < thrusts.size(); i++){
            initialPosition = initialPosition.add(initialVelocity.multiply(h));
            initialVelocity = initialVelocity.add(thrusts.get(i).multiply(h));
            allStates.add(new StateVector(new Vector[]{initialPosition,initialVelocity}));
        }
        return allStates;
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
    public double getUniformAcceleration(double displacement,double initialVelocity,double time){
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
    public Vector getAcceleration(double u, double v, double theta){
        double accelerationX =  u * Math.sin(theta);
        double accelerationY = u * Math.cos(theta) - g;
        double accelerationTheta = v;
        Vector acceleration = new Vector(new double[]{accelerationX,accelerationY,accelerationTheta});
        return acceleration;
    }

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

    public double [][] getPath(){
        simulateLanding();
        List<StateVector> list = applyThrust(this.initialState);
        double[][] path = new double[list.size()][2];
        for(int i = 0; i < list.size(); i ++){
            path[i][0] = list.get(i).getVector(0).get(0);
            path[i][1] = list.get(i).getVector(0).get(1);
        }
        return path;
    }

    public static void main(String[] args) {
        StateVector s = new StateVector(new Vector[]{new Vector(new double[]{55, 20,0}), new Vector(new double[]{0,0,0})});
        Vector landingCoordinates = new Vector(new double[]{0,0});
        OpenLoopController controller = new OpenLoopController(s, landingCoordinates,0,10,1);
        controller.simulateLanding();
        List <StateVector> states = controller.applyThrust(s);
        for(int i = 0; i < states.size(); i++){
            System.out.println(states.get(i).toString());

        }
    }

}

package physics.controllers;

import landing.LanderState;
import landing.LandingFunction;
import physics.simulation.SolarSystemPhysicsSimulation;
import physics.vectors.StateVector;
import physics.vectors.Vector;

import java.util.ArrayList;
import java.util.List;

public class IgnoreControllerDrafts implements Controller{
    double t0;
    double tf;
    double h;

    final double g = 1.352;
    final double umax = 10*g;
    final double vmax = 1; //radian

    double fuelConsumption;
    LanderState landerState;
    Vector landingPosition;
    SolarSystemPhysicsSimulation simulation;
    StateVector[] fullTripAdjustments;
    List<Vector> forces = new ArrayList<>();

    public IgnoreControllerDrafts(LanderState landerState, Vector landingPosition, double t0, double tf, double h){
        this.landerState = landerState;
        this.landingPosition = landingPosition;
        this.t0 = t0;
        this.tf =tf;
        this.h = h;
    }

    //Starting point for controller
    @Override
    public void startControl() {
    for(double t = t0; t < tf; t +=h){ //this makes a zigzag downwards
        approachLandingPosition();
        descendToLandingSpot();
    }
    }

    public StateVector[] getFullTrip(){
        return fullTripAdjustments;
    }

    public void approachLandingPosition(){
        LanderState currentState = this.landerState;
        LandingFunction landingFunc = new LandingFunction();
        List<LanderState> landing = new ArrayList<>();
        landing.add(currentState);
        System.out.println(currentState.getStateToString());

        double displacement = currentState.getPos().get(0) - this.landingPosition.get(0);

        if(displacement > 0){
            for(double t = t0; t < tf; t+= h){
                double u = currentState.getU();
                if(u>10*g)u = 10*g;

                Vector currentVelocity = currentState.getVel();
                Vector currentPosition = currentState.getPos();
                Vector deceleration = landingFunc.getAcceleration(currentState).multiply(-1);

                Vector alterVelocity = deceleration;
                Vector alterPosition = new Vector(new double[]{currentVelocity.get(0),0,0});

                StateVector current = new StateVector(new Vector[]{currentPosition,currentVelocity});
                StateVector altered = new StateVector(new Vector[]{landingFunc.calculateNewPos(currentState,h),landingFunc.calculateNewVel(currentState,h)});


                //currentState.setState();

                currentState = new LanderState(altered,u, currentState.getTorque());
                System.out.println(currentState.getStateToString());

            }
        }
        else if(displacement < 0){}


    }

    public void land(){

    }



    public void descendToLandingSpot(){
        StateVector landerState = this.landerState.getState();
        double landerYCoordinate = landerState.getVector(0).get(1);
        double landerYVelocity = landerState.getVector(1).get(1);
        double landingSpotYCoordinate = this.landingPosition.get(1);

        //deceleration in the y axis
        double distance = landerYCoordinate - landingSpotYCoordinate;
        double deceleration = getAcceleration(distance,landerYVelocity,h);
        double force = this.landerState.getMass() * deceleration;
        Vector decelerationForce = new Vector(new double[]{0,force,0});
        forces.add(decelerationForce);

    }

    public void simulateLanding(){
        double landerXCoordinate = this.landerState.getPos().get(0);
        double landerYCoordinate = this.landerState.getPos().get(1);
        double landingPositionXCoordinate = this.landingPosition.get(0);
        double landerXVelocity = this.landerState.getVel().get(0);
        double landerYVelocity = this.landerState.getVel().get(1);
        Vector xVector = new Vector(new double[]{landerXCoordinate,landerXVelocity});
        Vector yVector = new Vector(new double[]{landerYCoordinate,landerYVelocity});

        double u = 0;
        double v = 0;
        //double theta = Math.atan(landerYCoordinate/landerXCoordinate);
        double theta = angleBetweenVectors(xVector,yVector);
        System.out.println("theta:"+theta);

        double displacement = landerXCoordinate - landingPositionXCoordinate;

        int numberOfSteps = (int)((tf-t0)/h);
        boolean passedTarget;
        System.out.println(this.landerState.getStateToString());

        if(displacement > 0) passedTarget = true;
        else passedTarget = false;
        //approach from left/right is an alternative but same idea

        if(!passedTarget){ //need to 'go back'
            //need to find the vector direct to the area
            //split up the thrust such that bigger at the start and smaller/none toward the end
            //such that it stops right before it hits the ground
        }
        else{
            for(int i = 0; i < numberOfSteps; i++){
                //maybe make it proportional to number of h's ?
                //u = thrust / current iteration - e.g. 10/1 is full thrust 10/2 half 10/3 third etc
                u = umax/(i+2);
                System.out.println(u);
                v = vmax/(i+1);
                System.out.println(v);


                Vector acceleration = getAcceleration(444,u,v,theta).multiply(-1);
                //Vector acceleration = getAcceleration(displacement,landerXVelocity,h);
                //double changeX = acceleration.get(0)*-1;
                //acceleration.set(0,changeX);
                StateVector derivative = new StateVector(new Vector[]{this.landerState.getVel().multiply(-1),acceleration.multiply(-1)});

                //Euler step
                StateVector newVector = this.landerState.getState().add(derivative.multiply(h));

                this.landerState.setState(newVector);
                System.out.println(this.landerState.getStateToString());
                landerXVelocity = this.landerState.getVel().get(0);
                landerYVelocity = this.landerState.getVel().get(1);
                theta = Math.atan(landerYVelocity/landerXVelocity);

            }
        }


    }

    public void openLandingX(){
        double landerXCoordinate = this.landerState.getPos().get(0);
        double landSpotXCoordinate = this.landingPosition.get(0);
        //try drawing a single line to the target and just going across that - making adjustments for wind etc.
    }

    //best working atm
    public void startLanding(){
        double landerXCoordinate = this.landerState.getPos().get(0);
        double landSpotXCoordinate = this.landingPosition.get(0);
        double landerYCoordinate = this.landerState.getPos().get(0);
        double landSpotYCoordinate = this.landingPosition.get(0);
        double landerXVelocity = this.landerState.getVel().get(0);

        LandingFunction function = new LandingFunction();

        System.out.println(this.landerState.getStateToString());

        //passed the target
        if(landerXCoordinate - landSpotXCoordinate > 0){
            int numberOfSteps = (int)((tf-t0)/h);
            for(int i = 0; i < numberOfSteps; i++){
                //covers distance proportional to x - maybe change to have i etc and cover displacement / n-i
                double displacement = (landerXCoordinate - landSpotXCoordinate)/1.5;
                System.out.println("Displacement: "+displacement);
                double decelerationInXDirection = getAcceleration(displacement,this.landerState.getVel().get(0),h);
                System.out.println("Deceleration: " + decelerationInXDirection);
                Vector positionDerivativeX = new Vector(new double[]{landerXVelocity,0,0});
                Vector velocityDerivativeX = new Vector(new double[]{decelerationInXDirection,0,0});
                //only updating x
                Vector newPosition = this.landerState.getPos().subtract(positionDerivativeX.multiply(h));
                Vector newVelocity = this.landerState.getVel().add(velocityDerivativeX.multiply(h));

                double newXPosition = newPosition.get(0);
                double newYPosition = newPosition.get(1);
                double newXVelocity = newVelocity.get(0);
                double newYVelocity = newVelocity.get(1);
                double thetaPos = Math.atan((newYPosition-g)/newXPosition);
                double thetaVel = Math.atan((newYVelocity-g)/newXVelocity);

                newPosition.set(2,thetaPos);
                newVelocity.set(2,thetaVel);

                StateVector newState = new StateVector(new Vector[]{newPosition,newVelocity});

                //need to use u and v


                this.landerState.setState(newState);
                double u = function.calculateU(this.landerState,thetaPos);
                this.landerState.setU(u);

                System.out.println(this.landerState.getStateToString());

                landerXCoordinate = this.landerState.getPos().get(0);
                landSpotXCoordinate = this.landingPosition.get(0);
                landerYCoordinate = this.landerState.getPos().get(0);
                landSpotYCoordinate = this.landingPosition.get(0);
                landerXVelocity = this.landerState.getVel().get(0);
            }
        }
        //target is still ahead
        else if(landerXCoordinate - landSpotXCoordinate < 0){

        }
//        if(landerYCoordinate - landSpotYCoordinate != 0){
//            descendToLandingSpot();
//        }
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
    public double getAcceleration(double displacement,double initialVelocity,double time){
        double acceleration  = 2 * (displacement - (initialVelocity * time) ) / Math.pow(time,2);
        return acceleration;
    }

    public Vector getAcceleration(int index, double u, double v, double theta){
        double newX = u * Math.sin(theta);
        double newY = u * Math.cos(theta) - g;
        double newTheta = v;
        Vector acceleration = new Vector(new double[]{newX,newY,newTheta});
        return acceleration;
    }



    /**
     * Finds the angle between two vectors
     *      cos(x) = (a . b) / (|a||b|)
     * @param u one of two vectors to find the angle between
     * @param v the second of two vectors to find the angle between
     * @return the angle between u and v (in radians)
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

    public static void main(String[] args) {
        StateVector s = new StateVector(new Vector[]{new Vector(new double[]{55, 20,0}), new Vector(new double[]{1,0,0})});
        LanderState l = new LanderState(s, 1.352, 0);
        IgnoreControllerDrafts controller = new IgnoreControllerDrafts(l,new Vector(new double[]{0,0}),0,37,0.5);
        controller.startLanding();
    }

}

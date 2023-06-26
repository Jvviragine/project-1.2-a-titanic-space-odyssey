package physics.controllers;

import physics.vectors.StateVector;
import physics.vectors.Vector;

public class LanderState {

    private StateVector state; //State Vector containing the position and velocity, both position and velocity have theta as the last element of the vector
    //private double theta; //angle between the lander and the x-axis

    private double u; //acceleration provided by main thruster
    private double v;
    private double mass = 50000;

    public LanderState(StateVector state, double acceleration, double torque){
        this.state = state;
        this.v = torque;
        this.u = acceleration;
    }

    public StateVector getState() {
        return state;
    }

    public void setState(StateVector state) {
        this.state = state;
    }

    public double getThetaPos(){
        return getPos().get(2);
    }

    public void setThetaPos(double thetaPos){
        Vector nPos = new Vector(new double[]{getPos().get(0), getPos().get(1),thetaPos});
        StateVector nState = new StateVector(new Vector[]{nPos,getVel()});
        this.state = nState;
    }

    public double getThetaVel(){
        return getVel().get(2);
    }
    public double getU() {
        return u;
    }

    public void setU(double u) {
        this.u = u;
    }

    public double getTorque () {
        return v;
    }

    public String getStateToString(){
        return state.toString();
    }

    public Vector getPos(){
        return state.getVector(0);
    }

    public Vector getVel(){
        return state.getVector(1);
    }

    public void setVel(Vector newVel){
        StateVector n = new StateVector(new Vector[]{getPos(),newVel});
        setState(n);
    }

    public double getMass(){
        return mass;
    }

    public String getTotalState(){
        return "Position: " + getPos().toString() + "\n" + "Velocity: " + getVel().toString();
    }

}

package landing;

import physics.vectors.StateVector;
import physics.vectors.Vector;

public class LanderState {

    private StateVector state; //State Vector containing the position and velocity
    private double theta; //angle between the lander and the x-axis

    private double u; //acceleration provided by main thruster

    public LanderState(StateVector state, double angle, double acceleration){
        this.state = state;
        this.theta = angle;
        this.u = acceleration;
    }

    public StateVector getState() {
        return state;
    }

    public void setState(StateVector state) {
        this.state = state;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double angle) {
        this.theta = angle;
    }

    public double getU() {
        return u;
    }

    public void setU(double u) {
        this.u = u;
    }

    public String getStateToString(){
        return state.toString(state);
    }

    public Vector getPos(){
        return state.getVector(0);
    }

    public Vector getVel(){
        return state.getVector(1);
    }

    public String getTotalState(){
        return "Position: " + getPos().toString() + "\n" + "Velocity: " + getVel().toString() + "\n" + "Angle to x-axis: " + getTheta();
    }

}

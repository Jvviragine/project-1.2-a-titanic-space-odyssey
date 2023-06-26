package physics.controllers;

import physics.vectors.StateVector;
import physics.vectors.Vector;

public class LandingFunction {

    //Gravitational pull on Titan in m and km. If using km remember to convert at the begging if using meter convert at the end!!!

    //private double G = -1.352 * Math.pow(10,-3); // km/s² = N/kg
    private double G = -1.352; // m/s² = N/kg


    public LanderState LanderStep(LanderState state, double h){
        Vector newPos = calculateNewPos(state, h);
        Vector newVel = calculateNewVel(state, h);
        StateVector newState = new StateVector(new Vector[]{newPos, newVel});
        double theta = calculateTheta(state);
        state.setThetaPos(theta);
        state.setU(calculateU(state, theta));
        return new LanderState(newState, state.getU(),state.getTorque());
    }


    public Vector calculateNewPos(LanderState state, double h){
        double theta = calculateTheta(state);
        state.setThetaPos(theta);
        return state.getPos().add(state.getVel().multiply(h));
    }

    public Vector calculateNewVel(LanderState state, double h){
        Vector acc = getAcceleration(state);
        return state.getVel().add(acc.multiply(h));
    }

    public Vector getAcceleration(LanderState state){
        Vector acc = new Vector(new double[3]);
        double x = state.getU()*Math.sin(state.getThetaPos());
        double y = state.getU()*Math.cos(state.getThetaPos()) + G;
        acc.set(0, x);
        acc.set(1, y);
        acc.set(2, state.getTorque());
        return acc;
    }

    public double calculateTheta(LanderState state){
        double theta = Math.atan((state.getPos().get(1)-G)/state.getPos().get(0));
        return theta;
    }

    public double calculateU(LanderState state, double theta){
        double u = -state.getPos().get(0)/Math.sin(theta);
        if (Math.abs(u) > Math.abs(10 * G)) u = 10 * G;
        return u;
    }

    

}

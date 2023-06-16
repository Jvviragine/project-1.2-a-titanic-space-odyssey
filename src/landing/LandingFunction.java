package landing;

import physics.vectors.StateVector;
import physics.vectors.Vector;

import javax.swing.plaf.nimbus.State;

public class LandingFunction {

    //Gravitational pull on Titan in m and km. If using km remember to convert at the begging if using meter convert at the end!!!

    //private double G = -1.352 * Math.pow(10,-3); // km/s² = N/kg
    private double G = -1.352; // m/s² = N/kg


    public LanderState LanderStep(LanderState state, double h){
        Vector newPos = calculateNewPos(state, h);
        Vector newVel = calculateNewVel(state, h);
        double newTheta = calculateNewTheta(newPos);
        StateVector newState = new StateVector(new Vector[]{newPos, newVel});
        double u = 0;
        return new LanderState(newState, newTheta, u);
    }


    public Vector calculateNewPos(LanderState state, double h){
        return state.getPos().add(state.getVel().multiply(h));
    }

    public Vector calculateNewVel(LanderState state, double h){
        Vector acc = getAcceleration(state);
        return state.getVel().add(acc.multiply(h));
    }

    public Vector getAcceleration(LanderState state){
        Vector acc = new Vector(new double[2]);
        double x = state.getU()*Math.sin(state.getTheta());
        double y = state.getU()*Math.cos(state.getTheta()) + G;
        acc.set(0, x);
        acc.set(1, y);
        return acc;
    }

    public double calculateNewTheta(Vector v){
        return Math.atan(v.get(1)/v.get(0));
    }

    public static void main(String[] args) {
        LandingFunction f = new LandingFunction();
        StateVector s = new StateVector(new Vector[]{new Vector(new double[]{0.01,0.015}), new Vector(new double[]{0,0})});
        LanderState l = new LanderState(s, 0, 0);
        for(int i = 0;i<10;i++){
            l = f.LanderStep(l, 0.5);
            System.out.println(l.getTotalState());
        }
    }

}

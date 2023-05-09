package physics.solvers;

import physics.functions.Function;
import physics.vectors.StateVector;
import physics.vectors.Vector;

public class RK3Solver implements Solver{

    StateVector y0;
    double h;
    double t0;
    public RK3Solver(StateVector y0, double h, double t0){
        this.y0 = y0;
        this.h = h;
        this.t0 = t0;
    }

    public StateVector solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize) {
        StateVector currentState = initialCondition;

        for(double t = t0; t<tf; t += stepSize) {
            StateVector fty = function.applyFunction(currentState, t);

            //k1 = h*f(t,y)
            StateVector k1 = fty.multiply(stepSize);

            //k2 = h*f(t + 1/3*h,y + 1/3*k1)
            Vector tAdd = new Vector(new double[]{3});
            tAdd.setAll(1/3*stepSize);

            fty.getVector(0).add(tAdd);
            StateVector k2;

            //k3 = h*f(t + 2/3*h, y + 2/3*k2)

            //yn+1 = y + 1/4*(k1 + 3*k3)

        }
        return null;
    }
}

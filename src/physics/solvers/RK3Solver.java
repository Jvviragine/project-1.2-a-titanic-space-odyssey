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
        return null;
    }
}

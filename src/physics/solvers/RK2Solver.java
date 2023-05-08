package physics.solvers;
import physics.functions.Function;
import physics.vectors.Vector;
import physics.vectors.StateVector;

public class RK2Solver implements Solver {
    StateVector y0;
    double t0;
    double t1;
    double h;

    //fty -> f (replacement later)
    public RK2Solver(StateVector y0, StateVector fty, double h, double t0){
        this.y0 = y0;
        this.t0 = t0;
        this.h = h;
    }

    public StateVector solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize) {
        return y0;
    }
}

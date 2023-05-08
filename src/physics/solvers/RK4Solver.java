package physics.solvers;
import physics.functions.Function;
import physics.vectors.Vector;
import physics.vectors.StateVector;
public class RK4Solver implements Solver{

    StateVector v;


    @Override
    public StateVector solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize) {
        return v;
    }
}

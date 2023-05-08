package physics.solvers;
import physics.vectors.StateVector;
import physics.functions.Function;

public interface Solver {
     public StateVector solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize);
}

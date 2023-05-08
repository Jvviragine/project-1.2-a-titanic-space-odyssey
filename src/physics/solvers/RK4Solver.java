package physics.solvers;
import physics.vectors.Vector;
import physics.vectors.StateVector;
public class RK4Solver implements Solver{

    StateVector v;

    public StateVector solve() {
        return v;
    }
}

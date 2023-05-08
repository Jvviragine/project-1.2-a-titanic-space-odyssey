package physics.solvers;
import physics.vectors.Vector;
import physics.vectors.StateVector;

public interface Solver {
     StateVector solve(); // Always dealing with State Vector
}

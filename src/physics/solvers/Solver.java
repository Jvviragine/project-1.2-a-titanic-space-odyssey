package physics.solvers;
import physics.vectors.Vector;

public interface Solver {
     //return double, or Vector with new positions and velocities ?
     Vector solve();
}

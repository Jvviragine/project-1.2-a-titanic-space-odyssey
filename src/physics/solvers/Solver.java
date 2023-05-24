package physics.solvers;
import physics.vectors.StateVector;
import physics.functions.Function;

import java.util.ArrayList;

public interface Solver {
     public StateVector solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize);

     public StateVector[] solve(Function function, StateVector[] initialConditions, double t0, double tf, double stepSize);

     public ArrayList<StateVector> getAllStates();

     public ArrayList<StateVector> getAllStates(int index);
}

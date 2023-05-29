package physics.functions;

import physics.vectors.StateVector;
import physics.vectors.Vector;

public interface Function {
    public StateVector applyFunction(StateVector y, double t);

    public void resetState(StateVector[] stateVectors);
}

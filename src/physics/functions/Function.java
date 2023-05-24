package physics.functions;

import physics.vectors.StateVector;
import physics.vectors.Vector;

public interface Function {
    public StateVector applyFunction(StateVector y, double t);
    // Maybe change it to Abstract  to it can be Inherited

    public void resetState(StateVector[] stateVectors);
}

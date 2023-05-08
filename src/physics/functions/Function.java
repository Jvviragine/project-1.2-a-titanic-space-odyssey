package physics.functions;

import physics.vectors.StateVector;
import physics.vectors.Vector;

public interface Function {
    public StateVector applyFunction(StateVector stateVector);
    // Maybe change it to Abstract  to it can be Inherited
}

package physics.functions;

import physics.vectors.StateVector;
import physics.vectors.Vector;

public class TestODEDerivativeFunction implements Function{
    /**
     * Test Function for the ODE y'=y, y(0) = 1, y'=f(t,y)
     * @param y
     * @param t
     * @return
     */
    @Override
    public StateVector applyFunction(StateVector y, double t) {
        // Tests to make sure that there is only 1 Vector and that this vector is 1D
        return y;
    }

    public void resetState(StateVector[] stateVectors){
        //Do nothing
    }

    public static void main(String[] args) {

        // Testing the Evaluation of the Derivative of the Function y = e^t, namely y'=y
        Function dydt = new TestODEDerivativeFunction();
        Vector v = new Vector(new double[]{1});
        StateVector sv = new StateVector(new Vector[]{v});
        System.out.println(dydt.applyFunction(sv, 1).getVector(0).get(0));
    }
}

package physics.solvers;

import physics.functions.Function;
import physics.vectors.StateVector;

public class RK3Solver implements Solver{

    public RK3Solver(){
    }

    /**
     * Heun's method (third order Runge-Kutta)
     * @param function the derivative function
     * @param initialCondition the state of the vector at t0
     * @param t0 beginning of the time period
     * @param tf end of the time period
     * @param stepSize step size which determinates the difference in time periods after each step
     * @return the state of a vector at the period tf, which is the end of a given time period
     */
    public StateVector solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize) {
        StateVector currentState = initialCondition;

        for(double t = t0; t<tf; t += stepSize) {

            //derivative, f(t,y) in next calculation step
            StateVector fty = function.applyFunction(currentState, t);

            //k1 = h*f(t,y)
            StateVector k1 = fty.multiply(stepSize);

            //k2 = h*f(t + 1/3*h,y + 1/3*k1), getting the derivative than calculating k2
            StateVector fty2 = function.applyFunction(currentState.add(k1.multiply(1/3)), t + 1/3*stepSize);
            StateVector k2 = fty2.multiply(stepSize);

            //k3 = h*f(t + 2/3*h, y + 2/3*k2), getting the derivative than calculating k3
            StateVector fty3 = function.applyFunction(currentState.add(k2.multiply(2/3)), t + 2/3*stepSize);
            StateVector k3 = fty3.multiply(stepSize);

            //yn+1 = y + 1/4*(k1 + 3*k3), calculating new y and updating the current state
            StateVector add = k3.multiply(3).add(k1).multiply(1/4);
            currentState = currentState.add(add);

        }
        return currentState;
    }
}

package physics.solvers;

import physics.functions.Function;
import physics.vectors.Vector;
import physics.vectors.StateVector;

public class EulerSolver implements Solver{
    StateVector y0;
    StateVector fty;
    double h;

    static double t;

    /**
     * Constructor for an Euler solver
     * @param y0 the initial state vector on which to perform Euler's method
     * @param fty the derivative f(t,y) of the current state vector
     * @param h the step size
     * @param t0 the initial time
     */

    //y0, function f, h, t0
    public EulerSolver(StateVector y0, StateVector fty, double h, double t0){
        this.y0 = y0;
        this.fty = fty;
        this.h = h;
        this.t = t0;
    }

    /**
     * Applies Euler's method to the current state vector
     *      yn+1 = yn + hf(t,y)
     * @return the next state vector at state yn+1
     */
    public StateVector solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize) {

        StateVector currentState = initialCondition;

        //Solve Euler for the time period tf-t0
        for(double t=t0; t<tf; t+=stepSize){
            //Get derivative, f(t,y) of the function
            StateVector derivative = function.applyFunction(currentState,t);

            //h * f(t,y)
            StateVector hfty = derivative.multiply(h);

            //yn + hf(t,y)
            StateVector y1 = currentState.add(hfty);

            //Set y1 to y0 for next iteration
            currentState = y1;

            //Update solver time
            this.t +=h;
        }

        return currentState;
        
    }

}

package physics.solvers;
import physics.functions.Function;
import physics.vectors.Vector;
import physics.vectors.StateVector;

public class RK2Solver implements Solver {

    public RK2Solver(){

    }

    /**
     *Ralston's Method (Second-order Runge-Kutta)
     * @param function the derivative function of the equation
     * @param initialCondition the position and velocity at t0
     * @param t0 the time from which the Ralston's steps are calculated
     * @param tf the time until which the Ralston's steps are calculated
     * @param stepSize the time interval at which evaluations of the derivative are made
     * @return the state vector at the end of the time period tf - t0
     */
    public StateVector solve(Function function, StateVector initialCondition, double t0, double tf, double stepSize) {
        //ki1 = h * f(ti,wi)


        //ki2 = h * f(ti + 2/3h , wi + 2/3ki1)


        //wi+1 = wi + 1/4(ki1 + 3ki2)

        return initialCondition;
    }
}

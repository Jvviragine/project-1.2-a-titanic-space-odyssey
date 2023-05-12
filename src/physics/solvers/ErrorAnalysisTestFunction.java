package physics.solvers;
import physics.functions.Function;
import physics.functions.TestODEDerivativeFunction;
import physics.functions.TestODEFunction;
import physics.vectors.StateVector;
import physics.vectors.Vector;

/**
 * This Class is for Analysing the Error of the ODE Solvers for the Differential Equation f(t, y) = y
 */
public class ErrorAnalysisTestFunction {

    public static void main(String[] args) {
        // Declaring the Differential Equation
        Function dydt = new TestODEDerivativeFunction();
        // Declaring the Exact Solution Function
        Function ft = new TestODEFunction();
        // Setting the Initial Conditions - y(0) = 1
        double t0 = 0;
        Vector initialValueVector = new Vector(new double[]{1});
        StateVector initialValue = new StateVector(new Vector[]{initialValueVector});

        // Choosing the Input Values
        double tf = 1;
        double h = 0.025;

        // Exact Result of the Differential Equation
        StateVector exactStateVector = ft.applyFunction(initialValue, tf);
        double exactValue = exactStateVector.getVector(0).get(0);
        System.out.println("Exact Value = " + "\n" + exactValue + "\n");

        // Approximation using Euler - First Order - O(h)
        Solver eulerSolver = new EulerSolver();
        StateVector eulerStateVector = eulerSolver.solve(dydt, initialValue, t0, tf, h);
        double eulerApproximation = eulerStateVector.getVector(0).get(0);
        System.out.println("Euler = " + "\n" + eulerApproximation);
        double errorEuler = Math.abs(exactValue - eulerApproximation);
        System.out.println("Error Euler = " + "\n" + errorEuler + "\n");

        // Approximation using Runge Kutta 2 - Second Order - O(h2)
        Solver rungeKutta2Solver = new RK2Solver();
        StateVector rk2StateVector = rungeKutta2Solver.solve(dydt, initialValue, t0, tf, h);
        double rungeKutta2Approximation = rk2StateVector.getVector(0).get(0);
        System.out.println("RK2 = " + "\n" + rungeKutta2Approximation);
        double errorRK2 = Math.abs(exactValue - rungeKutta2Approximation);
        System.out.println("Error RK2 = " + "\n" + errorRK2 + "\n");

        // Approximation using Runge Kutta 3 - Second Order - O(h3)
        Solver rungeKutta3Solver = new RK3Solver();
        StateVector rk3StateVector = rungeKutta3Solver.solve(dydt, initialValue, t0, tf, h);
        double rungeKutta3Approximation = rk3StateVector.getVector(0).get(0);
        System.out.println("RK3 = " + "\n" + rungeKutta3Approximation + "\n");

        // Approximation using Runge Kutta 4 - Second Order - O(h4)
        Solver rungeKutta4Solver = new RK4Solver();
        StateVector rk4StateVector = rungeKutta4Solver.solve(dydt, initialValue, t0, tf, h);
        double rungeKutta4Approximation = rk4StateVector.getVector(0).get(0);
        System.out.println("RK4 = " + "\n" + rungeKutta4Approximation);
        double errorRK4 = Math.abs(exactValue - rungeKutta4Approximation);
        System.out.println("Error RK4 = " + "\n" + errorRK4 + "\n");


    }
}

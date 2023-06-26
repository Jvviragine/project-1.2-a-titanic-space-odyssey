package physics.solvers;

import physics.simulation.SolarSystemPhysicsSimulation;
import solar_system_data.PlanetaryData;

public class RunTimeExperiment {

    private Solver eulerSolver = new EulerSolver();
    private Solver RK2 = new RK2Solver();
    private Solver RK3 = new RK3Solver();
    private Solver RK4 = new RK4Solver();

    public RunTimeExperiment() {

        SolarSystemPhysicsSimulation system = new SolarSystemPhysicsSimulation(PlanetaryData.getCelestialBodiesStateVector(),PlanetaryData.getCelestialBodiesMasses(),PlanetaryData.getCelestialBodyNames(), RK4);
        system.simulateCelestialBodiesOrbit(36000000, 60);
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        RunTimeExperiment runTimeExperiment = new RunTimeExperiment();

        long stopTime = System.nanoTime();
        System.out.println((stopTime - startTime)/1000);
    }
}

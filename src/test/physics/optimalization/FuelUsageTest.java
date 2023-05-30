package physics.optimalization;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.vectors.Vector;

import static org.junit.jupiter.api.Assertions.*;

class FuelUsageTest {

    private FuelUsage fuelUsage;

    @BeforeEach
    void setUp() {
        fuelUsage = new FuelUsage();
    }

    @AfterEach
    void tearDown() {
        fuelUsage = null;
    }

    @Test
    //covers fuel
    void testFuel() {
        double startVelocity = 1;
        double endVelocity = -2;
        double timeframe = 50.5;
        double expected = Math.abs(fuelUsage.impulse(startVelocity, endVelocity, timeframe)) * 0.001;

        double output = fuelUsage.fuel(startVelocity, endVelocity, timeframe);

        assertEquals(expected, output);
    }

    @Test
    //covers fuelTakeOffLanding
    void testFuelTakeoffLanding() {
        double startVelocity = 1;
        double endVelocity = -2;
        double timeframe = 50.5;
        double expected = Math.abs(fuelUsage.impulseTakeoffLanding(startVelocity, endVelocity, timeframe)) * 0.001;

        double output = fuelUsage.fuelTakeoffLanding(startVelocity, endVelocity, timeframe);

        assertEquals(expected, output);
    }

    @Test
    //covers impulse
    void testImpulse() {
        double startVelocity = 1;
        double endVelocity = -2;
        double timeframe = 50.5;
        double expected = fuelUsage.force(startVelocity, endVelocity, timeframe) * timeframe;

        double output = fuelUsage.impulse(startVelocity, endVelocity, timeframe);

        assertEquals(expected, output);

    }

    @Test
    //covers impulseTakeOffLanding
    void testImpulseTakeoffLanding() {
        double startVelocity = 1;
        double endVelocity = -2;
        double timeframe = 50.5;
        double expected = fuelUsage.forceTakeoffLanding(startVelocity, endVelocity, timeframe) * timeframe;

        double output = fuelUsage.impulseTakeoffLanding(startVelocity, endVelocity, timeframe);

        assertEquals(expected, output);
    }

    @Test
    //covers force
    void testForce() {
        double startVelocity = 1;
        double endVelocity = -2;
        double timeframe = 50.5;
        double expected = 50000 * fuelUsage.acceleration(startVelocity, endVelocity, timeframe);

        double output = fuelUsage.force(startVelocity, endVelocity, timeframe);

        assertEquals(expected, output);
    }

    @Test
    //covers acceleration
    void testForceTakeoffLanding() {
        double startVelocity = 1;
        double endVelocity = -2;
        double timeframe = 50.5;
        double expected = 50000 * fuelUsage.acceleration(startVelocity, endVelocity, timeframe) + 9.81;

        double output = fuelUsage.forceTakeoffLanding(startVelocity, endVelocity, timeframe);

        assertEquals(expected, output);
    }

    @Test
    //covers acceleration
    void testAcceleration() {
        double startVelocity = 1;
        double endVelocity = -2;
        double timeframe = 50.5;
        double expected = Math.abs(startVelocity - endVelocity) / timeframe;;

        double output = fuelUsage.acceleration(startVelocity, endVelocity, timeframe);

        assertEquals(expected, output);

    }

    @Test
    //covers trap
    void testTrap() {
        double a = -1;
        double b = 2.2;
        int n = 10;
        double startVelocity = 1;
        double endVelocity = -2;
        double h = 0.5;
        double result;
        double x;
        int i;

        result = (fuelUsage.f(a, startVelocity, endVelocity, b - a) + fuelUsage.f(b, startVelocity, endVelocity, b - a))/2.0;
        for (i = 1; i <= n-1; i++) {
            x = a + i*h;
            result = result + fuelUsage.f(x, startVelocity, endVelocity, b - a);
        }
        result = result*h;
        double expected = result;

        double output = fuelUsage.trap(a, b, n, h, startVelocity, endVelocity);

        assertEquals(expected, output);
    }

    @Test
    //covers f
    void testF() {
        double startVelocity = 1;
        double endVelocity = -2;
        double timeframe = 50.5;
        double x = -3;

        double function;
        function = fuelUsage.force(startVelocity, endVelocity, timeframe); //Temporary; need a proper function here
        double expected =  function * x;

        double output = fuelUsage.f(x, startVelocity, endVelocity, timeframe);
    }
}
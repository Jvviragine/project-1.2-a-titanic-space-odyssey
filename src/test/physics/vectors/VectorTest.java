package physics.vectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VectorTest {

    private Vector u;
    private Vector v;

    @BeforeEach
    void setUp() {
        this.u = new Vector(new double[]{-1,0.5,0});
        this.v = new Vector(new double[]{1,2,3});
    }

    @AfterEach
    void tearDown() {
        this.u = null;
        this.v = null;
    }

    @Test
    void Vector(){
        double[] initialState = {1, 2, 3};
        Vector output = new Vector(initialState);
        for (int i = 0; i < initialState.length; i++) {
            assertEquals(initialState[i], output.get(i));
        }
    }

    @Test
    void get() {
        double output = u.get(2);
        assertEquals(0, output);
    }

    @Test
    void getDimension() {
        int output = u.getDimension();
        assertEquals(3, output);
    }

    @Test
    void set() {
        u.set(1, 10);
        assertEquals(10, u.get(1));

    }

    @Test
    void setAll() {
        u.setAll(0);
        for (int i = 0; i < u.getDimension(); i++) {
            assertEquals(0, u.get(i));
        }
    }

    @Test
    void add() {
        double[] expected = {0, 2.5, 3};
        Vector output1 = u.add(v);
        Vector output2 = v.add(u);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], output1.get(i));
            assertEquals(expected[i], output2.get(i));
        }
    }

    @Test
    void subtract() {
        double[] expected = {2, 1.5, 3};
        Vector output1 = u.subtract(v);
        Vector output2 = v.subtract(u);
        for (int i = 0; i < u.getDimension(); i++) {
            assertEquals(-1*expected[i], output1.get(i));
            assertEquals(expected[i], output2.get(i));
        }
    }

    @Test
    void multiply() {
        double[] expected = {2, -1, -0.0};  //negative zero at index 2
        Vector output = u.multiply(-2);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], output.get(i));
        }
    }

    @Test
    void getMagnitude() {
        double output = u.getMagnitude();
        assertEquals(Math.sqrt(1.25), output);
    }

    @Test
    void distance() {
        assertEquals(0, u.distance(u));

        double output = u.distance(v);
        assertEquals(Math.sqrt(15.25), output);

        output = v.distance(u);
        assertEquals(Math.sqrt(15.25), output);
    }
}
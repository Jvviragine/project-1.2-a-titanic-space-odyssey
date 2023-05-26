package physics.vectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    private Vector u;
    private Vector v;
    private Vector w;

    @BeforeEach
    void setUp() {
        this.u = new Vector(new double[]{-1,0.5,0});
        this.v = new Vector(new double[]{1,2,3});
        this.w = new Vector(new double[]{-0,5});
    }

    @AfterEach
    void tearDown() {
        this.u = null;
        this.v = null;
        this.w = null;
    }

    @Test
    //covers Vector for initialState.length > 0
    void testVectorWithNonEmptyInitialState(){
        double[] initialState = {1, 2, 3};
        Vector output = new Vector(initialState);
        for (int i = 0; i < initialState.length; i++) {
            assertEquals(initialState[i], output.get(i));
        }
    }

    @Test
    //covers Vector for initialState.length = 0
    void testVectorWithNullInitialState() {
        double[] initialState = new double[0];
        assertThrows(IllegalArgumentException.class, () -> new Vector(initialState));
    }

    @Test
    //covers get for index < state.length
    void testGetWithIndexInBounds() {
        double output = u.get(2);
        assertEquals(0, output);
    }

    @Test
    //covers get for index >= state.length
    void testGetWithIndexOutOfBounds() {
        int index = u.getDimension();
        assertThrows(IndexOutOfBoundsException.class, () -> u.get(index));
    }

    @Test
    //covers getDimension
    void testGetDimension() {
        int output = u.getDimension();
        assertEquals(3, output);
    }

    @Test
    //covers Set for index < state.length
    void testSetWithIndexInBounds() {
        u.set(1, 10);
        assertEquals(10, u.get(1));
    }

    @Test
    //covers Set for index >= state.length
    void testSetWithIndexOutOfBounds() {
        int index = u.getDimension() + 1;
        assertThrows(IndexOutOfBoundsException.class, () -> u.set(index, 10));
    }

    @Test
    //covers setAll
    void testSetAll() {
        u.setAll(0);
        for (int i = 0; i < u.getDimension(); i++) {
            assertEquals(0, u.get(i));
        }
    }

    @Test
    //covers add for a Vector with the same dimension
    void testAddWithVectorWithSameDimension() {
        double[] expected = {0, 2.5, 3};
        Vector output1 = u.add(v);
        Vector output2 = v.add(u);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], output1.get(i));
            assertEquals(expected[i], output2.get(i));
        }
    }

    @Test
    //covers add for a Vector with a different dimension
    void testAddWithVectorWithDifferentDimension() {
        assertThrows(IllegalArgumentException.class, () -> u.add(w));
        assertThrows(IllegalArgumentException.class, () -> w.add(u));
    }

    @Test
    //covers subtract for a Vector with the same dimension
    void testSubtractWithVectorWithSameDimension() {
        double[] expected = {2, 1.5, 3};
        Vector output1 = u.subtract(v);
        Vector output2 = v.subtract(u);
        for (int i = 0; i < u.getDimension(); i++) {
            assertEquals(-1*expected[i], output1.get(i));
            assertEquals(expected[i], output2.get(i));
        }
    }

    @Test
    //covers subtract for a Vector with a different dimension
    void testSubtractWithVectorWithDifferentDimension() {
        assertThrows(IllegalArgumentException.class, () -> u.subtract(w));
        assertThrows(IllegalArgumentException.class, () -> w.subtract(u));
    }

    @Test
    //covers multiply for scalar != 0
    void testMultiplyWithNonNullScalar() {
        double[] expected = {2, -1, -0.0};  //negative zero at index 2
        Vector output = u.multiply(-2);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], output.get(i));
        }
    }

    @Test
    //covers multiply for scalar = 0
    void testMultiplyWithNullScalar() {
        double[] expected = {-0.0, 0, 0};  //negative zero at index 0
        Vector output = u.multiply(0);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], output.get(i));
        }
    }

    @Test
    //covers getMagnitude
    void testGetMagnitude() {
        double output = u.getMagnitude();
        assertEquals(Math.sqrt(1.25), output);
    }

    @Test
    //covers distance for the same Vector
    void testDistanceWithTheSameVector() {
        assertEquals(0, u.distance(u));
    }

    @Test
    //covers distance for a different Vector
    void testDistanceWithDifferentVector() {
        double output = u.distance(v);
        assertEquals(Math.sqrt(15.25), output);

        output = v.distance(u);
        assertEquals(Math.sqrt(15.25), output);
    }

    @Test
    //covers copyOff
    void testCopyOff() {
        double[] excepted = new double[]{-1,0.5,0};
        Vector output = u.copyOf();
        for (int i = 0; i < excepted.length; i++) {
            assertEquals(excepted[i], output.get(i));
        }
    }

    @Test
    //covers isEqual for a Vector with the same state
    void testIsEqualWithIdenticalState() {
        Vector u2 = new Vector(new double[]{-1,0.5,0});
        assertTrue(u.isEqual(u2));
        assertTrue(u2.isEqual(u));
    }

    @Test
    //covers isEqual for a Vector with a different state
    void testIsEqualWithDifferentState() {
        assertFalse(u.isEqual(v));
        assertFalse(v.isEqual(u));
    }

    @Test
    //cover isEqual fo a Vector with a different dimension
    void testIsEqualWithDifferentDimension() {
        assertThrows(IllegalArgumentException.class, () -> u.isEqual(w));
        assertThrows(IllegalArgumentException.class, () -> w.isEqual(u));
    }
}
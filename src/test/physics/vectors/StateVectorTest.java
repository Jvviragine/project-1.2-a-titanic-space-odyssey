package physics.vectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.nimbus.State;

import static org.junit.jupiter.api.Assertions.*;

class StateVectorTest {

    private StateVector u;
    private StateVector v;
    private StateVector w;

    @BeforeEach
    //Initializes two 3-dimensional test vectors "u" and "v" before each test
    void setUp() {
        Vector vector_u1 = new Vector(new double[]{1, 2, 3});
        Vector vector_u2 = new Vector(new double[]{-1, -2, -3});
        Vector vector_v1 = new Vector(new double[]{0, 1, 2});
        Vector vector_v2 = new Vector(new double[]{0.5, 1, 2});
        Vector vector_w1 = new Vector(new double[]{0, -4, 0.5});
        u = new StateVector(new Vector[]{vector_u1, vector_u2});
        v = new StateVector(new Vector[]{vector_v1, vector_v2});
        w = new StateVector(new Vector[]{vector_w1});
    }

    @AfterEach
    //Deletes the two 3-dimensional test vectors "u" and "v" before reinitializing them
    void tearDown() {
        this.u = null;
        this.v = null;
    }

    @Test
    //covers StateVector with vector.length > 0
    void testStateVectorWithNonNullVector() {
        Vector vector1 = new Vector(new double[]{1, 2, 3});
        Vector vector2 = new Vector(new double[]{-1, -2, -3});
        Vector[] vectorsExpected = {vector1, vector2};
        int numberOfVectorsExpected = vectorsExpected.length;
        int numberOfDimensionsExpected = vectorsExpected[0].getDimension();
        StateVector output = new StateVector(vectorsExpected);

        for (int i = 0; i < numberOfVectorsExpected; i++) {
            for (int j = 0; j < numberOfDimensionsExpected; j++) {
                assertEquals(vectorsExpected[i].get(j), output.getStateVector()[i].get(j));
            }
        }

        assertEquals(numberOfVectorsExpected, output.getNumberOfVectors());
        assertEquals(numberOfDimensionsExpected, output.getNumberOfDimensions());
    }

    @Test
    //covers StateVector with vector.length = 0
    void testStateVectorWithEmptyVector() {
        Vector[] emptyVector = new Vector[]{};
        assertThrows(IllegalArgumentException.class, () -> new StateVector(emptyVector));
    }

    @Test
    //covers getStateVector
    void testGetStateVector() {
        Vector vector1 = new Vector(new double[]{1, 2, 3});
        Vector vector2 = new Vector(new double[]{-1, -2, -3});
        Vector[] expected = new Vector[]{vector1, vector2};
        Vector[] output = u.getStateVector();
        for (int i = 0; i < u.getNumberOfVectors(); i++) {
            for (int j = 0; j < u.getNumberOfDimensions(); j++) {
                assertEquals(expected[i].get(j), output[i].get(j));
            }
        }
    }

    @Test
    //covers getVector for index < stateVector.length
    void testGetVectorWithIndexInBounds() {
        double[] expected = {-1, -2, -3};
        Vector output = u.getVector(1);
        for (int i = 0; i < output.getDimension(); i++) {
            assertEquals(expected[i], output.get(i));
        }
    }

    @Test
    //covers getVector for index < stateVector.length
    void testGetVectorWithIndexOutOfBounds() {
        int index = u.getNumberOfVectors();
        assertThrows(IndexOutOfBoundsException.class, () -> u.getVector(index));
    }

    @Test
    //covers getNumberOfVectors
    void testGetNumberOfVectors() {
        int output = u.getNumberOfVectors();
        assertEquals(2, output);
    }

    @Test
    //covers getNumberOfDimensions
    void testGetNumberOfDimensions() {
        int output = u.getNumberOfDimensions();
        assertEquals(3, output);
    }

    @Test
    //covers add for a StateVector with the same number of vectors
    void testAddStateVectorWithSameVectorNumber() {
        Vector vector1 = new Vector(new double[]{1, 3, 5});
        Vector vector2 = new Vector(new double[]{-0.5, -1, -1});
        Vector[] expected = {vector1, vector2};
        StateVector output1 = u.add(v);
        StateVector output2 = v.add(u);
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[0].getDimension(); j++) {
                assertEquals(expected[i].get(j), output1.getStateVector()[i].get(j));
                assertEquals(expected[i].get(j), output2.getStateVector()[i].get(j));
            }
        }
    }

    @Test
    //covers add for a StateVector with the different number of vectors
    void testAddStateVectorWithDifferentVectorNumber() {
        assertThrows(IllegalArgumentException.class, () -> u.add(w));
        assertThrows(IllegalArgumentException.class, () -> w.add(u));
    }

    @Test
    //covers subtract for a StateVector with the same number of vectors
    void testSubtractStateVectorWithSameVectorNumber() {
        Vector vector1 = new Vector(new double[]{1, 1, 1});
        Vector vector2 = new Vector(new double[]{-1.5, -3, -5});
        Vector[] expected = {vector1, vector2};
        StateVector output1 = u.subtract(v);
        StateVector output2 = v.subtract(u);
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].getDimension(); j++) {
                assertEquals(expected[i].get(j), output1.getStateVector()[i].get(j));
                assertEquals(-1*expected[i].get(j), output2.getStateVector()[i].get(j));
            }
        }
    }

    @Test
    //covers add for a StateVector with the different number of vectors
    void testSubtractStateVectorWithDifferentVectorNumber() {
        assertThrows(IllegalArgumentException.class, () -> u.subtract(w));
        assertThrows(IllegalArgumentException.class, () -> w.subtract(u));
    }

    @Test
    //covers multiply for a negative integer scalar
    void testMultiplyWithNonNullScalar() {
        Vector vector1 = new Vector(new double[]{-2, -4, -6});
        Vector vector2 = new Vector(new double[]{2, 4, 6});
        Vector[] expected = {vector1, vector2};
        StateVector output = u.multiply(-2);
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].getDimension(); j++) {
                assertEquals(expected[i].get(j), output.getStateVector()[i].get(j));
            }
        }
    }

    @Test
    //covers multiply for a null scalar
    void testMultiplyWithNullScalar() {
        Vector[] expected = {new Vector(new double[]{0, -0.0, 0})};
        StateVector output = w.multiply(0);
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].getDimension(); j++) {
                assertEquals(expected[i].get(j), output.getStateVector()[i].get(j));
            }
        }
    }

    @Test
    //covers isEqual for StateVector containing the same vectors
    void testIsEqualForIdenticalStateVector() {
        Vector vector1 = new Vector(new double[]{1, 2, 3});
        Vector vector2 = new Vector(new double[]{-1, -2, -3});
        StateVector u2 = new StateVector(new Vector[]{vector1, vector2});
        assertTrue(u.isEqual(u2));
        assertTrue(u2.isEqual(u));
    }

    @Test
    //covers isEqual for StateVector with different vectors
    void testIsEqualForDifferentStateVector() {
        assertFalse(u.isEqual(v));
        assertFalse(v.isEqual(u));
    }

}
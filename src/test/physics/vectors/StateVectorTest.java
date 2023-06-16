package physics.vectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.nimbus.State;

import static org.junit.jupiter.api.Assertions.*;

/*
This class is responsible for testing the StateVector class.
Four test state vectors u, v, w and z are initialized before each test.
Those state vectors can have different number of vectors.
Those vectors can have different lengths and different type of values (positive, negative, null, decimal).

Find below the input partitioning for each method :

StateVector(Vector[] vectors) :
    - vectors.length == 0
    - vectors.length > 0

getVector(int index) :
    - index < stateVector.length
    - index >= stateVector.length

getNumberOfDimensions() :
    - all vectors inside this (stateVector) have the same dimension
    - vectors inside this (stateVector) have different dimensions

add(StateVector v) :
    - this.getNumberOfVectors() == v.getNumberOfVectors()
    - this.getNumberOfVectors() != v.getNumberOfVectors()

subtract(StateVector v) :
    - this.getNumberOfVectors() == v.getNumberOfVectors()
    - this.getNumberOfVectors() != v.getNumberOfVectors()

multiply(double scalar) :
    - scalar > 0
    - scalar < 0
    - scalar == 0

isEqual(StateVector v) :
    - this == v
    - this != v

setStateVector(StateVector sv) :
    - this (stateVector) has the same number of vectors as sv
    - this (stateVector) has different number of vectors as sv
 */

class StateVectorTest {

    private StateVector u;
    private StateVector v;
    private StateVector w;
    private StateVector z;

    @BeforeEach
    //Initializes two 3-dimensional test vectors "u" and "v" before each test
    void setUp() {
        Vector vector_u1 = new Vector(new double[]{1, 2, 3});
        Vector vector_u2 = new Vector(new double[]{-1, -2, -3});
        Vector vector_v1 = new Vector(new double[]{0, 1, 2});
        Vector vector_v2 = new Vector(new double[]{0.5, 1, 2});
        Vector vector_w1 = new Vector(new double[]{0.3, -4});
        u = new StateVector(new Vector[]{vector_u1, vector_u2});
        v = new StateVector(new Vector[]{vector_v1, vector_v2});
        w = new StateVector(new Vector[]{vector_w1});
        z = new StateVector((new Vector[]{vector_u1, vector_w1}));

    }

    @AfterEach
    //Deletes the two 3-dimensional test vectors "u" and "v" before reinitializing them
    void tearDown() {
        this.u = null;
        this.v = null;
        this.w = null;
    }

    @Test
    //covers StateVector with vector.length > 0
    void testStateVectorWithNonEmptyVector() {
        Vector vector1 = new Vector(new double[]{1, 2, 3});
        Vector vector2 = new Vector(new double[]{-1, -2, -3});
        Vector[] vectorsExpected = {vector1, vector2};
        int numberOfVectorsExpected = vectorsExpected.length;
        int numberOfDimensionsExpected = vectorsExpected[0].getDimension();
        StateVector output = new StateVector(vectorsExpected);

        for (int i = 0; i < numberOfVectorsExpected; i++) {
            for (int j = 0; j < numberOfDimensionsExpected; j++) {
                assertEquals(vectorsExpected[i].get(j), output.getVector(i).get(j));
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
        StateVector expected = new StateVector(new Vector[]{vector1, vector2});
        StateVector output = u;
        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers getVector for index < stateVector.length
    void testGetVectorWithIndexInBounds() {
        double expected = -1;
        Vector output = u.getVector(1);
        for (int i = 0; i < output.getDimension(); i++) {
            assertEquals(expected, output.get(i));
        }
    }

    @Test
    //covers getVector for index >= stateVector.length
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
    //covers getNumberOfDimensions for a stateVector with vectors of the same dimension
    void testGetNumberOfDimensionsWithSameDimensions() {
        int output = u.getNumberOfDimensions();
        for (Vector v : u.getStateVector()) {
            assertEquals(v.getDimension(), output);
        }
    }

    @Test
    //covers getNumberOfDimensions for a stateVector with vectors of different dimensions
    void testGetNumberOfDimensionsWithDifferentDimensions() {
        int output = z.getNumberOfDimensions();
        for (Vector v : z.getStateVector()) {
            assertEquals(v.getDimension(), output);
        }
    }

    @Test
    //covers add for a StateVector with the same number of vectors
    void testAddStateVectorWithSameVectorNumber() {
        Vector vector1 = new Vector(new double[]{1, 3, 5});
        Vector vector2 = new Vector(new double[]{-0.5, -1, -1});
        StateVector expected = new StateVector(new Vector[]{vector1, vector2});
        StateVector output1 = u.add(v);
        StateVector output2 = v.add(u);
        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output1.getVector(i).get(j));
                assertEquals(expected.getVector(i).get(j), output2.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers add for a StateVector with a different number of vectors
    void testAddStateVectorWithDifferentVectorNumber() {
        assertThrows(IllegalArgumentException.class, () -> u.add(w));
        assertThrows(IllegalArgumentException.class, () -> w.add(u));
    }

    @Test
    //covers subtract for a StateVector with the same number of vectors
    void testSubtractStateVectorWithSameVectorNumber() {
        Vector vector1 = new Vector(new double[]{1, 1, 1});
        Vector vector2 = new Vector(new double[]{-1.5, -3, -5});
        StateVector expected = new StateVector(new Vector[]{vector1, vector2});
        StateVector output1 = u.subtract(v);
        StateVector output2 = v.subtract(u);
        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output1.getVector(i).get(j));
                assertEquals(-1*expected.getVector(i).get(j), output2.getVector(i).get(j));
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
    //covers multiply for scalar < 0
    void testMultiplyWithPositiveScalar() {
        Vector vector1 = new Vector(new double[]{2, 4, 6});
        Vector vector2 = new Vector(new double[]{-2, -4, -6});
        StateVector expected = new StateVector(new Vector[]{vector1, vector2});
        StateVector output = u.multiply(2);
        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }
    @Test
    //covers multiply for scalar < 0
    void testMultiplyWithNegativeScalar() {
        Vector vector1 = new Vector(new double[]{-2, -4, -6});
        Vector vector2 = new Vector(new double[]{2, 4, 6});
        StateVector expected = new StateVector(new Vector[]{vector1, vector2});
        StateVector output = u.multiply(-2);
        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers multiply for scalar == 0
    void testMultiplyWithNullScalar() {
        Vector vector = new Vector(new double[]{0, -0.0});
        StateVector expected =new StateVector(new Vector[]{vector});
        StateVector output = w.multiply(0);
        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers isEqual for a StateVector containing the same vectors
    void testIsEqualWithIdenticalStateVector() {
        Vector vector1 = new Vector(new double[]{1, 2, 3});
        Vector vector2 = new Vector(new double[]{-1, -2, -3});
        StateVector u2 = new StateVector(new Vector[]{vector1, vector2});
        assertTrue(u.isEqual(u2));
        assertTrue(u2.isEqual(u));
    }

    @Test
    //covers isEqual for a StateVector with different vectors
    void testIsEqualWithDifferentStateVector() {
        assertFalse(u.isEqual(v));
        assertFalse(v.isEqual(u));
    }

    @Test
    //covers toString
    void testToString() {
        String expected = "1.0, 2.0, 3.0\n-1.0, -2.0, -3.0";
        assertEquals(expected, u.toString());
    }

    @Test
    //covers setStateVector to a vector with same number of vectors
    void testSetStateVectorWithSameNumberOfVectors() {
        StateVector expected = v;
        StateVector output = u.setStateVector(v);
        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }

    @Test
    //covers setStateVector to a vector with different number of vectors
    void testSetStateVectorWithDifferentNumberOfVectors() {
        StateVector expected = w;
        StateVector output = u.setStateVector(w);
        for (int i = 0; i < expected.getNumberOfVectors(); i++) {
            for (int j = 0; j < expected.getVector(i).getDimension(); j++) {
                assertEquals(expected.getVector(i).get(j), output.getVector(i).get(j));
            }
        }
    }
}
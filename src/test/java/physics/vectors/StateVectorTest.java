package physics.vectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StateVectorTest {

    private StateVector u;
    private StateVector v;

    @BeforeEach
    void setUp() {
        Vector vector_u1 = new Vector(new double[]{1, 2, 3});
        Vector vector_u2 = new Vector(new double[]{-1, -2, -3});
        Vector vector_v1 = new Vector(new double[]{0, 1, 2});
        Vector vector_v2 = new Vector(new double[]{0.5, 1, 2});
        u = new StateVector(new Vector[]{vector_u1, vector_u2});
        v = new StateVector(new Vector[]{vector_v1, vector_v2});
    }

    @AfterEach
    void tearDown() {
        this.u = null;
        this.v = null;
    }

    @Test
    void stateVector() {
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
    void getStateVector() {
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
    void getVector() {
        double[] expected = {-1, -2, -3};
        Vector output = u.getVector(1);
        for (int i = 0; i < output.getDimension(); i++) {
            assertEquals(expected[i], output.get(i));
        }
    }

    @Test
    void getNumberOfVectors() {
        int output = u.getNumberOfVectors();
        assertEquals(2, output);
    }

    @Test
    void getNumberOfDimensions() {
        int output = u.getNumberOfDimensions();
        assertEquals(3, output);
    }

    @Test
    void add() {
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
    void subtract() {
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
    void multiply() {
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
}
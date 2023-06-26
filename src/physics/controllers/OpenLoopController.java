package physics.controllers;

import physics.landing.OpenLoopLanding;
import physics.vectors.StateVector;
import physics.vectors.Vector;
import stochastic_wind_simulation.WindModel;

import java.util.ArrayList;
import java.util.List;

public class OpenLoopController{

    private StateVector initialState;
    private Vector landingPosition;

    private final double g = 1.352;
    private final double umax = 10 * g;
    private double t0;
    private double tf;
    private double h;
    private List<Vector>thrusts = new ArrayList<>();
    private List<Vector>torques = new ArrayList<>();
    private List<Vector>thrustsWithoutWind = new ArrayList<>();
    private List<Vector> windThrusts = new ArrayList<>();
    private List<StateVector> allStates = new ArrayList<>();
    OpenLoopLanding landing = new OpenLoopLanding(h);
    public OpenLoopController(StateVector initialState, Vector landingPosition, double t0, double tf, double h){
        this.initialState = initialState;
        this.landingPosition = landingPosition;
        this.t0 = t0;
        this.tf = tf;
        this.h = h;
    }

    /**
     *
     * @param initialState initial position and velocity of the probe: x, y, and theta
     * @param landingPosition two-dimensional vector representing the landing position
     */
    public void OpenLoop(StateVector initialState, Vector landingPosition, double t0, double tf, double h) {
        this.initialState = initialState;
        this.landingPosition = landingPosition;
        this.t0 = t0;
        this.tf = tf;
        this.h = h;
    }

    public void simulateLanding(){
        stabiliseLandingModule();
        rotateModuleHorizontally();
        approachXCoordinate();
        rotateModuleVertically();
        descendToLandingPoint();
        stabiliseWind();
        simulateWind();
    }

    /**
     * Stabilises the landing module at the initial conditions so that a clear path to the landing point
     * can be made without the landing module moving further away/closer to the spot in the process.
     */
    public void stabiliseLandingModule(){
        Vector stabilisingVelocity = initialState.getVector(1).multiply(-1);
        double stabilisingVelocityMagnitude = stabilisingVelocity.getMagnitude();
        if(stabilisingVelocityMagnitude < umax){
            thrusts.add(stabilisingVelocity);
        }
        else{
            double timeToStabilise = stabilisingVelocityMagnitude / umax;
            int roundStabilisingTime = (int)Math.round(timeToStabilise);
            double residual = timeToStabilise - roundStabilisingTime;
            if(residual == 0){
                Vector stableUnitVector = stabilisingVelocity.multiply(1/stabilisingVelocityMagnitude);
                for(int i = 0; i < roundStabilisingTime; i++){
                    Vector thrust = stableUnitVector.multiply(umax);
                    thrusts.add(thrust);
                }
            }
            else{
                Vector stableUnitVector = stabilisingVelocity.multiply(1/stabilisingVelocityMagnitude);
                roundStabilisingTime = (int)Math.floor(timeToStabilise);
                for(int i = 0; i < roundStabilisingTime; i++){
                    Vector thrust = stableUnitVector.multiply(umax);
                    thrusts.add(thrust);
                }
                double partialThrust = residual*umax;
                Vector residualThrust = stableUnitVector.multiply(partialThrust);
                thrusts.add(residualThrust);
            }
        }
    }

    /**
     * Rotates the landing module so that there is only movement in the x-direction
     *  theta = 90º
     */
    public void rotateModuleHorizontally(){
        //Parallel to the x-axis
        //velocity in the y direction should be zero
        double goalTheta = 0.5 * Math.PI;
        double currentOrientation = initialState.getVector(0).get(2);

        //theta = 2pi
        Vector goalOrientation;
    }

    public void approachXCoordinate(){
        double landerXCoordinate = initialState.getVector(0).get(0);
        double landingXCoordinate = landingPosition.get(0);
        List<Vector> thrusts = OpenLoopLanding.moveAcrossXAxis(landerXCoordinate,landingXCoordinate,this.tf,this.t0,this.h);
        addThrusts(thrusts);
    }

    /**
     * Rotates the landing module so that there is only movement in the y-direction (downward)
     */
    public void rotateModuleVertically(){
        ///velocity in the y direction should be equal to zero
        //Perpendicular to the x-axis
        //From earlier rotation horizontally, it can be assumed that yVelocity = 0
        double goalTheta = Math.PI;
        Vector goalOrientation;
    }

    /**
     * Simulates probe descent in the y-direction given initial y conditions and landing y position
     */
    public void descendToLandingPoint(){
        double landerYCoordinate = initialState.getVector(0).get(1);
        double landingYCoordinate = landingPosition.get(1);
        List<Vector> thrusts = OpenLoopLanding.moveAcrossYAxis(landerYCoordinate,landingYCoordinate,this.tf,this.t0,this.h);
        addThrusts(thrusts);
    }

    /**
     * Provides a counter-acceleration to counter the velocity from any wind
     */
    public void stabiliseWind(){
        //calculate acceleration and add it to thrusts
        double maxHeight = initialState.getVector(0).get(1);

        if(maxHeight <= 7000){
            double estimatedWindSpeed = (0.0001) * (0.3 - 0.0);
            double acceleration = -(estimatedWindSpeed)/h;
            List<Vector> windThrusts = new ArrayList<>();
            Vector wind = new Vector(new double[]{acceleration,0,0});
            windThrusts.add(wind);
            for(int i = 1; i < thrusts.size(); i++){
                windThrusts.add(new Vector(new double[]{0,0,0}));
            }
            this.thrusts = correctForWind(windThrusts);
            this.allStates = applyThrust(initialState);

        }
        else if(maxHeight <= 60000){
            double estimatedWindSpeed = (0.0001) * (0.5 - 0.0);
            double acceleration = -(estimatedWindSpeed)/h;
            List<Vector> windThrusts = new ArrayList<>();
            Vector wind = new Vector(new double[]{acceleration,0,0});
            windThrusts.add(wind);
            for(int i = 1; i < thrusts.size(); i++){
                windThrusts.add(new Vector(new double[]{0,0,0}));
            }
            this.thrusts = correctForWind(windThrusts);
            this.allStates = applyThrust(initialState);
        }
        else if(maxHeight <= 120000){
            double estimatedWindSpeed = (0.0001) * (5.0 - 0.8);
            double acceleration = -(estimatedWindSpeed)/h;
            List<Vector> windThrusts = new ArrayList<>();
            Vector wind = new Vector(new double[]{acceleration,0,0});
            windThrusts.add(wind);
            for(int i = 1; i < thrusts.size(); i++){
                windThrusts.add(new Vector(new double[]{0,0,0}));
            }
            this.thrusts = correctForWind(windThrusts);
            this.allStates = applyThrust(initialState);
        }
        else{
            double estimatedWindSpeed = 0;
            double acceleration = estimatedWindSpeed;
            List<Vector> windThrusts = new ArrayList<>();
            Vector wind = new Vector(new double[]{acceleration,0,0});
            windThrusts.add(wind);
            for(int i = 1; i < thrusts.size(); i++){
                windThrusts.add(new Vector(new double[]{0,0,0}));
            }
            this.thrusts = correctForWind(windThrusts);
            this.allStates = applyThrust(initialState);
        }
    }

    /**
     * Simulates the effect of wind on the landing module
     */
    public void simulateWind(){
        WindModel windModel = new WindModel();
        double previousWind = 0;
        for(int i = 0; i < allStates.size(); i++){
            double position = allStates.get(i).getVector(0).get(0);
            double velocity = allStates.get(i).getVector(1).get(0);
            position += previousWind;
            previousWind = windModel.getWindSpeed(allStates.get(i).getVector(0).get(1)).get(0);
            velocity+= previousWind;
            allStates.get(i).getVector(0).set(0,position);
            allStates.get(i).getVector(1).set(0,velocity);
        }
    }

    /**
     * Adds all individual thrusts from a list of thrusts to the list of thrusts in the controller
     * @param thrusts list of thrusts to be added
     */
    public void addThrusts(List<Vector> thrusts){
        for(int i =0; i < thrusts.size(); i++){
            this.thrusts.add(thrusts.get(i));
        }
    }

    /**
     * Applies all thrusts for the period to an initial state vector
     * @param initialState state vector at time t0 (initial position & velocity)
     * @return list of all state vectors for the period after thrust is applied
     */
    public List <StateVector> applyThrust(StateVector initialState){
        Vector initialPosition = initialState.getVector(0);
        Vector initialVelocity = initialState.getVector(1);
        List <StateVector> allStates = new ArrayList<>();

        for(int i = 0; i < thrusts.size(); i++){
            initialPosition = initialPosition.add(initialVelocity.multiply(h));
            initialVelocity = initialVelocity.add(thrusts.get(i).multiply(h));
            allStates.add(new StateVector(new Vector[]{initialPosition,initialVelocity}));
        }
        return allStates;
    }

    /**
     * Adjusts the thrusts to account for wind
     * @param windThrusts list containing all thrusts corresponding to wind (or the thrust against wind)
     * @return list containing the updated thrust values that include wind
     */
    public List<Vector> correctForWind(List<Vector>windThrusts){
        List<Vector> preWind = this.thrusts;
        List<Vector> postWind = new ArrayList<>();
        for(int i = 0; i < windThrusts.size(); i++){
            Vector applyWind = preWind.get(i).add(windThrusts.get(i));
            postWind.add(applyWind);
        }
        return postWind;
    }

    /**
     * Gets the path of the landing module from its initial position to the landing position
     * @return double array containing the path of the landing module
     *         [0] = x coordinate
     *         [1] = y coordinate
     */
    public double [][] getPath(){
        simulateLanding();
        //List<StateVector> list = applyThrust(this.initialState);
        List<StateVector> list = allStates;
        double[][] path = new double[list.size()][2];
        for(int i = 0; i < list.size(); i ++){
            path[i][0] = list.get(i).getVector(0).get(0);
            path[i][1] = list.get(i).getVector(0).get(1);
        }
        return path;
    }

    /**
     * Gets all states of the probe over the course of the trip
     * @return all states as a list
     */
    public List<StateVector> getAllStates() {
        return allStates;
    }

    public static void main(String[] args) {
        StateVector s = new StateVector(new Vector[]{new Vector(new double[]{9999, 444,0}), new Vector(new double[]{0,0,0})});
        Vector landingCoordinates = new Vector(new double[]{0,0});
        OpenLoopController controller = new OpenLoopController(s, landingCoordinates,0,100,1);
        controller.simulateLanding();
        List <StateVector> states = controller.getAllStates();
        for(int i = 0; i < states.size(); i++){
            System.out.println(states.get(i).toString());
        }
    }
}

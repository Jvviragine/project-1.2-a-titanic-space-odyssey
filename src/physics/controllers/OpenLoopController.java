package physics.controllers;

import physics.landing.OpenLoopLanding;
import physics.vectors.StateVector;
import physics.vectors.Vector;
import stochastic_wind_simulation.WindModel;

import java.util.ArrayList;
import java.util.List;

public class OpenLoopController implements Controller{

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
    //Starting point for each controller
    @Override
    public void startControl() {

    }
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
        //simulateWind();
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

    public void rotateModuleHorizontally(){
        //Parallel to the x-axis
        Vector goalOrientation;
    }

    public void approachXCoordinate(){
        double landerXCoordinate = initialState.getVector(0).get(0);
        double landingXCoordinate = landingPosition.get(0);
        List<Vector> thrusts = OpenLoopLanding.moveAcrossXAxis(landerXCoordinate,landingXCoordinate,this.tf,this.t0,this.h);
        addThrusts(thrusts);
    }

    public void rotateModuleVertically(){
        //Perpendicular to the x-axis
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

    //public List <StateVector>

//    public List <StateVector> applyThrust(StateVector initialState,List<Vector> wind){
//        System.out.println("APPLIED");
//        Vector initialPosition = initialState.getVector(0);
//        Vector initialVelocity = initialState.getVector(1);
//        List <StateVector> allStates = new ArrayList<>();
//        System.out.println("Initial vel:" +initialVelocity.toString());
//
//        for(int i = 0; i < windThrusts.size(); i++){
//            Vector currentThrust = this.windThrusts.get(i);
//            System.out.println("Velocity 1:" +currentThrust.toString());
//            Vector newThrust = currentThrust.add(wind.get(i));
//            System.out.println("Velocity 2:" +newThrust.toString());
//            Vector resultantThrust = currentThrust.add(newThrust);
//            System.out.println("Velocity 3:" +resultantThrust.toString());
//            windThrusts.set(i,resultantThrust);
//        }
//        this.thrusts = correctForWind(windThrusts);
//        this.allStates = applyThrust(initialState);
//
//        return allStates;
//    }

    public List <StateVector> applyThrust(StateVector initialState,List<Vector> wind){
        System.out.println("APPLIED");
        Vector initialPosition = initialState.getVector(0);
        Vector initialVelocity = initialState.getVector(1);
        List <StateVector> allStates = new ArrayList<>();
        System.out.println("Initial vel:" +initialVelocity.toString());
        Vector previousInitialVelocity = initialVelocity;

        for(int i =0; i < thrusts.size()-1; i++){
            initialPosition = initialPosition.add(initialVelocity.add(wind.get(i)));
            initialVelocity = initialVelocity.add((thrusts.get(i).multiply(h)).add(wind.get(i)));
            allStates.add(new StateVector(new Vector[]{initialPosition,initialVelocity}));
            initialVelocity = previousInitialVelocity;
        }

        return allStates;
    }

    public void stabiliseWind(){
        //calculate acceleration and add it to thrusts
        double maxHeight = initialState.getVector(0).get(1);

        if(maxHeight <= 7000){
            double estimatedWindSpeed = (0.5) * (0.3 - 0.0);
            double acceleration = -(estimatedWindSpeed)/h;
            System.out.println("Wind acceleration:"+ acceleration);
            List<Vector> windThrusts = new ArrayList<>();
            Vector wind = new Vector(new double[]{acceleration,0,0});
            windThrusts.add(wind);
            for(int i = 1; i < thrusts.size(); i++){
                windThrusts.add(new Vector(new double[]{0,0,0}));
            }
            //this.windThrusts = windThrusts;
            this.thrusts = correctForWind(windThrusts);
            this.allStates = applyThrust(initialState);
            //thrustsWithoutWind = correctForWind(windThrusts);

        }
        else if(maxHeight <= 60000){
            double estimatedWindSpeed = (0.5) * (0.5 - 0.0);
            double acceleration = -(estimatedWindSpeed)/h;
            System.out.println("Wind acceleration:"+ acceleration);
            List<Vector> windThrusts = new ArrayList<>();
            Vector wind = new Vector(new double[]{acceleration,0,0});
            windThrusts.add(wind);
            for(int i = 1; i < thrusts.size(); i++){
                windThrusts.add(new Vector(new double[]{0,0,0}));
            }
            //this.windThrusts = windThrusts;
            this.thrusts = correctForWind(windThrusts);
            this.allStates = applyThrust(initialState);
            //thrustsWithoutWind = correctForWind(windThrusts);
        }
        else if(maxHeight <= 120000){
            double estimatedWindSpeed = (0.5) * (5.0 - 0.8);
            double acceleration = -(estimatedWindSpeed)/h;
            System.out.println("Wind acceleration:"+ acceleration);
            List<Vector> windThrusts = new ArrayList<>();
            Vector wind = new Vector(new double[]{acceleration,0,0});
            windThrusts.add(wind);
            for(int i = 1; i < thrusts.size(); i++){
                windThrusts.add(new Vector(new double[]{0,0,0}));
            }
            //this.windThrusts = windThrusts;
            this.thrusts = correctForWind(windThrusts);
            this.allStates = applyThrust(initialState);
            //thrustsWithoutWind = correctForWind(windThrusts);
        }
    }


    //need to remove previous velocity and then reset to this new velocity
    public void simulateWind(){
        WindModel windModel = new WindModel();
        List<StateVector> allStates = applyThrust(this.initialState);
        List<Vector> windThrusts = new ArrayList<>();
        double height = allStates.get(0).getVector(0).get(1);
        double velocity = windModel.getWindSpeed(height).get(0)/h;
        Vector wind = new Vector(new double[]{velocity,0,0});
        wind = windThrusts.get(0).add(wind);
        windThrusts.add(wind);
        System.out.println("All States size:"+allStates.size());


        for(int i = 1; i < allStates.size(); i++){
            System.out.println("Yep");
            height = allStates.get(i).getVector(0).get(1);
            //double currentVelocity = allStates.get(i).getVector(1).get(0);
            velocity = windModel.getWindSpeed(height).get(0)/h;
            System.out.println("Velocity of wind:" + velocity);
            //double difference = velocity - currentVelocity;
//            if(velocity > currentVelocity){
            //velocity = -difference;
            wind = new Vector(new double[]{velocity,0,0});
            wind = windThrusts.get(i).add(wind);
            windThrusts.add(wind);
//            }
//            else{
//                velocity = difference;
//                wind = new Vector(new double[]{velocity,0,0});
//                windThrusts.add(wind);
//            }
        }
        //this.thrusts = correctForWind(windThrusts);
        this.allStates = applyThrust(initialState,windThrusts);
    }

//    public List<StateVector> simulateWind(){
//        List<StateVector> allWind = new ArrayList<>();
//        WindModel wind = new WindModel();
//        List<StateVector> allStates = applyThrust(this.initialState);
//        for(int i = 0; i < allStates.size(); i++){
//            StateVector currentState = allStates.get(i);
//            double height = currentState.getVector(0).get(1);
//            Vector windAtHeight = wind.getWindSpeed(height);
//            double windVelocityX = windAtHeight.get(0) + currentState.getVector(1).get(0);
//            double windPositionX = windAtHeight.get(1) + currentState.getVector(0).get(0);
//            Vector newPosition = new Vector(new double[]{windPositionX,currentState.getVector(0).get(1),currentState.getVector(0).get(2)});
//            Vector newVelocity = new Vector(new double[]{windVelocityX,currentState.getVector(1).get(1),currentState.getVector(1).get(2)});
//            StateVector newState = new StateVector(new Vector[]{newPosition,newVelocity});
//            allStates.set(i,newState);
//            //StateVector newState = new StateVector(new Vector[]{newPostion,newVelocity});
//            //allStates.set(i,newState);
//        }
////        Vector initialPosition = allStates.get(0).getVector(0);
////        Vector initialVelocity = allStates.get(1).getVector(1);
////        for(int i = 0; i < allStates.size(); i++){
////            initialPosition = initialPosition.add(initialVelocity.multiply(h));
////            initialVelocity = initialVelocity.add(thrusts.get(i).multiply(h));
////            allStates.add(new StateVector(new Vector[]{initialPosition,initialVelocity}));
////        }
////        WindModel wind = new WindModel();
////        List<StateVector> allStates = applyThrust(this.initialState);
////        for(int i = 0; i < thrusts.size(); i++){
////            double height = allStates.get(i).getVector(0).get(1);
////            double windX = wind.getWindSpeed(height).get(0);
////            System.out.println("WInd:" + windX);
////            Vector thrustFromWind = new Vector(new double[]{windX,0,0});
////            Vector currentThrust = this.thrusts.get(i);
////            this.thrusts.set(i, currentThrust.add(thrustFromWind));
////        }
////        allStates = applyThrust(this.initialState);
////        List <Vector> windAtStep = new ArrayList<>();
////
////        Vector initialPosition = allStates.get(0).getVector(0);
////        Vector initialVelocity = allStates.get(0).getVector(1);
////
////        for(int i = 1; i < thrusts.size(); i++){
////            StateVector currentState = allStates.get(i);
////            //Vector initialPosition = currentState.getVector(0);
////            //Vector initialVelocity = currentState.getVector(1);
////            double height = initialPosition.get(1);
////            double windX = wind.getWindSpeed(height).get(0);
////            System.out.println("WInd:" + windX);
////
////            Vector thrustFromWind = new Vector(new double[]{windX,0,0});
////            Vector newPosition = currentState.getVector(0).add(allStates.get(i-1).getVector(1));
////            Vector newVelocity = currentState.getVector(1).add(thrustFromWind);
////            currentState = new StateVector(new Vector[]{newPosition,newVelocity});
////            allStates.set(i,currentState);
////        }
//        this.allStates = allStates;
//        return allStates;
//    }



    public List<Vector> correctForWind(List<Vector>windThrusts){
        List<Vector> preWind = this.thrusts;
        List<Vector> postWind = new ArrayList<>();
        for(int i = 0; i < windThrusts.size(); i++){
            Vector applyWind = preWind.get(i).add(windThrusts.get(i));
            postWind.add(applyWind);
        }
        return postWind;
    }

    public List<Vector> getThrusts(){
        return thrusts;
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

    public List<StateVector> getAllStates() {
        return allStates;
    }

    public static void main(String[] args) {
        StateVector s = new StateVector(new Vector[]{new Vector(new double[]{6000, 999,0}), new Vector(new double[]{0,0,0})});
        Vector landingCoordinates = new Vector(new double[]{0,0});
        OpenLoopController controller = new OpenLoopController(s, landingCoordinates,0,100,1);
        controller.simulateLanding();
        //List <StateVector> states = controller.applyThrust(s);
        //controller.simulateLanding();
        List <StateVector> states = controller.getAllStates();
        for(int i = 0; i < states.size(); i++){
            System.out.println(states.get(i).toString());

        }
//        List<Vector> thrusts = controller.getThrusts();
//        for(int i = 0; i < thrusts.size(); i++){
//            System.out.println(thrusts.get(i));
//
//        }
    }

}

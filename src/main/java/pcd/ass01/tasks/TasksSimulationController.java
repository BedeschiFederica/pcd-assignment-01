package pcd.ass01.tasks;

import pcd.ass01.utility.BoidsModel;
import pcd.ass01.utility.BoidsView;
import pcd.ass01.utility.SimulationController;
import pcd.ass01.utility.SynchBoid;

import java.util.List;

public class TasksSimulationController implements SimulationController {

    private static final double SEPARATION_WEIGHT = 1.0;
    private static final double ALIGNMENT_WEIGHT = 1.0;
    private static final double COHESION_WEIGHT = 1.0;

    private static final int ENVIRONMENT_WIDTH = 1000;
    private static final int ENVIRONMENT_HEIGHT = 1000;
    private static final double MAX_SPEED = 4.0;
    private static final double PERCEPTION_RADIUS = 50.0;
    private static final double AVOID_RADIUS = 20.0;

    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 800;

    private BoidsModel model;
    private final BoidsView view;
    private MasterWorker masterWorker;
    /*private final Flag stopFlag;
    private final SuspendMonitor suspendMonitor;
    private ExecutorService exec;*/

    public TasksSimulationController() {
        /*this.suspendMonitor = new SuspendMonitor();
        this.stopFlag = new Flag();*/
        this.view = new BoidsView(this, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public void startSimulation(final int nBoids) {
        this.model = new BoidsModel(
                nBoids,
                SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT,
                ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
                MAX_SPEED,
                PERCEPTION_RADIUS,
                AVOID_RADIUS);
        this.masterWorker = new MasterWorker(this.model, this.view);
        this.masterWorker.start();
    }

    public void stopSimulation() {
        //this.stopFlag.set();
    }

    public void suspendSimulation() {
        //this.suspendMonitor.setSuspend();
    }

    public void resumeSimulation() {
        //this.suspendMonitor.resume();
    }

    public double getWidth() {
        return this.model.getWidth();
    }

    public List<SynchBoid> getBoids() {
        return this.model.getBoids();
    }

    public void setSeparationWeight(final double value) {
        this.model.setSeparationWeight(value);
    }

    public void setCohesionWeight(final double value) {
        this.model.setCohesionWeight(value);
    }

    public void setAlignmentWeight(final double value) {
        this.model.setAlignmentWeight(value);
    }
}

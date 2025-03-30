package pcd.ass01.multithreaded;

import pcd.ass01.utility.*;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSimulationController implements SimulationController {

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
    private final Flag stopFlag;
    private final SuspendMonitor suspendMonitor;

    public MultiThreadedSimulationController() {
        this.suspendMonitor = new SuspendMonitor();
        this.stopFlag = new Flag();
        this.view = new BoidsView(this, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    @Override
    public void startSimulation(final int nBoids) {
        this.model = new BoidsModel(
                nBoids,
                SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT,
                ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
                MAX_SPEED,
                PERCEPTION_RADIUS,
                AVOID_RADIUS);
        List<ComputeWorker> workers = new ArrayList<>();
        final int nWorkers = Runtime.getRuntime().availableProcessors();
        final int size = nBoids / nWorkers;
        Barrier barrierVel = new BarrierImpl(nWorkers + 1);
        Barrier barrierPos = new BarrierImpl(nWorkers + 1);
        this.stopFlag.reset();
        this.suspendMonitor.resume();
        GUIWorker guiWorker = new GUIWorker(this.view, barrierVel, barrierPos, this.stopFlag, this.suspendMonitor);
        for (int i = 0; i < nWorkers; i++) {
            final int start = i * size;
            final int end = (i != nWorkers - 1) ? (i + 1) * size : nBoids;
            workers.add(new ComputeWorker(this.model, barrierVel, barrierPos, start, end,
                    this.stopFlag, this.suspendMonitor));
        }
        for (final ComputeWorker w: workers) {
            w.start();
        }
        guiWorker.start();
    }

    @Override
    public void stopSimulation() {
        this.stopFlag.set();
    }

    @Override
    public void suspendSimulation() {
        this.suspendMonitor.setSuspend();
    }

    @Override
    public void resumeSimulation() {
        this.suspendMonitor.resume();
    }

    @Override
    public double getWidth() {
        return this.model.getWidth();
    }

    @Override
    public List<SynchBoid> getBoids() {
        return this.model.getBoids();
    }

    @Override
    public void setSeparationWeight(final double value) {
        this.model.setSeparationWeight(value);
    }

    @Override
    public void setCohesionWeight(final double value) {
        this.model.setCohesionWeight(value);
    }

    @Override
    public void setAlignmentWeight(final double value) {
        this.model.setAlignmentWeight(value);
    }
}

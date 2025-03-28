package pcd.ass01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class SimulationController {

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

    BoidsModel model;
    final BoidsView view;
    List<ComputeWorker> workers;
    //CyclicBarrier barrierVel;
    //CyclicBarrier barrierPos;
    Barrier barrierVel;
    Barrier barrierPos;
    GUIWorker guiWorker;

    public SimulationController() {
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
        this.workers = new ArrayList<>();
        final int nWorkers = Runtime.getRuntime().availableProcessors();
        final int size = nBoids / nWorkers;
        //this.barrierVel = new CyclicBarrier(nWorkers + 1);
        //this.barrierPos = new CyclicBarrier(nWorkers + 1);
        this.barrierVel = new BarrierImpl(nWorkers + 1);
        this.barrierPos = new BarrierImpl(nWorkers + 1);
        this.guiWorker = new GUIWorker(this.view, this.barrierVel, this.barrierPos);
        for (int i = 0; i < nWorkers; i++) {
            final int start = i * size;
            final int end = (i != nWorkers - 1) ? (i + 1) * size : nBoids;
            this.workers.add(new ComputeWorker(this.model, this.barrierVel, this.barrierPos, start, end));
        }
        for (final ComputeWorker w: this.workers) {
            w.start();
        }
        this.guiWorker.start();
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

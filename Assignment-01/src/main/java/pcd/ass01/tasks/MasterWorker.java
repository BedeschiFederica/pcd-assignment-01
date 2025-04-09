package pcd.ass01.tasks;

import pcd.ass01.utility.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MasterWorker extends Thread {

    private final BoidsModel model;
    private final BoidsView view;
    private final ExecutorService exec;
    private final Flag stopFlag;
    private final SuspendMonitor suspendMonitor;
    private long t0;

    public MasterWorker(final BoidsModel model, final BoidsView view, final Flag stopFlag,
                        final SuspendMonitor suspendMonitor, final int nThreads) {
        super("master-worker");
        this.model = model;
        this.view = view;
        this.stopFlag = stopFlag;
        this.suspendMonitor = suspendMonitor;
        this.exec = Executors.newFixedThreadPool(nThreads);
    }

    public void update() {
        try {
            final Future<Long> futureT0 = this.exec.submit(new UpdateGUITask(this.view, this.t0));
            final List<Future<Void>> futureVel = new ArrayList<>();
            for (final SynchBoid boid: this.model.getBoids()){
                final Future<Void> vel = this.exec.submit(new UpdateVelocityTask(this.model, boid));
                futureVel.add(vel);
            }
            this.t0 = futureT0.get();
            for (final Future<Void> vel: futureVel) {
                vel.get();
            }

            final List<Future<Void>> futurePos = new ArrayList<>();
            for (final SynchBoid boid: this.model.getBoids()){
                final Future<Void> pos = this.exec.submit(new UpdatePositionTask(this.model, boid));
                futurePos.add(pos);
            }
            for (final Future<Void> pos: futurePos) {
                pos.get();
            }
        } catch (final InterruptedException | ExecutionException ex){
            throw new RuntimeException();
        }
    }

    private void stopExecutor() {
        try {
            this.exec.shutdown();
            this.exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (!this.stopFlag.isSet()) {
            this.suspendMonitor.suspendIfRequested();
            this.update();
        }
        this.stopExecutor();
    }

}

package pcd.ass01.tasks;

import pcd.ass01.utility.*;

import java.util.concurrent.*;

public class MasterWorker extends Thread {

    private final BoidsModel model;
    private final BoidsView view;
    private ExecutorService exec;
    private final Flag stopFlag;
    private final SuspendMonitor suspendMonitor;
    private final int nThreads;
    private long t0;

    public MasterWorker(final BoidsModel model, final BoidsView view, final Flag stopFlag,
                        final SuspendMonitor suspendMonitor, final int nThreads) {
        super("master-worker");
        this.model = model;
        this.view = view;
        this.stopFlag = stopFlag;
        this.suspendMonitor = suspendMonitor;
        this.nThreads = nThreads;
    }

    public void updateViewAndVelocity() {
        this.exec = Executors.newFixedThreadPool(this.nThreads);
        try {
            final Future<Long> futureT0 = this.exec.submit(new UpdateGUITask(this.view, this.t0));
            for (final SynchBoid boid: this.model.getBoids()){
                this.exec.execute(new UpdateVelocityTask(this.model, boid));
            }
            this.t0 = futureT0.get();
            this.exec.shutdown();
            this.exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (final InterruptedException | ExecutionException ex){
            throw new RuntimeException();
        }
    }

    public void updatePos() {
        this.exec = Executors.newFixedThreadPool(this.nThreads);
        try {
            for (final SynchBoid boid: this.model.getBoids()){
                this.exec.execute(new UpdatePositionTask(this.model, boid));
            }
            this.exec.shutdown();
            this.exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (final InterruptedException ex){
            throw new RuntimeException();
        }
    }

    @Override
    public void run() {
        while (!this.stopFlag.isSet()) {
            this.suspendMonitor.suspendIfRequested();
            this.updateViewAndVelocity();
            this.updatePos();
        }
    }

}

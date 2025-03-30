package pcd.ass01.tasks;

import pcd.ass01.utility.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MasterWorker extends Thread {

    private final BoidsModel model;
    private final BoidsView view;
    private ExecutorService exec;
    private final Flag stopFlag;
    private final SuspendMonitor suspendMonitor;

    public MasterWorker(final BoidsModel model, final BoidsView view, final Flag stopFlag, final SuspendMonitor suspendMonitor) {
        super("master-worker");
        this.model = model;
        this.view = view;
        this.stopFlag = stopFlag;
        this.suspendMonitor = suspendMonitor;
    }

    public void updateVelocity() {
        this.exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        try {
            for (final SynchBoid boid: this.model.getBoids()){
                this.exec.execute(new UpdateVelocityTask(this.model, boid));
            }
            this.exec.shutdown();
            this.exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (final InterruptedException ex){
            throw new RuntimeException();
        }
    }

    public void updatePos() {
        this.exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
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

    public void updateGUI(final long t0) {
        this.exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        try {
            this.exec.execute(new UpdateGUITask(this.view, t0));
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
            final long t0 = System.currentTimeMillis();
            this.updateVelocity();
            this.updatePos();
            this.updateGUI(t0);
        }
    }

}

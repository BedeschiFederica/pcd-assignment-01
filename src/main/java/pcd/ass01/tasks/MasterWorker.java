package pcd.ass01.tasks;

import pcd.ass01.utility.BoidsModel;
import pcd.ass01.utility.BoidsView;
import pcd.ass01.utility.SynchBoid;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MasterWorker extends Thread {

    private final BoidsModel model;
    private final BoidsView view;
    private ExecutorService exec;

    public MasterWorker(final BoidsModel model, final BoidsView view) {
        super("master-worker");
        this.model = model;
        this.view = view;
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
        while (true) {
            final long t0 = System.currentTimeMillis();
            this.updateVelocity();
            this.updatePos();
            this.updateGUI(t0);
        }
    }

}

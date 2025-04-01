package pcd.ass01.tasks;

import pcd.ass01.utility.BoidsModel;
import pcd.ass01.utility.SynchBoid;

import java.util.concurrent.Callable;

public abstract class AbstractUpdateTask implements Callable<Void> {

    protected final BoidsModel model;
    protected final SynchBoid boid;

    public AbstractUpdateTask(final BoidsModel model, final SynchBoid boid) {
        this.model = model;
        this.boid = boid;
    }
}

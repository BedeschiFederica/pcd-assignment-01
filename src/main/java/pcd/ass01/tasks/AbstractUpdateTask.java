package pcd.ass01.tasks;

import pcd.ass01.utility.BoidsModel;
import pcd.ass01.utility.SynchBoid;

public abstract class AbstractUpdateTask implements Runnable {

    protected final BoidsModel model;
    protected final SynchBoid boid;

    public AbstractUpdateTask(final BoidsModel model, final SynchBoid boid) {
        this.model = model;
        this.boid = boid;
    }

}

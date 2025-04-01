package pcd.ass01.tasks;

import pcd.ass01.utility.BoidsModel;
import pcd.ass01.utility.SynchBoid;

public class UpdateVelocityTask extends AbstractUpdateTask {

    public UpdateVelocityTask(final BoidsModel model, final SynchBoid boid) {
        super(model, boid);
    }

    @Override
    public Void call() {
        this.boid.updateVelocity(this.model);
        return null;
    }
}

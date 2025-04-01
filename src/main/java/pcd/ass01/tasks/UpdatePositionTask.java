package pcd.ass01.tasks;

import pcd.ass01.utility.BoidsModel;
import pcd.ass01.utility.SynchBoid;

public class UpdatePositionTask extends AbstractUpdateTask {

    public UpdatePositionTask(final BoidsModel model, final SynchBoid boid) {
        super(model, boid);
    }

    @Override
    public Void call() {
        this.boid.updatePos(this.model);
        return null;
    }
}

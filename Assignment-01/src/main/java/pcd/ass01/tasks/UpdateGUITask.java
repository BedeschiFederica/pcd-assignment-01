package pcd.ass01.tasks;

import pcd.ass01.utility.BoidsView;

import java.util.concurrent.Callable;

public class UpdateGUITask implements Callable<Long> {

    private final BoidsView view;
    private final long t0;

    private static final int FRAME_RATE = 50;

    public UpdateGUITask(final BoidsView view, final long t0) {
        this.view = view;
        this.t0 = t0;
    }

    @Override
    public Long call() {
        final long t1 = System.currentTimeMillis();
        final long dtElapsed = t1 - this.t0;
        final int frameRatePeriod = 1000 / FRAME_RATE;
        int frameRate;
        if (dtElapsed < frameRatePeriod) {
            try {
                Thread.sleep(frameRatePeriod - dtElapsed);
            } catch (final Exception ex) {
            }
            frameRate = FRAME_RATE;
        } else {
            frameRate = (int) (1000 / dtElapsed);
        }
        this.view.update(frameRate);
        return System.currentTimeMillis();
    }
}

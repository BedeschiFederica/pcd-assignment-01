package pcd.ass01.tasks;

import pcd.ass01.utility.BoidsView;

public class UpdateGUITask implements Runnable {

    private final BoidsView view;
    private final long t0;

    private static final int FRAMERATE = 1000;

    public UpdateGUITask(final BoidsView view, final long t0) {
        this.view = view;
        this.t0 = t0;
    }

    @Override
    public void run() {
        final long t1 = System.currentTimeMillis();
        final long dtElapsed = t1 - this.t0;
        final int frameRatePeriod = 1000 / FRAMERATE;
        int frameRate;
        if (dtElapsed < frameRatePeriod) {
            try {
                Thread.sleep(frameRatePeriod - dtElapsed);
            } catch (final Exception ex) {
            }
            frameRate = FRAMERATE;
        } else {
            frameRate = (int) (1000 / dtElapsed);
        }
        this.view.update(frameRate);
    }
}

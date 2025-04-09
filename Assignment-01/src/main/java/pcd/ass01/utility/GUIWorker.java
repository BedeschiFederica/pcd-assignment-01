package pcd.ass01.utility;

public class GUIWorker implements Runnable {

    private final Barrier barrierVel;
    private final Barrier barrierPos;
    private final BoidsView view;
    private final Flag stopFlag;
    private final SuspendMonitor suspendMonitor;

    private static final int FRAME_RATE = 50;
    private long t0;

    public GUIWorker(final BoidsView view, final Barrier barrierVel, final Barrier barrierPos, final Flag stopFlag,
                     final SuspendMonitor suspendMonitor) {
        this.view = view;
        this.barrierVel = barrierVel;
        this.barrierPos = barrierPos;
        this.stopFlag = stopFlag;
        this.suspendMonitor = suspendMonitor;
    }

    public void run() {
        while (!this.stopFlag.isSet()) {
            this.suspendMonitor.suspendIfRequested();

            try {
                this.barrierVel.await();
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                this.barrierPos.await();
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }

            final long t1 = System.currentTimeMillis();
            final long dtElapsed = t1 - t0;
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
            this.t0 = System.currentTimeMillis();
        }
    }

    private void log(final String st){
        synchronized(System.out) {
            System.out.println("[GUIWorker ] " + st);
        }
    }

}

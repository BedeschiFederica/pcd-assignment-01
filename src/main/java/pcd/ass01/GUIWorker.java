package pcd.ass01;

public class GUIWorker extends Thread {

    private final Barrier barrierVel;
    private final Barrier barrierPos;
    private final BoidsView view;
    private final Flag stopFlag;
    private final SuspendMonitor suspendMonitor;

    private static final int FRAMERATE = 1000;
    private int frameRate;

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

            final long t0 = System.currentTimeMillis();

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

            this.view.update(this.frameRate);
            final long t1 = System.currentTimeMillis();
            final long dtElapsed = t1 - t0;
            final int frameRatePeriod = 1000 / FRAMERATE;
            if (dtElapsed < frameRatePeriod) {
                try {
                    Thread.sleep(frameRatePeriod - dtElapsed);
                } catch (final Exception ex) {
                }
                this.frameRate = FRAMERATE;
            } else {
                this.frameRate = (int) (1000 / dtElapsed);
            }
        }
    }

    private void log(final String st){
        synchronized(System.out) {
            System.out.println("[" + this.getName() + "] " + st);
        }
    }

}

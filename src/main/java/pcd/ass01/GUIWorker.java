package pcd.ass01;

import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class GUIWorker extends Thread {

    private final BoidsModel model;
    private final CyclicBarrier barrierVel;
    private final CyclicBarrier barrierPos;
    private Optional<BoidsView> view;

    private static final int FRAMERATE = 25;
    private int framerate;

    public GUIWorker(final BoidsModel model, final CyclicBarrier barrierVel, final CyclicBarrier barrierPos) {
        this.model = model;
        this.barrierVel = barrierVel;
        this.barrierPos = barrierPos;
    }

    public void attachView(BoidsView view) {
        this.view = Optional.of(view);
    }

    public void run() {
        while (true) {
            var t0 = System.currentTimeMillis();

            log("wait vel");
            try {
                this.barrierVel.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
            log("wait positions");
            try {
                this.barrierPos.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
            log("update GUI");

            if (view.isPresent()) {
                var t2 = System.currentTimeMillis();
                view.get().update(framerate);
                var t1 = System.currentTimeMillis();
                var dtElapsed = t1 - t0;
                var frameratePeriod = 1000 / FRAMERATE;
                log(dtElapsed + " " + frameratePeriod);
                log("AAA " + (t2 - t0) + " " + frameratePeriod);

                if (dtElapsed < frameratePeriod) {
                    try {
                        Thread.sleep(frameratePeriod - dtElapsed);
                    } catch (Exception ex) {
                    }
                    framerate = FRAMERATE;
                } else {
                    framerate = (int) (1000 / dtElapsed);
                }
            }
        }
    }

    private void log(String st){
        synchronized(System.out){
            System.out.println("["+this.getName()+"] "+st);
            //System.out.println("[GUI] "+st);
        }
    }

}

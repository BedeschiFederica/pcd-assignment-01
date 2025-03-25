package pcd.ass01;

import java.util.concurrent.BrokenBarrierException;

public class NearbyWorker implements Runnable {

    private final BoidsModel model;

    public NearbyWorker(final BoidsModel model) {
        this.model = model;
    }

    @Override
    public void run() {

        log("nearbyworker");
        /*log("wait vel");
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
        log("update GUI");*/

        for (SynchBoid boid : model.getBoids()) {
            for (SynchBoid other : model.getBoids()) {
                if (other != boid) {
                    P2d otherPos = other.getPos();
                    double distance = boid.getPos().distance(otherPos);
                    this.model.putDistance(new Pair<>(boid, other), distance);
                }
            }
        }
    }

    private void log(String st){
        synchronized(System.out){
            //System.out.println("["+this.getName()+"] "+st);
            System.out.println("[NearbyWorker] "+st);
        }
    }

}

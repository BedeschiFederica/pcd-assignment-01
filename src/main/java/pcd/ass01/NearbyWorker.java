package pcd.ass01;

import java.util.ArrayList;
import java.util.List;

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

        for (SynchBoid boid : this.model.getBoids()) {
            final List<SynchBoid> list = new ArrayList<>();
            for (SynchBoid other : this.model.getBoids()) {
                if (other != boid) {
                    P2d otherPos = other.getPos();
                    double distance = boid.getPos().distance(otherPos);
                    if (distance < this.model.getPerceptionRadius()) {
                        list.add(other);
                    }
                }
            }
            this.model.setNearBoids(boid, list);
        }
    }

    private void log(String st){
        synchronized(System.out){
            //System.out.println("["+this.getName()+"] "+st);
            System.out.println("[NearbyWorker] "+st);
        }
    }

}

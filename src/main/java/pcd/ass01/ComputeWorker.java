package pcd.ass01;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ComputeWorker extends Thread {

	private final BoidsModel model;
	//private final CyclicBarrier barrierVel;
	//private final CyclicBarrier barrierPos;
	private final Barrier barrierVel;
	private final Barrier barrierPos;
	private final int start;
	private final int end;

	public ComputeWorker(final BoidsModel model, final Barrier barrierVel, final Barrier barrierPos, //final CyclicBarrier barrierVel, final CyclicBarrier barrierPos,
						 final int start, final int end) {
		super("worker");
		this.model = model;
		this.barrierVel = barrierVel;
		this.barrierPos = barrierPos;
		this.start = start;
		this.end = end;
	}

	public void run() {
		while (true) {
			var boids = this.model.getPartitionedBoids(this.start, this.end);

			/*
			 * Improved correctness: first update velocities...
			 */
			for (final SynchBoid boid: boids) {
				boid.updateVelocity(this.model);
			}
			log("wait vel");
            try {
                this.barrierVel.await();
            } catch (final InterruptedException e) {// | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
			log("update pos");

            /*
			 * ...then update positions
			 */
			for (final SynchBoid boid: boids) {
				boid.updatePos(this.model);
			}

			log("wait pos");
			try {
				this.barrierPos.await();
			} catch (final InterruptedException e) { //| BrokenBarrierException e) {
				throw new RuntimeException(e);
			}
			log("update vel");
		}
	}

	private void log(String st){
		synchronized(System.out){
			System.out.println("["+this.getName()+"] "+st);
		}
	}
}

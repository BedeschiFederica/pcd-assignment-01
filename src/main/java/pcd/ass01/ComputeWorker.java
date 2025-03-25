package pcd.ass01;

import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ComputeWorker extends Thread {

	private final BoidsModel model;
	private final CyclicBarrier barrierVel;
	private final CyclicBarrier barrierPos;
	private final int start;
	private final int end;
	private Optional<BoidsView> view;

	private static final int FRAMERATE = 25;
	private int framerate;

	public ComputeWorker(final BoidsModel model, final CyclicBarrier barrierVel, final CyclicBarrier barrierPos,
						 final int start, final int end) {
		super("worker");
		this.model = model;
		this.barrierVel = barrierVel;
		this.barrierPos = barrierPos;
		this.start = start;
		this.end = end;
		this.view = Optional.empty();
	}

	public void attachView(BoidsView view) {
		this.view = Optional.of(view);
	}

	public void run() {
		log(this.start + " -> " + this.end);
		while (true) {
			var t0 = System.currentTimeMillis();
			var boids = this.model.getPartitionedBoids(this.start, this.end);

			//log("Size: " +boids.size());
			/*
			 * Improved correctness: first update velocities...
			 */
			for (SynchBoid boid : boids) {
				boid.updateVelocity(model);
			}
			//log("t1 - t0: " + (System.currentTimeMillis() - t0));

			log("wait vel");
            try {
                this.barrierVel.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
			log("update pos");

            /*
			 * ..then update positions
			 */
			for (SynchBoid boid : boids) {
				boid.updatePos(model);
			}

			log("wait pos");
			try {
				this.barrierPos.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (BrokenBarrierException e) {
				throw new RuntimeException(e);
			}
			log("update vel");

			var t1 = System.currentTimeMillis();
			var dtElapsed = t1 - t0;
			var frameratePeriod = 1000 / FRAMERATE;
			log(dtElapsed + " " + frameratePeriod);

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
	
	/*public void run(){
		log("before setting");
		//cell.set(value);
		log("after setting");
	}*/

	private void log(String st){
		synchronized(System.out){
			System.out.println("["+this.getName()+"] "+st);
		}
	}
}

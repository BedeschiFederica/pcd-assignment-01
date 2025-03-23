package pcd.ass01;

import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker extends Thread {

	private final BoidsModel model;
	private final CyclicBarrier barrier;
	private final int start;
	private final int end;
	private Optional<BoidsView> view;

	private static final int FRAMERATE = 25;
	private int framerate;

	public Worker(final BoidsModel model, final CyclicBarrier barrier, final int start, final int end) {
		super("worker");
		this.model = model;
		this.barrier = barrier;
		this.start = start;
		this.end = end;
		this.view = Optional.empty();
	}

	public void attachView(BoidsView view) {
		this.view = Optional.of(view);
	}

	public void run() {
		while (true) {
			var t0 = System.currentTimeMillis();
			var boids = this.model.getPartitionedBoids(this.start, this.end);

			/*
			 * Improved correctness: first update velocities...
			 */
			for (SynchBoid boid : boids) {
				boid.updateVelocity(model);
			}

			log("wait1");
            try {
                this.barrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
			log("continue1");

            /*
			 * ..then update positions
			 */
			for (SynchBoid boid : boids) {
				boid.updatePos(model);
			}

			if (view.isPresent()) {
				view.get().update(framerate);
				var t1 = System.currentTimeMillis();
				var dtElapsed = t1 - t0;
				var framratePeriod = 1000 / FRAMERATE;
				log(dtElapsed + " " + framratePeriod);

				if (dtElapsed < framratePeriod) {
					try {
						Thread.sleep(framratePeriod - dtElapsed);
					} catch (Exception ex) {
					}
					framerate = FRAMERATE;
				} else {
					framerate = (int) (1000 / dtElapsed);
				}
			}

			log("wait2");
			try {
				this.barrier.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (BrokenBarrierException e) {
				throw new RuntimeException(e);
			}
			log("continue2");
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

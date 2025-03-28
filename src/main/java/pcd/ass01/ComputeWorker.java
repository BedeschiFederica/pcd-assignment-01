package pcd.ass01;

public class ComputeWorker extends Thread {

	private final BoidsModel model;
	private final Barrier barrierVel;
	private final Barrier barrierPos;
	private final int start;
	private final int end;
	private final Flag stopFlag;

	public ComputeWorker(final BoidsModel model, final Barrier barrierVel, final Barrier barrierPos,
						 final int start, final int end, final Flag stopFlag) {
		super("worker");
		this.model = model;
		this.barrierVel = barrierVel;
		this.barrierPos = barrierPos;
		this.start = start;
		this.end = end;
		this.stopFlag = stopFlag;
	}

	public void run() {
		while (!this.stopFlag.isSet()) {
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
            } catch (final InterruptedException e) {
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
			} catch (final InterruptedException e) {
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

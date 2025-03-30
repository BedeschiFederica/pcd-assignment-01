package pcd.ass01.multithreaded;

import pcd.ass01.utility.BoidsModel;
import pcd.ass01.utility.Flag;
import pcd.ass01.utility.SynchBoid;

public class ComputeWorker extends Thread {

	private final BoidsModel model;
	private final Barrier barrierVel;
	private final Barrier barrierPos;
	private final int start;
	private final int end;
	private final Flag stopFlag;
	private final SuspendMonitor suspendMonitor;

	public ComputeWorker(final BoidsModel model, final Barrier barrierVel, final Barrier barrierPos,
						 final int start, final int end, final Flag stopFlag, final SuspendMonitor suspendMonitor) {
		super("compute-worker");
		this.model = model;
		this.barrierVel = barrierVel;
		this.barrierPos = barrierPos;
		this.start = start;
		this.end = end;
		this.stopFlag = stopFlag;
		this.suspendMonitor = suspendMonitor;
	}

	public void run() {
		while (!this.stopFlag.isSet()) {
			this.suspendMonitor.suspendIfRequested();

			var boids = this.model.getPartitionedBoids(this.start, this.end);

			for (final SynchBoid boid: boids) {
				boid.updateVelocity(this.model);
			}
            try {
                this.barrierVel.await();
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }

			for (final SynchBoid boid: boids) {
				boid.updatePos(this.model);
			}
			try {
				this.barrierPos.await();
			} catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void log(String st){
		synchronized(System.out){
			System.out.println("["+this.getName()+"] "+st);
		}
	}
}

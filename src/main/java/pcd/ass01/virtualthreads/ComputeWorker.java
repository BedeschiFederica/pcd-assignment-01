package pcd.ass01.virtualthreads;

import pcd.ass01.utility.*;

public class ComputeWorker implements Runnable {

	private final BoidsModel model;
	private final Barrier barrierVel;
	private final Barrier barrierPos;
	private final SynchBoid boid;
	private final Flag stopFlag;
	private final SuspendMonitor suspendMonitor;

	public ComputeWorker(final BoidsModel model, final Barrier barrierVel, final Barrier barrierPos,
						 final SynchBoid boid, final Flag stopFlag, final SuspendMonitor suspendMonitor) {
		this.model = model;
		this.barrierVel = barrierVel;
		this.barrierPos = barrierPos;
		this.boid = boid;
		this.stopFlag = stopFlag;
		this.suspendMonitor = suspendMonitor;
	}

	public void run() {
		while (!this.stopFlag.isSet()) {
			this.suspendMonitor.suspendIfRequested();

			this.boid.updateVelocity(this.model);

            try {
                this.barrierVel.await();
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }

			this.boid.updatePos(this.model);

			try {
				this.barrierPos.await();
			} catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void log(String st){
		synchronized(System.out){
			System.out.println("[VirtualThread#" + Thread.currentThread().threadId() + "] " + st);
		}
	}
}

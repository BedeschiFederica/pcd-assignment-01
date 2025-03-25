package pcd.ass01;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchBoid {

	private P2d pos;
	private V2d vel;
	private Lock mutex;

	public SynchBoid(P2d pos, V2d vel) {
		this.pos = pos;
		this.vel = vel;
		this.mutex = new ReentrantLock();
	}

	public P2d getPos() {
		/*try {
			this.mutex.lock();*/
			return this.pos;
		/*} finally {
			this.mutex.unlock();
		}*/
	}

	public V2d getVel() {
		/*try {
			this.mutex.lock();*/
			return this.vel;
		/*} finally {
			this.mutex.unlock();
		}*/
	}

	public void updateVelocity(BoidsModel model) {
		/*try {
			this.mutex.lock();*/

			/* change velocity vector according to separation, alignment, cohesion */
			var t0 = System.nanoTime();
			List<SynchBoid> nearbyBoids = getNearbyBoids(model);
			//System.out.println("t1 - t0: " + (System.nanoTime() - t0));

			V2d separation = calculateSeparation(nearbyBoids, model);
			V2d alignment = calculateAlignment(nearbyBoids, model);
			V2d cohesion = calculateCohesion(nearbyBoids, model);

			vel = vel.sum(alignment.mul(model.getAlignmentWeight()))
					.sum(separation.mul(model.getSeparationWeight()))
					.sum(cohesion.mul(model.getCohesionWeight()));

			/* Limit speed to MAX_SPEED */

			double speed = vel.abs();

			if (speed > model.getMaxSpeed()) {
				vel = vel.getNormalized().mul(model.getMaxSpeed());
			}
		/*} finally {
			this.mutex.unlock();
		}*/
	}

	public void updatePos(BoidsModel model) {

		/* Update position */

		pos = pos.sum(vel);

		/* environment wrap-around */

		if (pos.x() < model.getMinX()) pos = pos.sum(new V2d(model.getWidth(), 0));
		if (pos.x() >= model.getMaxX()) pos = pos.sum(new V2d(-model.getWidth(), 0));
		if (pos.y() < model.getMinY()) pos = pos.sum(new V2d(0, model.getHeight()));
		if (pos.y() >= model.getMaxY()) pos = pos.sum(new V2d(0, -model.getHeight()));
	}

	private List<SynchBoid> getNearbyBoids(BoidsModel model) {
		var list = new ArrayList<SynchBoid>();
		/*final Map<Pair<SynchBoid, SynchBoid>, Double> distances = model.getNearBoids();
		for (SynchBoid other : model.getBoids()) {
			if (other != this && distances.get(new Pair<>(this, other)) < model.getPerceptionRadius()) {
				list.add(other);
			}
		}*/
		for (SynchBoid other : model.getBoids()) {
			if (other != this) {
				P2d otherPos = other.getPos();
				double distance = pos.distance(otherPos);
				if (distance < model.getPerceptionRadius()) {
					list.add(other);
				}
			}
		}
		return list;
		//return model.getNearBoids(this);
	}

	private V2d calculateAlignment(List<SynchBoid> nearbyBoids, BoidsModel model) {
		/*try {
			this.mutex.lock();*/
			double avgVx = 0;
			double avgVy = 0;
			if (!nearbyBoids.isEmpty()) {
				for (SynchBoid other : nearbyBoids) {
					V2d otherVel = other.getVel();
					avgVx += otherVel.x();
					avgVy += otherVel.y();
				}
				avgVx /= nearbyBoids.size();
				avgVy /= nearbyBoids.size();
				return new V2d(avgVx - vel.x(), avgVy - vel.y()).getNormalized();
			} else {
				return new V2d(0, 0);
			}
		/*} finally {
			this.mutex.unlock();
		}*/
	}

	private V2d calculateCohesion(List<SynchBoid> nearbyBoids, BoidsModel model) {
		double centerX = 0;
		double centerY = 0;
		if (!nearbyBoids.isEmpty()) {
			for (SynchBoid other: nearbyBoids) {
				P2d otherPos = other.getPos();
				centerX += otherPos.x();
				centerY += otherPos.y();
			}
			centerX /= nearbyBoids.size();
			centerY /= nearbyBoids.size();
			return new V2d(centerX - pos.x(), centerY - pos.y()).getNormalized();
		} else {
			return new V2d(0, 0);
		}
	}

	private V2d calculateSeparation(List<SynchBoid> nearbyBoids, BoidsModel model) {
		double dx = 0;
		double dy = 0;
		int count = 0;
		for (SynchBoid other: nearbyBoids) {
			P2d otherPos = other.getPos();
			double distance = pos.distance(otherPos);
			if (distance < model.getAvoidRadius()) {
				dx += pos.x() - otherPos.x();
				dy += pos.y() - otherPos.y();
				count++;
			}
		}
		if (count > 0) {
			dx /= count;
			dy /= count;
			return new V2d(dx, dy).getNormalized();
		} else {
			return new V2d(0, 0);
		}
	}

	/*private int value;
	private boolean available;

	private Condition isAvail;

	public SynchBoid(){
		available = false;
		mutex = new ReentrantLock(); 
		isAvail = mutex.newCondition();
	}

	public void set(int v){
		try {
			mutex.lock();
			value = v;
			available = true;
			isAvail.signalAll();  
		} finally {
			mutex.unlock();
		}
	}
	
	public int get() {
		try {
			mutex.lock();
			if (!available){
				try {
					isAvail.await();
				} catch (InterruptedException ex){}
			} 
			return value;
		} finally {
			mutex.unlock();
		}
	}*/
}


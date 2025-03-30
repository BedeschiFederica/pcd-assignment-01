package pcd.ass01.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchBoid {

	private P2d pos;
	private V2d vel;
	private final Lock mutex;

	public SynchBoid(final P2d pos, final V2d vel) {
		this.pos = pos;
		this.vel = vel;
		this.mutex = new ReentrantLock();
	}

	public P2d getPos() {
		return this.pos;
	}

	public V2d getVel() {
		return this.vel;
	}

	public void updateVelocity(final BoidsModel model) {
		/* change velocity vector according to separation, alignment, cohesion */
		final List<SynchBoid> nearbyBoids = getNearbyBoids(model);

		final V2d separation = calculateSeparation(nearbyBoids, model);
		final V2d alignment = calculateAlignment(nearbyBoids);
		final V2d cohesion = calculateCohesion(nearbyBoids);

		this.vel = this.vel.sum(alignment.mul(model.getAlignmentWeight()))
				.sum(separation.mul(model.getSeparationWeight()))
				.sum(cohesion.mul(model.getCohesionWeight()));

		/* Limit speed to MAX_SPEED */
		final double speed = this.vel.abs();
		if (speed > model.getMaxSpeed()) {
			this.vel = this.vel.getNormalized().mul(model.getMaxSpeed());
		}
	}

	public void updatePos(final BoidsModel model) {

		/* Update position */
		this.pos = this.pos.sum(this.vel);

		/* environment wrap-around */
		if (this.pos.x() < model.getMinX()) {
			this.pos = this.pos.sum(new V2d(model.getWidth(), 0));
		}
		if (this.pos.x() >= model.getMaxX()) {
			this.pos = this.pos.sum(new V2d(-model.getWidth(), 0));
		}
		if (this.pos.y() < model.getMinY()) {
			this.pos = this.pos.sum(new V2d(0, model.getHeight()));
		}
		if (this.pos.y() >= model.getMaxY()) {
			this.pos = this.pos.sum(new V2d(0, -model.getHeight()));
		}
	}

	private List<SynchBoid> getNearbyBoids(final BoidsModel model) {
		final List<SynchBoid> list = new ArrayList<>();
		for (final SynchBoid other: model.getBoids()) {
			if (other != this) {
				final P2d otherPos = other.getPos();
				final double distance = this.pos.distance(otherPos);
				if (distance < model.getPerceptionRadius()) {
					list.add(other);
				}
			}
		}
		return list;
	}

	private V2d calculateAlignment(final List<SynchBoid> nearbyBoids) {
		double avgVx = 0;
		double avgVy = 0;
		if (!nearbyBoids.isEmpty()) {
			for (final SynchBoid other: nearbyBoids) {
				V2d otherVel;
				try {
					this.mutex.lock();
					otherVel = other.getVel();
				} finally {
					this.mutex.unlock();
				}
				avgVx += otherVel.x();
				avgVy += otherVel.y();
			}
			avgVx /= nearbyBoids.size();
			avgVy /= nearbyBoids.size();
			return new V2d(avgVx - this.vel.x(), avgVy - this.vel.y()).getNormalized();
		} else {
			return new V2d(0, 0);
		}
	}

	private V2d calculateCohesion(final List<SynchBoid> nearbyBoids) {
		double centerX = 0;
		double centerY = 0;
		if (!nearbyBoids.isEmpty()) {
			for (final SynchBoid other: nearbyBoids) {
				final P2d otherPos = other.getPos();
				centerX += otherPos.x();
				centerY += otherPos.y();
			}
			centerX /= nearbyBoids.size();
			centerY /= nearbyBoids.size();
			return new V2d(centerX - this.pos.x(), centerY - this.pos.y()).getNormalized();
		} else {
			return new V2d(0, 0);
		}
	}

	private V2d calculateSeparation(final List<SynchBoid> nearbyBoids, final BoidsModel model) {
		double dx = 0;
		double dy = 0;
		int count = 0;
		for (final SynchBoid other: nearbyBoids) {
			final P2d otherPos = other.getPos();
			final double distance = this.pos.distance(otherPos);
			if (distance < model.getAvoidRadius()) {
				dx += this.pos.x() - otherPos.x();
				dy += this.pos.y() - otherPos.y();
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
}


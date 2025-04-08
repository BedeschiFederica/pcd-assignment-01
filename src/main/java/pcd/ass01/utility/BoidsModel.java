package pcd.ass01.utility;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoidsModel {
    
    private final List<SynchBoid> boids;
    private final Lock mutex;
    private double separationWeight; 
    private double alignmentWeight; 
    private double cohesionWeight; 
    private final double width;
    private final double height;
    private final double maxSpeed;
    private final double perceptionRadius;
    private final double avoidRadius;

    public BoidsModel(final int nBoids,
                      final double initialSeparationWeight,
                      final double initialAlignmentWeight,
                      final double initialCohesionWeight,
                      final double width,
                      final double height,
                      final double maxSpeed,
                      final double perceptionRadius,
                      final double avoidRadius
    ){
        this.separationWeight = initialSeparationWeight;
        this.alignmentWeight = initialAlignmentWeight;
        this.cohesionWeight = initialCohesionWeight;
        this.width = width;
        this.height = height;
        this.maxSpeed = maxSpeed;
        this.perceptionRadius = perceptionRadius;
        this.avoidRadius = avoidRadius;

        this.mutex = new ReentrantLock();

        this.boids = new ArrayList<>();
        for (int i = 0; i < nBoids; i++) {
        	final P2d pos = new P2d(-this.width/2 + Math.random() * this.width, -this.height/2 + Math.random() * this.height);
        	final V2d vel = new V2d(Math.random() * this.maxSpeed/2 - this.maxSpeed/4, Math.random() * this.maxSpeed/2 - this.maxSpeed/4);
            this.boids.add(new SynchBoid(pos, vel));
        }
    }
    
    public List<SynchBoid> getBoids(){
    	return List.copyOf(this.boids);
    }

    public List<SynchBoid> getPartitionedBoids(final int start, final int end) {
        return this.boids.subList(start, end);
    }

    public double getMinX() {
    	return -this.width/2;
    }

    public double getMaxX() {
    	return this.width/2;
    }

    public double getMinY() {
    	return -this.height/2;
    }

    public double getMaxY() {
    	return this.height/2;
    }
    
    public double getWidth() {
    	return this.width;
    }
 
    public double getHeight() {
    	return this.height;
    }

    public void setSeparationWeight(final double value) {
        try {
            this.mutex.lock();
            this.separationWeight = value;
        } finally {
            this.mutex.unlock();
        }
    }

    public void setAlignmentWeight(final double value) {
        try {
            this.mutex.lock();
            this.alignmentWeight = value;
        } finally {
            this.mutex.unlock();
        }
    }

    public void setCohesionWeight(final double value) {
        try {
            this.mutex.lock();
            this.cohesionWeight = value;
        } finally {
            this.mutex.unlock();
        }
    }

    public double getSeparationWeight() {
        try {
            this.mutex.lock();
            return this.separationWeight;
        } finally {
            this.mutex.unlock();
        }
    }

    public double getCohesionWeight() {
        try {
            this.mutex.lock();
            return this.cohesionWeight;
        } finally {
            this.mutex.unlock();
        }
    }

    public double getAlignmentWeight() {
        try {
            this.mutex.lock();
            return this.alignmentWeight;
        } finally {
            this.mutex.unlock();
        }
    }
    
    public double getMaxSpeed() {
    	return this.maxSpeed;
    }

    public double getAvoidRadius() {
    	return this.avoidRadius;
    }

    public double getPerceptionRadius() {
    	return this.perceptionRadius;
    }
}

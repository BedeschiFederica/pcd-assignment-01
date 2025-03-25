package pcd.ass01;

import java.util.*;

public class BoidsModel {
    
    private final List<SynchBoid> boids;
    private final Map<SynchBoid, List<SynchBoid>> nearBoids = new HashMap<>();
    private double separationWeight; 
    private double alignmentWeight; 
    private double cohesionWeight; 
    private final double width;
    private final double height;
    private final double maxSpeed;
    private final double perceptionRadius;
    private final double avoidRadius;

    public BoidsModel(int nboids,  
    						double initialSeparationWeight, 
    						double initialAlignmentWeight, 
    						double initialCohesionWeight,
    						double width, 
    						double height,
    						double maxSpeed,
    						double perceptionRadius,
    						double avoidRadius){
        separationWeight = initialSeparationWeight;
        alignmentWeight = initialAlignmentWeight;
        cohesionWeight = initialCohesionWeight;
        this.width = width;
        this.height = height;
        this.maxSpeed = maxSpeed;
        this.perceptionRadius = perceptionRadius;
        this.avoidRadius = avoidRadius;
        
    	boids = new ArrayList<>();
        for (int i = 0; i < nboids; i++) {
        	P2d pos = new P2d(-width/2 + Math.random() * width, -height/2 + Math.random() * height);
        	V2d vel = new V2d(Math.random() * maxSpeed/2 - maxSpeed/4, Math.random() * maxSpeed/2 - maxSpeed/4);
        	boids.add(new SynchBoid(pos, vel));
        }

        for (SynchBoid boid : this.getBoids()) {
            final List<SynchBoid> list = new ArrayList<>();
            for (SynchBoid other : this.getBoids()) {
                if (other != boid) {
                    P2d otherPos = other.getPos();
                    double distance = boid.getPos().distance(otherPos);
                    if (distance < this.getPerceptionRadius()) {
                        list.add(other);
                    }
                }
            }
            this.nearBoids.put(boid, list);
        }
    }
    
    public List<SynchBoid> getBoids(){
    	return List.copyOf(this.boids);
    }

    public List<SynchBoid> getPartitionedBoids(final int start, final int end) {
        return this.boids.subList(start, end);
    }

    public synchronized List<SynchBoid> getNearBoids(final SynchBoid boid) {
        return nearBoids.get(boid);
    }

    public synchronized void setNearBoids(final SynchBoid boid, final List<SynchBoid> list) {
        this.nearBoids.put(boid, list);
    }

    public double getMinX() {
    	return -width/2;
    }

    public double getMaxX() {
    	return width/2;
    }

    public double getMinY() {
    	return -height/2;
    }

    public double getMaxY() {
    	return height/2;
    }
    
    public double getWidth() {
    	return width;
    }
 
    public double getHeight() {
    	return height;
    }

    public synchronized void setSeparationWeight(double value) {
    	this.separationWeight = value;
    }

    public synchronized void setAlignmentWeight(double value) {
    	this.alignmentWeight = value;
    }

    public synchronized void setCohesionWeight(double value) {
    	this.cohesionWeight = value;
    }

    public double getSeparationWeight() {
    	return separationWeight;
    }

    public double getCohesionWeight() {
    	return cohesionWeight;
    }

    public double getAlignmentWeight() {
    	return alignmentWeight;
    }
    
    public double getMaxSpeed() {
    	return maxSpeed;
    }

    public double getAvoidRadius() {
    	return avoidRadius;
    }

    public double getPerceptionRadius() {
    	return perceptionRadius;
    }
}

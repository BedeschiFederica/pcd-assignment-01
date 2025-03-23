package pcd.ass01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BoidsSimulation {

	final static int N_BOIDS = 1500;

	final static double SEPARATION_WEIGHT = 1.0;
    final static double ALIGNMENT_WEIGHT = 1.0;
    final static double COHESION_WEIGHT = 1.0;

    final static int ENVIRONMENT_WIDTH = 1000; 
	final static int ENVIRONMENT_HEIGHT = 1000;
    static final double MAX_SPEED = 4.0;
    static final double PERCEPTION_RADIUS = 50.0;
    static final double AVOID_RADIUS = 20.0;

	final static int SCREEN_WIDTH = 800; 
	final static int SCREEN_HEIGHT = 800; 
	

    public static void main(String[] args) {      
    	var model = new BoidsModel(
    					N_BOIDS, 
    					SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT, 
    					ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
    					MAX_SPEED,
    					PERCEPTION_RADIUS,
    					AVOID_RADIUS); 
    	//var sim = new BoidsSimulator(model);
		final int nWorkers = Runtime.getRuntime().availableProcessors() + 1;
		final int size = N_BOIDS / nWorkers;
		CyclicBarrier barrier = new CyclicBarrier(nWorkers);
		List<Worker> workers = new ArrayList<>();
		for (int i = 0; i < nWorkers; i++) {
			final int start = i * size;
			final int end = (i != nWorkers - 1) ? (i + 1) * size : N_BOIDS;
			workers.add(new Worker(model, barrier, start, end));
		}
    	var view = new BoidsView(model, SCREEN_WIDTH, SCREEN_HEIGHT);
		for (Worker w: workers) {
			w.attachView(view);
		}
		for (Worker w: workers) {
			w.start();
		}
    	//sim.attachView(view);
    	//sim.runSimulation();
    }
}

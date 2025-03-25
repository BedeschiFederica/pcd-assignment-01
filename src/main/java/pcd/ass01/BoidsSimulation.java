package pcd.ass01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BoidsSimulation {

	private static final int N_BOIDS = 1500;

	private static final double SEPARATION_WEIGHT = 1.0;
    private static final double ALIGNMENT_WEIGHT = 1.0;
    private static final double COHESION_WEIGHT = 1.0;

    private static final int ENVIRONMENT_WIDTH = 1000;
	private static final int ENVIRONMENT_HEIGHT = 1000;
    private static final double MAX_SPEED = 4.0;
    private static final double PERCEPTION_RADIUS = 50.0;
    private static final double AVOID_RADIUS = 20.0;

	private static final int SCREEN_WIDTH = 800;
	private static final int SCREEN_HEIGHT = 800;
	

    public static void main(String[] args) {      
    	final BoidsModel model = new BoidsModel(
    					N_BOIDS, 
    					SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT, 
    					ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
    					MAX_SPEED,
    					PERCEPTION_RADIUS,
    					AVOID_RADIUS);
		final BoidsView view = new BoidsView(model, SCREEN_WIDTH, SCREEN_HEIGHT);
		final int nWorkers = Runtime.getRuntime().availableProcessors();
		final int size = N_BOIDS / nWorkers;
		final CyclicBarrier barrierVel = new CyclicBarrier(nWorkers + 1);
		final CyclicBarrier barrierPos = new CyclicBarrier(nWorkers + 1);
		final GUIWorker guiWorker = new GUIWorker(view, barrierVel, barrierPos);
		final List<ComputeWorker> workers = new ArrayList<>();
		for (int i = 0; i < nWorkers; i++) {
			final int start = i * size;
			final int end = (i != nWorkers - 1) ? (i + 1) * size : N_BOIDS;
			workers.add(new ComputeWorker(model, barrierVel, barrierPos, start, end));
		}
		for (ComputeWorker w: workers) {
			w.start();
		}
		guiWorker.start();
    }
}

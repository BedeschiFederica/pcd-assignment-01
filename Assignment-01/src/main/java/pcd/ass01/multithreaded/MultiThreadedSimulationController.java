package pcd.ass01.multithreaded;

import pcd.ass01.utility.*;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSimulationController extends AbstractSimulationController {

    public MultiThreadedSimulationController() {
        super();
    }

    @Override
    public void startSimulation(final int nBoids) {
        super.startSimulation(nBoids);
        List<ComputeWorker> workers = new ArrayList<>();
        final int nWorkers = Runtime.getRuntime().availableProcessors();
        final int size = nBoids / nWorkers;
        Barrier barrierVel = new BarrierImpl(nWorkers + 1);
        Barrier barrierPos = new BarrierImpl(nWorkers + 1);
        for (int i = 0; i < nWorkers; i++) {
            final int start = i * size;
            final int end = (i != nWorkers - 1) ? (i + 1) * size : nBoids;
            workers.add(new ComputeWorker(this.model, barrierVel, barrierPos, start, end,
                    this.stopFlag, this.suspendMonitor));
        }
        for (final ComputeWorker w: workers) {
            w.start();
        }
        new Thread(new GUIWorker(this.view, barrierVel, barrierPos, this.stopFlag, this.suspendMonitor)).start();
    }
}

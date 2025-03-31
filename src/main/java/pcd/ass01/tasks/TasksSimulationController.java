package pcd.ass01.tasks;

import pcd.ass01.utility.*;

public class TasksSimulationController extends AbstractSimulationController {

    private static final int N_THREADS = Runtime.getRuntime().availableProcessors() + 1;

    public TasksSimulationController() {
        super();
    }

    @Override
    public void startSimulation(final int nBoids) {
        super.startSimulation(nBoids);
        final MasterWorker masterWorker = new MasterWorker(this.model, this.view, this.stopFlag, this.suspendMonitor,
                N_THREADS);
        masterWorker.start();
    }
}

package pcd.ass01.virtualthreads;

import pcd.ass01.utility.*;

public class VirtualThreadsSimulationController extends AbstractSimulationController {

    public VirtualThreadsSimulationController() {
        super();
    }

    @Override
    public void startSimulation(final int nBoids) {
        super.startSimulation(nBoids);
        Barrier barrierVel = new BarrierImpl(nBoids + 1);
        Barrier barrierPos = new BarrierImpl(nBoids + 1);
        for (final SynchBoid boid: this.model.getBoids()) {
            Thread.ofVirtual().start(new ComputeWorker(this.model, barrierVel, barrierPos, boid, this.stopFlag,
                    this.suspendMonitor));
        }
        Thread.ofVirtual().start(new GUIWorker(this.view, barrierVel, barrierPos, this.stopFlag, this.suspendMonitor));
    }
}

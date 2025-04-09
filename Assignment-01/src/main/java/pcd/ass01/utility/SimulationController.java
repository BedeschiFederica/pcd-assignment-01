package pcd.ass01.utility;

import java.util.List;

public interface SimulationController {
    void startSimulation(int nBoids);

    void stopSimulation();

    void suspendSimulation();

    void resumeSimulation();

    double getWidth();

    List<SynchBoid> getBoids();

    void setSeparationWeight(double value);

    void setCohesionWeight(double value);

    void setAlignmentWeight(double value);
}

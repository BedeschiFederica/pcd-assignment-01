package pcd.ass01;

/**
 * 
 * Barrier behaviour
 * 
 * N agents blocks until they *all* arrive to a common sync point ("barrier")
 * 
 */
public interface Barrier {

	void await() throws InterruptedException;

}

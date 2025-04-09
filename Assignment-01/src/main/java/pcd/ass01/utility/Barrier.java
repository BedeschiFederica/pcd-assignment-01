package pcd.ass01.utility;

/**
 * 
 * Barrier behaviour
 * 
 * N agents blocks until they *all* arrive to a common sync point ("barrier")
 * 
 */
public interface Barrier {

	/**
	 * Agent await the arrival of all other agents.
	 *
	 * @throws InterruptedException
	 */
	void await() throws InterruptedException;

}

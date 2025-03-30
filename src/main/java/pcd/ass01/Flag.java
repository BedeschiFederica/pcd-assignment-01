package pcd.ass01;

public class Flag {

	private boolean flag;
	
	public Flag() {
		this.flag = false;
	}
	
	public synchronized void reset() {
		this.flag = false;
	}
	
	public synchronized void set() {
		this.flag = true;
	}
	
	public synchronized boolean isSet() {
		return this.flag;
	}
}

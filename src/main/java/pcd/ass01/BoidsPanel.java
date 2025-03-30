package pcd.ass01;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BoidsPanel extends JPanel {

	private final BoidsView view;
	private final SimulationController controller;
    private int frameRate;

    public BoidsPanel(final BoidsView view, final SimulationController controller) {
    	this.controller = controller;
    	this.view = view;
    }

    public void setFrameRate(final int frameRate) {
    	this.frameRate = frameRate;
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        
        final int w = this.view.getWidth();
        final int h = this.view.getHeight();
        final double envWidth = this.controller.getWidth();
        final double xScale = w / envWidth;

        final List<SynchBoid> boids = this.controller.getBoids();

        g.setColor(Color.BLUE);
        for (final SynchBoid boid: boids) {
        	final double x = boid.getPos().x();
        	final double y = boid.getPos().y();
        	final int px = (int)(w/2 + x*xScale);
        	final int py = (int)(h/2 - y*xScale);
            g.fillOval(px, py, 5, 5);
        }
        
        g.setColor(Color.BLACK);
        g.drawString("Num. Boids: " + boids.size(), 10, 25);
        g.drawString("Framerate: " + this.frameRate, 10, 40);
   }
}

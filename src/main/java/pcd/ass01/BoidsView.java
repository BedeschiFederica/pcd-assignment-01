package pcd.ass01;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.util.Hashtable;

public class BoidsView implements ChangeListener {

	private final JFrame frame;
	private final BoidsPanel boidsPanel;
	private final JSlider cohesionSlider, separationSlider, alignmentSlider;
	private final BoidsModel model;
	private final int width, height;

	public BoidsView(BoidsModel model, int width, int height) {
		this.model = model;
		this.width = width;
		this.height = height;

		this.frame = new JFrame("Boids Simulation");
        this.frame.setSize(this.width, this.height);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel cp = new JPanel();
		final LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);

        this.boidsPanel = new BoidsPanel(this, this.model);
		cp.add(BorderLayout.CENTER, this.boidsPanel);

        final JPanel slidersPanel = new JPanel();

        this.cohesionSlider = makeSlider();
		this.separationSlider = makeSlider();
		this.alignmentSlider = makeSlider();

        slidersPanel.add(new JLabel("Separation"));
        slidersPanel.add(this.separationSlider);
        slidersPanel.add(new JLabel("Alignment"));
        slidersPanel.add(this.alignmentSlider);
        slidersPanel.add(new JLabel("Cohesion"));
        slidersPanel.add(this.cohesionSlider);

		cp.add(BorderLayout.SOUTH, slidersPanel);

		this.frame.setContentPane(cp);

		this.frame.setVisible(true);
	}

	private JSlider makeSlider() {
		final JSlider slider = new JSlider(JSlider.HORIZONTAL, 0,20, 10);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		final Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
		labelTable.put(0, new JLabel("0"));
		labelTable.put(10, new JLabel("1"));
		labelTable.put(20, new JLabel("2"));
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);
        slider.addChangeListener(this);
		return slider;
	}

	public void update(final int frameRate) {
		this.boidsPanel.setFrameRate(frameRate);
		this.boidsPanel.repaint();
	}

	@Override
	public void stateChanged(final ChangeEvent e) {
		int val;
		if (e.getSource() == this.separationSlider) {
			val = this.separationSlider.getValue();
			this.model.setSeparationWeight(0.1 * val);
		} else if (e.getSource() == this.cohesionSlider) {
			val = this.cohesionSlider.getValue();
			model.setCohesionWeight(0.1 * val);
		} else {
			val = this.alignmentSlider.getValue();
			this.model.setAlignmentWeight(0.1 * val);
		}
	}
	
	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

}

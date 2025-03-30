package pcd.ass01;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.util.Hashtable;

public class BoidsView implements ChangeListener {

	private final JFrame frame;
	JPanel simulationPanel;
	private BoidsPanel boidsPanel;
	private JPanel buttonsPanel;
	private EnteringPanel enteringPanel;
	private JSlider cohesionSlider, separationSlider, alignmentSlider;
	private final SimulationController controller;
	private final int width, height;

	public BoidsView(final SimulationController controller, final int width, final int height) {
		this.controller = controller;

		this.width = width;
		this.height = height;

		this.frame = new JFrame("Boids Simulation");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.createSimulationPanel();

		this.enteringPanel = new EnteringPanel(this, this.controller, this.frame);
	}

	public void createSimulationPanel() {
		this.simulationPanel = new JPanel();
		final LayoutManager layout = new BorderLayout();
		this.simulationPanel.setLayout(layout);

		this.buttonsPanel = new JPanel(new FlowLayout());
		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(e -> {
			this.enteringPanel = new EnteringPanel(this, this.controller, this.frame);
			this.controller.stopSimulation();
		});
		JButton suspendResumeButton = new JButton("Suspend");
		suspendResumeButton.addActionListener(e -> {
			if (suspendResumeButton.getText().equals("Suspend")) {
				suspendResumeButton.setText("Resume");
				this.controller.suspendSimulation();
			} else {
				suspendResumeButton.setText("Suspend");
				this.controller.resumeSimulation();
			}
		});
		this.buttonsPanel.add(stopButton);
		this.buttonsPanel.add(suspendResumeButton);
		this.simulationPanel.add(BorderLayout.NORTH, this.buttonsPanel);

		this.boidsPanel = new BoidsPanel(this, this.controller);
		this.simulationPanel.add(BorderLayout.CENTER, this.boidsPanel);

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

		this.simulationPanel.add(BorderLayout.SOUTH, slidersPanel);
	}

	public void setPanel(final JPanel panel) {
		this.frame.getContentPane().removeAll();
		this.frame.add(panel);
		this.frame.revalidate();
		this.frame.setSize(this.width, this.height);
		this.frame.repaint();
		this.frame.setLocationRelativeTo(null);
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
			this.controller.setSeparationWeight(0.1 * val);
		} else if (e.getSource() == this.cohesionSlider) {
			val = this.cohesionSlider.getValue();
			this.controller.setCohesionWeight(0.1 * val);
		} else {
			val = this.alignmentSlider.getValue();
			this.controller.setAlignmentWeight(0.1 * val);
		}
	}
	
	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public JPanel getSimulationPanel() {
		return this.simulationPanel;
	}
}

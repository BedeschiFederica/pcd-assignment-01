package pcd.ass01;

import javax.swing.*;
import java.awt.*;

public class EnteringPanel extends JPanel {

    private static final int TEXT_FIELD_COLUMNS = 25;
    private static final int FONT_SIZE = 14;

	private final BoidsView view;
    private final JTextField field;

    public EnteringPanel(final BoidsView view, final SimulationController controller, final JFrame frame) {
    	this.view = view;
        this.field = new JTextField(TEXT_FIELD_COLUMNS);
        this.field.setMaximumSize(new Dimension(200, 30));
        this.field.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel label = new JLabel("Number of boids: ");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));

        final JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));

        final JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(label);
        centerPanel.add(this.field);
        centerPanel.add(startButton);

        this.setLayout(new BorderLayout());
        this.add(BorderLayout.CENTER, centerPanel);

        frame.setContentPane(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        startButton.addActionListener(e -> {
            try {
                final int nBoids = Integer.parseInt(this.field.getText());
                if (nBoids > 0) {
                    this.view.createSimulationPanel();
                    this.view.setPanel(this.view.getSimulationPanel());
                    controller.startSimulation(nBoids);
                } else {
                    System.err.println("Illegal value inserted! Positive integer is requested.");
                }
            } catch (final NumberFormatException ex) {
                System.err.println("Illegal value inserted! Integer is requested.");
            }
        });
    }
}

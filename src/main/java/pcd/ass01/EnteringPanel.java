package pcd.ass01;

import javax.swing.*;
import java.awt.*;

public class EnteringPanel extends JPanel {

    private static final int TEXT_FIELD_COLUMNS = 25;
    private static final int FONT_SIZE = 14;

	private final BoidsView view;
    private final JTextField field;
    private final JLabel label;
    private final JButton startButton;
    private final JPanel centerPanel;

    public EnteringPanel(final BoidsView view, final SimulationController controller, final JFrame frame) {
    	this.view = view;
        this.label = new JLabel("Number of boids: ");
        this.field = new JTextField(TEXT_FIELD_COLUMNS);
        this.startButton = new JButton("Start");

        this.centerPanel = new JPanel();
        this.centerPanel.setLayout(new BoxLayout(this.centerPanel, BoxLayout.Y_AXIS));
        this.centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));
        this.field.setMaximumSize(new Dimension(200, 30));
        this.field.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.startButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));

        this.setLayout(new BorderLayout());

        this.centerPanel.add(this.label);
        this.centerPanel.add(this.field);
        this.centerPanel.add(this.startButton);
        this.add(BorderLayout.CENTER, this.centerPanel);

        frame.setContentPane(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        this.startButton.addActionListener(e -> {
            try {
                final int nBoids = Integer.parseInt(this.field.getText());
                if (nBoids > 0) {
                    this.view.createBoidsPanel();
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

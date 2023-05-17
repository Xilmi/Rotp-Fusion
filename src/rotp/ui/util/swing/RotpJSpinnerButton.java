package rotp.ui.util.swing;

import java.awt.Dimension;

import javax.swing.JButton;

public class RotpJSpinnerButton extends JButton {
	RotpJSpinnerButton(int direction, int size) {
		super();
		this.setMinimumSize(new Dimension(5, 5));
		this.setPreferredSize(new Dimension(size, size));
		setIcon(new RotpJSpinnerIcon(direction));
	}
}
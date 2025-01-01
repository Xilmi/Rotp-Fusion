package rotp.ui.util.swing;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicSpinnerUI;

class LeftRightSpinnerUI extends BasicSpinnerUI {
	 
	public static ComponentUI createUI(JComponent c) { return new LeftRightSpinnerUI(); }

	@Override protected Component createNextButton() {
		Component c = createArrowButton(SwingConstants.EAST);
		c.setName("Spinner.nextButton");
		installNextButtonListeners(c);
		return c;
	}
	@Override protected Component createPreviousButton() {
		Component c = createArrowButton(SwingConstants.WEST);
		c.setName("Spinner.previousButton");
		installPreviousButtonListeners(c);
		return c;
	}
	@Override public void installUI(JComponent c) {
		super.installUI(c);
		c.removeAll();
		c.setLayout(new LeftRightSpinnerBorderLayout());
		c.add(createNextButton(), BorderLayout.EAST);
		c.add(createPreviousButton(), BorderLayout.WEST);
		c.add(createEditor(), BorderLayout.CENTER);
	}

	// copied from BasicSpinnerUI
	private Component createArrowButton(int direction) {
		JButton b = new BasicArrowButton(direction);
		Border buttonBorder = UIManager.getBorder("Spinner.arrowButtonBorder");
		if (buttonBorder instanceof UIResource)
		  b.setBorder(new CompoundBorder(buttonBorder, null));
		else
		  b.setBorder(buttonBorder);
		b.setInheritsPopupMenu(true);
		return b;
	}

}
	class LeftRightSpinnerBorderLayout extends BorderLayout {
		private static final long serialVersionUID = 1L;
		@Override public void addLayoutComponent(Component comp, Object constraints) {
			if (constraints.equals("Editor"))
				constraints = CENTER;
			super.addLayoutComponent(comp, constraints);
		}
	}


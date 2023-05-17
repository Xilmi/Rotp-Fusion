package rotp.ui.util.swing;

import java.awt.BorderLayout;
import java.awt.Component;

class RotpJSpinnerLayout extends BorderLayout {
	
	RotpJSpinnerLayout() { }
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if("Editor".equals(constraints)) {
			constraints = CENTER;
	    }
		else if("Next".equals(constraints)) {
			constraints = BorderLayout.EAST;
	    }
		else if("Previous".equals(constraints)) {
			constraints = BorderLayout.WEST;
	    }
		super.addLayoutComponent(comp, constraints);
	}
}
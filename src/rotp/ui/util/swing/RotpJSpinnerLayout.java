package rotp.ui.util.swing;

import java.awt.BorderLayout;
import java.awt.Component;

class RotpJSpinnerLayout extends BorderLayout {
	
	RotpJSpinnerLayout() { }
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
//		System.out.println("RotpJSpinnerLayout: constraints " + constraints + "    # Name = " + comp.getName());

		if("Editor".equals(constraints)) {
			constraints = CENTER;
			super.addLayoutComponent(comp, constraints);
			return;
	    }
		if("Next".equals(constraints)) {
			constraints = BorderLayout.EAST;
			super.addLayoutComponent(comp, constraints);
			return;
	    }
		if("Previous".equals(constraints)) {
			constraints = BorderLayout.WEST;
			super.addLayoutComponent(comp, constraints);
			return;
	    }
		// BR: No unknown Layout Addition !!!

//		if("Editor".equals(constraints)) {
//			constraints = CENTER;
//	    }
//		else if("Next".equals(constraints)) {
//			constraints = BorderLayout.EAST;
//	    }
//		else if("Previous".equals(constraints)) {
//			constraints = BorderLayout.WEST;
//	    }
//		super.addLayoutComponent(comp, constraints);
	}
}

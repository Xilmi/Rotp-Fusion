package rotp.ui.main;

import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import rotp.model.game.GovernorOptions;
import rotp.util.Base;
import rotp.util.ModifierKeysState;

class GovernorFrame extends JFrame implements Base, KeyListener {
	private static final long serialVersionUID = 1L;
	GovernorFrame (String title)	{
		super(title);
		addKeyListener(this);
		setFocusable(true);
        setFocusTraversalKeysEnabled(false);
	}
	@Override public void keyTyped(KeyEvent e)		{ }
	@Override public void keyPressed(KeyEvent e)	{ ModifierKeysState.set(e); }
	@Override public void keyReleased(KeyEvent e)	{
		ModifierKeysState.set(e);
		switch(e.getKeyCode()) {
		case KeyEvent.VK_L:
			if (e.isAltDown()) {
				debugReloadLabels("");
			}
			return;
		}
	}
	class GovernorComponentAdapter extends ComponentAdapter {
    	@Override public void componentMoved(java.awt.event.ComponentEvent evt) {
        	GovernorOptions options = govOptions();
        	Point pt = evt.getComponent().getLocation();
			options.setPosition(pt);
        }
    }
}

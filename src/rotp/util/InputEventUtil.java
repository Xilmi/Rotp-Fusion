package rotp.util;

import java.awt.event.InputEvent;

public interface InputEventUtil {
	/**
	 * Only valid if previously initialized in the panel or parent panel
	 * with setModifierKeysState(InputEvent e)
	 * 
	 * @return true if Alt Key is down
	 */
	default boolean isAltDown()		{ return ModifierKeysState.isAltDown(); }

	/**
	 * Only valid if previously initialized in the panel or parent panel
	 * with setModifierKeysState(InputEvent e)
	 * 
	 * @return true if Alt-Graphic Key is down
	 */
	default boolean isAltGrDown()	{ return ModifierKeysState.isAltGrDown(); }

	/**
	 * Only valid if previously initialized in the panel or parent panel
	 * with setModifierKeysState(InputEvent e)
	 * 
	 * @return true if Ctrl Key is down
	 */
	default boolean isCtrlDown()	{ return ModifierKeysState.isCtrlDown(); }

	/**
	 * Only valid if previously initialized in the panel or parent panel
	 * with setModifierKeysState(InputEvent e)
	 * 
	 * @return true if Meta Key is down
	 */
	default boolean isMetaDown()	{ return ModifierKeysState.isMetaDown(); }

	/**
	 * Only valid if previously initialized in the panel or parent panel
	 * with setModifierKeysState(InputEvent e)
	 * 
	 * @return true if Shift Key is down
	 */
	default boolean isShiftDown()	{ return ModifierKeysState.isShiftDown(); }

	/**
	 * Only valid if previously initialized in the panel or parent panel
	 * with setModifierKeysState(InputEvent e)
	 * 
	 * @return true if Shift or Control Key is down
	 */
	default boolean isShiftOrCtrlDown()	{ return ModifierKeysState.isShiftOrCtrlDown(); }

	// BR:
	default void setModifierKeysState(InputEvent e)	{ ModifierKeysState.set(e); }
	// BR: Only use if initialized first
	default boolean checkForChange(InputEvent e)	{ return ModifierKeysState.checkForChange(e); }
}

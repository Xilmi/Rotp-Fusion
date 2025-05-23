package rotp.ui.util;

public class ParamIntegerSound extends ParamInteger{
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultvalue The default value
	 * @param minValue The minimum value (null = none)
	 * @param maxValue The maximum value (null = none)
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 */
	public ParamIntegerSound(String gui, String name, Integer defaultValue
			, Integer minValue, Integer maxValue
			, Integer baseInc, Integer shiftInc, Integer ctrlInc) {
		super(gui, name, defaultValue);
		setLimits(minValue, maxValue);
		setIncrements(baseInc, shiftInc, ctrlInc);
		isCfgFile(true);
	}
	@Override public Integer set(Integer val) {
		if (val != get()) {
			rotp.util.sound.WavClip.clearDelayClips();
			rotp.util.sound.OggClip.clearDelayClips();
		}
		return super.set(val);
	}
	@Override public ParamIntegerSound pctValue(boolean pctValue) {
		super.pctValue(pctValue);
		return this;
	}
}

package rotp.model.game;

public final class DefaultValues {
	public static final DefaultValues DEF_VAL = new DefaultValues();

	public static final String FUSION_DEFAULT	= "SETTINGS_MOD_DEFAULT_IS_FUSION";
	public static final String MOO1_DEFAULT		= "SETTINGS_MOD_DEFAULT_IS_MOO1";
	public static final String ROTP_DEFAULT		= "SETTINGS_MOD_DEFAULT_IS_ROTP";
	private String selectedDefault = FUSION_DEFAULT;
	
	public void selectedDefault(String mode)	{ selectedDefault = mode.toUpperCase(); }
	public String  defVal()		{ return selectedDefault; }
	public boolean isFusion()	{ return defVal().equals(FUSION_DEFAULT); }
	public boolean isRotp()		{ return defVal().equals(ROTP_DEFAULT); }
	public boolean isMoo1()		{ return defVal().equals(MOO1_DEFAULT); }
	public String getSettingName()	{
		switch (defVal()) {
			case MOO1_DEFAULT:	return "MoO1";
			case ROTP_DEFAULT:	return "RotP";
			case FUSION_DEFAULT:
			default:				return "Fusion";
		}		
	}
	public void setFromSettingName(String val)	{
		switch (val.toUpperCase()) {
			case "MOO":
			case "MOO1": selectedDefault(MOO1_DEFAULT);	 return;
			case "ROTP": selectedDefault(ROTP_DEFAULT);	 return;
			case "FUSION":
			default:	 selectedDefault(FUSION_DEFAULT); return;
		}		
	}

	public int defaultMaxBases() {
		switch (defVal()) {
			case ROTP_DEFAULT:	return 1;
			default:				return 0;
		}
	}
}

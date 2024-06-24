package rotp.ui.util;

public final class LinkValue {
	private float	floatValue;
	private int		intValue;
	private String	stringValue; // never null
	private int type;

	public LinkValue()				{ type = -1; }
	public LinkValue(float val)		{ type = 0; floatValue  = val;}
	public LinkValue(int val)		{ type = 1; intValue	= val; }
	public LinkValue(String val)	{ type = 2; stringValue = val==null? "" : val; }
	public Float	floatValue()	{ return type==0? floatValue : null; }
	public Integer	intValue()		{ return type==1? intValue : null; }
	public String	stringValue()	{ return type==2? stringValue : null; }
	public boolean	equals(LinkValue val)	{
		if (type != val.type)
			return false;
		switch (type) {
			case 0: return floatValue == val.floatValue;
			case 1: return intValue   == val.intValue;
			case 2: return stringValue.equals(val.stringValue);
		}
		return false;
	}
	public float getDiff(float toRemove) { return floatValue - toRemove; }
	public int	 getDiff(int   toRemove) { return intValue   - toRemove; }
	Boolean isPositiveDiff(LinkValue toRemove) {
		if (type != toRemove.type)
			return null;
		switch (type) {
			case 0: return floatValue > toRemove.floatValue;
			case 1: return intValue   > toRemove.intValue;
			case 2: return null;
		}
		return false;
	}
	@Override
	public String toString() {
		switch (type) {
			case -1: return "Empty";
			case 0:  return "Float = "	 + floatValue;
			case 1:  return "Integer = " + intValue;
			case 2:  return "String = "	 + stringValue;
		}
		return "LinkValue toString Error:";
	}
}

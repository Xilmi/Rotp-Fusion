package rotp.model.galaxy;

import java.io.Serializable;

import rotp.util.Base;

public class ObjectNamedByKey implements NamedObject, Base, Serializable {
	private static final long serialVersionUID = 1L;
	private final String key;
	public ObjectNamedByKey (String key)	{ this.key = key; }
	@Override public String name()			{ return text(key); }
}

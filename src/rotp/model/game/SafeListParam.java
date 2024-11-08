package rotp.model.game;

import java.util.ArrayList;
import java.util.Collection;

import rotp.ui.util.IParam;

public class SafeListParam extends ArrayList<IParam>{
	public final String name;
    public SafeListParam(String name) { this.name = name; }
    public SafeListParam(String name, Collection<IParam> c) {
    	this(name);
    	addAll(c);
    }
    public SafeListParam(Collection<IParam> c) {
    	this("");
    	addAll(c);
    }
    public boolean sameAs(SafeListParam list)	{
    	if (name.isEmpty())
    		return false;
    	if (name.equals(list.name))
    		return size() == list.size();
    	return false;
    }
	@Override public IParam get(int id) 		{
		if (id<0 || size() == 0)
			return null;
		if (id>=size())
			return super.get(0);
		return super.get(id);
	}
	@Override public boolean addAll(Collection<? extends IParam> c) 	{
		if (c == null)
			return false;
		return super.addAll(c);
	}
}

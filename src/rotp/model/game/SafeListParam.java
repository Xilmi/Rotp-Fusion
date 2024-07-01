package rotp.model.game;

import java.util.ArrayList;
import java.util.Collection;

import rotp.ui.util.IParam;

public class SafeListParam extends ArrayList<IParam>{
    public SafeListParam() {}
    public SafeListParam(Collection<IParam> c) { addAll(c); }

	@Override public IParam get(int id) {
		if (id<0 || size() == 0)
			return null;
		if (id>=size())
			return super.get(0);
		return super.get(id);
	}
}

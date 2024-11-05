package rotp.model.game;

import java.util.ArrayList;

import rotp.ui.util.IParam;

public class SafeListPanel extends ArrayList<SafeListParam>{
	public final String name;
	public SafeListPanel(String name) {
		this.name = name;
	}
	 @Override public SafeListParam get(int id) {
		if (id<0 || size() == 0)
			return new SafeListParam(name + id);
		if (id>=size())
			return super.get(0);
		return super.get(id);
	}
	public SafeListParam getList() {
		SafeListParam paramList = new SafeListParam(name);
		for ( SafeListParam list : this )
			for (IParam param : list)
				if (param != null && !param.isTitle())
					paramList.add(param);
		return paramList;
	}
}

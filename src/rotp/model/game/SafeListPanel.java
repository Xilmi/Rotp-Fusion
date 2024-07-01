package rotp.model.game;

import java.util.ArrayList;

import rotp.ui.util.IParam;

public class SafeListPanel extends ArrayList<SafeListParam>{
	 @Override public SafeListParam get(int id) {
		if (id<0 || size() == 0)
			return new SafeListParam();
		if (id>=size())
			return super.get(0);
		return super.get(id);
	}
	public SafeListParam getSingleList() {
		SafeListParam paramList = new SafeListParam();
		for ( SafeListParam list : this )
			for (IParam param : list)
				if (param != null && !param.isTitle())
					paramList.add(param);
		return paramList;
	}
}

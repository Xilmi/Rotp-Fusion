
/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *	 https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mod.br.profileManager;

import static br.profileManager.src.main.java.Validation.History.Default;

import br.profileManager.src.main.java.AbstractParameter;
import br.profileManager.src.main.java.AbstractT;
import br.profileManager.src.main.java.T_String;
import br.profileManager.src.main.java.Validation;
import rotp.ui.util.ParamAAN2;

/**
 * @author BrokenRegistry
 * For Parameters ParamAAN2
 */
public class AAN2Param extends 
		AbstractParameter <String, Validation<String>, ClientClasses> {

	private final ParamAAN2 param;
	
	AAN2Param(ClientClasses go, ParamAAN2 tech) {
		super( tech.getCfgLabel(),
				new Validation<String>(
						new T_String(tech.get()),
						tech.getOptions()));
		this.param = tech;
		setHistoryCodeView(Default, tech.defaultValue());
		setDefaultRandomLimits(ParamAAN2.ALWAYS, ParamAAN2.NEVER);
	}
	@Override public AbstractT<String> getFromGame (ClientClasses go) {
		return new T_String();
	}
	@Override public void putToGame(ClientClasses go, AbstractT<String> value) {}
	@Override public AbstractT<String> getFromUI (ClientClasses go) {
		return new T_String(param.get());
	}
	@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
		param.set(value.getCodeView());
	}
	@Override public void initComments() {}
}

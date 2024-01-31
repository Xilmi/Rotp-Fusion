/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.model.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rotp.model.game.IGameOptions;

import static rotp.Rotp.random;

public class AIList extends LinkedList<AIEntry>{
	public AIEntry entry(int id)		{
		for (AIEntry entry : this)
			if (entry.id == id)
				return entry;
		return IGameOptions.defaultAI;
	}
	public AIEntry entry(String key)	{
		for (AIEntry entry : this)
			if (entry.aliensKey.equals(key)
					|| entry.playerKey.equals(key))
				return entry;
		return IGameOptions.defaultAI;
	}
	public String playerKey(int id)		{ return entry(id).playerKey; }
	public String aliensKey(int id)		{ return entry(id).aliensKey; }
	public String name(int id)			{ return entry(id).name(); }
	public String name(String key)		{ return entry(key).name(); }
	public int	  id(String key)		{ return entry(key).id;  }
	public List<String> getAliens()		{
		List<String> list = new ArrayList<>();
		for (AIEntry entry : this)
			list.add(entry.aliensKey);
		return list;
	}
	public List<String> getAutoPlay()	{
		List<String> list = new ArrayList<>();
		for (AIEntry entry : this)
			list.add(entry.playerKey);
		return list;
	}
	public List<String> getNames()		{
		List<String> list = new ArrayList<>();
		for (AIEntry entry : this)
			list.add(entry.name());
		return list;
	}
	public int random()					{ return get(random.nextInt(size())).id; }
}


/**
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

package br.profileManager.src.main.java;

/**
 * @author BrokenRegistry
 * For basic User Entry Lines
 */
class LineString {

    // ==================================================
	// Variables Properties
    //
	// Keep the initializations for Junit test
	private static String keyValueSeparator  = ":";
	private static String valueSpacer        = " ";
	private static String lineSplitPosition  = "16";
	private static String commentEndPosition = "30";
	
	private String key;
	private String value;
	private String comment;

   	// ==========================================================
    // Constructors
    //
	/**
	 * Base constructor
	 */
	protected LineString() {
		set();
	}
	/**
	 * Constructor for full Entry line to analyze
	 * @param line the user entry line {@code String}
	 */
	protected LineString(String line) {
		set(line);
	}
	/**
	 * Constructor for Key - Value entry
	 * @param key   the key or name {@code String}
	 * @param value the value {@code String}
	 */
	protected LineString(String key, String value) {
		set(key, value);
	}
	/**
	 * Constructor for Key - Value - Comment entry
	 * @param key     the key or name {@code String}
	 * @param value   the value {@code String}
	 * @param comment a comment {@code String}
	 */
	protected LineString(String key, String value, String comment) {
		set(key, value, comment);
	}
   	// ==========================================================
    // Initialization Methods
    //
	/**
	 * To be notified that config has been updated
	 */
	static void newConfig(PMconfig PM) {
		keyValueSeparator  = PM.getConfig("keyValueSeparator");
		valueSpacer        = PM.getConfig("valueSpacer");
		lineSplitPosition  = PM.getConfig("lineSplitPosition");
		commentEndPosition = PM.getConfig("commentEndPosition");
	}
	/**
	 * Reset the line
	 */
	private LineString set() {
		key = "";
		value = "";
		comment = "";
		return this;
	}
	/**
	 * Reset the line with the new line
	 * @param line the new line {@code String}
	 */
	private LineString set(String line) {
 		if (PMutil.isBlank(line)) {
 			set();
 			}
 		// Get the comment if one
 		comment(WriteUtil.extractComment(line));
 		// Split the Key and the value
		String[] list = WriteUtil.removeComment(line).split(keyValueSeparator, 2);
		key(list[0]);
		if (list.length == 2) {
			value(list[1]);
		} 
		else {
			value = "";
		}
		return this;
	}
	/**
	 * Reset the line with the new Key - Value entry
	 * @param key   the key or name {@code String}
	 * @param value the value {@code String}
	 */
	private LineString set(String key, String value) {
		key(key);
		value(value);
		comment = "";
		return this;
	}
	/**
	 * Reset the line with the new Key - Value - Comment entry
	 * @param key     the key or name {@code String}
	 * @param value   the value {@code String}
	 * @param comment a comment {@code String}
	 */
	private LineString set(String key, String value, String comment) {
		key(key);
		value(value);
		comment(comment);
		return this;
	}
   	// ==========================================================
    // Setters and Getters 
    //
	protected LineString key(String newName) { 
		key = PMutil.clean(newName);
		return this;
	}
	protected LineString value(String newValue) { 
		value = PMutil.clean(newValue);
		return this;
	}
	protected LineString comment(String newComment) { 
		comment = PMutil.clean(newComment);
		return this;
	}

	protected String key()     { return key; }
	protected String value()   { return value; }
	protected String comment() { return comment; }
	
	protected boolean isBlankValue() { return value.isBlank(); }

   	// ==========================================================
    // Overriders
    //
	@Override public String toString() {
		String out = "";
		out += String.format(keyFormat(), key);
		out += value;
		if (!comment.isBlank()) {
			out = String.format(keyValueFormat(), out);
			if (!" ".equals(PMutil.getLastChar(out))) {
				out += " ";
			}
			out += WriteUtil.toComment(comment);		
		}
		return out;
	}

   	// ==========================================================
    // Private Methods
    //
    private static String keyFormat() { 
		return "%-" + lineSplitPosition + "s"
					+ keyValueSeparator + valueSpacer;
	}
    private static String keyValueFormat() { 
		return "%-" + commentEndPosition + "s";
	}

  	// ==========================================================
    // Public Static Methods
    //
	/**
	 * Format to line with the new Key - Value - Comment entry
	 * @param key     the key or name {@code String}
	 * @param value   the value {@code String}
	 * @param comment a comment {@code String}
	 * @return Formated line
	 */
	public static String lineFormat(String key, String value, String comment) {
		return new LineString(key, value, comment).toString();
	}
	/**
	 * Format to line with the new Key - Value entry
	 * @param key     the key or name {@code String}
	 * @param value   the value {@code String}
	 * @return Formated line
	 */
	public static String lineFormat(String key, String value) {
		return new LineString(key, value).toString();
	}
	/**
	 * Reformat to line with line entry
	 * @param line     the line to split and reformat;
	 * @return Reformatted line
	 */
	public static String lineFormat(String line) {
		return new LineString(line).toString();
	}
}

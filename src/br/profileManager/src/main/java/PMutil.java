
/**
 * Licensed under the GNU General License, Version 3 (the "License");
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author BrokenRegistry
 * common tools for the configuration package
 */
public class PMutil {

	/**
	 * Upper case value accepted as true entry
	 */
	static final 
	List<String> YES_LIST = List.of("YES", "TRUE");
	/**
	 * Upper case value accepted as false entry
	 */
	static final 
	List<String> NO_LIST = List.of("NO", "FALSE");
	/**
	 * All Upper case value accepted as {@code Boolean} entry
	 */
	static final 
	List<String> BOOLEAN_LIST = List.of("YES", "NO", "TRUE", "FALSE");
	/**
	 * Word Splitter REGEX
	 */
	static final String  
	CAPITALIZE_REGEX  = "((?<= )|(?<=_)|(?<=-)|(?<=/)|(?<=\\[))";

	private static boolean defaultCaseSensitivity = false;
	private static boolean forceCreationMissingProfile = true;
	
    // ==================================================
    // Constructors (blocked)
    //
	private PMutil() {}
	
    // ==================================================
    // Setters and Getters
    //
	/**
	 * Set the global Case Sensitivity
	 * @param newValue the new {@code Boolean} defaultCaseSensitivity
	 */
	static void setDefaultCaseSensitivity(boolean newValue) {
		defaultCaseSensitivity = newValue;
	}

	/**
	 * Get the global Case Sensitivity
	 * @return defaultCaseSensitivity
	 */
	static boolean getDefaultCaseSensitivity() {
		return defaultCaseSensitivity;
	}

	/**
	 * Set if missing configurations must be added
	 * @param newValue the new {@code Boolean} forceCreationMissingProfile
	 */
	static void setForceCreationMissingProfile(boolean newValue) {
		forceCreationMissingProfile = newValue;
	}

	/**
	 * Get if missing profiles must be added
	 * @return forceCreationMissingProfile
	 */
	static boolean getForceCreationMissingProfile() {
		return forceCreationMissingProfile;
	}

	// ==================================================
    // Generic Converters with default values
    //
	/**
	 * Convert String to {@code Byte} without throwing errors
	 * they are replaced by returning a default value
     * @param source the {@code String} containing 
     *               the {@code Byte} representation to be parsed.
     * @param defaultValue the {@code Byte} containing return on error value.
     * @return the {@code Byte} found in the string
     *  or the on error value if no {@code Byte} was found
	 */
	static Byte getOrDefault(String source, Byte defaultValue) {
		return toByteOrDefault(source, defaultValue);
	}

	/**
	 * Convert String to {@code Short} without throwing errors
	 * they are replaced by returning a default value
     * @param source the {@code String} containing 
     *               the {@code Short} representation to be parsed.
     * @param defaultValue the {@code Short} containing return on error value.
     * @return the {@code Short} found in the string
     *  or the on error value if no {@code Short} was found
	 */
	static Short getOrDefault(String source, Short defaultValue) {
		return toShortOrDefault(source, defaultValue);
	}

	/**
	 * Convert String to {@code Integer} without throwing errors
	 * they are replaced by returning a default value
     * @param source the {@code String} containing 
     *               the {@code Integer} representation to be parsed.
     * @param defaultValue the {@code Integer} containing return on error value.
     * @return the {@code Integer} found in the string
     *  or the on error value if no {@code Integer} was found
	 */
	static Integer getOrDefault(String source, Integer defaultValue) {
		return toIntegerOrDefault(source, defaultValue);
	}

	/**
	 * Convert String to {@code Long} without throwing errors
	 * they are replaced by returning a default value
     * @param source the {@code String} containing 
     *               the {@code Long} representation to be parsed.
     * @param defaultValue the {@code Long} containing return on error value.
     * @return the {@code Long} found in the string
     *  or the on error value if no {@code Long} was found
	 */
	static Long getOrDefault(String source, Long defaultValue) {
		return toLongOrDefault(source, defaultValue);
	}

	/**
	 * Convert String to {@code Float} without throwing errors
	 * they are replaced by returning a default value
     * @param source the {@code String} containing 
     *               the {@code Float} representation to be parsed.
     * @param defaultValue the {@code Float} containing return on error value.
     * @return the {@code Float} found in the string
     *  or the on error value if no {@code Float} was found
	 */
	static Float getOrDefault(String source, Float defaultValue) {
		return toFloatOrDefault(source, defaultValue);
	}

	/**
	 * Convert the source {@code String} to {@code Double}
	 * and return defaultValue if none was found
	 * @param source the {@code String} to convert
	 * @param defaultValue the {@code Double} to return if none in source 
	 * @return the converted {@code Double} value, defaultValue if none
	 */
	static Double getOrDefault(String source, Double defaultValue) {
		return toDoubleOrDefault(source, defaultValue);
	}

	/**
	 * Convert the source {@code String} to {@code Boolean}
	 * and return defaultValue if none was found
	 * @param source the {@code String} to convert
	 * @param defaultValue the {@code Boolean} to return if none in source 
	 * @return the converted {@code Boolean} value, defaultValue if none
	 */
	static Boolean getOrDefault(String source, Boolean defaultValue) {
    	if (containsIgnoreCase(YES_LIST, source)) {
    		return true;
    	}
    	if (containsIgnoreCase(NO_LIST, source)) {
    		return false;
    	}
        return defaultValue;
	}

	/**
	 * Check the validity of the source and return a valid one
	 * @param source the {@code String} to validate
	 * @param defaultValue the {@code String} to return if none in source 
	 * @return the source or the default value
	 */
	static String getOrDefault(String source, String defaultValue) {
    	if (isBlank(source)) {
    		return defaultValue;
    	}
        return source;
	}

	// ==================================================
    // Some List Methods
    //
	
	/**
	 * Add an element to the list and return the list (instead of a boolean)
	 * @param list
	 * @param element
	 * @return the new list
	 */
	public static List<String> addToList(List<String> list, String element) {
		list.add(element);
		return list;
	}

	// ==================================================
    // Some String Methods
    //
	/**
	 * Split string to not be longer than maxLineLength,
	 * the splitting can only be done at splitter positions.
	 * @param input the {@code String} to process
	 * @param splitter the {@code String} delimiter
	 * @param maxLineLength  the {@code int} max line length
	 * @param keepSplitters  the {@code boolean} 
	 * <b>true</b> keep all delimiters 
	 * <b>false</b> trim and remove duplicates 
	 * @return the new split lines
	 */
	static String addLinebreaks(String input, String splitter,
								int maxLineLength, boolean keepSplitters) {
	    List<String> lines = new ArrayList<String>();
	    String[] elements;
		String line = "";

		if (keepSplitters) {
			String regex = "((?<=" + splitter + ")|(?=" + splitter + "))";
			elements = input.split(regex);
			for (String element : elements) {
		        if (line.length() + element.length() > maxLineLength) {
		        	lines.add(line);
		        	line = element;
		        } else {
		        	line += element;
		        }
			}
		} else { // keepSplitters = false 
			elements = input.split(splitter);
			int slen = splitter.length();
			for (String element : elements) {
				if (line.length() + slen + element.length() > maxLineLength) {
		        	lines.add(line);
		        	line = element;
		        } else {
		        	if (line.isEmpty()) {
		        		line = element;
		        	} else {
		        		line += splitter + element;
		        	}
		        }
			}
		}
		if (!line.isEmpty()) {
			lines.add(line);
		}
		return String.join(System.lineSeparator(), lines);
	}
	/**
	 * Remove first Space if one. 
	 * Originally done to restore original comment
	 * after removing the comment key, as one space
	 * was added after the key.
	 * @param string the {@code String} to process
	 * @return the processed  {@code String}
	 */
	static String removeFirstSpace (String string) {
		if (string == null ) {
			return null;
		}
		if (string.isEmpty() ) {
			return "";
		}
		if (string.charAt(0) == ' ') {
			return string.substring(1);
		}
		return string;
	}
	/**
	 * Remove last Space if one. Never null.
	 * @param string the {@code String} to process
	 * @return the processed  {@code String}
	 */
	static String removeLastSpace (String string) {
		if (string == null || string.isEmpty() ) {
			return "";
		}
		if (string.charAt(string.length()-1) == ' ') {
			return string.substring(0, string.length()-1);
		}
		return string;
	}
	/**
	 * Get the last char of the {@code String}  
	 * @param string the {@code String} to process
	 * @return the last char as {@code String} to allow <b>null</b>
	 */
	static String getLastChar(String string) {
		if (string == null || string.isEmpty()) {
			return null;
		}
		return string.substring(string.length() - 1);
	}
	/**
	 * Convert {@code objects} to  {@code String}
	 * null {@code objects} are replaced by Empty {@code String}
	 * @param obj the {@code objects} to process
	 * @return the processed {@code String}
	 */
	static String neverNull(Object obj) {
		if (obj == null) { 
			return "";
		}
		return obj.toString(); 
    }
	/**
	 * Convert {@code objects} to  {@code String} and strip them.
	 * null {@code objects} are replaced by Empty {@code String}
	 * @param obj the {@code objects} to process
	 * @return the cleaned {@code String}
	 */
	public static String clean(Object obj) {
		if (obj == null) { 
			return "";
		}
		return obj.toString().strip(); 
    }
	/**
	 * Strip and convert to upper case
	 * @param source the {@code String} to process
	 * @return Upper Case of stripped string, never null
	 */
	static String toKey(String source) {
		return clean(source).toUpperCase();
	}
	/**
	 * Strip and return in lower case with first char to upper case, never null
	 * @param source the {@code String} to process
	 * @return the processed {@code String} Sentence
	 */
	static String toSentence(String source) {
		String result = "";
		String value = clean(source).toLowerCase();
		if (value.length() > 0) {
			String[] elements = value.split("[a-z]", 1);
			for(String s : elements) {
				result += s.substring(0, 1).toUpperCase();
				result += s.substring(1);
			}
		}
		return result.strip();
	}
	/**
	 * Strip and return every word capitalized, never null
	 * @param source the {@code String} to process
	 * @return the processed {@code String}
	 */
	public static String capitalize(String source) {
		String result = "";
		String value = clean(source);
		if (value.length() > 0) {
			String[] elements = value.toLowerCase().split(CAPITALIZE_REGEX);
			for(String s : elements) {
				result += s.substring(0, 1).toUpperCase();
				result += s.substring(1);
			}
		}
		return result;
	}
	/**
	 * Strip and return every word capitalized, never null
	 *  or only first word
	 * @param source the {@code String} to process
	 * @param onlyFirstWord ... every word otherwise
	 * @return the processed {@code String}
	 */
	static String capitalize(String source, boolean onlyFirstWord) {
			if (onlyFirstWord) {
				return toSentence(source);
			}
			else {
				return capitalize(source);				
			}
	}
	/**
	 * @param str    Containing String
	 * @param target String to find
	 * @return The number of Occurrence
	 */
	public static int countStringOccurrence(String str, String target) {
	    return (str.length() - str.replace(target, "").length()) / target.length();
	}
	/**
	 * Convert a {@code String} with several "_" to a more user friendly one
	 * @param option the {@code String} Option 
	 * @param minLength the minimal length of the two las elements String
	 * @return  the capitalized last element of the {@code String} (after "_")
	 */
	static String suggestedUserViewFromCodeView (Object value, int minLength) {
		if (value == null) {
			return "null";
		}
		String codeView = value.toString();
		if (codeView == null) {
			return "null";
		}
		String[] elements = codeView.split("_", 0);
		int last = elements.length-1;
		if (last > 1 && 
				(elements[last-1].length() <= minLength
				|| elements[last].length() <= minLength)) {
			return capitalize(elements[last-1]) + "_" + capitalize(elements[last]);
		}
		return capitalize(elements[last]);
	}
	/**
	 * Convert a {@code String} with several "_" to a more user friendly one
	 * @param option the {@code String} Option 
	 * @return  the capitalized last element of the {@code String} (after "_")
	 */
	static String suggestedUserViewFromCodeView (Object value) {
		return suggestedUserViewFromCodeView (value, 1);
	}
	/**
	 * Convert a {@code String} with several "_" to a more user friendly one
	 * @param list the {@code List<String>} Option 
	 * @param minLength the minimal length of the two las elements String
	 * @return  the capitalized last element of the {@code String} (after "_")
	 */
	public static List<String> suggestedUserViewFromCodeView (List<?> list, int minLength) {
		List<String> result = new ArrayList<String>();
		if (list != null) {
			for (Object value : list) {
				result.add(suggestedUserViewFromCodeView(value, minLength));
				}
		}
		return result;
	}
	/**
	 * Convert a {@code String} with several "_" to a more user friendly one
	 * @param list the {@code List<String>} Option 
	 * @return  the capitalized last element of the {@code String} (after "_")
	 */
	public static List<String> suggestedUserViewFromCodeView (List<?> list) {
		return suggestedUserViewFromCodeView (list, 1);
	}

	// ==================================================
    // Math Methods
    //
	/**
	 * Convert a probability density to
	 * a probability cumulative sum
	 * @param density the probability density
	 * @return the probability cumulative sum Normalized to 1
	 */
	public static float[] probDensityToCumul(float[] density) {
    	float[] cumSum = new float[density.length];
    	// Non normalized sum 
    	float total = 0;
        for (int i = 0; i < density.length; i++) {
            total += density[i];
            cumSum[i] = total;
        }
        // Normalize
        for (int i = 0; i < density.length; i++) {
        	cumSum[i] = cumSum[i] / total;
        }
        return cumSum;
    }
	
	/**
	 * Convert a probability cumulative sum to
	 * a probability density
	 * @param cumSum the probability cumulative sum 
	 * @return the probability density
	 */
	public static float[] probCumulToDensity(float[] cumSum) {
		if (cumSum == null || cumSum.length == 0) {
			return cumSum; // return to the sender!
		}
    	float[] density = new float[cumSum.length];
    	density[0] = cumSum[0];
        for (int i = 1; i < cumSum.length; i++) {
            density[i] = cumSum[i] - cumSum[i-1];
        }
        return density;
    }
	
	/**
	 * Compare the {@code Integer} value to the limits and return a value inside them.
	 * <br> If value is null limits average will be returned
	 * <br> If all parameters are null
	 * @param  value the value to test
	 * @param  limit1 the first limit, assumed as min if the other is null
	 * @param  limit2 the second limit, assumed as max if the other is null
	 * @return the validated value, null if all parameters are null
	 */
	static Integer validateLimits(Integer value, Integer limit1, Integer limit2) {
		if (value == null) {
			if (limit1 == null) {
				return limit2;
			}
			if (limit2 == null) {
				return limit1;
			}
			return (limit1 + limit2)/2;
		}
		// The value is not null
		if (limit1 == null) {
			return min(value, limit2);
		}
		if (limit2 == null) {
			return max(value, limit1);
		}
		// no null parameters
		value = max(value, min(limit1, limit2));
		return min(value, max(limit1, limit2));
	}

	/**
	 * Compare the two {@code Integer} value and return the biggest one.
	 * If value are null the other one win. If both are null return null
	 * @param  value1 the first value to compare
	 * @param  value2 the second value to compare
	 * @return the biggest value
	 */
	static Integer max(Integer value1, Integer value2) {
		// If one is null, return the other, return null if both are null
		if (value1 == null) {
			return value2;
		}
		if (value2 == null) {
			return value1;
		}
		if (value1 >= value2) {
			return value1;
		}
		return value2;
	}

	/**
	 * Compare the two {@code Integer} value and return the smallest one.
	 * If value are null the other one win. If both are null return null
	 * @param  value1 the first value to compare
	 * @param  value2 the second value to compare
	 * @return the smallest value
	 */
	static Integer min(Integer value1, Integer value2) {
		// If one is null, return the other, return null if both are null
		if (value1 == null) {
			return value2;
		}
		if (value2 == null) {
			return value1;
		}
		if (value1 <= value2) {
			return value1;
		}
		return value2;
	}

	/**
	 * Compare the {@code Float} value to the limits and return a value inside them.
	 * <br> If value is null limits average will be returned
	 * <br> If all parameters are null
	 * @param  value the value to test
	 * @param  limit1 the first limit, assumed as min if the other is null
	 * @param  limit2 the second limit, assumed as max if the other is null
	 * @return the validated value, null if all parameters are null
	 */
	static Float validateLimits(Float value, Float limit1, Float limit2) {
		if (value == null) {
			if (limit1 == null) {
				return limit2;
			}
			if (limit2 == null) {
				return limit1;
			}
			return (limit1 + limit2)/2;
		}
		// The value is not null
		if (limit1 == null) {
			return min(value, limit2);
		}
		if (limit2 == null) {
			return max(value, limit1);
		}
		// no null parameters
		value = max(value, min(limit1, limit2));
		return min(value, max(limit1, limit2));
	}

	/**
	 * Compare the two {@code Float} value and return the biggest one.
	 * If value are null the other one win. If both are null return null
	 * @param  value1 the first value to compare
	 * @param  value2 the second value to compare
	 * @return the biggest value
	 */
	static Float max(Float value1, Float value2) {
		// If one is null, return the other, return null if both are null
		if (value1 == null) {
			return value2;
		}
		if (value2 == null) {
			return value1;
		}
		if (value1 >= value2) {
			return value1;
		}
		return value2;
	}

	/**
	 * Compare the two {@code Double} value and return the smallest one.
	 * If value are null the other one win. If both are null return null
	 * @param  value1 the first value to compare
	 * @param  value2 the second value to compare
	 * @return the smallest value
	 */
	static Float min(Float value1, Float value2) {
		// If one is null, return the other, return null if both are null
		if (value1 == null) {
			return value2;
		}
		if (value2 == null) {
			return value1;
		}
		if (value1 <= value2) {
			return value1;
		}
		return value2;
	}	

	/**
	 * Compare the {@code Double} value to the limits and return a value inside them.
	 * <br> If value is null limits average will be returned
	 * <br> If all parameters are null
	 * @param  value the value to test
	 * @param  limit1 the first limit, assumed as min if the other is null
	 * @param  limit2 the second limit, assumed as max if the other is null
	 * @return the validated value, null if all parameters are null
	 */
	static Double validateLimits(Double value, Double limit1, Double limit2) {
		if (value == null) {
			if (limit1 == null) {
				return limit2;
			}
			if (limit2 == null) {
				return limit1;
			}
			return (limit1 + limit2)/2;
		}
		// The value is not null
		if (limit1 == null) {
			return min(value, limit2);
		}
		if (limit2 == null) {
			return max(value, limit1);
		}
		// no null parameters
		value = max(value, min(limit1, limit2));
		return min(value, max(limit1, limit2));
	}

	/**
	 * Compare the two {@code Double} value and return the biggest one.
	 * If value are null the other one win. If both are null return null
	 * @param  value1 the first value to compare
	 * @param  value2 the second value to compare
	 * @return the biggest value
	 */
	static Double max(Double value1, Double value2) {
		// If one is null, return the other, return null if both are null
		if (value1 == null) {
			return value2;
		}
		if (value2 == null) {
			return value1;
		}
		if (value1 >= value2) {
			return value1;
		}
		return value2;
	}

	/**
	 * Compare the two {@code Double} value and return the smallest one.
	 * If value are null the other one win. If both are null return null
	 * @param  value1 the first value to compare
	 * @param  value2 the second value to compare
	 * @return the smallest value
	 */
	static Double min(Double value1, Double value2) {
		// If one is null, return the other, return null if both are null
		if (value1 == null) {
			return value2;
		}
		if (value2 == null) {
			return value1;
		}
		if (value1 <= value2) {
			return value1;
		}
		return value2;
	}

	// ==================================================
    // Random Generation Methods
    //
	/**
	 * Generate a random {@code Boolean}
	 * @return a random {@code Boolean}
	 */
	static Boolean getBooleanRandom() {
		return ThreadLocalRandom.current().nextBoolean();
	}		

	/**
	 * Check boundaries to avoid error throwing 
	 * and generate random {@code Double} number
	 * @param min inclusive {@code Double} minimum bound
	 * @param max exclusive {@code Double} maximum bound
	 * @return random {@code Double} value in the specified range
	 * <br> if min = max : return min
	 */
	static Double nextRandomDouble(Double min, Double max) {
		if (isFiniteDouble(min) && isFiniteDouble(max)) { // also test for null
			if (max.doubleValue() == min.doubleValue()) {
				return min;
			}
			if (max < min) {
				return ThreadLocalRandom.current().nextDouble(max, min);
			}
			return ThreadLocalRandom.current().nextDouble(min, max);
		}
		return null; // what else?
	}

	/**
	 * Check boundaries to avoid error throwing 
	 * and generate random {@code Float} number
	 * @param min inclusive {@code Float} minimum bound
	 * @param max exclusive {@code Float} maximum bound
	 * @return random {@code Float} value in the specified range
	 * <br> if min = max : return min
	 */
	static Float nextRandomFloat(Float min, Float max) {
		if (isFiniteFloat(min) && isFiniteFloat(max)) { // also test for null
			if (max.floatValue() == min.floatValue()) {
				return min;
			}
			if (max < min) {
				Double D =  ThreadLocalRandom.current().nextDouble(max, min);
				return D.floatValue();
			}
			Double D = ThreadLocalRandom.current().nextDouble(min, max);
			return D.floatValue();
		}
		return null; // what else?
	}

	/**
	 * Check boundaries to avoid error throwing 
	 * and generate random {@code Long} number
	 * @param min inclusive minimum bound
	 * @param max exclusive maximum bound
	 * @return random {@code Long} value in the specified range
	 * <br> if min = max : return min
	 */
	static Long nextRandomLong(Long min, Long max) {
		if (min == null || max == null) {
			return null; // what else?
		}
		if (max.longValue() == min.longValue()) {
			return min;
		}
		if (max < min) {
			return ThreadLocalRandom.current().nextLong(max, min);
		}
		return ThreadLocalRandom.current().nextLong(min, max);
	}

	/**
	 * Check boundaries to avoid error throwing 
	 * and generate random {@code Double} number
	 * @param min inclusive {@code Double} minimum bound
	 * @param max exclusive {@code Double} maximum bound
	 * @return random {@code Double} value in the specified range
	 * <br> if min = max : return min
	 */
	static Double getRandom(Double min, Double max) {
		return nextRandomDouble(min, max);
	}
	
	/**
	 * Check boundaries to avoid error throwing 
	 * and generate random {@code Float} number
	 * @param min inclusive {@code Float} minimum bound
	 * @param max exclusive {@code Float} maximum bound
	 * @return random {@code Float} value in the specified range
	 * <br> if min = max : return min
	 */
	static Float getRandom(Float min, Float max) {
		return nextRandomFloat(min, max);
	}
	
	/**
	 * Check boundaries to avoid error throwing 
	 * and generate random {@code Long} number
	 * @param min inclusive {@code Long} minimum bound
	 * @param max exclusive {@code Long} maximum bound
	 * @return random {@code Long} value in the specified range
	 * <br> if min = max : return min
	 */
	static Long getRandom(Long min, Long max) {
		return nextRandomLong(min, max);
	}
	
	/**
	 * Check boundaries to avoid error throwing 
	 * and generate random {@code Integer} number
	 * @param min inclusive {@code Integer} minimum bound
	 * @param max exclusive {@code Integer} maximum bound
	 * @return random {@code Integer} value in the specified range
	 * <br> if min = max : return min
	 */
	public static Integer getRandom(Integer min, Integer max) {
		return nextRandomLong(min.longValue(), max.longValue()).intValue();
	}
	
	// ==================================================
    // Tests Methods
    //
	/**
     * Test if a {@code String} in {@code List<String>} contains a {@code String}
     * the case not being important
     * <br> null contains null and {@code String} contains null
	 * @param Container the containing {@code String}
	 * @param Element   the contained  {@code String}
	 * @param isCaseSensitive ...
	 * @param isEqual if <b>false</b> then contains
     * @return <b>true</b> if the conditions are verified
     */
	static boolean genericTest (String Container, String Element,
            				boolean isCaseSensitive, boolean isEqual) {
		if (isCaseSensitive) {
			if (isEqual) {
				return Container.equals(Element);
			}
			return Container.contains(Element);
		}
		if (isEqual) {
			return Container.equalsIgnoreCase(Element);
		}
		return PMutil.containsIgnoreCase(Container, Element);
	}

	/**
     * Test if a {@code String} in {@code List<String>} contains a {@code String}
     * the case not being important
     * <br> null contains null and {@code String} contains null
     * @param list      the containing {@code List<String>}
     * @param element   the contained {@code String}
     * @return true if the conditions are verified
     */
	static Boolean anyContainsIgnoreCase(List<String> list, String element) {
		if (element == null) {
			return true;
		}
		if (list == null) {
			return false;
		}
		for (String entry : list) {
			if (containsIgnoreCase(entry, element)) {
				return true;
			}
		}
		return false;
	}

	/**
     * Test if a {@code List<String>} contains a {@code String}
     * the case not being important
     * @param list      the containing {@code List<String>}
     * @param element   the contained {@code String}
     * @return true if the conditions are verified
     */
	static Boolean containsIgnoreCase(List<String> list, String element) {
		if (list == null || element == null) {
			return false;
		}
		for (String entry : list) {
			if (entry.equalsIgnoreCase(element)) {
				return true;
			}
		}
		return false;
	}

	/**
     * Test if the longest {@code String} contains the other
     * the case not being important
     * <br> null contains null and {@code String} contains null
	 * @param str1 first {@code String} to test
	 * @param str2 second {@code String} to test
     * @return true if the conditions are verified
     */
	static Boolean isContainedIgnoreCase(String str1, String str2) {
		if (str1 == null || str2 == null) {
			return true;
		}
		if (str2.length() < str1.length()) {
			return containsIgnoreCase(str1, str2);
		}
		return containsIgnoreCase(str2, str1);
	}

	/**
     * Test if a {@code String} contains another {@code String} 
     * the case not being important
     * <br> null contains null and {@code String} contains null
     * @param container the containing {@code String}
     * @param element   the contained {@code String}
     * @return true if the conditions are verified
     */
	static Boolean containsIgnoreCase(String container, String element) {
		if (element == null) {
			return true;
		}
		if (container == null) {
			return false;
		}
		return container.toUpperCase().contains(element.toUpperCase());
	}

	/**
	 * Check for Empty or null
	 * @param source The {@code String} to analyze  
	 * @return true if Empty or null
	 */
	static boolean isEmpty(String source) {
	    return (source == null || source.isEmpty());
    }

	/**
	 * Check for Blank, Empty or null
	 * @param source The {@code String} to analyze  
	 * @return true if Blank, Empty or null
	 */
	static boolean isBlank(String source) {
	    return (source == null || source.isBlank());
    }

	/**
	 * check if is member of BOOLEAN_LIST
	 * @param source The {@code String} to analyze
	 * @return true if one valid {@code Boolean} word is found 
	 */
	static boolean testForBoolean(String source) {
        return BOOLEAN_LIST.contains(clean(source).toUpperCase());
    }

	/**
	 * Validate if {@code String} could be converted or rounded
	 * to {@code Integer} then validate if the result is
	 * inside the boundaries
	 * @param source the {@code String} to analyze
	 * @param min    the lower boundary
	 * @param max    the upper boundary
	 * @return       is Valid
	 */
	static boolean testIntegerBoundaries(String source, int min, int max) {
		Integer value = toInteger(source);
		return (value != null
				&& value >= min
				&& value <= max);
	}

	/**
	 * Validate if {@code String} could be converted 
	 * or rounded to Integer
	 * @param source the {@code String} to analyze
	 * @return {@code Boolean} may be converted
	 */
	static boolean testForInteger(String source) {
		return toInteger(source) != null;
	}

	/**
	 * Test if String is Numeric Value
	 * @param string the {@code String} to be tested.
     * @return true if {@code Double} compatible value was found
	 */
	static boolean testForNumeric(String string) {
		return toDouble(string) != null;
    }

	/**
	 * Test if String is usable Numeric Value: Not NAN nor Infinity
	 * @param string the {@code String} to be tested.
	 * @return true if Finite {@code Double} was found
	 */
	static boolean testForFiniteNumeric(String string) {
		return isFiniteDouble(toDouble(string));
    }

	/**
	 * Check {@code Double} for null NaN or Infinity
	 * @param value the {@code Double} to be checked
     * @return the {@code Boolean} result
	 */
	static Boolean isFiniteDouble(Double value) {
		return (value != null && Double.isFinite(value));
	}

	// ==============================================================
    // String Conversion Tools
    //
	/**
	 * Check {@code Float} for null NaN or Infinity
	 * @param value the {@code Float} to be checked
     * @return the {@code Float} result
	 */
	static Boolean isFiniteFloat(Float value) {
		return (value != null && Float.isFinite(value));
	}
	/**
	 * Convert {@code String} to {@code Boolean}
	 * @param string 
	 * @return the {@code Boolean} value, {@code null} if none
	 */
	static Boolean toBoolean(String string) {
		if (string != null) {
	    	if (YES_LIST.contains(toKey(string))) {
	    		return true;
	    	}
	    	if (NO_LIST.contains(toKey(string))) {
	    		return false;
	    	}
		}
        return null;
	}

	/**
	 * Convert a {@code Boolean} to {@code String}
	 * @param   value   the {@code Boolean} to be converted
	 * @return the boolean value as {@code String}.
	 *  <br>   - true  = "YES"
	 *  <br>   - false = "NO"
	 *  <br>   - null  = "null"
	 */
	public static String toYesNoString(Boolean value) {
		if (value == null) {
			return "";
		}
		return value ? "YES" : "NO";
	}

	/**
	 * Convert NaN and Infinity are converted to Null
     * @param value the {@code Double} to be checked and adjusted.
     * @return the {@code Double} true numeric or null
	 */
	static Double toFiniteDouble(Double value) {
		return isFiniteDouble(value) ? value : null;
	}

	/**
	 * Convert String to Double without throwing errors
	 * they are replaced by returning a {@code null} value
	 * NaN and Infinity are converted to Null
     * @param string the {@code String} containing the {@code Double} representation to be parsed.
     * @return the {@code Double} represented by the string argument in
     *         the specified radix, or {@code null} if no {@code Double} was found
	 */
	static Double toFiniteDouble(String string) {
		return toFiniteDouble(toDouble(string));
	}

	/**
	 * Convert String to Double without throwing errors
	 * they are replaced by returning a {@code null} value
     * @param string the {@code String} containing the {@code Double} representation to be parsed.
     * @return the {@code Double} found in the string
     *         or {@code null} if no {@code Double} was found
	 */
	static Double toDouble(String string) {
		return toDoubleOrDefault(string, (Double) null);
	}

	/**
	 * Convert String to Double without throwing errors
	 * they are replaced by returning a default value
     * @param string the {@code String} containing the {@code Double} representation to be parsed.
     * @param onWrong the {@code Double} containing return on error value.
     * @return the {@code Double} found in the string
     *         or the on error value if no {@code Double} was found
	 */
	static Double toDoubleOrDefault(String string, Double onWrong) {
		if (string == null) {
			return onWrong;
		}
		try {
			return Double.valueOf(string);
		} catch (Exception e) {
			return onWrong;
		}
	}

	/**
	 * Convert String to {@code Integer} without throwing errors
	 * they are replaced by returning a {@code null} value
     * @param string the {@code String} containing the 
     *               {@code Integer} representation to be parsed.
     * @return       the {@code Integer} found in the string
     *                or {@code null} if no {@code Integer} was found
	 */
	static Float toFloat(String string) {
		return toFloatOrDefault(string, (Float) null);
	}

	/**
	 * Convert String to {@code Float} without throwing errors
	 * they are replaced by returning a default value
     * @param string the {@code String} containing 
     *               the {@code Float} representation to be parsed.
     * @param onWrong the {@code Float} containing return on error value.
     * @return the {@code Float} found in the string
     *  or the on error value if no {@code Float} was found
	 */
	static Float toFloatOrDefault(String string, Float onWrong) {
		Double dbl = toDouble(string);
		if (dbl != null 
				&& dbl <= Float.MAX_VALUE 
				&& dbl >= -Float.MAX_VALUE) { 
			return dbl.floatValue();
		}
		return onWrong;
	}

	/**
	 * Convert (round) {@code Double} to {@code Long} or return a default value 
	 * @param value   the {@code Double} to be converted
	 * @param onWrong the value to return if the conversion fail
	 * @return the converted value or onWrong
	 */
	static Long toLong(Double value, Long onWrong) {
		if (value != null
				&& value <= Long.MAX_VALUE 
				&& value >= Long.MIN_VALUE) {
			return Math.round(value);
		}
		return onWrong;
	}

	/**
	 * Convert String to {@code Long} without throwing errors
	 * they are replaced by returning a {@code null} value
     * @param string the {@code String} containing the {@code Long} representation to be parsed.
     * @return the {@code Long} found in the string
     *         or {@code null} if no {@code Long} was found
	 */
	static Long toLong(String string) {
		return toLongOrDefault(string, (Long) null);
	}

	/**
	 * Convert String to {@code Long} without throwing errors
	 * they are replaced by returning a default value
     * @param string the {@code String} containing the 
     *               {@code Long} representation to be parsed.
     * @param onWrong the {@code Long} containing return on error value.
     * @return the {@code Long} found in the string
     *  or the on error value if no {@code Long} was found
	 */
	static Long toLongOrDefault(String string, Long onWrong) {
		if (string == null) {
			return onWrong;
		}
		// Try standard String to Long
		try {
			return Long.valueOf(string);
		} catch (Exception e) { }
		// Try thru String to Double
		return toLong(toDoubleOrDefault(string, (Double) null), onWrong);
	}

	/**
	 * Convert String to {@code Integer} without throwing errors
	 * they are replaced by returning a {@code null} value
     * @param string the {@code String} containing the 
     *               {@code Integer} representation to be parsed.
     * @return       the {@code Integer} found in the string
     *                or {@code null} if no {@code Integer} was found
	 */
	static Integer toInteger(String string) {
		return toIntegerOrDefault(string, (Integer) null);
	}

	/**
	 * Convert String to {@code Integer} without throwing errors
	 * they are replaced by returning a default value
     * @param string the {@code String} containing 
     *               the {@code Integer} representation to be parsed.
     * @param onWrong the {@code Integer} containing return on error value.
     * @return the {@code Integer} found in the string
     *  or the on error value if no {@code Integer} was found
	 */
	static Integer toIntegerOrDefault(String string, Integer onWrong) {
		Long lg = toLong(string);
		if (lg != null 
				&& lg <= Integer.MAX_VALUE 
				&& lg >= Integer.MIN_VALUE) { 
			return lg.intValue();
		}
		return onWrong;
	}

	/**
	 * Convert String to {@code Short} without throwing errors
	 * they are replaced by returning a {@code null} value
     * @param string the {@code String} containing the 
     *               {@code Short} representation to be parsed.
     * @return       the {@code Short} found in the string
     *                or {@code null} if no {@code Short} was found
	 */
	static Short toShort(String string) {
		return toShortOrDefault(string, (Short) null);
	}

	/**
	 * Convert String to {@code Short} without throwing errors
	 * they are replaced by returning a default value
     * @param string the {@code String} containing 
     *               the {@code Short} representation to be parsed.
     * @param onWrong the {@code Short} containing return on error value.
     * @return the {@code Short} found in the string
     *  or the on error value if no {@code Short} was found
	 */
	static Short toShortOrDefault(String string, Short onWrong) {
		Long lg = toLong(string);
		if (lg != null 
				&& lg <= Short.MAX_VALUE 
				&& lg >= Short.MIN_VALUE) { 
			return lg.shortValue();
		}
		return onWrong;
	}

	/**
	 * Convert String to {@code Byte} without throwing errors
	 * they are replaced by returning a {@code null} value
     * @param string the {@code String} containing the 
     *               {@code Byte} representation to be parsed.
     * @return       the {@code Byte} found in the string
     *                or {@code null} if no {@code Byte} was found
	 */
	static Byte toByte(String string) {
		return toByteOrDefault(string, (Byte) null);
	}

	/**
	 * Convert String to {@code Byte} without throwing errors
	 * they are replaced by returning a default value
     * @param string the {@code String} containing 
     *               the {@code Byte} representation to be parsed.
     * @param onWrong the {@code Byte} containing return on error value.
     * @return the {@code Byte} found in the string
     *  or the on error value if no {@code Byte} was found
	 */
	static Byte toByteOrDefault(String string, Byte onWrong) {
		Long lg = toLong(string);
		if (lg != null 
				&& lg <= Byte.MAX_VALUE 
				&& lg >= Byte.MIN_VALUE) { 
			return lg.byteValue();
		}
		return onWrong;
	}
}

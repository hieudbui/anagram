package com.ibotta.anagram

public class AnagramCalculator {

	/**
	 *
	 * @author Francois Rousseau
	 */
	public enum Method {
		SORT(){
			/**
			 * O(mlogm) time, O(m) if bin sort.
			 */
			@Override
			String getKey(String s) {
				char[] chars = s.toCharArray()
				Arrays.sort(chars)
				return new String(chars)
			}
		},
		PRIME(){
			// Mapping ASCII letter to prime number based on English letter frequency.
			// The more frequent, the lower prime number.
			// http://www.math.cornell.edu/~mec/2003-2004/cryptography/subs/frequencies.html
			private def asciiPrimes = [
					5, 71, 41, 29, 2, 47, 61, 23, 11, 97, 79, 31, 43,
					13, 7, 67, 89, 19, 17, 3, 37, 73, 59, 83, 53, 101]
			/**
			 * O(m) time, but arbitrarily large words will require arbitrarily large integers.
			 */
			@Override
			String getKey(String s) {
				int p = 1
				for (char c : s.toCharArray()) {
					if (Character.isAlphabetic((int) c)) {
						//we are ignoring special characters and numeric for now
						def index = (int) c - (int) ('a' as char)
						p *= asciiPrimes[index]
					}
				}
				return Integer.toString(p)  // for consistency with the other method
			}
		};

		abstract String getKey(String s)
	};

	/**
	 * Let n = number of strings and m = average length of a string.
	 * Time complexity:  O(nm)
	 * Space complexity: O(nm)
	 *
	 * Uses an hashmap with keys computed using the specified method and values the list of words
	 * sharing the same key, i.e. begin anagrams.
	 */
	public Map<String, Set<String>> calculate(Collection<String> words, Method m) {
		Map<String, Set<String>> groups = new HashMap<String, Set<String>>()
		for (String word : words) {
			String key = calculate(word, m)
			if (!groups.containsKey(key)) {
				groups.put(key, new HashSet<String>())
			}
			groups.get(key).add(word)
		}
		return groups
	}

	public String calculate(String word, Method m) {
		m.getKey(word.toLowerCase())
	}

}
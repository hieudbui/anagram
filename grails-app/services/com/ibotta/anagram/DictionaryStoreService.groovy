package com.ibotta.anagram

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.annotation.PostConstruct
import java.util.concurrent.ConcurrentHashMap

class DictionaryStoreService {

	static Logger logger = LoggerFactory.getLogger(DictionaryStoreService.class)

	private Map<String, Set<String>> anagrams = new ConcurrentHashMap()
	private def calculator = new AnagramCalculator()

	@PostConstruct
	def load() {
		def start = System.currentTimeMillis()
		def lines = new File('dictionary.txt').readLines()
		anagrams << calculator.calculate(lines, AnagramCalculator.Method.PRIME)
		def end = System.currentTimeMillis()
		logger.debug("loading and indexing took ${end - start} ms");
	}

	void add(Collection<String> words) {
		def indexedWords = calculator.calculate(words, AnagramCalculator.Method.PRIME)
		indexedWords.each {
			def key = it.key
			def value = it.value
			anagrams.put(key, value + (anagrams.get(key) ?: []))
		}
	}

	//no guarantee on order yet
	List<String> search(String word, Integer limit, Boolean properNoun) {
		def key = calculator.calculate(word, AnagramCalculator.Method.PRIME)
		List<String> value = ((anagrams[key] ?: []) - word) as List
		if (properNoun) {
			value = value.findAll { Character.isUpperCase(it.charAt(0)) }
		}
		value[(0..<(limit > value.size() ? value.size() : limit))]
	}

	void deleteAll() {
		anagrams.clear()
	}

	void delete(String word, boolean allAnagrams = false) {
		def key = calculator.calculate(word, AnagramCalculator.Method.PRIME)
		def value = (anagrams[key] ?: [])
		if (!allAnagrams) {
			anagrams[key] = (value - word)
		} else {
			anagrams.remove(key)
		}

	}

	//should cache this and expires the stats on stores modification
	Map stats() {
		def stats = [:]
		stats.count = anagrams.values().inject(0) { result, entry -> result + entry.size() } ?: 0
		def allWords = anagrams.values().flatten()
		stats.min = allWords.min { it.length() }?.length() ?: 0
		stats.max = allWords.max { it.length() }?.length() ?: 0
		stats.average = (allWords.sum { it.length() } ?: 0) / (stats.count ?: 1)
		stats
	}

	Set<String> most() {
		anagrams.max { it.value.size() }?.value ?: []
	}

	boolean allAnagrams(Collection<String> words) {
		1 == calculator.calculate(words, AnagramCalculator.Method.PRIME).size()
	}

	//ugh this is ugly
	Collection<Set<String>> groups(Integer minSizeLimit) {
		anagrams.values().findAll { it.size() >= minSizeLimit }
	}

}

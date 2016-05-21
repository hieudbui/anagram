package com.ibotta.anagram

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(AnagramsController)
class AnagramsControllerSpec extends Specification {

	DictionaryStoreService service = Mock()

	def setup() {
		controller.dictionaryStoreService = service
	}

	def cleanup() {
	}

	void "search with no limit and no proper noun"() {
		given:
		def word = 'word'
		def words = ['word1', 'word2']
		1 * service.search(word, Integer.MAX_VALUE, false) >> words

		when:
		controller.search('word', null, null)

		then:
		words == response.json.anagrams
	}

	void "search with limit and proper noun"() {
		given:
		def word = 'word'
		def words = ['word1', 'word2']
		def limit = 5
		def properNoun = true
		1 * service.search(word, limit, properNoun) >> words

		when:
		controller.search('word', limit, properNoun)

		then:
		words == response.json.anagrams
	}

	void "most"() {
		given:
		def words = ['word1', 'word2']
		1 * service.most() >> words

		when:
		controller.most()

		then:
		words == response.json
	}

	//need to circle back to this
	//having difficult time stubbing with expected set as arg
	void "match"() {
		given:
		def words = ['word1', 'word2', 'word1', 'word3']
		1 * service.allAnagrams(_) >> true

		when:
		request.json = [words: words]
		controller.match()

		then:
		true as String == response.text
	}

	void "groups no size"() {
		given:
		def group1 = ['group', 'puorg'] as Set
		def group2 = ['read', 'daer'] as Set
		1 * service.groups(0) >> [group1, group2]

		when:
		controller.groups(null)

		then:
		[group1 as List, group2 as List] == response.json
	}

	void "groups with size"() {
		given:
		def size = 5
		def group1 = ['group', 'puorg'] as Set
		def group2 = ['read', 'daer'] as Set
		1 * service.groups(size) >> [group1, group2]

		when:
		controller.groups(size)

		then:
		[group1 as List, group2 as List] == response.json
	}

	void "delete"() {
		given:
		def word = 'word'

		when:
		controller.delete(word)

		then:
		1 * service.delete(word, true)
		200 == response.status
	}

}

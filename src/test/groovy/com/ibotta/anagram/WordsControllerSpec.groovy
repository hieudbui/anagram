package com.ibotta.anagram

import grails.converters.JSON
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(WordsController)
class WordsControllerSpec extends Specification {
	DictionaryStoreService service = Mock()

	def setup() {
		controller.dictionaryStoreService = service
	}

	def cleanup() {
	}

	void "add"() {
		given:
		def words = ['word', 'word2']

		when:
		request.json = [words: words] as JSON
		controller.add()

		then:
		1 * service.add(words)
		201 == response.status
	}

	void "delete a word"() {
		given:
		def word = "word"

		when:
		controller.delete(word)

		then:
		1 * service.delete(word)
		200 == response.status
	}

	void "delete all words"() {
		when:
		controller.deleteAll()

		then:
		1 * service.deleteAll()
		204 == response.status
	}

	void "stats on store"() {
		given:
		def stats = [name: 'value']
		1 * service.stats() >> stats

		when:
		controller.stats()

		then:
		stats == response.json
	}

	void "reload store"() {
		when:
		controller.reload()

		then:
		1 * service.reload()
		"reload successful" == response.text
	}


}

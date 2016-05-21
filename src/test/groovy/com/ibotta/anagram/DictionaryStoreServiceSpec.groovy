package com.ibotta.anagram

import org.springframework.core.io.ByteArrayResource
import spock.lang.Specification

class DictionaryStoreServiceSpec extends Specification {

	DictionaryStoreService service
	def words = ['read', 'eard', 'daer', 'raed']

	def setup() {
		service = new DictionaryStoreService()
		service.source = new ByteArrayResource(words.join(System.lineSeparator()).getBytes())
		service.load()
	}

	def cleanup() {
	}

	void "delete a word"() {
		when:
		service.delete("read")

		then:
		3 == service.stats().count
	}

	void "delete a word and all its anagrams"() {
		when:
		service.delete("read", true)

		then:
		0 == service.stats().count
	}

	void "delete all"() {
		when:
		service.deleteAll()

		then:
		def stats = service.stats()
		[stats.count, stats.min, stats.max, stats.average, stats.median].every { it == 0 }
	}

	void "stats"() {
		given:
		def words = ['a', 'ab', 'abc', 'abcd', 'abcde', 'abcdef']
		service.source = new ByteArrayResource(words.join(System.lineSeparator()).getBytes())
		service.reload()

		when:
		def stats = service.stats()

		then:
		6 == stats.count
		1 == stats.min
		6 == stats.max
		3.5 == stats.average
		4 == stats.median
	}

	void "groups"() {
		given:
		def words = ['abcde', 'edcba', 'abcdef', 'fedcba', 'abcfed']
		service.source = new ByteArrayResource(words.join(System.lineSeparator()).getBytes())
		service.reload()

		when:
		def groups = service.groups(minSizeLimit)

		then:
		expectedSize == groups.size()

		where:
		minSizeLimit | expectedSize
		2            | 2
		3            | 1
		5            | 0
		1            | 2
		0            | 2
	}

	void "most"() {
		given:
		def group1 = ['abcde', 'edcba']
		def group2 = ['abcdef', 'fedcba', 'abcfed']
		service.source = new ByteArrayResource((group1 + group2).join(System.lineSeparator()).getBytes())
		service.reload()

		when:
		def most = service.most()

		then:
		group2 as Set == most
	}

	void "search proper noun"() {
		given:
		def words = ['Jean', 'naej', 'jean']
		service.source = new ByteArrayResource(words.join(System.lineSeparator()).getBytes())
		service.reload()

		when:
		def foundWords = service.search("eajn", Integer.MAX_VALUE, true)

		then:
		1 == foundWords.size()
		'Jean' == foundWords.first()
	}

	void "search size limit"() {
		given:
		def words = ['jnae', 'naej', 'jean', 'other']
		service.source = new ByteArrayResource(words.join(System.lineSeparator()).getBytes())
		service.reload()

		when:
		def foundWords = service.search("eajn", sizeLimit, false)

		then:
		expectedSize == foundWords.size()
		expectedWords == foundWords

		where:
		sizeLimit | expectedSize | expectedWords
		0         | 0            | []
		1         | 1            | ['jean']
		2         | 2            | ['jean', 'jnae']
		5         | 3            | ['jean', 'jnae', 'naej']
	}

	void "search word is not its own anagram"() {
		given:
		def words = ['Jean', 'naej', 'aenj', 'other']
		service.source = new ByteArrayResource(words.join(System.lineSeparator()).getBytes())
		service.reload()

		when:
		def foundWords = service.search("aenj", Integer.MAX_VALUE, false)

		then:
		['Jean', 'naej'] == foundWords
	}

	void "add"() {
		given:
		def group1 = ['Jean', 'naej', 'aenj']
		def group2 = ['other']

		when:
		service.add(group1 + group2)

		then:
		group1.sort() == service.search('eanj', Integer.MAX_VALUE, false)
		group2 == service.search('rehto', Integer.MAX_VALUE, false)
	}
}

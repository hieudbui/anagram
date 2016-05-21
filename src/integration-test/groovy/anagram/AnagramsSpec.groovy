package anagram

import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.test.mixin.integration.Integration
import spock.lang.Specification

//integration annotation add support for starting a server
//the test will run in the same memory space as the app
//which is nice for rolling back, transactionality
//at the cost of startup time
@Integration
class AnagramsSpec extends Specification {
	def serverUrl = "http://localhost:8080"
	def anagramsEndpoint = "$serverUrl/anagrams"

	def setup() {
	}

	def cleanup() {
	}

	void "search for anagrams"() {
		given:
		RestBuilder rest = new RestBuilder()

		when:
		RestResponse response = rest.get("$anagramsEndpoint/read.json")

		then:
		response.status == 200
		['ared', 'daer', 'dare', 'dear'] == response.json.anagrams
	}

	void "delete a word"() {
		given:
		RestBuilder rest = new RestBuilder()

		when:
		RestResponse response = rest.delete("$anagramsEndpoint/read.json")

		then:
		response.status == 200

		when:
		response = rest.get("$anagramsEndpoint/read.json")

		then:
		response.status == 200
		[] == response.json.anagrams

	}
}

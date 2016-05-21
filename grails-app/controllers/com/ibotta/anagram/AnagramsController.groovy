package com.ibotta.anagram

import grails.converters.JSON

/**
 * this controller deals with anagrams related actions
 */
class AnagramsController {
	DictionaryStoreService dictionaryStoreService

	/**
	 * HATEOAS perhaps?
	 */
	def search(String word, Integer limit, Boolean properNoun) {
		def words = dictionaryStoreService.search(word, limit ?: Integer.MAX_VALUE, properNoun ?: false)
		withFormat {
			json {
				render([anagrams: words] as JSON)
			}
		}
	}

	//json response?
	def match() {
		def words = request.JSON.words as Set
		render(dictionaryStoreService.allAnagrams(words))
	}

	def most() {
		render dictionaryStoreService.most() as JSON
	}

	//cpu & memory issue here
	//have a min size perhaps?
	def groups(Integer size) {
		render(dictionaryStoreService.groups(size ?: 0) as JSON)
	}

	def delete(String word) {
		dictionaryStoreService.delete(word, true)
		withFormat {
			json {
				response.status = 200
			}
		}
	}
}

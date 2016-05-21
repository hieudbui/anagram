package com.ibotta.anagram

import grails.converters.JSON

/**
 * this controller deals with words related actions
 */
class WordsController {
	DictionaryStoreService dictionaryStoreService

	/**
	 * HATEOAS perhaps?
	 */
	def add() {
		dictionaryStoreService.add(request.JSON.words)
		withFormat {
			json {
				response.status = 201
			}
		}
	}

	def deleteAll() {
		dictionaryStoreService.deleteAll()
		withFormat {
			json {
				response.status = 204
			}
		}
	}

	def delete(String word) {
		dictionaryStoreService.delete(word)
		withFormat {
			json {
				response.status = 200
			}
		}
	}

	def stats() {
		render dictionaryStoreService.stats() as JSON
	}

	//json response?
	def reload() {
		dictionaryStoreService.reload()
		render "reload successful"
	}
}


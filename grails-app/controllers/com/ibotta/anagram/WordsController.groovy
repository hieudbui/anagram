package com.ibotta.anagram

import grails.converters.JSON

class WordsController {
	DictionaryStoreService dictionaryStoreService

	def stats() {
		render dictionaryStoreService.stats() as JSON
	}

	def reload() {
		dictionaryStoreService.load()
		render "reload successful"
	}

	def add() {
		dictionaryStoreService.add(request.JSON.words)
		response.status = 201
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
}


package anagram

class UrlMappings {

	static mappings = {
		//TODO
		//need to rework this to be less verbose

		"/anagrams/$word?(.$format)?"(controller: 'anagrams') {
			action = [GET: "search", DELETE: "delete"]
		}
		"/anagrams/most"(controller: "anagrams", action: 'most')
		"/anagrams/match"(controller: 'anagrams', action: 'match')
		"/anagrams/groups"(controller: 'anagrams', action: 'groups')

		"/words/reload"(controller: "words", action: 'reload')
		"/words/stats"(controller: "words", action: 'stats')

		"/words(.$format)?"(controller: "words") {
			action = [POST: "add", DELETE: "deleteAll"]
		}

		"/words/$word?(.$format)?"(controller: 'words') {
			action = [DELETE: "delete"]
		}
//		"/$controller/$action?/$id?(.$format)?" {
//			constraints {
//				// apply constraints here
//			}
//		}

		"/"(view: "/index")
		"500"(view: '/error')
		"404"(view: '/notFound')

	}
}

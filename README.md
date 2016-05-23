Anagram solution
================


# The Project

---

The following endpoints have been implemented
- `POST /words.json`: Takes a JSON array of English-language words and adds them to the corpus (data store).
- `GET /anagrams/:word.json`:
  - Returns a JSON array of English-language words that are anagrams of the word passed in the URL.
  - This endpoint should support an optional query param that indicates the maximum number of results to return.
- `DELETE /words/:word.json`: Deletes a single word from the data store.
- `DELETE /words.json`: Deletes all contents of the data store.
- Endpoint that returns a count of words in the corpus and min/max/median/average word length
- Respect a query param for whether or not to include proper nouns in the list of anagrams
- Endpoint that identifies words with the most anagrams
- Endpoint that takes a set of words and returns whether or not they are all anagrams of each other
- Endpoint to return all anagram groups of size >= *x*
- Endpoint to delete a word *and all of its anagrams*

Added the following endpoints

- Endpoint to reload store

## Run projects
- Run ./gradlew bootRun at root directory to start app
- Wait for the message "Grails application running"
- The app runs on port 8080.  I changed the ruby client script to use 8080 as well.

## Tests

- Unit tests are in src/test
- End-end tests  arein src/integration-test
- ./gradlew test integrationTest to run unit and integration test

## API Client

- I use postman to test the APIs.  Open up Anagram.postman_collection

## Documentation

Need to look into the various REST documentation solution (RAML, Spring REST)

## Known issues
- First request of dictionary cause the dictionary load (1000ms+)  
- Anagrams calculation could result in arithmetic overflow
- Performance issue in calculating store stats (300ms+)
- Performance issue on load (900ms+)

## Things to do
- Load the data in a separate thread on startup, which will eliminate the *hang* impression on the initial request of the dictionary store.
- More integration tests
- Profile memory usage
- Consider decomposing DictionaryStoreService
- Some of the endpoints do not honor content type request
- Cache stats
- Consider using map/reduce to load the dictionary file (split the file, process in parallel, merge into a map)

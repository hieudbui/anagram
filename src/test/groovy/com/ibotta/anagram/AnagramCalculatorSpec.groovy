package com.ibotta.anagram

import spock.lang.Specification
import spock.lang.Unroll

class AnagramCalculatorSpec extends Specification {
	AnagramCalculator calculator = new AnagramCalculator()

	@Unroll
	void "calculate word #word"() {
		when:
		def key = calculator.calculate(word, AnagramCalculator.Method.PRIME)

		then:
		"5510" == key

		where:
		word << ['read', 'daer', 'aedr', 'rdae']
	}

	@Unroll
	void "calculate same words #words"() {
		when:
		def key = calculator.calculate(words, AnagramCalculator.Method.PRIME).keySet().first()

		then:
		expectedKey == key

		where:
		words                            | expectedKey
		['read', 'daer', 'aedr', 'rdae'] | "5510"
		['test', 'tset', 'ttes', 'estt'] | "306"
	}

	@Unroll
	void "calculate fix words #words"() {
		when:
		def keyMap = calculator.calculate(words, AnagramCalculator.Method.PRIME)

		then:
		['read', 'aedr', 'rdae'] as Set == keyMap.get(expectedKey[0])
		['test', 'tset', 'ttes', 'estt'] as Set == keyMap.get(expectedKey[1])


		where:
		words                                                            | expectedKey
		['read', 'test', 'aedr', 'rdae', 'test', 'tset', 'ttes', 'estt'] | ["5510", "306"]
	}
}

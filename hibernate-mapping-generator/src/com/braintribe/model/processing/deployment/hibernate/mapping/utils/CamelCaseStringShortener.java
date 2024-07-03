// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.model.processing.deployment.hibernate.mapping.utils;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 
 */
public class CamelCaseStringShortener {

	private String shortenedString;
	private final int targetLength;
	private int currentLength;
	private final ShorteningStrategy shorteningStrategy;
	private final String[] words;

	public enum ShorteningStrategy {
		ABBREVIATE_THEN_STRIP_VOWELS;
	}
	
	public CamelCaseStringShortener(String string, int targetLength) {
		this(string, targetLength, ShorteningStrategy.ABBREVIATE_THEN_STRIP_VOWELS);
	}
	
	public CamelCaseStringShortener(String string, int targetLength, ShorteningStrategy shorteningStrategy) {
		this.targetLength = targetLength;
		this.currentLength = string.length();
		this.shorteningStrategy = shorteningStrategy;
		this.words = splitCamelCase(string);
	}
	
	public String shorten() {
		switch (shorteningStrategy) {
			case ABBREVIATE_THEN_STRIP_VOWELS:
				applyAbbreviation();
				applyVowelsRemoval();
				applyWordTruncation();
				applyFinalTruncation();
				break;
		}
		return getShortenedWord();
	}
	
	/**
	 * Abbreviate words, from longest to shortest
	 */
	private void applyAbbreviation() { 
		if (isDone()) return;
        final int[][] wordsMap = generateIndexLengthMap();
        for (final int[] map : wordsMap) {
			modifyWord(map[0], abbreviate(words[map[0]], 5));
            if (isDone()) break;
        }
	}
	
	private void applyVowelsRemoval() {
		if (isDone()) return;
		for (int i = 0; i < words.length && !isDone(); i++) {
			modifyWord(i, removeVowels(words[i]));
		}
	}
	
	/**
	 * 
	 */
	private void applyWordTruncation() { 
		if (isDone()) return;
        final int[][] wordsMap = generateIndexLengthMap();
        for (final int[] map : wordsMap) {
        	if (words[map[0]].length() <= 1) continue;
			modifyWord(map[0], words[map[0]].substring(0, words[map[0]].length()-1));
            if (isDone()) break;
        }
	}
	
	private void applyFinalTruncation() {
		if (isDone()) return;
		shortenedString = this.getShortenedWord().substring(0, targetLength);
	}
	
	private int[][] generateIndexLengthMap() { 
        final int[][] wordsMap = new int[this.words.length][];
        for (int i = 0; i < words.length; i++) {
        	wordsMap[i] = new int[] {i, this.words[i].length()};
        }
        Arrays.sort(wordsMap, new Comparator<int[]>() {
        	@Override
			public int compare(final int[] entry1, final int[] entry2) {
        		return Integer.valueOf(entry2[1]).compareTo(Integer.valueOf(entry1[1]));
        	}
        });
        return wordsMap;
	}
	
	/**
	 * Removes not-leading vowels from the given String.
	 */
	private static String removeVowels(String word) {
		return word.replaceAll("[aeiou](?!^)", "");
	}
	
	private static String abbreviate(String word, int minAbbreviationLength) {
		if (word.length() <= minAbbreviationLength) return word;
		int remainingChars = (word.length()/2);
		if (remainingChars < minAbbreviationLength) remainingChars = minAbbreviationLength;
		return word.substring(0, remainingChars).replaceAll("[aeiou]$", "");
	}
	
	private void modifyWord(int wordIndex, String newWord) {
		int wordLengthBefore = words[wordIndex].length();
		words[wordIndex] = newWord;
		this.currentLength-=wordLengthBefore-newWord.length();   
	}
	
	private boolean isDone() {
		return (this.currentLength <= this.targetLength);
	}

	private static String[] splitCamelCase(String camelCaseString) {
		if (camelCaseString == null) return null;
		return camelCaseString.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])");
	}
	
	private void renderShortenedWord() {
		StringBuilder sb = new StringBuilder();
		for (String s : this.words) {
			sb.append(s);
		}
		shortenedString = sb.toString();
	}
	
	private String getShortenedWord() {
		if (shortenedString == null) renderShortenedWord();
		return shortenedString;
	}
}

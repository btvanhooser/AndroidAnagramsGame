package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 5;
    private Random random = new Random();
    private HashSet<String> wordSet = new HashSet<String>();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            // Add word to list of all words
            String word = line.trim();
            wordSet.add(word);
            wordList.add(word);
            if (sizeToWords.get(word.length()) == null){
                ArrayList<String> temp = new ArrayList<>();
                temp.add(word);
                sizeToWords.put(word.length(),temp);
            }
            else
                sizeToWords.get(word.length()).add(word);

            // Add word to letterToWord
            String word_sorted = sortLetters(word);
            if (lettersToWord.get(word_sorted) == null) {
                ArrayList<String> values = new ArrayList<String>();
                values.add(word);
                lettersToWord.put(word_sorted, values);

            }
            else lettersToWord.get(word_sorted).add(word);
        }
    }

    public boolean isGoodWord(String word, String base) {
        if (word.contains(base))
            return false;
        else if (!wordSet.contains(word))
            return false;
        return true;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = lettersToWord.get(sortLetters(targetWord));
        return result;
    }

    private static String sortLetters(String word) {

        char[] word_array = word.toCharArray();
        Arrays.sort(word_array);
        String result = new String(word_array);
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String sorted = sortLetters(word);
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (char c : alphabet){
            ArrayList<String> temp = getAnagrams(sortLetters(sorted + c));
            if (temp != null)
                result.addAll(temp);
        }
        for (int i = 0; i < result.size(); i++){
            if (result.get(i).contains(word)){
                result.remove(i);
                i--;
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        Random rand = new Random();
        int i = rand.nextInt(9999+1);
        while ((getAnagramsWithOneMoreLetter(sortLetters(wordList.get(i))).size() < MIN_NUM_ANAGRAMS)
                && (wordList.get(i).length() < DEFAULT_WORD_LENGTH
                || wordList.get(i).length() > MAX_WORD_LENGTH)){
            i = ++i%wordList.size();
        }
        Log.d("Answer", getAnagramsWithOneMoreLetter(sortLetters(wordList.get(i))).toString());
        return wordList.get(i);
    }
}
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Jake Sonsini
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // Calls the makeWords recursion method
        makeWords("", letters);
        // YOUR CODE HERE — Call your recursive method!
    }
    public void makeWords(String word, String letters){
        // Base case checks if there are any letters left
        if (letters.equals("")){
            words.add(word);
            return;
        }
        // Call the method recursively by the amount of letters present
        for (int i = 0; i < letters.length(); i++){
            // Using the offset of letters (i) change what characters become the word
            makeWords((word + letters.substring(i, i + 1)), (letters.substring(0, i) + letters.substring(i + 1)));
        }
        // Add the altered word
        words.add(word);
    }
    public void sort() {
        // Call the mergesort function on the words list
        mergeSort(words, 0, words.size() - 1);
    }
    private static ArrayList<String> mergeSort(ArrayList<String> arr, int low, int high) {
        // Base case: if reach one word stop the recursion
        if (high - low == 0) {
            ArrayList<String> newArr = new ArrayList<String>();
            newArr.add(arr.get(low));
            return newArr;
        }
        // Create the medium variable that splits the array
        int med = (high + low) / 2;
        // Call the method with the left side cut and right side cut
        ArrayList<String> arr1 = mergeSort(arr, low, med);
        ArrayList<String> arr2 = mergeSort(arr, med + 1, high);
        // Merge both arrays
        return merge(arr1, arr2);
    }

    public static ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2){
        // Shift variables hold the location of each array
        int oneShift = 0;
        int twoShift = 0;
        // finalArr holds the final values
        ArrayList<String> finalArr = new ArrayList<String>();
        // Run the loop until it reaches the end of either array
        while(oneShift < arr1.size() && twoShift < arr2.size()){
            // Compare to see which is in order then add to final array and change shift value
            if (arr1.get(oneShift).compareTo(arr2.get(twoShift)) <= 0){
                finalArr.add(arr1.get(oneShift));
                oneShift++;
            }
            else{
                finalArr.add(arr2.get(twoShift));
                twoShift++;
            }

        }
        // After one shift finishes the array, add the rest of the other array to finalArr
        while (twoShift < arr2.size()){
            finalArr.add(arr2.get(twoShift));
            twoShift++;
        }
        while (oneShift < arr1.size()){
            finalArr.add(arr1.get(oneShift));
            oneShift++;
        }
        // Returns the finish product
        return finalArr;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    public void checkWords() {
        // Check each word
        for (int i = 0; i < words.size(); i++){
            // Call binary search
            if (!binarySearch(words.get(i))){
                words.remove(i);
                i--;
            }
        }
    }
    public static boolean binarySearch(String word){
        // Create high and low variables
        int high = DICTIONARY_SIZE - 1;
        int low = 0;
        // While the dictionary has more than one word to compare to repeat
        while (high >= low){
            // Make the mid variable
            int mid = (low + high) / 2;
            // If the word is less than, split the array the other side
            if (word.compareTo(DICTIONARY[mid]) < 0){
                high = mid - 1;
            }
            // If there is a match return true
            else if (word.equals(DICTIONARY[mid])){
                return true;
            }
            // If the word is greater than go to the lower side of array
            else{
                low = mid + 1;
            }
        }
        return false;
    }
    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}

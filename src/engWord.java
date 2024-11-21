import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

public class engWord{
	String name;
	String type;
	List<String> definitions;
	public static List<engWord> allWords = new ArrayList<>();
	public static Hashtable<String, engWord> wordDict = new Hashtable<>();
	public static Trie acTrie;
	private static Comparator<engWord> wordCompare = new Comparator<engWord>() {
		public int compare(engWord o1, engWord o2) {
			return o1.name.compareTo(o2.name);
		}
	};
	
	public engWord(String name) {
		this.name = name;
	}
	
	public engWord(String name, String type, List<String> defs) {
		this.name = name;
		this.type = type;
		this.definitions = defs;
		if(defs != null && getWord(this) == null) {
			//allWords.add(this);
			wordDict.put(this.name, this);
		}
	}
	
	public static List<String> getDefinition(engWord word){
		System.out.println(word.definitions);
		return word.definitions;
	}
	
	public static engWord getWord(String word) {
		return wordDict.get(word);
	}
	
	public static engWord getWord(engWord word) {
//		allWords.sort(wordCompare);
//		int idx = Collections.binarySearch(allWords, word, wordCompare);
//		if(idx < 0)
//			return null;
//		else
//			return allWords.get(idx);
		return wordDict.get(word.name);
	}
	
    public static void importDict(int limit) { // -1 to import all
		String delimiter = ",";
		String line; // hold each line read
		
		// Try-with-resources: Automatically close the BufferedReader after use
		try (BufferedReader br = new BufferedReader(new FileReader("OPTED-Dictionary.csv"))) {
			List<String> header = List.of(br.readLine().split(delimiter));
			int wordCol = header.indexOf("Word");
			int typeCol = header.indexOf("POS");
			int defCol = header.indexOf("Definition");
			int lineNum = 0;
			
			while ((line = br.readLine()) != null && lineNum != limit) {
				lineNum++;
				String[] values = line.split(delimiter);
				String word = values[wordCol];
				
				if(word.matches("\\w+-?\\w")) {
					new engWord(word.toLowerCase(), values[typeCol], List.of(values[defCol])); // adds each word to dictionary upon construction					
				}
			}
	    	List<String> words = new ArrayList<>();
//	    	for(engWord word : engWord.allWords) {
//	    		words.add(word.name);
//	    	}
	    	for(String word : engWord.wordDict.keySet() ) {
	    		words.add(word);
	    	}
	        //List<String> words = List.of("hello", "dog", "hell", "cat", "a", "hel","help","helps","helping");
	        acTrie = new Trie(words);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
    }
}

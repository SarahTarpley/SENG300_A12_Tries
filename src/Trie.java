import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class Trie {
 
    public class TrieNode {
        Map<Character, TrieNode> children;
        char c;
        boolean isWord;
 
        public TrieNode(char c) {
            this.c = Character.toLowerCase(c);
            children = new HashMap<>();
        }
 
        public TrieNode() {
            children = new HashMap<>();
        }
 
        public void insert(String word) {
        	word = word.toLowerCase();
            if (word == null || word.isEmpty())
                return;
            char firstChar = word.charAt(0);
            TrieNode child = children.get(firstChar);
            if (child == null) {
                child = new TrieNode(firstChar);
                children.put(firstChar, child);
            }
 
            if (word.length() > 1)
                child.insert(word.substring(1));
            else
                child.isWord = true;
        }
 
    }
 
    TrieNode root;
 
    public Trie(List<String> words) {
        root = new TrieNode();
        for (String word : words)
            root.insert(word);
 
    }
    
    public boolean find(String prefix, boolean exact) {
        TrieNode lastNode = root;
        for (char c : prefix.toCharArray()) {
            lastNode = lastNode.children.get(c);
            if (lastNode == null)
                return false;
        }
        return !exact || lastNode.isWord;
    }
 
    public boolean find(String prefix) {
        return find(prefix, false);
    }
 
    public void suggestHelper(TrieNode root, List<String> list, StringBuffer curr) {
        if (root.isWord) {
            list.add(curr.toString());
        }
 
        if (root.children == null || root.children.isEmpty())
            return;
 
        for (TrieNode child : root.children.values()) {
            suggestHelper(child, list, curr.append(child.c));
            curr.setLength(curr.length() - 1);
        }
    }
 
    public List<String> suggest(String prefix) {
        List<String> list = new ArrayList<>();
        TrieNode lastNode = root;
        StringBuffer curr = new StringBuffer();
        for (char c : prefix.toCharArray()) {
            lastNode = lastNode.children.get(c);
            if (lastNode == null)
                return list;
            curr.append(c);
        }
        suggestHelper(lastNode, list, curr);
        return list;
    }
    
    public List<String> suggestShortest(String prefix){
    	List<String> list = suggest(prefix);
    	list.sort(new Comparator<String>() {
	    	@Override
			public int compare(String o1, String o2) {
	    		Integer len1 = o1.length();
	    		Integer len2 = o2.length();
				return len1.compareTo(len2);
			}
    	});
    	
    	return list.subList(0, Math.min(list.size(), 10)) ;
    }
}


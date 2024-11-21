import java.awt.EventQueue;

public class Driver{
    public static void main(String[] args) {
    	
    	engWord.importDict(-1);
    	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AutoComplete frame = new AutoComplete();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
        //System.out.println(trie.suggestShortest("A"));
    }
}

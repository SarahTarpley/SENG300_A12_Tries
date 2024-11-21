import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneLayout;
import javax.swing.SpringLayout;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractListModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class AutoComplete extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField searchBar;
	boolean isNewSearch = true;
	String[] colNames = {"Word", "Type", "Definition"};
	DefaultTableModel resultDict = new DefaultTableModel(null, colNames);
	private JTable resultTable = new JTable(resultDict);
	String[] CurrentResults = new String[0];
	JScrollPane resultPane = new JScrollPane(resultTable);

	ScrollPaneLayout spl_resultPane = new ScrollPaneLayout();


	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AutoComplete() {
		resultPane.setLayout(spl_resultPane);
		setMinimumSize(new Dimension(1280, 720));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 720);
		getContentPane().setLayout(new GridLayout(1, 1, 0, 0));
		contentPane = new JPanel();
		contentPane.setBounds(new Rectangle(0, 0, 1280, 720));
		contentPane.setBackground(new Color(174, 178, 195));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setEnabled(true);
		contentPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(isNewSearch) {
					initializeSearch(e);
				}
			}
		});
		
		// north panel
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setBounds(new Rectangle(5, 5, getContentPane().getBounds().width, 250));
		panel.setPreferredSize(new Dimension(getContentPane().getBounds().width - 50, 250));
		panel.setBackground(contentPane.getBackground());
		
		// search results
		resultPane.enableInputMethods(false);
		resultPane.setBackground(new Color(255, 255, 255));
		resultPane.setBounds(new Rectangle(20, 275, 1230, getCustomDimensions(375).height));
		resultPane.setPreferredSize(getCustomDimensions(375));
		resultTable.setAutoCreateColumnsFromModel(true);
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		resultTable.getColumn("Definition").setPreferredWidth((resultPane.getWidth() / 3) * 2);
		resultTable.setFillsViewportHeight(true);
		resultTable.setAutoCreateRowSorter(true);
		resultTable.setRowSelectionAllowed(false);
		resultTable.setRowMargin(25);
		resultTable.setRowHeight(100);
		resultTable.setEnabled(false);
		resultTable.setFont(new Font("Calibri", Font.PLAIN, 18));
		resultPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		contentPane.add(resultPane);
		
		// layout
		SpringLayout spl = new SpringLayout();
		contentPane.setLayout(spl);
		//spl.putConstraint(SpringLayout.NORTH, panel, 0, SpringLayout.NORTH, contentPane);
		spl.putConstraint(SpringLayout.SOUTH, contentPane, 20, SpringLayout.SOUTH, resultPane);
		spl.putConstraint(SpringLayout.EAST, contentPane, 20, SpringLayout.EAST, resultPane);
		spl.putConstraint(SpringLayout.WEST, resultPane, 20, SpringLayout.WEST, contentPane);
		spl.putConstraint(SpringLayout.NORTH, resultPane, 250, SpringLayout.NORTH, panel);
//		spl.putConstraint(SpringLayout.NORTH, resultPane, 20, SpringLayout.SOUTH, panel);
//		spl.putConstraint(SpringLayout.SOUTH, panel, 20, SpringLayout.NORTH, resultPane);
//		spl.putConstraint(SpringLayout.SOUTH, resultTable, 0, SpringLayout.SOUTH, resultPane);
//		spl.putConstraint(SpringLayout.EAST, resultTable, 0, SpringLayout.EAST, resultPane);

		// search button
		JButton btnSearch = new JButton("Search");
		btnSearch.setForeground(new Color(255, 255, 255));
		btnSearch.setFont(new Font("Arial Black", Font.PLAIN, 14));
		btnSearch.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				resultDict.getDataVector().clear();
				//System.out.println(searchBar.getText());
				int i = 0;
				for(String word : CurrentResults) {
					engWord wo = engWord.getWord(word);
					if(wo != null) {
						StringBuilder def = new StringBuilder();
						Arrays.stream(wo.definitions.toArray(new String[0]))
							.map(x -> x.replace("\"", ""))
							.forEach(x -> def.append(x + "\r\n"));
						
						String[] row = {wo.name, wo.type.replace("\"", ""), def.toString()};
						if(wo.name.toLowerCase().equals(searchBar.getText())) {
							resultDict.insertRow(0, row);
						}
						else
							System.out.println(wo.name);
							resultDict.addRow(row);
						resultTable.doLayout();
					}
					i++;
				}
				resultTable.tableChanged(new TableModelEvent(resultDict));
			}
		});
		btnSearch.setBorder(new CompoundBorder(null, new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
		btnSearch.setMargin(new Insets(0, 0, 0, 0));
		btnSearch.setAlignmentY(0.0f);
		btnSearch.setBackground(new Color(194, 171, 160));
		btnSearch.setBounds(1110, 200, 100, 30);
		panel.add(btnSearch);
		
		// auto complete suggestions
		JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setPreferredSize(new Dimension(100, 0));
		list.setFixedCellWidth(-1);
		list.setBackground(contentPane.getBackground());
		list.setForeground(new Color(64, 64, 64));
		list.setFont(new Font("Calibri", Font.PLAIN, 32));
		list.setVisibleRowCount(1);
		list.setSize(new Dimension(1100, 50));
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setBounds(20, 160, panel.getWidth()-50, 35);
		list.setCellRenderer(new cellRender());
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String selectedText = list.getModel().getElementAt((list.locationToIndex(e.getPoint()))).toString();
				searchBar.setText(selectedText);
				btnSearch.doClick();
			}
		});
		panel.add(list);
		
		// search field
		searchBar = new JTextField();
		searchBar.setMargin(new Insets(5, 5, 5, 5));
		searchBar.setFont(new Font("Calibri", Font.PLAIN, 14));
		searchBar.setBounds(20, 200, 1090, 30);
		searchBar.setEnabled(true);
		searchBar.grabFocus();
		searchBar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(isNewSearch) {
					initializeSearch(e);
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				String search = searchBar.getText().toLowerCase();
				List<String> refList = new ArrayList<>();
				CurrentResults = engWord.acTrie.suggestShortest(search).toArray(new String[0]);
				engWord.acTrie.suggestShortest(search)
					.stream()
					.forEach(
						x -> refList.addAll(List.of(x, " "))
					);
				//System.out.println(search);
		        //System.out.println(refList);
				list.setModel(new AbstractListModel() {
					String[] values = refList.toArray(new String[0]);
					public int getSize() {
						return values.length;
					}
					public Object getElementAt(int index) {
						return values[index];
					}
				});
			}
		});
		searchBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(isNewSearch) {
					initializeSearch(e);
				}
			}
		});
		panel.setLayout(null);
		searchBar.setForeground(new Color(128, 128, 128));
		searchBar.setText(" Type here to search...");
		searchBar.setBackground(new Color(255, 255, 255));
		searchBar.setColumns(40);
		panel.add(searchBar);

		// title bar
		JTextPane txtpnAutocomplete = new JTextPane();
		txtpnAutocomplete.setDisabledTextColor(new Color(255, 255, 255));
		txtpnAutocomplete.setEnabled(false);
		txtpnAutocomplete.setEditable(false);
		txtpnAutocomplete.setForeground(new Color(255, 255, 255));
		txtpnAutocomplete.setFont(new Font("Chianti XBd BT", Font.BOLD, 60));
		txtpnAutocomplete.setText("Smart Search");
		txtpnAutocomplete.setBackground(contentPane.getBackground());
		txtpnAutocomplete.setBounds(435, 20, 420, 60);
		panel.add(txtpnAutocomplete);
	}
	
	// method to clear out the default search tip when user interacts
	public void initializeSearch(InputEvent e) {
			searchBar.setText("");
			searchBar.setEditable(true);
			searchBar.setForeground(new Color(0, 0, 0));
			isNewSearch = false;	
	}
	
	// method to calculate size against main frame
    private Dimension getCustomDimensions(int height){
    		double ratio = height/720.0;
            return new Dimension((int)(super.getContentPane().getBounds().width - 50), (int) (getContentPane().getSize().height * ratio));
    }
    
    // custom settings for the autosearch suggestions list
    // the list will be spaced with an empty item between each suggestion for visibility
    // blank items are set to be non-interactable
	class cellRender extends JLabel implements ListCellRenderer<Object>{

		@Override
		public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
	         String s = value.toString();
	         setText(s);
	         if (isSelected && s != " ") {
	             setBackground(list.getSelectionBackground());
	             setForeground(list.getSelectionForeground());
	         } else {
	             setBackground(list.getBackground());
	             setForeground(list.getForeground());
	         }
	         setVisible(true);
	         setEnabled(list.isEnabled());
	         setFont(list.getFont());
	         setOpaque(true);
	         return this;
		}
		
	}
}

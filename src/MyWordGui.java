import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MyWordGui {

	private static void constructGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		MyJFrame frame = new MyJFrame();
		frame.setVisible(true);

	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				constructGUI();
			}
		});

	}

}

class MyJFrame extends JFrame {
	public JLabel userWord;
	public JLabel answer;
	public JTextField word;

	public MyJFrame() {
		super();
		init();
	}

	private void init() {
		word = new JTextField();
		JButton btn1 = new JButton("Calculate");
		btn1.addActionListener(new MyButtonListener(this));
		userWord = new JLabel("Enter a word: ");
		answer = new JLabel("Answer: ");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(2, 2));
		this.add(userWord);
		this.add(word);
		this.add(btn1);
		this.add(answer);
		this.pack();
		this.setVisible(true);

	}
}

class MyButtonListener implements ActionListener {
	MyJFrame fr;

	public MyButtonListener(MyJFrame frame) {
		fr = frame;
	}

	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		Scanner inFS = null;
		String userInput = fr.word.getText();
		inFS = new Scanner(userInput);

		try {
			Document doc = Jsoup.connect("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm").get();
			Elements poem = doc.getElementsByClass("chapter");

			try {

				FileOutputStream fileStream = null;
				PrintWriter outFS = null;
				fileStream = new FileOutputStream("RavenPoem.txt");

				outFS = new PrintWriter(fileStream);
				outFS.println(poem.text());
				outFS.close();

				FileReader file = new FileReader("RavenPoem.txt");
				BufferedReader reader = new BufferedReader(file);

				Map<String, Integer> frequency = new HashMap<>();

				String line = reader.readLine();

				while (line != null) {

					// Processing each line and splitting to separate words
					// then storing those words into array

					if (!line.trim().equals("")) {
						String[] words = line.split(" ");

						for (String word : words) {

							if (word == null || word.trim().equals("")) {
								continue;
							}
							String processed = word.toLowerCase();

							// Removing special characters

							processed = processed.replaceAll("[^a-zA-Z0-9]", "");

							// searching for current word in keyset
							// if word is found, incrementing the integer value

							if (frequency.containsKey(processed)) {
								frequency.put(processed, frequency.get(processed) + 1);
							} else {
								frequency.put(processed, 1);

							}

						}

					}

					line = reader.readLine();

				}

				// System.out.println(frequency);

				// Finding most frequently used word by iterating over the keyset
				int mostFrequentlyUsed = 0;
				String theWord = null;

				for (String word : frequency.keySet()) {
					Integer theVal = frequency.get(word);
					if (theVal > mostFrequentlyUsed) {
						mostFrequentlyUsed = theVal;
						theWord = word;

					}

				}

				System.out.printf("The most frequently used word is '%s', %d, times", theWord, mostFrequentlyUsed);
				fr.answer.setText("The most frequently used word is " + theWord + " which occured " + mostFrequentlyUsed  + " times");

				System.out.println();

				// Sort HashMap by keys
				TreeMap<String, Integer> sorted = new TreeMap<>(frequency);
				Set<Entry<String, Integer>> mappings = sorted.entrySet();

				for (Entry<String, Integer> mapping : mappings) {

					System.out.println(mapping.getKey() + " ==> " + mapping.getValue());

				}

				System.out.println();
				System.out.println();

				// Sorting HashMap by values
				// no direct way to sort HashMap by values
				// need to create comparator
				// that takes Map.Entry object and arranges them in order
				// of increasing or decreasing value

				// Converting Set to List to use Collections Sort method
				List<Entry<String, Integer>> listOfEntries = new ArrayList<Entry<String, Integer>>(mappings);

				// Sorting HashMap by values using comparator
				Collections.sort(listOfEntries, Collections.reverseOrder(valueComparator));

				LinkedHashMap<String, Integer> sortedbyValue = new LinkedHashMap<String, Integer>(listOfEntries.size());

				// Copying entries from the sorted List to Map
				for (Entry<String, Integer> entry : listOfEntries) {

					sortedbyValue.put(entry.getKey(), entry.getValue());

				}

				System.out.println("HashMap after sorting entries by values");
				Set<Entry<String, Integer>> entrySetSortedByValue = sortedbyValue.entrySet();

				for (Entry<String, Integer> mapping : entrySetSortedByValue) {
					System.out.println(mapping.getKey() + " ==> " + mapping.getValue());

				}

				System.out.println();
				System.out.println();
				System.out.println("The top 20 word frequencies are: ");

				List<Entry<String, Integer>> sortedListOfEntries = new ArrayList<Entry<String, Integer>>(
						entrySetSortedByValue);

				for (int i = 0; i < 20; i++) {
					System.out.println(sortedListOfEntries.get(i));

				}

			} catch (Exception exc) {
				// TODO Auto-generated catch block
				exc.printStackTrace();
			}

		} catch (IOException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		}

	}

	static Comparator<Entry<String, Integer>> valueComparator = new Comparator<Entry<String, Integer>>() {

		@Override
		public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
			Integer v1 = e1.getValue();
			Integer v2 = e2.getValue();
			return v1.compareTo(v2);

		}
	};

}
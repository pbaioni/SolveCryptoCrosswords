package app.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import app.model.properties.GridProperties;
import app.utils.AppFiles;

public class Grid {

	private final static Logger LOGGER = Logger.getLogger(Grid.class);

	private GridProperties gridProperties;

	private List<String> cryptoRows;

	private List<String> cryptoColumns;

	private Map<String, String> wordsToDecode;

	private List<String> solutionRows;

	private Key solutionKey;
	

	public Grid(String filename, GridProperties gridProperties) {

		this.gridProperties = gridProperties;
		
		String gridPath = gridProperties.getPathToGrids() + filename.trim();
		this.cryptoRows = AppFiles.getFileAsLines(gridPath);

		this.cryptoColumns = new ArrayList<String>();

		wordsToDecode = new TreeMap<String, String>(new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				if (s1.length() > s2.length()) {
					return -1;
				} else if (s1.length() < s2.length()) {
					return 1;
				} else {
					return s1.compareTo(s2);
				}
			}
		});

		solutionKey = new Key(gridProperties);
		createCryptoColumns();
		fillWordsToDecode();
		updateWordsToDecode();

		LOGGER.info("Grid loaded");
		printNumericalGrid(cryptoRows);
	}

	private void createCryptoColumns() {

		List<Scanner> scannerList = new ArrayList<Scanner>();

		for (String row : this.cryptoRows) {
			scannerList.add(new Scanner(row));
		}

		//getting first element as reference for length
		Scanner referenceScanner = scannerList.get(0);

		//all the scanners have the same number of elements
		while (referenceScanner.hasNext()) {
			String letterSeparator = gridProperties.getLetterSeparator();
			StringBuilder builder = new StringBuilder();
			for (Scanner scanner : scannerList) {
				builder.append(scanner.next());
				builder.append(letterSeparator);
			}

			cryptoColumns.add(builder.toString().trim());
		}
		
		for (Scanner scanner : scannerList) {
			scanner.close();
		}

	}

	private void fillWordsToDecode() {

		String wordSeparator = gridProperties.getWordSeparator();
		//adding horizontal words
		for (String row : this.cryptoRows) {

			for (String s : row.split(wordSeparator)) {
				String numericalRelativeCrypto = s.trim();
				String word = decodeWord(numericalRelativeCrypto, "");
				// short words are unuseful for decoding purpose
				if (word.length() > gridProperties.getShortWordMaxLength()) {
					for (int i = 1; i < word.length() + 1; i++) {
						wordsToDecode.put(numericalRelativeCrypto, word);
					}
				}
			}
		}
		
		//adding vertical words
		for (String column : this.cryptoColumns) {

			for (String s : column.split(wordSeparator)) {
				String numericalRelativeCrypto = s.trim();
				String word = decodeWord(numericalRelativeCrypto, "");
				// short words are unuseful for decoding purpose
				if (word.length() > gridProperties.getShortWordMaxLength()) {
					for (int i = 1; i < word.length() + 1; i++) {
						wordsToDecode.put(numericalRelativeCrypto, word);
					}
				}
			}
		}
	}

	public void calculateSolution() {

		for (String row : cryptoRows) {
			solutionRows.add(decodeWord(row, " "));
		}
	}

	public void updateWordsToDecode() {
		for (Map.Entry<String, String> entry : wordsToDecode.entrySet()) {
			wordsToDecode.put(entry.getKey(), decodeWord(entry.getKey(), ""));
		}
	}

	private String decodeWord(String numericalRelativeCrypto, String separator) {
		Scanner scanner = new Scanner(numericalRelativeCrypto);
		StringBuilder builder = new StringBuilder();
		while (scanner.hasNext()) {
			builder.append(solutionKey.getLetterForNumber(scanner.next()));
			builder.append(separator);
		}
		scanner.close();
		return builder.toString();
	}

	public void printWordsToDecode() {
		LOGGER.info("Words:");
		for (Map.Entry<String, String> entry : wordsToDecode.entrySet()) {
			LOGGER.info(entry.getKey() + " : " + entry.getValue().toString());
		}
	}

	private void printNumericalGrid(List<String> list) {
		for (String line : list) {
			LOGGER.info(line);
		}
	}

	public void printSolution() {
		solutionRows = new ArrayList<String>();
		calculateSolution();
		LOGGER.info("");
		LOGGER.info("Solution key:");
		LOGGER.info(solutionKey.toString());
		LOGGER.info("");
		printWordsToDecode();
		LOGGER.info("");
		String space = "   ";
		LOGGER.info(space + "Solution:");
		LOGGER.info(space + "-----------------------------");
		for (String row : solutionRows) {
			LOGGER.info(space + "| " + row + "|");
		}
		LOGGER.info(space + "-----------------------------");
	}

	public Map<String, String> getWordsToDecode() {
		return wordsToDecode;
	}

	public void setSolutionKey(Key solutionKey) {
		this.solutionKey = solutionKey;
	}
}

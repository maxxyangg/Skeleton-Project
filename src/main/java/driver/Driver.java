package driver;

import java.awt.Desktop;
import java.awt.Dimension;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.json.JSONArray;
import org.json.JSONObject;

import APIConnector.APIConnector;
import powerpoint.Powerpoint;
import skeleton.Letter;
import skeleton.SkeletonBoard;
import skeleton.Word;
import wordReader.WordReader;

public class Driver {

	/**
	 * What is essentially happening 1. Reading the words from the wordlist 2.
	 * Shuffling the wordlist and placing the words on the board 3. Saving the board
	 * into powerpoint.
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
		/**
		 * wordList will contain the words that the reader object has read. logicalChars
		 * holds the Letter objects of each word
		 */
		ArrayList<String> wordList = new ArrayList<String>();
		ArrayList<Letter> logicalChars = new ArrayList<Letter>();

		/**
		 * wordReader object reads the words contained in the word_list and adds it to
		 * the arrayList wordList
		 */
		WordReader reader = new WordReader(Preferences.WORD_LIST_FOLDER_LOCATION + Preferences.WORD_LIST_FILE_NAME);
		reader.readFileIntoArray(wordList);

		/**
		 * board represents the skeleton board that will be played on.
		 */
		SkeletonBoard board = new SkeletonBoard();

		board.findBoardSize(Preferences.TABLE_ROW, Preferences.TABLE_COL);
		board.fillBoardWithSpaces();

		Powerpoint skeletonPuzzles = new Powerpoint();
		skeletonPuzzles.getPpt().setPageSize(new Dimension(Preferences.SLIDE_WIDTH, Preferences.SLIDE_HEIGHT));

//		Powerpoint solutionsPpt = new Powerpoint();
//		solutionsPpt.getPpt().setPageSize(new Dimension(Preferences.SLIDE_WIDTH, Preferences.SLIDE_HEIGHT));
//		Powerpoint puzzlesPpt = new Powerpoint();
//		puzzlesPpt.getPpt().setPageSize(new Dimension(Preferences.SLIDE_WIDTH, Preferences.SLIDE_HEIGHT));

		ArrayList<Letter[][]> solutionBoards = new ArrayList<Letter[][]>();

		int slideCount = 0;
		int numOfPuzzles = 0;
		int repeat = 0;

		// Shuffle the wordlist before placing words on board.
		Collections.shuffle(wordList);

		long start = System.currentTimeMillis();
		Iterator<String> itr = wordList.iterator();

		while (itr.hasNext() && numOfPuzzles < Preferences.MAX_NUMBER_OF_PUZZLES) {
			String currentWord = itr.next();
			APIConnector connector = new APIConnector(currentWord);
			connector.getLogicalChars();
			try {
				int responseCode = connector.getConnection().getResponseCode();
				if (responseCode != 200) {
					throw new RuntimeException("HttpResponseCode: " + responseCode);
				} else {
					InputStream inputStream = connector.getConnection().getInputStream();
					Scanner scanner = new Scanner(inputStream);
					String line = new String();
					while (scanner.hasNextLine()) {
						line = scanner.nextLine();
						line = line.substring(line.indexOf("{"));
					}

					JSONObject jsonObj = new JSONObject(line);
					JSONArray jsonArray = jsonObj.getJSONArray("data");

					logicalChars = new ArrayList<Letter>();

					Word word = new Word(currentWord, logicalChars, -1, -1, "none");
					for (int k = 0; k < jsonArray.length(); k++) {
						Letter letter = new Letter(-1, -1, jsonArray.getString(k), word);
						logicalChars.add(letter);
					}
					if (board.insertToBoard(logicalChars, word)) {
						wordList.removeAll(Collections.singleton(currentWord));
						itr = wordList.iterator();
						repeat = 0;
					} else {
						Collections.shuffle(wordList);
						itr = wordList.iterator();
						repeat++;
					}

					if (repeat == 100) {
						System.out.println("Repeat");
						Collections.shuffle(wordList);
						itr = wordList.iterator();
						board = new SkeletonBoard();
						board.findBoardSize(Preferences.TABLE_ROW, Preferences.TABLE_COL);
						board.fillBoardWithSpaces();

					}

					if (board.getNumberOfWords() == Preferences.MAX_NUMBER_OF_WORDS) {
						long newStart = System.currentTimeMillis();
						HSLFSlide puzzleSlide = skeletonPuzzles.createPuzzleSlide(++slideCount);
						skeletonPuzzles.createPuzzleTable(puzzleSlide, Preferences.TABLE_ROW, Preferences.TABLE_COL,
								board.getBoard());
						skeletonPuzzles.createAvailableLetters(puzzleSlide, board);

						solutionBoards.add(board.getBoard());

						numOfPuzzles++;
						board = new SkeletonBoard();
						board.findBoardSize(Preferences.TABLE_ROW, Preferences.TABLE_COL);
						board.fillBoardWithSpaces();
					}

				}
				connector.disconnect(connector.getConnection());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		int solutionSlideCount = 1;
		for (int i = 0; i < solutionBoards.size(); i++) {
			try {
				HSLFSlide solutionSlide = skeletonPuzzles.createSolutionSlide(solutionSlideCount++);
				skeletonPuzzles.createSolutionTable(solutionSlide, Preferences.TABLE_ROW, Preferences.TABLE_COL,
						solutionBoards.get(i));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		skeletonPuzzles.writeToPpt(skeletonPuzzles.getPpt(),
				Preferences.OUTPUT_FOLDER_LOCATION + Preferences.PUZZLE_FILE_NAME);

		long end = System.currentTimeMillis();
		float seconds = (end - start) / 1000F;
		System.out.println("DONE! It took " + seconds + " Seconds");

		File puzPPT = new File(Preferences.OUTPUT_FOLDER_LOCATION + Preferences.PUZZLE_FILE_NAME);
		Desktop dt = Desktop.getDesktop();
		if (puzPPT.exists()) {
			try {
				dt.open(puzPPT);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {

			File unusedWords = new File(Preferences.WORD_LIST_FOLDER_LOCATION + Preferences.UNUSED_WORDS_FILENAME);
//			FileWriter fileWriter = new FileWriter(unusedWords);
			OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(unusedWords), "UTF-8");

			fileWriter.write("\ufeff");
			for (int i = 0; i < wordList.size(); i++) {
				fileWriter.write(wordList.get(i) + "\n");
			}
			fileWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

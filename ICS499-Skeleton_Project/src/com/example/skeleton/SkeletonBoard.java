package com.example.skeleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import com.example.driver.Driver;
import com.example.driver.Preferences;

// ToDo: remove 'occupied' from Letter

public class SkeletonBoard {
	private ArrayList<String> wordList;
	private Letter[][] board;
	private ArrayList<Letter> lettersOnBoard;
	private ArrayList<Word> wordsOnBoard;
	private int numberOfWords;
	private int rowDimension;
	private int colDimension;

	public SkeletonBoard() {
		this.wordList = new ArrayList<String>();
		this.wordsOnBoard = new ArrayList<Word>();
		this.lettersOnBoard = new ArrayList<Letter>();
		this.numberOfWords = 0;

	}

	public ArrayList<String> getWords(String word, ArrayList<String> mainWordList) {
		if (wordList.contains(word)) {
			return wordList;
		} else {
			wordList.add(word);
		}

		for (int k = 0; k < wordList.size(); k++) {
			mainWordList.removeAll(Collections.singleton(wordList.get(k)));
		}
		return wordList;
	}

	public void findBoardSize(int row, int col) {
		board = new Letter[row][col];

	}

	public void fillBoardWithSpaces() {
		for (int i = 0; i < Preferences.TABLE_ROW; i++) {
			for (int col = 0; col < Preferences.TABLE_COL; col++) {
				Letter underScore = new Letter(i, col, " ", new Word());
				board[i][col] = underScore;
			}
		}
	}

	public boolean insertToBoard(ArrayList<Letter> logicalChars, Word word) {
		long start = System.currentTimeMillis();

		if (wordsOnBoard.isEmpty()) {
			if ((Preferences.TABLE_COL / 2) - 4 + word.getWord().length() < Preferences.TABLE_COL) {
				for (int i = 0; i < logicalChars.size(); i++) {
					logicalChars.get(i).setRow(Preferences.TABLE_ROW / 2 - 1);
					logicalChars.get(i).setColumn((Preferences.TABLE_COL / 2) - 4 + i);
					word.setDirection("Horizontal");

					board[(Preferences.TABLE_ROW / 2) - 1][(Preferences.TABLE_COL / 2) - 4 + i] = logicalChars.get(i);
					lettersOnBoard.add(logicalChars.get(i));
					wordsOnBoard.add(word);
				}
				numberOfWords++;

				/**
				 * If you want the board to print in the console after each word, uncomment the
				 * nested for loop
				 */

//				for (int j = 0; j < board.length; j++) {
//					for (int l = 0; l < board.length; l++) {
//						System.out.print("[" + board[j][l].getLetter() + "]");
//					}
//					System.out.println();
//				}
//				System.out.println();

			}
		} else {
			if (!(findMatch(logicalChars, word) == null)) {
				/**
				 * match is the Letter object that is returned when you try to find a letter
				 * that matches a letter in the word object matchedIndex is the index that the
				 * word matches. direction is the direction that the word will be going.
				 */
				Letter match = findMatch(logicalChars, word);
				int matchedIndex = findIndexMatching(match, logicalChars);
				String direction = findDirection(match);
				System.out.println("Direction:" + direction);
				System.out.println("Match:" + match.getRow() + " " + match.getColumn());

				if (isValid(logicalChars, word, match, direction, matchedIndex)) {
					System.out.println(word.getWord() + "-> : true");
					System.out.println("");

					if (direction.equalsIgnoreCase("Vertical")) {
						for (int k = 0; k < logicalChars.size(); k++) {
							if (word.getRow() == -1 && word.getCol() == -1) {
								word.setRow(match.getRow());
								word.setCol(match.getColumn());
							}
							word.setDirection("Vertical");
							if (matchedIndex >= 0) {
								if (match.getRow() - matchedIndex >= 0) {
									board[match.getRow() - matchedIndex + k][match.getColumn()] = logicalChars.get(k);
									logicalChars.get(k).setRow(match.getRow() - matchedIndex + k);
									logicalChars.get(k).setColumn(match.getColumn());
									lettersOnBoard.add(logicalChars.get(k));
								}
							}
						}
						wordsOnBoard.add(word);
						numberOfWords++;
					} else if (direction.equalsIgnoreCase("Horizontal")) {
						for (int k = 0; k < logicalChars.size(); k++) {
							if (word.getRow() == -1 && word.getCol() == -1) {
								word.setRow(match.getRow());
								word.setCol(match.getColumn());
							}
							word.setDirection("Horizontal");
							logicalChars.get(k).setRow(match.getRow());
							logicalChars.get(k).setColumn(match.getColumn() + k);
							if (matchedIndex >= 0) {
								if (match.getColumn() - matchedIndex >= 0) {
									board[match.getRow()][match.getColumn() - matchedIndex + k] = logicalChars.get(k);
									logicalChars.get(k).setRow(match.getRow());
									logicalChars.get(k).setColumn(match.getColumn() - matchedIndex + k);
									lettersOnBoard.add(logicalChars.get(k));
								}

							}

						}
						wordsOnBoard.add(word);
						numberOfWords++;
					}

				} else {
					System.out.println(word.getWord() + "-> : false");
				}

				/**
				 * If you want the board to print in the console after each word, uncomment the
				 * nested for loop
				 */

//				for (int j = 0; j < board.length; j++) {
//					for (int l = 0; l < board.length; l++) {
//						System.out.print("[" + board[j][l].getLetter() + "]");
//					}
//					System.out.println();
//				}
//				System.out.println();
//
			}

		}

		if (wordsOnBoard.contains(word)) {

			return true;
		} else {
			return false;
		}

	}

	/**
	 * This method verifies that the word will fit on the board.
	 * 
	 * @param logicalChars
	 * @param word
	 * @param matched
	 * @param direction
	 * @param matchedIndex
	 * @return
	 */
	public boolean isValid(ArrayList<Letter> logicalChars, Word word, Letter matched, String direction,
			int matchedIndex) {
		if (direction.equalsIgnoreCase("Vertical")) {
			// if the first letter of the word is the matched letter then follow this
			// conditional.
			if (matchedIndex == 0) {
				if (matched.getRow() + logicalChars.size() - 1 < Preferences.TABLE_ROW) {
					// If the word fits within the board's dimension.
					if ((matched.getColumn() - 1 >= 0) && (matched.getColumn() + 1 < Preferences.TABLE_COL)) {
						// Checks if the spaces next to the placement is open.

						if (matched.getRow() + logicalChars.size() < Preferences.TABLE_ROW) {
							if (!board[matched.getRow() + logicalChars.size()][matched.getColumn()].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}
						}

						if (matched.getRow() - matchedIndex - 1 > 0) {
							if (!board[matched.getRow() - matchedIndex - 1][matched.getColumn()].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}
						}

						if (matched.getRow() - matchedIndex < 0) {
							return false;
						}

						for (int j = 0; j < logicalChars.size(); j++) {
							if (matched.getRow() - matchedIndex + j == matched.getRow()) {
								continue;
							} else if (!(board[matched.getRow() + j][matched.getColumn() - 1].getLetter()
									.equalsIgnoreCase(" ")
									&& board[matched.getRow() + j][matched.getColumn() + 1].getLetter()
											.equalsIgnoreCase(" "))) {
								return false;
							} else if (!board[matched.getRow() - matchedIndex + j][matched.getColumn()].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}
						}
						return true;
					} else if (matched.getColumn() - 1 < 0) {
						if (matched.getRow() + logicalChars.size() < Preferences.TABLE_ROW) {
							if (!board[matched.getRow() + logicalChars.size()][matched.getColumn()].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}
						}
						if (matched.getRow() - 1 >= 0) {
							if (!board[matched.getRow() - 1][matched.getColumn()].getLetter().equalsIgnoreCase(" ")) {
								return false;
							}
						}

						if (matched.getRow() - matchedIndex < 0) {
							return false;
						}

						for (int j = 0; j < logicalChars.size(); j++) {

							if (!board[matched.getRow() - matchedIndex + j][matched.getColumn() + 1].getLetter()
									.equals(" ")) {
								return false;
							} else if (!board[matched.getRow() - matchedIndex + j][matched.getColumn()].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}
//							System.out.println("Condition: 1.11");
						}
						return true;

					} else if (matched.getColumn() + 1 >= Preferences.TABLE_COL) {
						if (matched.getRow() + logicalChars.size() < Preferences.TABLE_ROW) {
							if (!board[matched.getRow() + logicalChars.size()][matched.getColumn()].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}
						}
						if (matched.getRow() - 1 >= 0) {
							if (!board[matched.getRow() - 1][matched.getColumn()].getLetter().equalsIgnoreCase(" ")) {
								return false;
							}
						}

						if (matched.getRow() - matchedIndex < 0) {
							return false;
						}

						for (int j = 0; j < logicalChars.size(); j++) {
							if (!board[matched.getRow() - matchedIndex + j][matched.getColumn() - 1].getLetter()
									.equals(" ")) {
								return false;
							} else if (!board[matched.getRow() - matchedIndex + j][matched.getColumn()].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}
						}

						return true;
					}
				}

			} else if (matchedIndex > 0) {
				if (matched.getRow() - matchedIndex >= 0
						&& matched.getRow() - matchedIndex + logicalChars.size() - 1 < Preferences.TABLE_ROW) {

					if ((matched.getColumn() - 1 >= 0) && (matched.getColumn() + 1 < Preferences.TABLE_COL)) {

						if (matched.getRow() - matchedIndex + logicalChars.size() < Preferences.TABLE_ROW) {
							if (!board[matched.getRow() - matchedIndex + logicalChars.size()][matched.getColumn()]
									.getLetter().equalsIgnoreCase(" ")) {
								return false;
							}
						}

						if (matched.getRow() - matchedIndex - 1 >= 0) {
							if (!board[matched.getRow() - matchedIndex - 1][matched.getColumn()].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}
						}

						if (matched.getRow() - matchedIndex < 0) {
							return false;
						}

						for (int j = 0; j < logicalChars.size(); j++) {
							if (matched.getRow() - matchedIndex + j == matched.getRow()) {
								continue;
							} else if (!board[matched.getRow() - matchedIndex + j][matched.getColumn()].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							} else if (!(board[matched.getRow() - matchedIndex + j][matched.getColumn() - 1].getLetter()
									.equalsIgnoreCase(" ")
									&& board[matched.getRow() - matchedIndex + j][matched.getColumn() + 1].getLetter()
											.equalsIgnoreCase(" "))) {
								return false;
							}
						}
						return true;

					}
				} else if (matched.getColumn() - 1 < 0) {

					if (matched.getRow() - matchedIndex + logicalChars.size() < Preferences.TABLE_ROW) {
						if (!board[matched.getRow() - matchedIndex + logicalChars.size()][matched.getColumn()]
								.getLetter().equalsIgnoreCase(" ")) {
							return false;
						}
					}
					if (matched.getRow() - matchedIndex - 1 >= 0) {
						if (!board[matched.getRow() - matchedIndex - 1][matched.getColumn()].getLetter()
								.equalsIgnoreCase(" ")) {
							return false;
						}
					}

					if (matched.getRow() - matchedIndex < 0) {
						return false;
					}

					for (int j = 0; j < logicalChars.size(); j++) {

						if (!board[matched.getRow() - matchedIndex + j][matched.getColumn() + 1].getLetter()
								.equals(" ")) {
							return false;
						} else if (!board[matched.getRow() - matchedIndex + j][matched.getColumn()].getLetter()
								.equalsIgnoreCase(" ")) {
							return false;
						}
					}
					return true;

				} else if (matched.getColumn() + 1 >= Preferences.TABLE_COL) {

					if (matched.getRow() - matchedIndex + logicalChars.size() - 1 < Preferences.TABLE_ROW) {
						if (!board[matched.getRow() - matchedIndex + logicalChars.size()][matched.getColumn()]
								.getLetter().equalsIgnoreCase(" ")) {
							return false;
						}
					}
					if (matched.getRow() - matchedIndex - 1 >= 0) {
						if (!board[matched.getRow() - matchedIndex - 1][matched.getColumn()].getLetter()
								.equalsIgnoreCase(" ")) {
							return false;
						}
					}

					if (matched.getRow() - matchedIndex < 0) {
						return false;
					}

					for (int j = 0; j < logicalChars.size(); j++) {
						if (!board[matched.getRow() - matchedIndex + j][matched.getColumn() - 1].getLetter()
								.equals(" ")) {
							return false;
						} else if (!board[matched.getRow() - matchedIndex + j][matched.getColumn()].getLetter()
								.equalsIgnoreCase(" ")) {
							return false;
						}
					}

					return true;
				}

			}
		}

		else if (direction.equalsIgnoreCase("Horizontal")) {
			// if the first letter of the word is the matched letter then follow this
			// conditional.
			if (matchedIndex == 0) {
				if (matched.getColumn() + logicalChars.size() - 1 < Preferences.TABLE_COL) {
					// If the word fits within the board's dimension.
					if ((matched.getRow() - 1 >= 0) && (matched.getRow() + 1 < Preferences.TABLE_ROW)) {
						// Checks if the spaces next to the placement is open.

						if (matched.getColumn() + logicalChars.size() < Preferences.TABLE_COL) {
							if (!board[matched.getRow()][matched.getColumn() + logicalChars.size()].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}
						}

						if (matched.getColumn() - matchedIndex - 1 > 0) {
							if (!board[matched.getRow()][matched.getColumn() - matchedIndex - 1].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}
						}

						if (matched.getColumn() - matchedIndex < 0) {
							return false;
						}

						for (int j = 0; j < logicalChars.size(); j++) {
							if (matched.getColumn() - matchedIndex + j == matched.getColumn()) {
								continue;
							} else if (!board[matched.getRow()][matched.getColumn() - matchedIndex + j].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							} else if (!(board[matched.getRow() - 1][matched.getColumn() - matchedIndex + j].getLetter()
									.equalsIgnoreCase(" ")
									&& board[matched.getRow() + 1][matched.getColumn() - matchedIndex + j].getLetter()
											.equalsIgnoreCase(" "))) {
								return false;
							}
						}
						return true;
					} else if (matched.getRow() - 1 < 0) {
						if (matched.getColumn() - matchedIndex + logicalChars.size() - 1 < Preferences.TABLE_COL) {
							if (!board[matched.getRow()][matched.getColumn() - matchedIndex + logicalChars.size() - 1]
									.getLetter().equalsIgnoreCase(" ")) {
								return false;
							}
						}
						if (matched.getColumn() - matchedIndex - 1 >= 0) {
							if (!board[matched.getRow()][matched.getColumn() - matchedIndex - 1].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}
						}

						if (matched.getColumn() - matchedIndex < 0) {
							return false;
						}

						for (int j = 0; j < logicalChars.size(); j++) {

							if (!board[matched.getRow() + 1][matched.getColumn() - matchedIndex + j].getLetter()
									.equals(" ")) {
								return false;
							} else if (!board[matched.getRow()][matched.getColumn() - matchedIndex + j].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}
						}

						return true;

					} else if (matched.getRow() + 1 >= Preferences.TABLE_ROW) {
						if (matched.getColumn() + logicalChars.size() < Preferences.TABLE_COL) {
							if (!board[matched.getRow()][matched.getColumn() + logicalChars.size()].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}
						}
						if (matched.getColumn() - 1 >= 0) {
							if (!board[matched.getRow()][matched.getColumn() - 1].getLetter().equalsIgnoreCase(" ")) {
								return false;
							}
						}

						if (matched.getColumn() - matchedIndex < 0) {
							return false;
						}

						for (int j = 0; j < logicalChars.size(); j++) {
							if (!board[matched.getRow() - 1][matched.getColumn() - matchedIndex + j].getLetter()
									.equals(" ")) {
								return false;
							} else if (!board[matched.getRow()][matched.getColumn() - matchedIndex + j].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}

						}
						return true;
					}
				}

			} else if (matchedIndex > 0) {
				if (matched.getColumn() - matchedIndex >= 0
						&& matched.getColumn() - matchedIndex + logicalChars.size() - 1 < Preferences.TABLE_COL) {

					if ((matched.getRow() - 1 >= 0) && (matched.getRow() + 1 < Preferences.TABLE_ROW)) {

						if (matched.getColumn() - matchedIndex + logicalChars.size() < Preferences.TABLE_COL) {
							if (!board[matched.getRow()][matched.getColumn() - matchedIndex + logicalChars.size()]
									.getLetter().equalsIgnoreCase(" ")) {
								return false;
							}
						}
						if (matched.getColumn() - matchedIndex - 1 >= 0) {
							if (!board[matched.getRow()][matched.getColumn() - matchedIndex - 1].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							}
						}

						if (matched.getColumn() - matchedIndex < 0) {
							return false;
						}

						for (int j = 0; j < logicalChars.size(); j++) {
							if (matched.getColumn() - matchedIndex + j == matched.getColumn()) {
								continue;
							} else if (!board[matched.getRow()][matched.getColumn() - matchedIndex + j].getLetter()
									.equalsIgnoreCase(" ")) {
								return false;
							} else if (!(board[matched.getRow() - 1][matched.getColumn() - matchedIndex + j].getLetter()
									.equalsIgnoreCase(" ")
									&& board[matched.getRow() + 1][matched.getColumn() - matchedIndex + j].getLetter()
											.equalsIgnoreCase(" "))) {
								return false;
							}
						}
						return true;

					}
				} else if (matched.getRow() - 1 < 0) {

					if (matched.getColumn() - matchedIndex + logicalChars.size() < Preferences.TABLE_COL) {
						if (!board[matched.getRow()][matched.getColumn() - matchedIndex + logicalChars.size()]
								.getLetter().equalsIgnoreCase(" ")) {
							return false;
						}
					} else if (matched.getColumn() - matchedIndex - 1 >= 0) {
						if (!board[matched.getRow()][matched.getColumn() - matchedIndex - 1].getLetter()
								.equalsIgnoreCase(" ")) {
							return false;
						}
					}

					if (matched.getColumn() - matchedIndex < 0) {
						return false;
					}

					for (int j = 0; j < logicalChars.size(); j++) {
						if (!board[matched.getRow() + 1][matched.getColumn() - matchedIndex + j].getLetter()
								.equals(" ")) {
							return false;
						} else if (!board[matched.getRow()][matched.getColumn() - matchedIndex + j].getLetter()
								.equalsIgnoreCase(" ")) {
							return false;
						}
					}
					return true;

				} else if (matched.getRow() + 1 >= Preferences.TABLE_ROW) {

					if (matched.getColumn() - matchedIndex + logicalChars.size() < Preferences.TABLE_COL) {
						if (!board[matched.getRow()][matched.getColumn() - matchedIndex + logicalChars.size()]
								.getLetter().equalsIgnoreCase(" ")) {
							return false;
						}
					} else if (matched.getColumn() - matchedIndex - 1 >= 0) {
						if (!board[matched.getRow() - matchedIndex - 1][matched.getColumn()].getLetter()
								.equalsIgnoreCase(" ")) {
							return false;
						}
					}

					if (matched.getColumn() - matchedIndex < 0) {
						return false;
					}

					for (int j = 0; j < logicalChars.size(); j++) {
						if (!board[matched.getRow() - 1][matched.getColumn() - matchedIndex + j].getLetter()
								.equals(" ")) {
							return false;
						} else if (!board[matched.getRow()][matched.getColumn() - matchedIndex + j].getLetter()
								.equalsIgnoreCase(" ")) {
							return false;
						}
					}

					return true;

				}

			}
		}
		return false;

	}

	public int findIndexMatching(Letter matching, ArrayList<Letter> logicalChars) {
		for (int i = 0; i < logicalChars.size(); i++) {
			if (logicalChars.get(i).getLetter().equalsIgnoreCase(matching.getLetter())) {
				return i;
			}
		}
		return -1;
	}

	public String findDirection(Letter matching) {
		if (matching.getWord().getDirection().equalsIgnoreCase("Horizontal")) {
			return "Vertical";
		} else if (matching.getWord().getDirection().equalsIgnoreCase("Vertical")) {
			return "Horizontal";
		}
		return null;

	}

	public Letter findMatch(ArrayList<Letter> charList, Word word) {

		for (int i = 0; i < charList.size(); i++) {
			for (int row = 0; row < Preferences.TABLE_ROW; row++) {
				for (int col = 0; col < Preferences.TABLE_COL; col++) {
					if (charList.get(i).getLetter().equalsIgnoreCase(board[row][col].getLetter())) {
						int matchedIndex = findIndexMatching(board[row][col], charList);
						String direction = findDirection(board[row][col]);
						if (isValid(charList, word, board[row][col], direction, matchedIndex)) {
							return board[row][col];
						} else {
							continue;
						}

					}
				}
			}
		}
		return null;

	}

	public void removeWordFromList(String word) {
		wordList.remove(word);
	}

	public ArrayList<String> getWordList() {
		return wordList;
	}

	public void setWordList(ArrayList<String> board) {
		this.wordList = board;
	}

	public Letter[][] getBoard() {
		return board;
	}

	public ArrayList<Letter> getLettersOnBoard() {
		return lettersOnBoard;
	}

	public void setLettersOnBoard(ArrayList<Letter> lettersOnBoard) {
		this.lettersOnBoard = lettersOnBoard;
	}

	public ArrayList<Word> getWordsOnBoard() {
		return wordsOnBoard;
	}

	public void setWordsOnBoard(ArrayList<Word> wordsOnBoard) {
		this.wordsOnBoard = wordsOnBoard;
	}

	public void setBoard(Letter[][] board) {
		this.board = board;
	}

	public int getNumberOfWords() {
		return numberOfWords;
	}

	public void setNumberOfWords(int numberOfWords) {
		this.numberOfWords = numberOfWords;
	}

	public int getRowDimension() {
		return rowDimension;
	}

	public void setRowDimension(int rowDimension) {
		this.rowDimension = rowDimension;
	}

	public int getColDimension() {
		return colDimension;
	}

	public void setColDimension(int colDimension) {
		this.colDimension = colDimension;
	}

}

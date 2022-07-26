package com.example.powerpoint;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;

import org.apache.poi.hslf.usermodel.HSLFAutoShape;
import org.apache.poi.hslf.usermodel.HSLFPictureData;
import org.apache.poi.hslf.usermodel.HSLFPictureShape;
import org.apache.poi.hslf.usermodel.HSLFShape;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTable;
import org.apache.poi.hslf.usermodel.HSLFTableCell;
import org.apache.poi.hslf.usermodel.HSLFTextBox;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hslf.usermodel.HSLFTextRun;

import org.apache.poi.sl.usermodel.VerticalAlignment;

import com.example.driver.Preferences;
import com.example.skeleton.Letter;
import com.example.skeleton.SkeletonBoard;

import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.ShapeType;
import org.apache.poi.sl.usermodel.TableCell.BorderEdge;
import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;

public class Powerpoint {
	private HSLFSlideShow ppt;

	public Powerpoint() {
		ppt = new HSLFSlideShow();

	}

	public HSLFSlide createPuzzleSlide(int count) throws IOException {
		int shapeWidth = Preferences.TITLE_WIDTH;
		int shapeHeight = Preferences.TITLE_HEIGHT;
		int x = (int) ((this.ppt.getPageSize().getWidth() / 2) - (shapeWidth / 2));
		int y = 10;
		
		HSLFSlide slide = this.ppt.createSlide();
		HSLFTextBox title = slide.createTextBox();
		HSLFTextParagraph p = title.getTextParagraphs().get(0);
		HSLFTextRun r = p.getTextRuns().get(0);
		r.setFontColor(Preferences.TITLE_FONT_COLOR);
		r.setText(Preferences.TITLE_NAME);
		r.setBold(Preferences.TITLE_BOLD);
		r.setFontSize(Preferences.TITLE_FONT_SIZE);
		title.setAnchor(new Rectangle(x, y, shapeWidth, shapeHeight));

		HSLFPictureData logo = this.ppt.addPicture(new File(Preferences.PROJECT_DIRECTORY + "/docs/Picture1.png"),
				PictureData.PictureType.PNG);
		HSLFPictureShape shape = new HSLFPictureShape(logo);
		shape.setAnchor(new Rectangle(0, 0, 300, 100));
		slide.addShape(shape);

		HSLFTextBox slideNumber = slide.createTextBox();
		slideNumber.setText(""+count);
		slideNumber.setHorizontalCentered(true);
		slideNumber.setVerticalAlignment(VerticalAlignment.MIDDLE);
		slideNumber.setAnchor(new Rectangle(310, 0, 100, 100));
		HSLFTextParagraph tp = slideNumber.getTextParagraphs().get(0);
		tp.setTextAlign(TextAlign.CENTER);
		HSLFTextRun tr = tp.getTextRuns().get(0);
		tr.setFontSize(Preferences.SLIDE_NUMBER_FONTSIZE);
		tr.setBold(Preferences.SLIDE_NUMBER_FONTBOLD);
		tr.setFontColor(Preferences.SLIDE_NUMBER_COLOR);
		return slide;
	}
	
	public HSLFSlide createSolutionSlide(int count) throws IOException {
		int shapeWidth = Preferences.TITLE_WIDTH;
		int shapeHeight = Preferences.TITLE_HEIGHT;
		int x = (int) ((this.ppt.getPageSize().getWidth() / 2) - (shapeWidth / 2));
		int y = 10;
		
		HSLFSlide slide = this.ppt.createSlide();
		HSLFTextBox title = slide.createTextBox();
		HSLFTextParagraph p = title.getTextParagraphs().get(0);
		HSLFTextRun r = p.getTextRuns().get(0);
		r.setFontColor(Preferences.TITLE_FONT_COLOR);
		r.setText(Preferences.TITLE_SOLUTION_NAME);
		r.setBold(Preferences.TITLE_BOLD);
		r.setFontSize(Preferences.TITLE_FONT_SIZE);
		title.setAnchor(new Rectangle(x, y, shapeWidth, shapeHeight));

		HSLFPictureData logo = this.ppt.addPicture(new File(Preferences.PROJECT_DIRECTORY + "/docs/Picture1.png"),
				PictureData.PictureType.PNG);
		HSLFPictureShape shape = new HSLFPictureShape(logo);
		shape.setAnchor(new Rectangle(0, 0, 300, 100));
		slide.addShape(shape);

		HSLFTextBox slideNumber = slide.createTextBox();
		slideNumber.setText(""+count);
		slideNumber.setHorizontalCentered(true);
		slideNumber.setVerticalAlignment(VerticalAlignment.MIDDLE);
		slideNumber.setAnchor(new Rectangle(310, 0, 100, 100));
		HSLFTextParagraph tp = slideNumber.getTextParagraphs().get(0);
		tp.setTextAlign(TextAlign.CENTER);
		HSLFTextRun tr = tp.getTextRuns().get(0);
		tr.setFontSize(Preferences.SLIDE_NUMBER_FONTSIZE);
		tr.setBold(Preferences.SLIDE_NUMBER_FONTBOLD);
		tr.setFontColor(Preferences.SLIDE_NUMBER_COLOR);
		return slide;
	}

	public HSLFTable createSolutionTable(HSLFSlide slide, int row, int col, Letter[][] board) {

		//(Preferences.TABLE_COL_WIDTH * Preferences.TABLE_COL)
		int shapeWidth = 0;
		int shapeHeight = 0;
		int x = (int) ((this.ppt.getPageSize().getWidth() / 2) - ((Preferences.TABLE_COL_WIDTH * Preferences.TABLE_COL) / 2));
		int y = 110;
		HSLFTable table = slide.createTable(row, col);
		table.setAnchor(new Rectangle(x, y, shapeWidth, shapeHeight));
		
		for (int i = 0; i < table.getNumberOfRows(); i++) {
			for (int k = 0; k < table.getNumberOfColumns(); k++) {
				HSLFTableCell cell = table.getCell(i, k);
				cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
				cell.setHorizontalCentered(true);
				
				cell.setBorderColor(BorderEdge.top, Preferences.TABLE_BORDER_NOTINPLAY_COLOR);
				cell.setBorderColor(BorderEdge.right, Preferences.TABLE_BORDER_NOTINPLAY_COLOR);
				cell.setBorderColor(BorderEdge.bottom, Preferences.TABLE_BORDER_NOTINPLAY_COLOR);
				cell.setBorderColor(BorderEdge.left, Preferences.TABLE_BORDER_NOTINPLAY_COLOR);

				table.setColumnWidth(k, Preferences.TABLE_COL_WIDTH);
				table.setRowHeight(i, Preferences.TABLE_ROW_HEIGHT);
			}
		}

		for (int i = 0; i < table.getNumberOfRows(); i++) {
			for (int k = 0; k < table.getNumberOfColumns(); k++) {

				HSLFTableCell cell = table.getCell(i, k);
				cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
				cell.setHorizontalCentered(true);
				cell.setText(board[i][k].getLetter().toUpperCase());
				HSLFTextRun textBox = cell.getTextParagraphs().get(0).getTextRuns().get(0);
				textBox.setFontColor(Preferences.TABLE_FONT_COLOR);
				textBox.setFontSize(Preferences.TABLE_FONT_SIZE);
				textBox.setBold(Preferences.TABLE_FONT_BOLD);


				if (!cell.getText().equalsIgnoreCase(" ")) {
					cell.setBorderColor(BorderEdge.top, Preferences.TABLE_BORDER_INPLAY_COLOR);
					cell.setBorderColor(BorderEdge.right, Preferences.TABLE_BORDER_INPLAY_COLOR);
					cell.setBorderColor(BorderEdge.bottom, Preferences.TABLE_BORDER_INPLAY_COLOR);
					cell.setBorderColor(BorderEdge.left, Preferences.TABLE_BORDER_INPLAY_COLOR);
				}else {
					cell.setFillColor(Preferences.TABLE_BACKGROUND_COLOR);
				}
			}
		}
		return table;
	}

	public HSLFTable createPuzzleTable(HSLFSlide slide, int row, int col, Letter[][] board) {
		//(Preferences.TABLE_COL_WIDTH * Preferences.TABLE_COL)
		//Preferences.GRID_HEIGHT - 100
		int shapeWidth = 0;
		int shapeHeight = 0;
		int x = (int) ((this.ppt.getPageSize().getWidth() / 2) - ((Preferences.TABLE_COL_WIDTH * Preferences.TABLE_COL) / 2));
		int y = 110;
		HSLFTable table = slide.createTable(row, col);
		table.setAnchor(new Rectangle(x, y, shapeWidth, shapeHeight));
		
		
		for (int i = 0; i < table.getNumberOfRows(); i++) {
			for (int k = 0; k < table.getNumberOfColumns(); k++) {
				HSLFTableCell cell = table.getCell(i, k);
				cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
				cell.setHorizontalCentered(true);
				
				cell.setBorderColor(BorderEdge.top, Preferences.TABLE_BORDER_NOTINPLAY_COLOR);
				cell.setBorderColor(BorderEdge.right, Preferences.TABLE_BORDER_NOTINPLAY_COLOR);
				cell.setBorderColor(BorderEdge.bottom, Preferences.TABLE_BORDER_NOTINPLAY_COLOR);
				cell.setBorderColor(BorderEdge.left, Preferences.TABLE_BORDER_NOTINPLAY_COLOR);
				
				table.setColumnWidth(k, Preferences.TABLE_COL_WIDTH);
				table.setRowHeight(i, Preferences.TABLE_ROW_HEIGHT);

			}
		}

		for (int i = 0; i < table.getNumberOfRows(); i++) {
			for (int k = 0; k < table.getNumberOfColumns(); k++) {
				HSLFTableCell cell = table.getCell(i, k);
				cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
				cell.setHorizontalCentered(true);
				if (!board[i][k].getLetter().equalsIgnoreCase(" ")) {
					cell.setText(" ");
				} else {
					cell.setText(board[i][k].getLetter());
				}
				


				if (!board[i][k].getLetter().equalsIgnoreCase(" ")) {
					cell.setBorderColor(BorderEdge.top, Preferences.TABLE_BORDER_INPLAY_COLOR);
					cell.setBorderColor(BorderEdge.right, Preferences.TABLE_BORDER_INPLAY_COLOR);
					cell.setBorderColor(BorderEdge.bottom, Preferences.TABLE_BORDER_INPLAY_COLOR);
					cell.setBorderColor(BorderEdge.left, Preferences.TABLE_BORDER_INPLAY_COLOR);
				}else {
					cell.setFillColor(Preferences.TABLE_BACKGROUND_COLOR);
				}

			}
		}
		return table;
	}

	public void createAvailableLetters(HSLFSlide slide, SkeletonBoard board) {
		Collections.shuffle(board.getLettersOnBoard());
		HSLFTextBox textBox = slide.createTextBox();
		for (int letterObject = 0; letterObject < board.getLettersOnBoard().size(); letterObject++) {
			textBox.appendText(board.getLettersOnBoard().get(letterObject).getLetter().toUpperCase(), false);
			textBox.appendText(",", false);
		}
		HSLFTextParagraph p = textBox.getTextParagraphs().get(0);
		for(int i = 0; i < p.getTextRuns().size(); i++) {
			HSLFTextRun r = p.getTextRuns().get(i);
			r.setFontColor(Preferences.CLUE_FONT_COLOR);
			r.setFontSize(Preferences.CLUE_FONT_SIZE);
		}
		
		
		int shapeWidth = Preferences.CLUE_TEXTBOX_WIDTH;
		int shapeHeight = Preferences.CLUE_TEXTBOX_HEIGHT;
		int x = (int) ((this.ppt.getPageSize().getWidth() / 2) - (shapeWidth / 2));
		int y = (int) (this.ppt.getPageSize().getHeight() - Preferences.CLUE_TEXTBOX_HEIGHT);

		textBox.setAnchor(new Rectangle(x, y, shapeWidth, shapeHeight));
		textBox.setAlignToBaseline(true);

	}

	public void writeToPpt(HSLFSlideShow ppt, String fileName) {
		try {
			FileOutputStream out = new FileOutputStream(fileName);
			ppt.write(out);
			out.close();
			ppt.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	public HSLFSlideShow getPpt() {
		return ppt;
	}

	public void setPpt(HSLFSlideShow ppt) {
		this.ppt = ppt;
	}

}
// // Copyright 2016 theaigames.com (developers@theaigames.com)

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//  
//    For the full copyright and license information, please view the LICENSE
//    file that was distributed with this source code.

package bot;

import java.util.ArrayList;

/**
 * Field class
 * 
 * Handles everything that has to do with the field, such 
 * as storing the current state and performing calculations
 * on the field.
 * 
 * @author Jim van Eeden <jim@starapple.nl>, Joost de Meij <joost@starapple.nl>
 */

public class Field {
    private int mRoundNr;
    private int mMoveNr;
	private int[][] mBoard;
	private int[][] mMacroboard;

	private final int COLS = 9, ROWS = 9;
	private String mLastError = "";
	
	public Field() {
		mBoard = new int[COLS][ROWS];
		mMacroboard = new int[COLS / 3][ROWS / 3];
		clearBoard();
	}
	
	/**
	 * Parse data about the game given by the engine
	 * @param key : type of data given
	 * @param value : value
	 */
	public void parseGameData(String key, String value) {
	    if (key.equals("round")) {
	        mRoundNr = Integer.parseInt(value);
	    } else if (key.equals("move")) {
	        mMoveNr = Integer.parseInt(value);
	    } else if (key.equals("field")) {
            parseFromString(value); /* Parse Field with data */
        } else if (key.equals("macroboard")) {
            parseMacroboardFromString(value); /* Parse macroboard with data */
        }
	}
	
	/**
	 * Initialise field from comma separated String
	 * @param String : 
	 */
	public void parseFromString(String s) {
	    System.err.println("Move " + mMoveNr);
		s = s.replace(";", ",");
		String[] r = s.split(",");
		int counter = 0;
		for (int y = 0; y < ROWS; y++) {
			for (int x = 0; x < COLS; x++) {
				mBoard[x][y] = Integer.parseInt(r[counter]); 
				counter++;
			}
		}
	}
	
	/**
	 * Initialise macroboard from comma separated String
	 * @param String : 
	 */
	public void parseMacroboardFromString(String s) {
		String[] r = s.split(",");
		int counter = 0;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				mMacroboard[x][y] = Integer.parseInt(r[counter]);
				counter++;
			}
		}
	}
	
	public void clearBoard() {
		for (int x = 0; x < COLS; x++) {
			for (int y = 0; y < ROWS; y++) {
				mBoard[x][y] = 0;
			}
		}
	} 
	private void setMacroBoard() {
		for (int x = 0; x < COLS/3; x++) {
			for (int y = 0; y < ROWS/3; y++) {
				if(mMacroboard[x][y]==0 && isMacroFilled(x,y) == false) {
					mMacroboard[x][y] = -1;
				}
			}
		}
	  
	} 
	
	private void clearMacroBoard() {
		for (int x = 0; x < COLS/3; x++) {
			for (int y = 0; y < ROWS/3; y++) {
				mMacroboard[x][y] = 0;
			}
		}
		
	}
	private boolean isMacroFilled(int colIndex,int rowIndex) {
		int firstCol = colIndex * 3; 
		int firstRow = rowIndex *  3;  
		for (int x = firstCol; x < firstCol+3; x++) {
			for (int y = firstRow; y < firstRow+3; y++) {
				if(mBoard[x][y] == 0) return false;
			}
		}
		return true; 
	}

	public ArrayList<Move> getAvailableMoves() {
	    ArrayList<Move> moves = new ArrayList<Move>();
		
		for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLS; x++) {
                if (isInActiveMicroboard(x, y) && mBoard[x][y] == 0) {
                    moves.add(new Move(x, y));
                }
            }
        }

		return moves;
	}
	
	public Boolean isInActiveMicroboard(int x, int y) {
	    return mMacroboard[(int) x/3][(int) y/3] == -1;
	}
	
	/**
	 * Returns reason why addMove returns false
	 * @param args : 
	 * @return : reason why addMove returns false
	 */
	public String getLastError() {
		return mLastError;
	}

	
	@Override
	/**
	 * Creates comma separated String with player ids for the microboards.
	 * @param args : 
	 * @return : String with player names for every cell, or 'empty' when cell is empty.
	 */
	public String toString() {
		String r = "";
		int counter = 0;
		for (int y = 0; y < ROWS; y++) {
			for (int x = 0; x < COLS; x++) {
				if (counter > 0) {
					r += ",";
				}
				r += mBoard[x][y];
				counter++;
			}
		}
		return r;
	}
	
	/**
	 * Checks whether the field is full
	 * @param args : 
	 * @return : Returns true when field is full, otherwise returns false.
	 */
	public boolean isFull() {
		for (int x = 0; x < COLS; x++)
		  for (int y = 0; y < ROWS; y++)
		    if (mBoard[x][y] == 0)
		      return false; // At least one cell is not filled
		// All cells are filled
		return true;
	}
	
	public int getNrColumns() {
		return COLS;
	}
	
	public int getNrRows() {
		return ROWS;
	}

	public boolean isEmpty() {
		for (int x = 0; x < COLS; x++) {
			  for (int y = 0; y < ROWS; y++) {
				  if (mBoard[x][y] > 0) {
					  return false;
				  }
			  }
		}
		return true;
	}
	
	/**
	 * Returns the player id on given column and row
	 * @param args : int column, int row
	 * @return : int
	 */
	public int getPlayerId(int column, int row) {
		return mBoard[column][row];
	}
	
	// 0 ->draw 1->bot id 2->other bot id 
	public int whoWinGame() { 

		int checkLeftBottomDiagonal = 1; 
		int checkRightBottomDiagonal = 1 ; 
		//row and column check 
		for(int row=0;row<ROWS/9;row++) { 
			int checkHoriznotanl = 1;
			int checkVertical =1;
			for(int column = 0;column <COLS/3;column++) {
				checkHoriznotanl = checkHoriznotanl * mMacroboard[column][row] ;
				checkVertical = checkVertical * mMacroboard[row][column] ; 
				if(row == column) {
					checkLeftBottomDiagonal = checkLeftBottomDiagonal *  mMacroboard[row][column] ; 
				} 
				if(row==0 && column ==2) {
					checkRightBottomDiagonal = checkRightBottomDiagonal *  mMacroboard[row][column] ; 
				}
				if(row==2 && column ==0) {
					checkRightBottomDiagonal = checkRightBottomDiagonal *  mMacroboard[row][column] *  mMacroboard[1][1]  ; 
				}
			}
			if(checkHoriznotanl == 8 || checkVertical == 8) return 2; 
			if(checkHoriznotanl == 1 || checkVertical == 1) return 1; 
		} 
		if(checkLeftBottomDiagonal == 8 || checkRightBottomDiagonal == 8) return 2; 
		if(checkLeftBottomDiagonal == 1 || checkLeftBottomDiagonal == 1) return 1; 
		return 0 ; 
		
	} 
	
	public void updateTemporaryField(int mx,int my,int botID ) {
		int macroBoardCol = mx/3; 
		int macroBoardRow = my/3 ; 
		mBoard[mx][my] = botID;
		if(mMacroboard[macroBoardCol][macroBoardRow] == 1 ||mMacroboard[macroBoardCol][macroBoardRow] == 2 || ( mMacroboard[macroBoardCol][macroBoardRow] == 0 && isMacroFilled(macroBoardCol,macroBoardRow) == true ) ) {
			setMacroBoard() ; 
		} else {
			clearMacroBoard() ;
			mMacroboard[macroBoardCol][macroBoardRow] =-1;
			
		}	
	}
}
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
import java.util.HashMap;
import java.util.List;

import com.sun.javafx.collections.MappingChange.Map;

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
	public Field(List mboard,List macroBoard) {
		mBoard = new int[COLS][ROWS];
		mMacroboard = new int[COLS / 3][ROWS / 3];
		clearBoard();
		this.setMboard(mboard);
		this.setMacroBoard(macroBoard);
	}
	private void  setMboard(List list) { 
		int index = 0;
		for (int x = 0; x < COLS; x++) {
			for (int y = 0; y < ROWS; y++) {
				mBoard[x][y] = (int) list.get(index++);
			}
		}
		
	}
	private void setMacroBoard(List list) {
		int index = 0 ;
		for (int x = 0; x < COLS/3; x++) {
			for (int y = 0; y < ROWS/3; y++) {
				mMacroboard[x][y] = (int) list.get(index++);
			}
		}
	}
	
	public List<Integer>  getMboard() {
		List mBoardList = new ArrayList<>()  ;
		for (int x = 0; x < COLS; x++) {
			for (int y = 0; y < ROWS; y++) {
				mBoardList.add(mBoard[x][y]) ;
			}
		}
		return mBoardList ;
	}
	public List<Integer>  getMacroboard() {
		List mBoardList = new ArrayList<>()  ;
		for (int x = 0; x < COLS/3; x++) {
			for (int y = 0; y < ROWS/3; y++) {
				mBoardList.add(mMacroboard[x][y]) ;
			}
		}
		return mBoardList ;
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
				if(mMacroboard[x][y]==-1)
				mMacroboard[x][y] = 0;
			}
		}
		
	}
	private boolean isMacroFilled(int colIndex,int rowIndex) {
		if(whoWinGame() !=0) return false ; 
		int firstCol = colIndex * 3; 
		int firstRow = rowIndex *  3;  
		for (int x = firstCol; x < firstCol+3; x++) {
			for (int y = firstRow; y < firstRow+3; y++) {
				//int z =  mBoard[y][x] ;
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
		for(int row=0;row<ROWS/3;row++) { 
			int checkHoriznotanl = 1;
			int checkVertical =1;
			for(int column = 0;column <COLS/3;column++) {
				checkHoriznotanl = checkHoriznotanl * mMacroboard[column][row] ;
				checkVertical = checkVertical * mMacroboard[row][column] ; 
				if(mMacroboard[column][row] == -1) checkHoriznotanl *= 10 ; 
				if(mMacroboard[row][column] == -1) checkVertical *= 10 ; 
				if(row == column) {
					checkLeftBottomDiagonal = checkLeftBottomDiagonal *  mMacroboard[row][column] ; 
					if(mMacroboard[row][column] == -1) checkLeftBottomDiagonal *= 10 ;
				} 
				if(row==0 && column ==2) {
					checkRightBottomDiagonal = checkRightBottomDiagonal *  mMacroboard[row][column] ; 
					if(mMacroboard[row][column] == -1) checkRightBottomDiagonal *= 10 ;
				}
				if(row==2 && column ==0) {
					checkRightBottomDiagonal = checkRightBottomDiagonal *  mMacroboard[row][column] *  mMacroboard[1][1]  ; 
					if(mMacroboard[1][1] == -1) {
						checkRightBottomDiagonal *= 10 ;
					}
				}
			}
			if(checkHoriznotanl == 8 || checkVertical == 8) return 2; 
			if(checkHoriznotanl == 1 || checkVertical == 1) return 1; 
		} 
		if(checkLeftBottomDiagonal == 8 || checkRightBottomDiagonal == 8) return 2; 
		if(checkLeftBottomDiagonal == 1 || checkRightBottomDiagonal == 1) return 1; 
		return 0 ; 
		
	} 
	
	public void updateTemporaryField(int mx,int my,int botID ) {
		int macroBoardCol = mx%3; 
		int macroBoardRow = my%3 ; 
		mBoard[mx][my] = botID;
		
		mMacroboard[mx/3][my/3] = getConditionMacroBoard(mx/3, my/3) ;
		if(mMacroboard[macroBoardCol][macroBoardRow] == 1 ||mMacroboard[macroBoardCol][macroBoardRow] == 2 || ( mMacroboard[macroBoardCol][macroBoardRow] == 0 && isMacroFilled(macroBoardCol,macroBoardRow) == true ) ) {
			setMacroBoard() ; 
		} else {
			clearMacroBoard() ;
			mMacroboard[macroBoardCol][macroBoardRow] =-1;
			
		}
		
	}
	
	private int  getConditionMacroBoard(int mCol,int mRow) { 
		int firstCol = mCol * 3; 
		int firstRow = mRow *  3;  
		int horizon = 1 ; 
		int vertical = 1 ;
		for (int x = firstCol; x < firstCol+3; x++) {
			 vertical = 1;
			for (int y = firstRow; y < firstRow+3; y++) {
				vertical *=  mBoard[x][y];
			} 
			if(vertical == 1  ) return 1;
			if(vertical == 8 ) return 2;	
		}
		for (int x = firstRow; x < firstRow+3; x++) {
			horizon = 1;
			for (int y = firstCol; y < firstCol+3; y++) {
				horizon *=  mBoard[y][x];
			} 
			if(horizon == 1  ) return 1;
			if(horizon == 8 ) return 2;	
		} 
		int diagonal1 = mBoard[firstCol][firstRow] * mBoard[firstCol+1][firstRow+1] * mBoard[firstCol+2][firstRow+2] ;
		int diagonal2 = mBoard[firstCol+2][firstRow] * mBoard[firstCol+1][firstRow+1] * mBoard[firstCol][firstRow+2] ;
		
		if(diagonal1 == 1 || diagonal2 == 1 ) return 1;
		if(diagonal1 == 8 || diagonal2 == 8 ) return 2;	
		return 0; 
		
	}
	public int hursistic(int id) {
		int horizon_sum = 0 ; 
		int vertical_sum = 0;
		for (int x = 0; x < COLS/3; x++) {
			int horizon = 0 ; 
			int vertical = 0;
			  for (int y = 0; y < ROWS/3; y++) {
				   if(mMacroboard[x][y] == id) {
					   if(vertical == 0) vertical += 5; 
					   else if(vertical == 0)  vertical += 10; 
				  } else if(mMacroboard[x][y] != 0 && mMacroboard[x][y] != -1) {
					  if(vertical == 0) vertical -= 5; 
					   else  vertical -= 10; 
				  }
						  
			  }
			  vertical_sum += vertical;
		}
		for (int y = 0; y < COLS/3; y++) {
			int horizon = 0 ; 
			int vertical = 0;
			  for (int x = 0; x < ROWS/3; x++) {
				   if(mMacroboard[x][y] == id) {
					   if(horizon == 0) horizon += 5; 
					   else  horizon += 10; 
				  } else if(mMacroboard[x][y] != 0 && mMacroboard[x][y] != -1) {
					  if(horizon == 0) horizon -= 5; 
					   else  horizon -= 10; 
				  }
						  
			  }
			  horizon_sum += horizon;
		}
		return  horizon_sum + vertical_sum;
	}
	private HashMap canwin(int botid,int mcol,int mrow) {
	
		int boardCol = mcol*3 ; 
		int boardRow = mrow * 3; 
		int point = 0 ; 
		int totalPoint = 0;
		boolean winPos = false;
		HashMap  map =  new HashMap() ;
		
		//horizon
		for(int y = boardRow ; y<boardRow+3 ; y++ ) {
			boolean win = true;
			point = 0;
			for(int x = boardCol ; x<boardCol+3 ; x++ ) {
				if(mBoard[x][y] !=0 && mBoard[x][y] !=botid  ) {
					win = false; 
					point = 0;
					break;
				}
				if(mBoard[x][y] == botid) {
					point++;
				}
			}
			if(win ) {
				winPos = true ;
				if(point >1) totalPoint += point ;
			}
		}
		
		//vertical
		
		for(int y = boardCol ; y<boardCol+3 ; y++ ) {
			boolean win = true;
			point = 0;
			for(int x = boardRow ; x<boardRow+3 ; x++ ) {
				if(mBoard[y][x] !=0 && mBoard[y][x] !=botid  ) {
					win = false; 
					point = 0;
					break;
				}
				if(mBoard[y][x] == botid) {
					point++;
				}
			}
			if(win) {
				winPos = true ;
				if(point >1) totalPoint += point ;
			}
		}
		point = 0;
		if(mBoard[boardCol+1][boardRow+1] != 0 && mBoard[boardCol+1][boardRow+1]!= botid ) {
			
		} else {
			if(mBoard[boardCol][boardRow] == 0 || mBoard[boardCol][boardRow]== botid ) {
				if(mBoard[boardCol+2][boardRow+2] == 0 || mBoard[boardCol+2][boardRow+2]== botid ) {
					winPos = true ;
					if(mBoard[boardCol][boardRow] == botid)  point++; 
					if(mBoard[boardCol+1][boardRow+1] == botid)  point++; 
					if(mBoard[boardCol+2][boardRow+2] == botid)  point++; 
					if(point >1) totalPoint += point ;
				}
			}
			point = 0;
			if(mBoard[boardCol+2][boardRow] == 0 || mBoard[boardCol+2][boardRow]== botid ) {
				if(mBoard[boardCol][boardRow+2] == 0 || mBoard[boardCol][boardRow+2]== botid ) {
					winPos = true ;
					if(mBoard[boardCol][boardRow+2] == botid)  point++; 
					if(mBoard[boardCol+1][boardRow+1] == botid)  point++; 
					if(mBoard[boardCol+2][boardRow] == botid)  point++; 
					if(point >1) totalPoint += point ;
				}
			}
		}
		
		
		map.put("canWin", winPos);
		map.put("point", totalPoint);
		return map;
		
	}
	
	private int measurement(int botID,int optid) {
		
        int sum = 0;
        int leftDiag = 0;
		for (int x = 0; x < COLS/3; x++) {
	          int vertical =0;
			  for (int y = 0; y < ROWS/3; y++) { 
				if(mMacroboard[x][y] == optid) {
					vertical = 0;
					break;
				}
				if(mMacroboard[x][y] == botID) { 
					
					vertical =  (vertical == 0) ?  5 :  vertical + vertical;
				}
				if(mMacroboard[x][y] == 0 || mMacroboard[x][y] == -1 ) {
					HashMap map = canwin(botID, x, y) ;
					if(  ((boolean) map.get("canWin")) ==  false) {
						vertical = 0; 
						break;
					} else {
						vertical =  (vertical == 0) ?  (int) map.get("point") :  vertical + (int) map.get("point");
					}
						
				}
				
			  } 
			  if(vertical !=0) vertical++ ;
			  sum += vertical;
		}
		for (int x = 0; x < ROWS/3; x++) {
	          int horizon =0;
			  for (int y = 0; y < COLS/3; y++) {
				if(mMacroboard[y][x] == optid) {
					horizon = 0; 
					break;
				}
				if(mMacroboard[y][x] == botID) {
					horizon =  (horizon == 0) ?  5 :  horizon + horizon;
				}
				if(mMacroboard[y][x] == 0 || mMacroboard[y][x] == -1 ) {
					HashMap map = canwin(botID, x, y) ;
					if(((boolean) map.get("canWin")) ==  false) {
						horizon = 0; 
						break;
					} else {
						horizon =  (horizon == 0) ?  (int) map.get("point") :  horizon + (int) map.get("point");
					}
				}
				
			  } 
			  if(horizon !=0) horizon++ ;
			  sum += horizon;
		}
		int lDiag =0;
        int rDiag =0;
		for (int x = 0; x < ROWS/3; x++) {
			  for (int y = 0; y < COLS/3; y++) {
				  if(x == y || (x==0 && y==2) || (y==2 && x==0)) {
					  if(x==y) {
						  if(mMacroboard[y][x] == optid) {
							  lDiag=0;
							  if(x==1) rDiag = -1;
						  }
							if(mMacroboard[y][x] == botID) {
								lDiag = (lDiag == 0) ? 5 : lDiag + lDiag;
								if(x==1) rDiag = (rDiag == 0) ? 5 : rDiag + rDiag;
							}
							if(mMacroboard[y][x] == 0 || mMacroboard[y][x] == -1 ) {
								HashMap map = canwin(botID, x, y) ;
								if(((boolean) map.get("canWin")) ==  false) {
									if(x==1) rDiag = -1;
									lDiag = 0; 
								}else {
									lDiag =  (lDiag == 0) ?  (int) map.get("point") :  lDiag + (int) map.get("point");
								}
					       }
					 } else if(rDiag !=-1) {
						 if(mMacroboard[y][x] == optid) {
							     rDiag = 0; 
							}
							if(mMacroboard[y][x] == botID) {
								 rDiag = (rDiag == 0) ? 5 : rDiag + rDiag;
							}
							if(mMacroboard[y][x] == 0 || mMacroboard[y][x] == -1 ) {
								HashMap map = canwin(botID, x, y) ;
								if(((boolean) map.get("canWin")) ==  false) {
									rDiag = 0; 
								}else {
									rDiag =  (rDiag == 0) ?  (int) map.get("point") :  rDiag + (int) map.get("point");
								}
							}
						 
					   }
				  } else {
					  continue;
				  }
				
				}
				
			  } 
			  if(lDiag !=0) lDiag++ ;
			  if(rDiag !=0) rDiag++ ;
			  sum += lDiag + rDiag;
			  return sum ;
		}
		
	public int advanceHuristicAnalysis(int botID,int opID) {
		return measurement(botID,opID) - measurement(opID,botID)  ;
	}
	public int seeEffect(int mx ,int my,int botId,int opId) {
		int effect = 0 ;
		if(mMacroboard[mx/3][my/3]==botId)  effect =20;
		if(whoWinGame()==botId) effect =70; 
		if(effect == 0) {
				effect = sameRowColumn( mx, my, botId) ; 
				if(effect != 0) effect++;
				if(mx% 3 ==1 && my% 3 == 1 && effect != 0 ) effect  = effect - 1 ;
				
			
		}
		mBoard[mx][my] = opId;
		mMacroboard[mx/3][my/3] = getConditionMacroBoard(mx/3, my/3) ;
		if(mMacroboard[mx/3][my/3]==opId)  effect =10;
		if(whoWinGame()==opId) effect =50;  
        if(effect == 0) {
        	effect = sameRowColumn( mx, my, opId) ; 
        	if(mx% 3 ==1 && my% 3 == 1 && effect != 0 ) effect  = effect - 1 ;
		} 
       
		
		//fall back 
        mBoard[mx][my] = botId;
		mMacroboard[mx/3][my/3] = getConditionMacroBoard(mx/3, my/3) ;
		if(botId == BotParser.oBotID) return effect *(-1);
		return effect;
		
	}
	 private int  sameRowColumn(int mx,int my,int botId) {
		 
		 
		 // not checinh diagonal 
				int mcol = mx/3; 
				int mrow = my/3;
				int boardCol = mcol*3 ; 
				int boardRow = mrow * 3; 
				int point = 0;
				//horizon
				for(int y = boardRow ; y<boardRow+3 ; y++ ) {
					if(my !=y) continue;
					boolean win = true;
					for(int x = boardCol ; x<boardCol+3 ; x++ ) {
						if(mBoard[x][y] !=0 && mBoard[x][y] !=botId  ) {
							win = false; 
							break;
						} 
						if(mBoard[x][y] == botId) point = point + 2 ; 
					}
					if(!win) point = 0 ;
				}
				
				//vertical
				
				for(int y = boardCol ; y<boardCol+3 ; y++ ) {
					if(mx !=y) continue;
					boolean win = true;
					for(int x = boardRow ; x<boardRow+3 ; x++ ) {
						if(mBoard[y][x] !=0 && mBoard[y][x] !=botId  ) {
							win = false; 
							break;
						}
						if(mBoard[x][y] == botId) point = point + 2 ;
					}
					if(!win) point = 0 ;
				}
				
				return point ; 
     }
	public int seeEffect_huristic(int mx ,int my,int botId,int opId) {
		int effect = 0 ;
		int effectHur = 0 ; int  effectHurOp = 0;
		if(mMacroboard[mx/3][my/3]==botId)  effectHur =measurement(botId,opId);
		if(whoWinGame()==botId) effect =70; 
		mBoard[mx][my] = opId;
		
		mMacroboard[mx/3][my/3] = getConditionMacroBoard(mx/3, my/3) ;
		if(mMacroboard[mx/3][my/3]==opId)  effectHurOp = measurement(opId,botId);
		if(whoWinGame()==opId) effect =50; 
		
		//fall back 
        mBoard[mx][my] = botId;
		mMacroboard[mx/3][my/3] = getConditionMacroBoard(mx/3, my/3) ;
		if(effect == 0)  effect = effectHur +  effectHurOp ;
		if(botId == BotParser.oBotID) return effect *(-1);
		return effect;
		
	}
	public int crosscheck(int mx ,int my,int botId,int opId,int realID) {
	
		
		int effect = 0 ;
		mBoard[mx][my] = botId;
		mMacroboard[mx/3][my/3] = getConditionMacroBoard(mx/3, my/3) ;
		if(whoWinGame()==botId) effect+=1000; 
		
		mBoard[mx][my] = opId;
		mMacroboard[mx/3][my/3] = getConditionMacroBoard(mx/3, my/3) ;
		if(whoWinGame()==opId) effect +=1000; 
		
		//fall back 
        mBoard[mx][my] = realID;
		mMacroboard[mx/3][my/3] = getConditionMacroBoard(mx/3, my/3) ;
		return effect;
		
	} 
	public int getMoveNumber() {
		return mMoveNr ;
	}
	
}
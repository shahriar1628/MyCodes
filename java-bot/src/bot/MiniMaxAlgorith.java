package bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MiniMaxAlgorith { 
	private Move _playerMove; 
	private int _depth ;
	MiniMaxAlgorith (int depth) {
		_playerMove = new Move() ;
		_depth = depth; 
	}
	
public Integer miniMax(Field game,int depth,int botID,int alpha,int bita) {
		if(game.isFull() || game.whoWinGame() != 0 || game.getAvailableMoves().isEmpty()  ) return score(game,depth) ; 
		int oponentBotID = 0;
		if(botID == BotParser.mBotId) oponentBotID = BotParser.oBotID ; 
		else oponentBotID = BotParser.mBotId ; 
		depth++;
        int returnScore = 0;
        int instantEffect = 0;
        if(botID == BotParser.mBotId)  {
        	returnScore = -5000;
        	instantEffect = -200;
        }
        else {
        	returnScore = 5000;
        	instantEffect = 200;
        	
        }
		List availabelMoves = game.getAvailableMoves() ; 
		if(depth >this._depth && this._depth == 3 ) return huristicanAnalysis(game,botID);  
		else if(depth >this._depth) {
			 if(this._playerMove.mX == 7 && this._playerMove.mY == 3 ) {
				// System.out.println( instantEffect);
			 }
			return advanceHuristicAnalysis(game,botID, oponentBotID)  ;
		}
		if(availabelMoves.size()>9 && depth !=1  ) {
			if(botID == BotParser.mBotId) return bestmoveByGreedyAlgo(game,botID,oponentBotID,depth) +10;  
			return bestmoveByGreedyAlgo(game,botID,oponentBotID,depth) -10;
		}
		
		if(availabelMoves.size()>9 && depth ==1  ) this._depth = 2;
		for(int index =0; index<availabelMoves.size();index++) {
				Move move = (Move) availabelMoves.get(index) ;
			   Field newGameInstance = null;
			  newGameInstance = new Field(game.getMboard(),game.getMacroboard()) ;
				newGameInstance.updateTemporaryField(move.getX(),move.getY(), botID); 
				int getScore  = 0;
				if(depth == 1) {
					//System.out.println(move.mX + " "+move.mY );
				}
				getScore = miniMax(newGameInstance,depth,oponentBotID,alpha,bita)  ;
				int tempInstantEff = newGameInstance.seeEffect(move.mX ,move.mY,botID,oponentBotID);
				if(newGameInstance.getAvailableMoves().size() > 9   ) {
					if(getScore <=0 && botID == BotParser.mBotId) {
						getScore = getScore - 100 ;
					}
					if(getScore >=0 && botID == BotParser.oBotID) {
						getScore = getScore + 100 ;
					}
				}
				
				if (depth == 1 && getScore >-288) {
					getScore = getScore + newGameInstance.crosscheck(move.mX ,move.mY,BotParser.mBotId,BotParser.oBotID,botID);
				} 
				if(depth == 1) {
					//System.out.println(move.mX + " "+move.mY + " " + getScore + " "  + tempInstantEff);
				}
				
					if(botID == BotParser.mBotId) { 
						if(returnScore <getScore) {
							instantEffect = tempInstantEff;
						} else if(returnScore == getScore && tempInstantEff > instantEffect && depth == 1 ) {
							this._playerMove = move;
						}
						int temp = getMax(returnScore,getScore) ;
						if(temp != returnScore && depth == 1) 
							this._playerMove = move;
						returnScore = temp;
						alpha = getMax(alpha,returnScore);
						if(bita <=alpha) break;
						}
					if(botID == BotParser.oBotID) {
						if(returnScore >getScore) {
							instantEffect = tempInstantEff;
						}
						returnScore = getMin(returnScore,getScore);
						bita = getMin(bita,returnScore);
						if(bita <=alpha) break;
						} 
		}
        return returnScore   ;
		     
}
			
	private int bestmoveByGreedyAlgo(Field game,int botID,int opBotID,int depth) {
		List availabelMoves = game.getAvailableMoves() ;
		int returnScore = 0; 
		if(botID == BotParser.mBotId) returnScore = -1000;  
		else returnScore = 1000; 
		for(int index =0; index<availabelMoves.size();index++) {
			Move move = (Move) availabelMoves.get(index) ;
		   Field newGameInstance = null;
		   int getScore  = 0;
		   newGameInstance = new Field(game.getMboard(),game.getMacroboard()) ;
		   newGameInstance.updateTemporaryField(move.getX(),move.getY(), botID);  
		   if(newGameInstance.isFull() || newGameInstance.whoWinGame() != 0 || newGameInstance.getAvailableMoves().isEmpty()  ) {
			   getScore = score(newGameInstance, depth)  ;
		   } else {
			       getScore = newGameInstance.seeEffect_huristic(move.mX ,move.mY,botID,opBotID)   ;
			        if(newGameInstance.getAvailableMoves().size() > 9   ) {
						if(getScore <=0 && botID == BotParser.mBotId) {
							getScore = getScore - 100 ;
						}
						if(getScore >=0 && botID == BotParser.oBotID) {
							getScore = getScore + 100 ;
						}
					}	
			}
		   if(depth ==1  && getScore >-288 )
			   getScore = getScore + newGameInstance.crosscheck(move.mX ,move.mY,BotParser.mBotId,BotParser.oBotID,botID);
		   if(depth == 1) {
				//System.out.println(move.mX + " "+move.mY + " " + getScore );
			}
		   
		   if(botID == BotParser.mBotId) {
			   int temp = getMax(returnScore, getScore) ;
			   if(depth == 1 && returnScore !=temp) this._playerMove = move;
			   returnScore = temp;
		   } else {
			   returnScore = getMin(returnScore, getScore) ;
		   }
		   if(Math.abs(returnScore) >=280 && Math.abs(returnScore) <=300 ) return returnScore ; 
		}
		return returnScore ;
	}

	public int score(Field game,int depth) {
		if(game.whoWinGame() == BotParser.mBotId) return 300 - depth ;
		if(game.whoWinGame()== BotParser.oBotID) return  depth - 300 ;
		return 0;
		
	} 
	
	
	public Move getTurn() {
		return this._playerMove ;
	}
	private int getIndex(List list,boolean max) { 
		int result = 0;
		int ind = 0;
		if(max == true) result = -10000;
		else result = 1000;
		
		for(int index = 0; index<list.size();index++) {
			int tempResult = ( (Integer) list.get(index)).intValue() ; 
			if(max && tempResult > result ) {
				result = tempResult;
				ind = index;
			} else if(max == false && tempResult < result ) {
				result = tempResult;
				ind = index;
			}
		} 
		return ind;
	} 
	
	private int  getMax(int a,int b) {
		if(a>b) return a ; 
		return b;
	}
	private int  getMin(int a,int b) {
		if(a<b) return a ; 
		return b;
	}
	private int huristicanAnalysis(Field game,int botID) {
		int huristicValue = game.hursistic(botID); 
		if(botID == BotParser.oBotID) return huristicValue * (-1) ;
		return huristicValue;
		
	}
	
	private int advanceHuristicAnalysis(Field game,int botID,int opID) {
		int huristicValue = game.advanceHuristicAnalysis(botID,opID); 
		if(huristicValue==0)  huristicValue = -1;
		if(botID ==  BotParser.mBotId)  return huristicValue;
		return huristicValue * (-1) ;
		
	}
	
	

}

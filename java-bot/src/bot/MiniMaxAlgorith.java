package bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MiniMaxAlgorith { 
	private Move _playerMove;
	MiniMaxAlgorith () {
		_playerMove = new Move() ;
		
	}
	public Integer miniMax(Field game,int depth,int botID) {
		if(game.isFull()) return score(game,depth) ; 
		int oponentBotID = 0;
		if(botID == BotParser.mBotId) oponentBotID = BotParser.oBotID ; 
		else oponentBotID = BotParser.mBotId ; 
		depth++;
		List scores = new ArrayList<Integer> () ; 
		List moves = new ArrayList<Integer> () ;  
		List availabelMoves = game.getAvailableMoves() ; 
		for(int index =0; index<availabelMoves.size();index++) {
			Move move = (Move) availabelMoves.get(index) ;
			Field newGameInstance = game;
			newGameInstance.updateTemporaryField(move.getX(),move.getY(), botID); 
			scores.add(miniMax(newGameInstance,depth,oponentBotID)) ; 
			moves.add(new Integer(index)) ;	
		} 
		if(botID == BotParser.mBotId) {
			int index = getIndex(scores,true) ;
			this._playerMove = (Move) availabelMoves.get(index) ;
			return (Integer) scores.get(index);
			
		} else {
			int index = getIndex(scores,false) ;
			return (Integer) scores.get(index);
		}
	} 
	public int score(Field game,int depth) {
		if(game.whoWinGame() == BotParser.mBotId) return 10 - depth ;
		if(game.whoWinGame()== BotParser.oBotID) return  depth - 10 ;
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

}

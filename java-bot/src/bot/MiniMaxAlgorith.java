package bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MiniMaxAlgorith { 
	private Move _playerMove;
	MiniMaxAlgorith () {
		_playerMove = new Move() ;
		
	}

	public Integer miniMax(Field game,int depth,int botID,int alpha,int bita) {
		if(game.isFull() || game.whoWinGame() != 0 || game.getAvailableMoves().isEmpty()  ) return score(game,depth) ; 
		int oponentBotID = 0;
		if(botID == BotParser.mBotId) oponentBotID = BotParser.oBotID ; 
		else oponentBotID = BotParser.mBotId ; 
		depth++;
		List scores = new ArrayList<Integer> () ; 
		List moves = new ArrayList<Integer> () ;  
		List availabelMoves = game.getAvailableMoves() ; 
		if(depth >9) return huristicanAnalysis(game,botID);
        
		if(availabelMoves.size()<=18  || depth <=1) {
			for(int index =0; index<availabelMoves.size();index++) {
				Move move = (Move) availabelMoves.get(index) ;
			  //  if(move.mX == 7 &&  move.mY == 1 && depth == 2   ) {
			    	//System.out.println(move.getX() + " " + move.getY()) ;
			  //  }
			   
				Field newGameInstance = new Field(game.getMboard(),game.getMacroboard()) ;
				newGameInstance.updateTemporaryField(move.getX(),move.getY(), botID); 
				int getScore  = 0;
				if(newGameInstance.getAvailableMoves().size()>18) {
					int effect = newGameInstance.seeEffect(move.mX ,move.mY,botID,oponentBotID);
					if(effect !=0) getScore = effect ;  
				} else if(getScore == 0)
				{
					 getScore = miniMax(newGameInstance,depth,oponentBotID,alpha,bita) ;
						
				}
				
				// if(depth == 1 ) {
				    	//System.out.println(move.getX() + " " + move.getY() +"   scores "+getScore) ;
				//    }
				scores.add(getScore) ; 
				moves.add(new Integer(index)) ;	
				
				if(!scores.isEmpty()) {
					if(botID == BotParser.mBotId) {
						int ind = getIndex(scores,true) ; 
						alpha = getMax(alpha,(int) scores.get(ind));
						if(bita <=alpha) break;
						}
					if(botID == BotParser.oBotID) {
						int ind = getIndex(scores,false) ;
						bita = getMin(bita,(int) scores.get(ind));
						if(bita <=alpha) break;
						}
					}
			}
			
		} else if (availabelMoves.size()>18 )  {
			if(botID == BotParser.mBotId) return 200 - depth ;
			if(botID== BotParser.oBotID) return  depth - 200 ;
			
		}
		
		
		if(depth ==  1) {
			//System.out.println("scores "+scores.toString()) ;
		//	System.out.println("availabelMoves  "+availabelMoves.toString()) ;
			
		}
		if(!scores.isEmpty()) {
			if(botID == BotParser.mBotId) {
				int index = getIndex(scores,true) ;
				this._playerMove = (Move) availabelMoves.get( (int) moves.get(index)) ;
				return (Integer) scores.get(index);
				
			} else {
				int index = getIndex(scores,false) ;
				return (Integer) scores.get(index);
			}
		} else {
			System.err.println("why emtpy!!!");
			this._playerMove = (Move) availabelMoves.get(0) ;
			return 0;
		}
		
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
	
	

}

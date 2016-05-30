package bot;

public class MiniMaxAlgorith { 
	MiniMaxAlgorith () {
		
	}
	public void miniMax(Field game,int depth,int botID) {
		
		
	} 
	public int score(Field game,int depth) {
		if(game.whoWinGame() == BotParser.mBotId) return 10 - depth ;
		if(game.whoWinGame()== BotParser.oBotID) return  depth - 10 ;
		return 0;
		
	}

}

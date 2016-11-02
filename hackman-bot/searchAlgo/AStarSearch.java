package searchAlgo;

import bot.BotState;
import field.Field;

public class AStarSearch {
  private BotState _botState ; 
  private Field _filed;
  
  AStarSearch(BotState botState$) {
    this._botState = botState$;
    this._filed = _botState.getField();
    this.run();
  }
  private void run() {
    
  }
  

}

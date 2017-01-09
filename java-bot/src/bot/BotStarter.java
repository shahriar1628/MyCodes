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
import java.util.Random;

/**
 * BotStarter class
 * 
 * Magic happens here. You should edit this file, or more specifically
 * the makeTurn() method to make your bot do more than random moves.
 * 
 * @author Jim van Eeden <jim@starapple.nl>
 */

public class BotStarter {

    /**
     * Makes a turn. Edit this method to make your bot smarter.
     * Currently does only random moves.
     *
     * @return The column where the turn was made.
     */
	public Move makeTurn(Field field) {
		long start_time = System.currentTimeMillis();
		int moveNumnber = field.getMoveNumber() ; 
		if(moveNumnber == 1) return new Move(4,4);
		int depth = 2;
		if(moveNumnber<=9) depth = 4;
		else if(moveNumnber <=40) depth =  6; 
		else depth = 10; 
		MiniMaxAlgorith minimax = new MiniMaxAlgorith(depth);
		
		Integer result = minimax.miniMax(field, 0, BotParser.mBotId,-10000,10000,0);
		Move move = new Move() ;
		move = minimax.getTurn() ;
		long end_time = System.currentTimeMillis();
		long difference = end_time-start_time; 
		System.err.println("get time "+difference) ;
		return move ;
	}


	public static void main(String[] args) {
		BotParser parser = new BotParser(new BotStarter());
		parser.run();
	}
	
}

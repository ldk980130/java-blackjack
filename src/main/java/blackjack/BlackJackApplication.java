package blackjack;

import blackjack.controller.BlackJackGame;

public class BlackJackApplication {

	public static void main(String[] args) {
		BlackJackGame blackJackGame = new BlackJackGame();
		blackJackGame.play();
	}
}

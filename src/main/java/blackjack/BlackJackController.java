package blackjack;

import java.util.List;

import blackjack.domain.card.Card;
import blackjack.domain.card.Deck;
import blackjack.domain.game.BlackJackGame;
import blackjack.domain.gamer.Dealer;
import blackjack.domain.gamer.Player;
import blackjack.domain.result.BettingResult;
import blackjack.view.InputView;
import blackjack.view.OutputView;

public class BlackJackController {

	public void run() {
		BlackJackGame blackJackGame = new BlackJackGame(InputView.askNames(), InputView::askBet,
			new Deck(Card.getCards()));

		Dealer dealer = blackJackGame.getDealer();
		List<Player> players = blackJackGame.getPlayers();

		OutputView.printGamers(dealer, players);

		BettingResult result = blackJackGame.play(InputView::askHitOrStay, OutputView::printNameAndCards);

		OutputView.printAdditionalDrawDealer(dealer.findHitCount());
		OutputView.printFinalCards(dealer, players);
		OutputView.printFinalResult(result);
	}
}

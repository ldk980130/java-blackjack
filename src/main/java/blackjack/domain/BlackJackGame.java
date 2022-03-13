package blackjack.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import blackjack.domain.card.DrawStrategy;
import blackjack.domain.gamer.Dealer;
import blackjack.domain.gamer.Gamers;
import blackjack.domain.gamer.Player;
import blackjack.domain.result.BlackJackResult;
import blackjack.dto.GameResultDto;
import blackjack.dto.GamerDto;

public class BlackJackGame {

	public static final int MAX_CARD_VALUE = 21;
	public static final int INIT_DISTRIBUTION_COUNT = 2;
	public static final int ADDITIONAL_DISTRIBUTE_STANDARD = 16;
	private static final int DEFAULT_COUNT = 0;
	private static final int INCREASE_COUNT = 1;

	private final DrawStrategy drawStrategy;
	private final Gamers gamers;

	private BlackJackGame(List<String> names, DrawStrategy drawStrategy) {
		this.drawStrategy = drawStrategy;
		this.gamers = new Gamers(names);

		for (int i = 0; i < INIT_DISTRIBUTION_COUNT; i++) {
			gamers.giveCardToAllGamers(this.drawStrategy::draw);
		}
	}

	public static BlackJackGame start(List<String> names, DrawStrategy drawStrategy) {
		return new BlackJackGame(names, drawStrategy);
	}

	public void askPlayerHitOrStay(Function<String, Boolean> answerReceiver, Consumer<GamerDto> cardsSender) {
		for (String name : gamers.findPlayerNames()) {
			hitOrStay(name, answerReceiver, cardsSender);
		}
	}

	private void hitOrStay(String name,
		Function<String, Boolean> answerReceiver,
		Consumer<GamerDto> cardsSender) {
		while (!isBust(name) && answerReceiver.apply(name)) {
			gamers.giveCardToPlayer(name, drawStrategy::draw);
			cardsSender.accept(getPlayerDto(name));
		}
	}

	private boolean isBust(String name) {
		return gamers.checkPlayerDrawPossible(name);
	}

	public int askDealerHitOrStay() {
		int count = 0;
		while (gamers.checkDealerDrawPossible()) {
			gamers.giveCardToDealer(drawStrategy::draw);
			count++;
		}
		return count;
	}

	public GameResultDto createResult() {
		Map<BlackJackResult, Integer> dealerResult = createDealerResult();
		Map<String, BlackJackResult> playerResults = createPlayerResults(dealerResult);
		return new GameResultDto(playerResults, dealerResult);
	}

	private Map<String, BlackJackResult> createPlayerResults(Map<BlackJackResult, Integer> dealerResult) {
		Dealer dealer = gamers.getDealer();
		List<Player> players = gamers.getPlayers();

		Map<String, BlackJackResult> playerResults = new LinkedHashMap<>();
		for (Player player : players) {
			BlackJackResult result = player.match(dealer);
			playerResults.put(player.getName().getValue(), result);
			dealerResult.merge(result.getReverse(), INCREASE_COUNT, Integer::sum);
		}
		return playerResults;
	}

	private Map<BlackJackResult, Integer> createDealerResult() {
		Map<BlackJackResult, Integer> dealerResult = new LinkedHashMap<>();
		for (BlackJackResult blackJackResult : BlackJackResult.values()) {
			dealerResult.put(blackJackResult, DEFAULT_COUNT);
		}
		return dealerResult;
	}

	public GamerDto getDealerDto() {
		return new GamerDto(gamers.getDealer());
	}

	public List<GamerDto> getPlayerDtos() {
		return gamers.getPlayers().stream()
			.map(GamerDto::new)
			.collect(Collectors.toList());
	}

	private GamerDto getPlayerDto(String name) {
		return new GamerDto(gamers.findPlayerByName(name));
	}
}

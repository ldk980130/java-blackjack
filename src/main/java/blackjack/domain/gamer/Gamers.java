package blackjack.domain.gamer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import blackjack.domain.game.BettingInjector;
import blackjack.domain.card.Cards;
import blackjack.domain.card.DrawStrategy;

public class Gamers {

	private static final String DUPLICATION_NAME_ERROR = "중복된 이름이 존재합니다.";
	private static final String NOT_EXIST_PLAYER_ERROR = "플레이어가 존재하지 않습니다.";

	private static final int ADDITIONAL_DISTRIBUTE_STANDARD = 16;

	private final Dealer dealer;
	private final List<Player> players;

	public Gamers(List<String> names, BettingInjector betting) {
		validateDuplicationNames(names);
		this.dealer = new Dealer();
		this.players = names.stream()
			.map(name -> new Player(name, betting.inject(name)))
			.collect(Collectors.toList());
	}

	private void validateDuplicationNames(List<String> names) {
		int count = (int) names.stream()
			.distinct()
			.count();
		if (count != names.size()) {
			throw new IllegalArgumentException(DUPLICATION_NAME_ERROR);
		}
	}

	public void giveCardToAllGamers(DrawStrategy deck) {
		dealer.addCard(deck.draw());
		for (Player player : players) {
			player.addCard(deck.draw());
		}
	}

	public void giveCardToDealer(DrawStrategy deck) {
		dealer.addCard(deck.draw());
	}

	public void giveCardToPlayer(String name, DrawStrategy deck) {
		Player player = findPlayerByName(name);
		player.addCard(deck.draw());
	}

	public boolean checkDealerDrawPossible() {
		return !dealer.isOverThan(ADDITIONAL_DISTRIBUTE_STANDARD);
	}

	public Player findPlayerByName(String name) {
		return players.stream()
			.filter(player -> player.isSameName(name))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_PLAYER_ERROR));
	}

	public boolean checkPlayerDrawPossible(String name) {
		return findPlayerByName(name).isBust();
	}

	public List<String> findPlayerNames() {
		return players.stream()
			.map(Player::getName)
			.collect(Collectors.toList());
	}

	public int findDealerHitCount() {
		return dealer.getCardSize() - Cards.CARD_SIZE_BLACKJACK;
	}

	public Dealer getDealer() {
		return dealer;
	}

	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
	}
}

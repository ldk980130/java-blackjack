package blackjack.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import blackjack.domain.card.Card;
import blackjack.domain.card.Deck;
import blackjack.domain.card.CardNumber;
import blackjack.domain.card.CardShape;
import blackjack.domain.result.BlackJackResult;
import blackjack.dto.GameResultDto;
import blackjack.dto.GamerDto;

class BlackJackGameTest {
    @Test
    @DisplayName("딜러와 플레이어에게 게임 시작 시 2장씩 배분한다.")
    void initDistribution() {
        BlackJackGame blackJackGame = new BlackJackGame(
            Arrays.asList("a", "b"), s -> 1, new Deck(Card.getCards()));
        blackJackGame.start((a, b) -> {});

        GamerDto dealerDto = blackJackGame.getDealerDto();
        List<GamerDto> playerDtos = blackJackGame.getPlayerDtos();

        assertThat(dealerDto.getCards().size()).isEqualTo(2);
        assertThat(playerDtos)
                .map(dto -> dto.getCards().size())
                .containsExactly(2, 2);
    }

    @Test
    @DisplayName("딜러의 점수가 17이상일 때 까지 카드를 1장씩 받는다.")
    void dealerDistribution() {
        BlackJackGame blackJackGame = new BlackJackGame(List.of("name"), s -> 1, new Deck(Card.getCards()));
        blackJackGame.askDealerHitOrStay();
        GamerDto dealer = blackJackGame.getDealerDto();
        int cardNumberSum = dealer.getCardNumberSum();
        assertThat(cardNumberSum).isGreaterThan(16);
    }

    @Test
    @DisplayName("1000원 배팅일 때 무승부이면 수익 0원")
    void createResultDraw() {
        BlackJackGame blackJackGame = new BlackJackGame(
            List.of("name"), s -> 1000, () -> Card.getInstance(CardShape.CLOVER, CardNumber.EIGHT));

        blackJackGame.askPlayerHitOrStay(answer -> false, dto -> {});
        GameResultDto result = blackJackGame.createResult();

        int dealerEarning = result.getDealerEarning();
        Map<String, Integer> playerEarnings = result.getPlayerEarnings();

        assertThat(playerEarnings.get("name")).isEqualTo(0);
        assertThat(dealerEarning).isEqualTo(0);
    }

    @Test
    @DisplayName("1000원 배팅일 때 지면 수익 -1000원")
    void createResultLose() {
        BlackJackGame blackJackGame = new BlackJackGame(
            List.of("name"), s -> 1000, () -> Card.getInstance(CardShape.CLOVER, CardNumber.EIGHT));

        blackJackGame.askPlayerHitOrStay(answer -> true, dto -> {});
        GameResultDto result = blackJackGame.createResult();

        int dealerEarning = result.getDealerEarning();
        Map<String, Integer> playerEarnings = result.getPlayerEarnings();

        assertThat(playerEarnings.get("name")).isEqualTo(-1000);
        assertThat(dealerEarning).isEqualTo(1000);
    }
}

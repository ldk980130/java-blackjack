package blackjack.domain.gamer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoneyTest {

	@Test
	@DisplayName("생성자에 0 이하가 들어가면 예외 발생")
	void createWithMinusException() {
		assertThatThrownBy(() -> new Money(-1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("금액은 0보다 커야 합니다");
	}

	@Test
	@DisplayName("0보다 큰 금액으로는 생성된다.")
	void create() {
		assertThat(new Money(1000))
			.isNotNull();
	}

	@Test
	@DisplayName("입력 받은 금액을 가지고 있다.")
	void moneyHaveValue() {
		Money money = new Money(1000);
		assertThat(money).isEqualTo(new Money(1000));
	}
}
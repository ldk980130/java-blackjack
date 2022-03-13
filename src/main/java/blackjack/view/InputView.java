package blackjack.view;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InputView {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static List<String> askNames() {
        System.out.println("게임에 참여할 사람의 이름을 입력하세요.(쉼표 기준으로 분리)");
        List<String> names = Arrays.stream(SCANNER.nextLine().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        System.out.println();
        return names;
    }

    public static boolean askHitOrStay(String name) {
        System.out.println(name + "는 한 장의 카드를 더 받겠습니까?(예는 y, 아니오는 n)");
        return Answer.from(SCANNER.nextLine()).isYes();
    }
}

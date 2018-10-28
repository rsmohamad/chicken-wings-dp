import java.math.BigDecimal;
import java.util.*;

public class ChickenDP {

    private static Map<Integer, Double> prices = new TreeMap<>();

    static class PriceAndSequence {
        BigDecimal price;
        List<Integer> optimalSequence;
        BigDecimal saving;
    }

    // Reconstruction is another DP problem
    private static List<Integer> reconstructOptimalSequence(int optimalSequence[], int wings) {

        List<List<Integer>> optimalSequences = new ArrayList<>(wings + 1);

        for (int i = 0; i <= wings; i++) {
            int j = optimalSequence[i];
            if (j == i) {

                optimalSequences.add(Collections.singletonList(i));
            } else if (i - j < optimalSequences.size()) {

                List<Integer> list = new LinkedList<>();
                list.addAll(optimalSequences.get(j));
                list.addAll(optimalSequences.get(i - j));
                list.sort(Integer::compareTo);
                optimalSequences.add(list);

            } else {
                optimalSequences.add(null);
            }
        }

        return optimalSequences.get(wings);
    }

    private static PriceAndSequence optimalPrice(Map<Integer, Double> prices, int wings) {

        int smallest = Collections.min(prices.keySet());
        BigDecimal optimalPrices[] = new BigDecimal[wings + 1];
        int optimalStep[] = new int[wings + 1];

        for (int i = 0; i <= wings; i++) {
            if (i < smallest) continue;

            BigDecimal cheapest = BigDecimal.valueOf(prices.getOrDefault(i, Double.MAX_VALUE));
            optimalStep[i] = i;

            for (int j = smallest; j < i - smallest; j++) {
                BigDecimal alternatePrice = optimalPrices[i - j].add(optimalPrices[j]);
                if (alternatePrice.compareTo(cheapest) < 0) {
                    cheapest = alternatePrice;
                    optimalStep[i] = j;
                }
            }

            optimalPrices[i] = cheapest;
        }

        PriceAndSequence rv = new PriceAndSequence();

        rv.optimalSequence = reconstructOptimalSequence(optimalStep, wings);
        rv.price = optimalPrices[wings];
        rv.saving = prices.containsKey(wings) ? BigDecimal.valueOf(prices.get(wings)).subtract(optimalPrices[wings]) :
                BigDecimal.ZERO;

        return rv;
    }

    public static void main(String args[]) {
        prices.put(4, 4.55);
        prices.put(5, 5.7);
        prices.put(6, 6.8);
        prices.put(7, 7.95);
        prices.put(8, 9.10);
        prices.put(9, 10.2);
        prices.put(10, 11.35);
        prices.put(11, 12.50);
        prices.put(12, 13.6);
        prices.put(13, 14.75);
        prices.put(14, 15.9);
        prices.put(15, 17.0);
        prices.put(16, 18.15);
        prices.put(17, 19.3);
        prices.put(18, 20.4);
        prices.put(19, 21.55);
        prices.put(20, 22.7);
        prices.put(21, 23.8);
        prices.put(22, 24.95);
        prices.put(23, 26.1);
        prices.put(24, 27.25);
        prices.put(25, 27.8);
        prices.put(26, 28.95);
        prices.put(27, 30.1);
        prices.put(28, 31.2);
        prices.put(29, 32.35);
        prices.put(30, 33.5);
        prices.put(35, 39.15);
        prices.put(40, 44.8);
        prices.put(45, 50.5);
        prices.put(60, 67.0);
        prices.put(70, 78.3);
        prices.put(80, 89.1);
        prices.put(90, 100.45);
        prices.put(100, 111.25);
        prices.put(125, 139.0);
        prices.put(150, 166.85);
        prices.put(200, 222.5);

        for (int i = 4; i <= 200; i++) {
            PriceAndSequence p = optimalPrice(prices, i);
            System.out.print(i + ") " + p.price + " " + p.optimalSequence);
            if (p.saving.compareTo(BigDecimal.ZERO) != 0)
                System.out.print(" saving: " + p.saving);
            System.out.println();

            // Check correctness
            BigDecimal sum = p.optimalSequence.stream()
                    .map(n -> BigDecimal.valueOf(prices.get(n)))
                    .reduce(BigDecimal::add)
                    .get();

            if (sum.compareTo(p.price) != 0) {
                System.out.println("Incorrect " + sum + " " + p.price);
            }

        }
    }

}

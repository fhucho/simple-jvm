package cz.simplejvm;

public class Knapsack {
	private final int maxWeight = 10;
	private final int[] prices = { 4, 7, 5, 6, 2 };
	private final int[] weights = { 6, 5, 4, 7, 3 };

	private int priceOfConfig(boolean[] config) {
		int price = 0;
		int weight = 0;
		for (int i = 0; i < config.length; i++) {
			if (config[i]) {
				price += prices[i];
				weight += weights[i];
			}
		}
		return weight <= maxWeight ? price : -1;
	}

	private void copyConfig(boolean[] src, boolean[] dst) {
		for (int i = 0; i < src.length; i++) {
			dst[i] = src[i];
		}
	}

	private boolean[] solve() {
		boolean[] config = new boolean[5];
		boolean[] bestConfig = new boolean[5];
		solve(0, config, bestConfig, 0);
		return bestConfig;
	}

	private int solve(int index, boolean[] config, boolean[] bestConfig, int bestPrice) {
		if (index == weights.length) {
			int price = priceOfConfig(config);
			if (price > bestPrice) {
				price = priceOfConfig(config);
				copyConfig(config, bestConfig);
			}
			return bestPrice;
		} else {
			config[index] = false;
			int price1 = solve(index + 1, config, bestConfig, bestPrice);
			if (price1 > bestPrice) bestPrice = price1;

			config[index] = true;
			solve(index + 1, config, bestConfig, bestPrice);
			int price2 = solve(index + 1, config, bestConfig, bestPrice);
			if (price2 > bestPrice) bestPrice = price1;

			return bestPrice;
		}
	}

	public static void start() {
		boolean[] config = new Knapsack().solve();
	}
}
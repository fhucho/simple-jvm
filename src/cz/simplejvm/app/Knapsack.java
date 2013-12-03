package cz.simplejvm.app;

public class Knapsack {
	private final int maxWeight = 10;
	private final int[] prices = { 4, 7, 5, 6, 2 };
	private final int[] weights = { 6, 5, 4, 7, 3 };

	public int priceOfConfig(boolean[] config) {
		new NativeMethods().print(2222);
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

	public void copyConfig(boolean[] src, boolean[] dst) {
		new NativeMethods().print(3333);
		for (int i = 0; i < src.length; i++) {
			dst[i] = src[i];
		}
	}

	public boolean[] solve() {
		boolean[] config = new boolean[5];
		boolean[] bestConfig = new boolean[5];
		new NativeMethods().print(111);
		solve(0, config, bestConfig, 0);
		return bestConfig;
	}

	public int solve(int index, boolean[] config, boolean[] bestConfig, int bestPrice) {
		new NativeMethods().print(000004544);
		if (index == weights.length) {
			new NativeMethods().print(11111);
			int price = priceOfConfig(config);
			if (price > bestPrice) {
				price = priceOfConfig(config);
				copyConfig(config, bestConfig);
			}
			return bestPrice;
		} else {
			new NativeMethods().print(bestPrice);
			config[index] = false;
			int price1 = solve(index + 1, config, bestConfig, bestPrice);
			if (price1 > bestPrice) {
				bestPrice = price1;
			}

			config[index] = true;
			solve(index + 1, config, bestConfig, bestPrice);
			int price2 = solve(index + 1, config, bestConfig, bestPrice);
			if (price2 > bestPrice) {
				bestPrice = price1;
			}

			return bestPrice;
		}
	}

	public static void start() {
		boolean[] config = new Knapsack().solve();
	}
}
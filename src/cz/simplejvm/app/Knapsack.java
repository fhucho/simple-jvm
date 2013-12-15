package cz.simplejvm.app;

public class Knapsack {
	private final int maxWeight;
	private final int[] prices;
	private final int[] weights;



	public Knapsack(int maxWeight, int[] prices, int[] weights) {
		this.maxWeight=maxWeight;
		this.prices=prices;
		this.weights=weights;
	}

	public int priceOfConfig(boolean[] config) {
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
		for (int i = 0; i < src.length; i++) {
			dst[i] = src[i];
		}
	}

	private void printConfig(boolean[] config) {
		for(int i=0; i<config.length; i++) {
			boolean value =config[i];
			new NativeMethods().print(value?1:0);
		}
		new NativeMethods().println(' ');
	}

	public boolean[] solve() {
		boolean[] config = new boolean[weights.length];
		boolean[] bestConfig = new boolean[weights.length];
		solve(0, config, bestConfig, 0);
		return bestConfig;
	}

	public int solve(int index, boolean[] config, boolean[] bestConfig, int bestPrice) {
		//		new NativeMethods().print(000004544);

		if (index == weights.length) {
			int price = priceOfConfig(config);
			if (price > bestPrice) {
				bestPrice=price;
				copyConfig(config, bestConfig);
			}
			return bestPrice;
		} else {
			//			new NativeMethods().print(bestPrice);
			config[index] = false;
			int price1 = solve(index + 1, config, bestConfig, bestPrice);
			if (price1 > bestPrice) {
				bestPrice = price1;
			}

			config[index] = true;
			int price2 = solve(index + 1, config, bestConfig, bestPrice);
			if (price2 > bestPrice) {
				bestPrice = price2;
			}

			return bestPrice;
		}
	}

	public static void start() {
		NativeMethods nm = new NativeMethods();
		char[] inputFname  = {'k', 'n', 'a', 'p', 'I', 'n'};
		char[] inputText = nm.readFromFile(inputFname);
		int[] numbers = nm.stringToIntArray(inputText);
		int n = numbers[0];
		int maxWeight=numbers[1];
		int[] prices = new int[n];
		int[] weights = new int[n];
		for(int i=0; i<n; i++) {
			weights[i] = numbers[i+2];
			prices[i] = numbers[n+i+2];
		}

		nm.println(maxWeight);
		nm.println(prices);
		nm.println(weights);

		nm.println('-');

		int totalCost=0;
		int totalWeight=0;

		Knapsack kn = new Knapsack(maxWeight, prices, weights);
		boolean[] config = kn.solve();
		for(int i=0; i<config.length; i++) {
			boolean value =config[i];
			if(value) {
				totalCost+=prices[i];
				totalWeight+=weights[i];
			}
			nm.print(value?1:0);
		}
		nm.println('*');
		nm.println(totalCost);
		nm.println(totalWeight);
	}
}
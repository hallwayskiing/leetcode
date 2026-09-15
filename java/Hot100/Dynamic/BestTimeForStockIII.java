package Hot100.Dynamic;

public class BestTimeForStockIII {
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0) return 0;

        int buy1 = Integer.MIN_VALUE;
        int sell1 = 0;
        int buy2 = Integer.MIN_VALUE;
        int sell2 = 0;

        for (int price : prices) {
            // Buy 1: Pay less
            buy1 = Math.max(buy1, -price);
            // Sell 1: Earn more
            sell1 = Math.max(sell1, buy1 + price);
            // Buy 2: Reinvest
            buy2 = Math.max(buy2, sell1 - price);
            // Sell 2: Maximize the sum
            sell2 = Math.max(sell2, buy2 + price);
        }

        return sell2;
    }
}

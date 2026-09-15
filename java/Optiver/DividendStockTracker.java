package Optiver;

class Dividend {
    long amount;
    int day;

    public Dividend(long amount, int day) {
        this.amount = amount;
        this.day = day;
    }
}

/**
 * Prefix sum and dirty flag
 */
public class DividendStockTracker {
    private final long initialPrice;
    private final Dividend[] dividends;
    private final long[] dailyAmount;
    private final long[] prefixSums;
    private boolean isDirty = true;

    private static final int MAX_DAY = 1000000;

    public DividendStockTracker(long initialPrice, int n) {
        this.initialPrice = initialPrice;
        this.dividends = new Dividend[n + 1];
        this.dailyAmount = new long[MAX_DAY + 1];
        this.prefixSums = new long[MAX_DAY + 1];
    }

    public void updateDividend(int i, long newAmount, int newDay) {
        // 1. Remove old record
        if (dividends[i] != null) {
            dailyAmount[dividends[i].day] -= dividends[i].amount;
        }

        // 2. Update record
        dividends[i] = new Dividend(newAmount, newDay);
        dailyAmount[newDay] += newAmount;

        // 3. Set isDirty
        isDirty = true;
    }

    private void refreshPrefixSums() {
        if (!isDirty) return;

        long currentTotal = 0;
        for (int d = 1; d <= MAX_DAY; d++) {
            currentTotal += dailyAmount[d];
            prefixSums[d] = currentTotal;
        }
        isDirty = false;
    }

    public long calculateFuturePrice(int futureDay) {
        refreshPrefixSums();

        int day = Math.min(futureDay, MAX_DAY);
        return initialPrice - prefixSums[day];
    }
}

/**
 * Fenwick Tree to efficiently calculate a prefix sum
 */
class FenwickTree {
    private final long[] tree;
    private final int size;

    public FenwickTree(int size) {
        this.size = size;
        this.tree = new long[size + 1];
    }

    public void update(int day, long delta) {
        for (; day <= size; day += day & -day) {
            tree[day] += delta;
        }
    }

    public long query(int day) {
        long sum = 0;
        day = Math.min(day, size); // 防止越界
        for (; day > 0; day -= day & -day) {
            sum += tree[day];
        }
        return sum;
    }
}

/**
 * Fenwick Tree implementation
 */
class DividendStockTrackerWithFenwick {
    private final long initialPrice;
    private final Dividend[] dividends;
    private final FenwickTree ft;
    private static final int MAX_DAY = 1000000;

    public DividendStockTrackerWithFenwick(long initialPrice, int n) {
        this.initialPrice = initialPrice;
        this.dividends = new Dividend[n + 1]; // 1-based indexing
        this.ft = new FenwickTree(MAX_DAY);
    }

    public void updateDividend(int i, long newAmount, int newDay) {
        if (dividends[i] != null) {
            ft.update(dividends[i].day, -dividends[i].amount);
        }

        dividends[i] = new Dividend(newAmount, newDay);

        ft.update(newDay, newAmount);
    }

    public long calculateFuturePrice(int futureDay) {
        long totalDividendsUntilF = ft.query(futureDay);
        return initialPrice - totalDividendsUntilF;
    }
}
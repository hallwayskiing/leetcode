package Optiver;

public class DaysCounter {
    private final int EPOCH_YEAR = 1970;
    private final int[] DAYS_IN_MONTH = new int[]{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /**
     * @param date format: 2026-4-12
     * @return days to 1970-1-1
     */
    public int countDays(String date) {
        String[] ymd = date.split("-");
        int days = 0;

        // 1. Years
        int year = Integer.parseInt(ymd[0]);
        for (int i = EPOCH_YEAR; i < year; i++) {
            days += isLeap(i) ? 366 : 365;
        }
        // 2. Months
        int month = Integer.parseInt(ymd[1]);
        for (int i = 1; i < month; i++) {
            days += DAYS_IN_MONTH[i];
        }
        if (isLeap(year) && month > 2) {
            days++;
        }
        // 3. Days
        days += Integer.parseInt(ymd[2]);

        return days;
    }

    public int daysBetween(String date1, String date2) {
        return Math.abs(countDays(date1) - countDays(date2));
    }

    private boolean isLeap(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }
}

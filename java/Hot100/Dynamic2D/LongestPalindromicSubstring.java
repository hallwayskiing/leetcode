package Hot100.Dynamic2D;

/**
 * Given a string s, return the longest palindromic substring in s.
 */
public class LongestPalindromicSubstring {
    public String longestPalindrome(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        int maxLen = 1, start = 0;

        // ensure j-i goes from short to long
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    if (j - i <= 2) {
                        dp[i][j] = true;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                }

                if (dp[i][j] && (j - i + 1) > maxLen) {
                    maxLen = j - i + 1;
                    start = i;
                }
            }
        }
        return s.substring(start, start + maxLen);
    }

    // center expansion
    public String longestPalindromeII(String s) {
        char[] c = s.toCharArray();
        int n = c.length;
        int left = 0;
        int right = 0;
        // i = 2 * n-1 : n * char centers, n-1 * space centers
        for (int i = 0; i < 2 * n - 1; i++) {
            int l = i / 2;
            int r = (i + 1) / 2;
            while (l >= 0 && r < n && c[l] == c[r]) {
                l--;
                r++;
            }
            if (r - l - 1 > right - left) {
                left = l + 1;
                right = r;
            }
        }
        return s.substring(left, right);
    }
}

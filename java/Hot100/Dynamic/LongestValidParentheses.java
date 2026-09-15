package Hot100.Dynamic;

/**
 * Given a string containing just the characters '(' and ')', return the length of the longest valid (well-formed) parentheses substring.
 */
public class LongestValidParentheses {
    public int longestValidParentheses(String s) {
        if (s == null || s.length() < 2) {
            return 0;
        }

        int maxLength = 0;
        // dp[i] is the Longest valid length of substring ending with i
        int[] dp = new int[s.length()];

        for (int i = 1; i < s.length(); i++) {
            char currentChar = s.charAt(i);

            // there might be a valid matching only if current=')'
            if (currentChar == ')') {
                char prevChar = s.charAt(i - 1);

                // 1. ...()  match successfully
                if (prevChar == '(') {
                    dp[i] = i >= 2 ? dp[i - 2] + 2 : 2;
                }
                // 2. ...))  exam if the latter ')' could match to previous '('
                else if (prevChar == ')' && dp[i - 1] > 0) {
                    // find the possible position of '('
                    int matchingPos = i - dp[i - 1] - 1;

                    // exam matching position
                    if (matchingPos >= 0 && s.charAt(matchingPos) == '(') {
                        // new length = matched length of the former ')' + 2 + matched length before new matched '('
                        dp[i] = dp[i - 1] + 2 + (matchingPos >= 1 ? dp[matchingPos - 1] : 0);
                    }
                }

                // update the max
                maxLength = Math.max(maxLength, dp[i]);
            }
        }

        return maxLength;
    }

    public int longestValidParenthesesII(String s) {
        if (s == null || s.length() < 2) return 0;

        int maxLen = 0;
        int[] dp = new int[s.length()];

        for (int i = 0; i < s.length(); i++) {
            char curr = s.charAt(i);
            if (curr == ')') {
                char prev = s.charAt(i - 1);
                if (prev == '(') {
                    dp[i] = i >= 2 ? dp[i - 2] + 2 : 2;
                } else if (prev == ')') {
                    int match = i - dp[i - 1] - 1;
                    if (match >=0 && s.charAt(match) == '(') {
                        dp[i] = 2 + dp[i - 1] + (match >= 1 ? dp[match - 1] : 0);
                    }
                }
            }
            maxLen = Math.max(maxLen, dp[i]);
        }
        return maxLen;
    }
}

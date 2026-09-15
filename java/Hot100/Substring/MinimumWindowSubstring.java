package Hot100.Substring;

/**
 * Given two strings s and t of lengths m and n respectively,
 * return the minimum window substring of s such that every character in t (including duplicates) is included in the window.
 * If there is no such substring, return the empty string "".
 * <p>
 * The testcases will be generated such that the answer is unique.
 */
public class MinimumWindowSubstring {
    public String minWindow(String s, String t) {
        int m = s.length();
        int n = t.length();
        if (m < n) return "";

        int[] cntT = new int[128];
        for (char c : t.toCharArray()) {
            cntT[c]++;
        }

        int[] cntS = new int[128];
        int target = 0;
        int minLeft = 0;
        int minLen = Integer.MAX_VALUE;

        int left = 0;
        int right = 0;
        while (right < m) {
            char r = s.charAt(right);

            cntS[r]++;
            right++;

            // find a target
            if (cntS[r] <= cntT[r]) {
                target++;
            }

            while (target == n) {
                if (right - left < minLen) {
                    minLeft = left;
                    minLen = right - left;
                }

                char l = s.charAt(left);

                cntS[l]--;
                left++;

                if (cntS[l] < cntT[l]) {
                    target--;
                }
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(minLeft, minLeft + minLen);
    }
}

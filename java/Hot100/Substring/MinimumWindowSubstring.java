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
        int[] need = new int[128];
        for (char c : t.toCharArray()) {
            need[c]++;
        }
        int remain = t.length();

        int start = 0, end = Integer.MAX_VALUE;
        int left = 0;
        for (int right = 0; right < s.length(); right++) {
            char curr = s.charAt(right);
            if (need[curr]>0){
                remain--;
            }
            need[curr]--;

            if (remain==0){
                while (need[s.charAt(left)]<0){
                    need[s.charAt(left)]++;
                    left++;
                }

                if(right - left < end - start){
                    start=left;
                    end=right;
                }

                need[s.charAt(left)]++;
                left++;
                remain++;
            }
        }

        return end == Integer.MAX_VALUE ? "" : s.substring(start, end + 1);
    }
}

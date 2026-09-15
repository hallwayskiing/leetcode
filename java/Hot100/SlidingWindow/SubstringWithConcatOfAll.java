package Hot100.SlidingWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * You are given a string s and an array of strings words. All the strings of words are of the same length.
 * <p>
 * A concatenated string is a string that exactly contains all the strings of any permutation of words concatenated.
 * <p>
 * For example, if words = ["ab","cd","ef"], then "abcdef", "abefcd", "cdabef", "cdefab", "efabcd", and "efcdab" are all concatenated strings.
 * "acdbef" is not a concatenated string because it is not the concatenation of any permutation of words.
 * Return an array of the starting indices of all the concatenated substrings in s. You can return the answer in any order.
 */
public class SubstringWithConcatOfAll {
    public List<Integer> findSubstring(String s, String[] words) {
        List<Integer> res = new ArrayList<>();
        if (s == null || words == null || words.length == 0) return res;

        int wordLen = words[0].length();
        int wordCount = words.length;
        int totalLen = wordLen * wordCount;
        int n = s.length();
        if (n < totalLen) return res;

        // Build frequency of target words
        Map<String, Integer> target = new HashMap<>();
        for (String w : words) target.put(w, target.getOrDefault(w, 0) + 1);

        // Sliding windows by offset
        for (int offset = 0; offset < wordLen; offset++) {
            int left = offset;
            int right = offset;
            int count = 0;
            Map<String, Integer> window = new HashMap<>();

            while (right + wordLen <= n) {
                String currWord = s.substring(right, right + wordLen);
                right += wordLen;

                // if current word is not in target, reset
                if (!target.containsKey(currWord)) {
                    window.clear();
                    count = 0;
                    left = right;
                    continue;
                }

                window.put(currWord, window.getOrDefault(currWord, 0) + 1);
                count++;

                // remove extra words
                while (window.get(currWord) > target.get(currWord)) {
                    String lw = s.substring(left, left + wordLen);
                    window.put(lw, window.get(lw) - 1);
                    left += wordLen;
                    count--;
                }

                if (count == wordCount) res.add(left);
            }
        }
        return res;
    }
}


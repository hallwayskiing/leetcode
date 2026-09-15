package Hot100.SlidingWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given two strings s and p, return an array of all the start indices of p's anagrams in s. You may return the answer in any order.
 */
public class FindAllAnagrams {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        int len_s = s.length(), len_p = p.length();
        if (len_s < len_p) {
            return res;
        }

        int[] map_s = new int[26], map_p = new int[26];

        for (int i = 0; i < len_p; i++) {
            map_s[s.charAt(i) - 'a']++;
            map_p[p.charAt(i) - 'a']++;
        }

        if (Arrays.equals(map_p, map_s)) {
            res.add(0);
        }

        for (int i = len_p; i < len_s; i++) {
            map_s[s.charAt(i - len_p) - 'a']--;
            map_s[s.charAt(i) - 'a']++;

            if (Arrays.equals(map_p, map_s)) {
                res.add(i - len_p + 1);
            }
        }

        return res;
    }
}

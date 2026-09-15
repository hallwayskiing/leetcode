package Hot100.Greedy;

import java.util.ArrayList;
import java.util.List;

/**
 * You are given a string s. We want to partition the string into as many parts as possible so that each letter appears in at most one part.
 * For example, the string "ababcc" can be partitioned into ["abab", "cc"], but partitions such as ["aba", "bcc"] or ["ab", "ab", "cc"] are invalid.
 * <p>
 * Note that the partition is done so that after concatenating all the parts in order, the resultant string should be s.
 * <p>
 * Return a list of integers representing the size of these parts.
 */
public class PartitionLabels {
    public List<Integer> partitionLabels(String s) {
        int[] lastAppear = new int[26];
        for (int i = 0; i < s.length(); i++) {
            lastAppear[s.charAt(i) - 'a'] = i;
        }

        List<Integer> res = new ArrayList<>();
        int begin = 0;
        int bound = 0;
        for (int i = 0; i < s.length(); i++) {
            bound = Math.max(bound, lastAppear[s.charAt(i) - 'a']);
            if (i == bound) {
                res.add(bound - begin + 1);
                begin = bound + 1;
            }
        }

        return res;
    }
}

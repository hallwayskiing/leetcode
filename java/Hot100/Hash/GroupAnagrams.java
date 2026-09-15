package Hot100.Hash;

import java.util.*;

/**
 * Given an array of strings strs, group the anagrams together. You can return the answer in any order.
 */
public class GroupAnagrams {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String,List<String>> map=new HashMap<>();

        for(String str:strs){
            char[]chars=str.toCharArray();
            Arrays.sort(chars);
            String key=new String(chars);

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
        }

        return map.values().stream().toList();
    }
}

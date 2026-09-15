package Hot100.Backtrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the number could represent.
 * Return the answer in any order.
 * <p>
 * A mapping of digits to letters (just like on the telephone buttons) is given below. Note that 1 does not map to any letters.
 */
public class CombinationOfPhoneNumber {
    public List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();

        if(digits.isEmpty()) {
            return res;
        }

        Map<Character, String> map = new HashMap<>();
        map.put('2', "abc");
        map.put('3', "def");
        map.put('4', "ghi");
        map.put('5', "jkl");
        map.put('6', "mno");
        map.put('7', "pqrs");
        map.put('8', "tuv");
        map.put('9', "wxyz");

        backtrack(0, digits.toCharArray(), map, new StringBuilder(), res);
        return res;
    }

    public void backtrack(int curr, char[] digits, Map<Character, String> map, StringBuilder sb, List<String> res) {
        if (curr == digits.length) {
            res.add(new String(sb));
            return;
        }

        char digit = digits[curr];
        String str = map.get(digit);
        for (char c : str.toCharArray()) {
            sb.append(c);
            backtrack(curr + 1, digits, map, sb, res);
            sb.deleteCharAt(sb.length() - 1);
        }

    }
}

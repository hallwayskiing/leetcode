package Hot100.Backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.
 */
public class GenerateParentheses {
    public List<String> generateParenthesis(int n) {
        List<String>res=new ArrayList<>();
        backtrack(n,0,0,new StringBuilder(),res);
        return res;
    }

    public void backtrack(int n, int left, int right , StringBuilder sb, List<String>res){
        if(sb.length()==n*2){
            res.add(sb.toString());
            return;
        }

        if(left<n){
            sb.append('(');
            backtrack(n,left+1,right,sb,res);
            sb.deleteCharAt(sb.length()-1);
        }

        if(right<left){
            sb.append(')');
            backtrack(n,left,right+1,sb,res);
            sb.deleteCharAt(sb.length()-1);
        }
    }
}

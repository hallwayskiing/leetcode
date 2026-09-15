package Hot100.Backtrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given an array of distinct integers candidates and a target integer target,
 * return a list of all unique combinations of candidates where the chosen numbers sum to target.
 * You may return the combinations in any order.
 * <p>
 * The same number may be chosen from candidates an unlimited number of times.
 * Two combinations are unique if the frequency of at least one of the chosen numbers is different.
 * <p>
 * The test cases are generated such that the number of unique combinations that sum up to target is less than 150 combinations for the given input.
 */
public class CombinationSum {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>>res=new ArrayList<>();
        Arrays.sort(candidates);
        backtrack(candidates,target,0,new ArrayList<>(),res);
        return res;
    }

    public void backtrack(int[] candidates, int remain, int curr, List<Integer>combination, List<List<Integer>>res){
        if(remain==0){
            res.add(new ArrayList<>(combination));
            return;
        }

        for (int i = curr; i < candidates.length; i++) {
            if(candidates[i]>remain) break;
            combination.add(candidates[i]);
            backtrack(candidates,remain-candidates[i],i,combination,res);
            combination.remove(combination.size()-1);
        }
    }
}

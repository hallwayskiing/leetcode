package Hot100.Backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an integer array nums of unique elements, return all possible subsets (the power set).
 * <p>
 * The solution set must not contain duplicate subsets. Return the solution in any order.
 */
public class Subsets {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>>res=new ArrayList<>();
        List<Integer>subset=new ArrayList<>();

        backtrack(nums,0,subset,res);
        return res;
    }

    public void backtrack(int[]nums, int curr, List<Integer>subset, List<List<Integer>>res){
        res.add(new ArrayList<>(subset));

        for (int i = curr; i < nums.length; i++) {
            subset.add(nums[i]);
            backtrack(nums,i+1,subset,res);
            subset.remove(subset.size()-1);
        }
    }
}

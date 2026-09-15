package Hot100.Backtrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given a collection of numbers, nums, that might contain duplicates,
 * return all possible unique permutations in any order.
 */
public class PermutationsII {
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>>res=new ArrayList<>();
        Arrays.sort(nums);
        boolean[] used = new boolean[nums.length];

        backtrack(nums,used,new ArrayList<>(),res);

        return res;
    }

    public void backtrack(int[] nums, boolean[] used, List<Integer>permutation, List<List<Integer>>res){
        if(permutation.size() == nums.length){
            res.add(new ArrayList<>(permutation));
            return;
        }

        for (int i=0;i<nums.length;i++){
            if(used[i]) continue;

            if(i>0 && nums[i]==nums[i-1] && !used[i-1]) continue;

            permutation.add(nums[i]);
            used[i]=true;
            backtrack(nums,used,permutation,res);
            used[i]=false;
            permutation.remove(permutation.size()-1);
        }
    }

}

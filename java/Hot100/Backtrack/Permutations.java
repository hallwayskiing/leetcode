package Hot100.Backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an array nums of distinct integers, return all the possible permutations.
 * You can return the answer in any order.
 */
public class Permutations {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>>res=new ArrayList<>();
        List<Integer>order=new ArrayList<>();
        boolean[]visited=new boolean[nums.length];

        backtrack(nums,visited,order,res);
        return res;
    }

    public void backtrack(int[] nums, boolean[] visited, List<Integer>order, List<List<Integer>>res){
        if(order.size()==nums.length){
            res.add(new ArrayList<>(order));
            return;
        }

        for (int i=0; i<nums.length; i++){
            if(visited[i]) continue;
            order.add(nums[i]);
            visited[i]=true;
            backtrack(nums,visited,order,res);
            visited[i]=false;
            order.remove(order.size()-1);
        }
    }
}

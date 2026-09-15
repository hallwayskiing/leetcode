package Interview150.ArrayOrString;

public class RemoveDuplicatesII {
    public int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int curr = 1;
        int times = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) {
                if (times < 2) {
                    nums[curr] = nums[i];
                    curr++;
                    times++;
                }
                // pass if times >=2
            }
            else {
                nums[curr] = nums[i];
                curr++;
                times = 1;
            }
        }

        return curr;
    }

    public int removeDuplicatesII(int[] nums) {
        int curr=2;
        for (int i = 2; i < nums.length; i++) {
            if(nums[i]!=nums[curr-2]){
                nums[curr]=nums[i];
                curr++;
            }
        }
        return curr;
    }
}

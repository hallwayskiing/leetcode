package Interview150.ArrayOrString;

public class RotateArray {
    public void rotate(int[] nums, int k) {
        int n=nums.length;
        if(n==1) return;
        k=k%n;

        reverse(nums,0,n-1);
        reverse(nums,0,k-1);
        reverse(nums,k,n-1);
    }

    private void reverse(int[]nums, int begin, int end){
        while (begin<end){
            int temp=nums[begin];
            nums[begin]=nums[end];
            nums[end]=temp;
            begin++;
            end--;
        }
    }
}

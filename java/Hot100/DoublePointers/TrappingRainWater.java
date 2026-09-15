package Hot100.DoublePointers;

/**
 * Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.
 */
public class TrappingRainWater {
    public int trap(int[] height) {
        int n=height.length;
        int left=0,right=n-1;
        int leftMax=0,rightMax=0;
        int ans=0;
        while(left<right){
            leftMax = Math.max(leftMax, height[left]);
            rightMax = Math.max(rightMax, height[right]);
            if(height[left]<height[right]){
                ans+=leftMax-height[left];
                left++;
            }
            else{
                ans+=rightMax-height[right];
                right--;
            }
        }
        return ans;
    }
}

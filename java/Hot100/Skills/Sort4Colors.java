package Hot100.Skills;

public class Sort4Colors {
    public void sort4Colors(int[] nums) {
        int p0 = 0, p1 = 0, p2 = 0, p3 = nums.length - 1;

        while (p2 <= p3) {
            if (nums[p2] == 0) {
                swap(nums, p0, p2);
                if (p0 < p1) { // area 1 is established and p0 is a part of it
                    swap(nums, p1, p2); // swap part of area 1 back to area 1
                }
                p0++;
                p1++;
                p2++;
            } else if (nums[p2] == 1) {
                swap(nums, p1, p2);
                p1++;
                p2++;
            } else if (nums[p2] == 2) {
                p2++;
            } else { // nums[p2] == 3
                swap(nums, p2, p3);
                p3--;
            }
        }
    }

    private void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}

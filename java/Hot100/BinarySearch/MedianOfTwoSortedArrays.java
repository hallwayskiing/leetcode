package Hot100.BinarySearch;

/**
 * Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.
 * <p>
 * The overall run time complexity should be O(log (m+n)).
 */
public class MedianOfTwoSortedArrays {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int total = nums1.length + nums2.length;
        if (total % 2 == 1) {
            return getKthMin(nums1, nums2, total / 2 + 1);
        } else {
            int left = getKthMin(nums1, nums2, total / 2);
            int right = getKthMin(nums1, nums2, total / 2 + 1);
            return (left + right) / 2.0;
        }
    }

    private int getKthMin(int[] nums1, int[] nums2, int k) {
        int i = 0; // pointer of nums1
        int j = 0; // pointer of nums2
        int len1 = nums1.length, len2 = nums2.length;

        while (true) {
            if (i == len1) {
                return nums2[j + k - 1];
            }
            if (j == len2) {
                return nums1[i + k - 1];
            }
            if (k == 1) {
                return Math.min(nums1[i], nums2[j]);
            }

            int half = k / 2;
            int newI = Math.min(i + half, len1) - 1;
            int newJ = Math.min(j + half, len2) - 1;

            if (nums1[newI] <= nums2[newJ]) {
                k -= (newI - i + 1);
                i = newI + 1;
            } else {
                k -= (newJ - j + 1);
                j = newJ + 1;
            }
        }
    }
}

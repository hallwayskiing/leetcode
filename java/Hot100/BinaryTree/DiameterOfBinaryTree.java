package Hot100.BinaryTree;

/**
 * Given the root of a binary tree, return the length of the diameter of the tree.
 * <p>
 * The diameter of a binary tree is the length of the longest path between any two nodes in a tree. This path may or may not pass through the root.
 * <p>
 * The length of a path between two nodes is represented by the number of edges between them.
 */
public class DiameterOfBinaryTree {
    int ans = 0;

    public int diameterOfBinaryTree(TreeNode root) {
        search(root);
        return ans;
    }

    private int search(TreeNode root) {
        if (root == null)
            return 0;

        int L = search(root.left);
        int R = search(root.right);
        ans = Math.max(ans, L + R);
        return Math.max(L, R) + 1;
    }
}

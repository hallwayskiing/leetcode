package Hot100.BinaryTree;

import java.util.HashMap;
import java.util.Map;

public class BuildTreeFromPreorderAndInorder {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        // Optimization: Use a HashMap to store the value -> index mapping for Inorder array
        // This allows us to find the root's position in O(1) time.
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }

        // [start, end)
        return buildTree(preorder, 0, preorder.length, 0, inorder.length, map);
    }

    private TreeNode buildTree(int[] preorder, int p_start, int p_end,
                                int i_start, int i_end, Map<Integer, Integer> map) {
        // Base case: If the range is empty, return null
        if (p_start == p_end) {
            return null;
        }

        // 1. The first element in preorder is always the root of the current subtree
        int root_val = preorder[p_start];
        TreeNode root = new TreeNode(root_val);

        // 2. Find the root's index in the inorder array to split left and right subtrees
        int i_root = map.get(root_val);

        // 3. Calculate the number of nodes in the left subtree
        int leftNum = i_root - i_start;

        // 4. Recursively build the left and right subtrees
        // Left Subtree:
        // Preorder range: [p_start + 1, p_start + 1 + leftNum)
        // Inorder range: [i_start, root_index)
        root.left = buildTree(preorder, p_start + 1, p_start + leftNum + 1,
                i_start, i_root, map);

        // Right Subtree:
        // Preorder range: [p_start + 1 + leftNum, p_end)
        // Inorder range:  [root_index + 1, i_end)
        root.right = buildTree(preorder, p_start + leftNum + 1, p_end,
                i_root + 1, i_end, map);

        return root;
    }
}

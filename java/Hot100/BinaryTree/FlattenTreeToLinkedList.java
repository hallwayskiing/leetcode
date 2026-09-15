package Hot100.BinaryTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Given the root of a binary tree, flatten the tree into a "linked list":
 * <p>
 * The "linked list" should use the same TreeNode class,
 * where the right child pointer points to the next node in the list and the left child pointer is always null.
 * The "linked list" should be in the same order as a pre-order traversal of the binary tree.
 */
public class FlattenTreeToLinkedList {
    public void flatten(TreeNode root) {
        List<TreeNode> order = new ArrayList<>();
        preorder(root, order);

        TreeNode curr = root;
        for (int i = 1; i < order.size(); i++) {
            curr.left = null;
            curr.right = order.get(i);
            curr = curr.right;
        }
    }

    public void preorder(TreeNode root, List<TreeNode> list) {
        if (root == null) return;

        list.add(root);
        preorder(root.left, list);
        preorder(root.right, list);
    }

    public void flatten_o1space(TreeNode root) {
        if (root == null) return;

        TreeNode curr = root;
        while (curr != null) {
            if (curr.left != null) {
                TreeNode predecessor = curr.left;
                while (predecessor.right != null) {
                    predecessor = predecessor.right;
                }
                predecessor.right = curr.right;
                curr.right = curr.left;
                curr.left = null;
            }
            curr = curr.right;
        }
    }
}

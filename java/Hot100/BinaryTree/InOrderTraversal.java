package Hot100.BinaryTree;

import java.util.ArrayList;
import java.util.List;

public class InOrderTraversal {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer>res=new ArrayList<>();
        inorder(root,res);
        return res;
    }

    private void inorder(TreeNode node, List<Integer>list){
        if(node==null)return;

        inorder(node.left,list);
        list.add(node.val);
        inorder(node.right,list);
    }
}

package Hot100.BinaryTree;

import java.util.*;

public class RightSideView {
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer>res=new ArrayList<>();
        if(root==null){
            return res;
        }

        Queue<TreeNode>queue=new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()){
            int n=queue.size();
            while (n-->0){
                TreeNode node=queue.poll();
                if(node.left!=null){
                    queue.add(node.left);
                }
                if(node.right!=null){
                    queue.add(node.right);
                }

                if(n==0){
                    res.add(node.val);
                }
            }

        }

        return res;
    }
}

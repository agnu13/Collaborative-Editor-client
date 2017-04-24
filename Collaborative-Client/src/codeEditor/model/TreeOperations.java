package codeEditor.model;

class TreeOperations {
    public static int getSize(Node root){
        if (root == null) {
            return 0;
        } else {
            return root.subTreeSize;
        }
    }
    
    public static void adjustAugmentation(Node root){
        int leftChildSize = getSize(root.left);
        int rightChildSize = getSize(root.right);
        root.subTreeSize = leftChildSize + rightChildSize + 1;
    }
    
    public static Node rotateLeft(Node root){
        try {
            if (root.right == null) {
                throw new NullPointerException("Root Right Not Present");
            } else {
                Node rightChild = root.right;
                Node A = root.left;
                Node B = rightChild.left;
                Node C = rightChild.right;
                rightChild.left = root;
                root.right = B;
                //
                    adjustAugmentation(root);
                    adjustAugmentation(rightChild);                    
                //
                return rightChild;
            }  
        } catch(NullPointerException e) {
            e.printStackTrace(System.out);
        }
        return null;
    }
    
    public static Node rotateRight(Node root)
    {
        try {
            if (root.left == null) {
                throw new NullPointerException("Root Left Not Present");
            } else {
                Node leftChild = root.left;
                Node A = leftChild.left;
                Node B = leftChild.right;
                Node C = root.right;
                leftChild.right = root;
                root.left = B;
                //
                    adjustAugmentation(root);
                    adjustAugmentation(leftChild);                    
                //
                return leftChild;
            }  
        } catch(NullPointerException e) {
            e.printStackTrace(System.out);
        }
        return null;
    }
}

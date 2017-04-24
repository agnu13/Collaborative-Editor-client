package codeEditor.model;

class Node {
    static final int maxRange = 10000; 
    
    int subTreeSize;
    int priority;
    char character;
    Node left, right;
    Node(char character){
        this.character = character;
        priority = (int)(Math.random() * maxRange);
        subTreeSize = 1;
        left = right = null;
    }
}

public class Treap implements Model{   
    
    Node root;
    
    private Node insert(Node currentNode, int positionToInsert, char character){
        if (currentNode == null) {
            Node newNode = new Node(character);
            return newNode;
        } else {
            currentNode.subTreeSize++;
            int leftSubTreeSize = TreeOperations.getSize(currentNode.left); 
            if (positionToInsert <= leftSubTreeSize) {
                currentNode.left = insert(currentNode.left, positionToInsert, character);
                if (currentNode.left.priority < currentNode.priority) {
                    return TreeOperations.rotateRight(currentNode);
                }
            } else {
                currentNode.right = insert(currentNode.right, positionToInsert - (leftSubTreeSize+1), character);
                if (currentNode.right.priority < currentNode.priority) {
                    return TreeOperations.rotateLeft(currentNode);
                }
            }
        }
        return currentNode;
    }
    
    private Node merge(Node leftRoot, Node rightRoot){
        if (leftRoot == null) {
            return rightRoot;
        } else if (rightRoot == null) {
            return leftRoot;
        } else {
            if (leftRoot.priority < rightRoot.priority) {
                leftRoot.right = merge(leftRoot.right, rightRoot);
                TreeOperations.adjustAugmentation(leftRoot);
                return leftRoot;
            } else {
                rightRoot.left = merge(leftRoot, rightRoot.left);
                TreeOperations.adjustAugmentation(rightRoot);
                return rightRoot;
            }
        }
    }
    
    private Node erase(Node currentNode, int positionToErase){
        try {
            if (currentNode == null) {
                throw new NullPointerException("Unable to Erase. NullPointer Exception.");
            } else {
                int currentPosition = TreeOperations.getSize(currentNode.left);
                if (currentPosition == positionToErase) {
                    return merge(currentNode.left, currentNode.right);
                }  else {
                    currentNode.subTreeSize--;
                    if (currentPosition > positionToErase) {
                        currentNode.left = erase(currentNode.left, positionToErase);
                    } else {
                        currentNode.right = erase(currentNode.right, positionToErase - currentPosition - 1);
                    }
                    return currentNode;
                }
            }
        } catch (NullPointerException e){
            e.printStackTrace(System.out);
        }
        return null;
    }
    
    private char charAt(Node currentNode, int positionToSeek){
        try {
            if (currentNode == null) {
                throw new NullPointerException("Unable to Seek. NullPointer Exception.");
            } else {
                int currentPosition = TreeOperations.getSize(currentNode.left);
                if (currentPosition == positionToSeek) {
                    return currentNode.character;
                }  else {
                    if (currentPosition > positionToSeek) {
                        return charAt(currentNode.left, positionToSeek);
                    } else {
                        return charAt(currentNode.right, positionToSeek - currentPosition - 1);
                    }
                }
            }
        } catch (NullPointerException e){
            System.out.println("positionToSeek: " + positionToSeek);
            e.printStackTrace(System.out);
        }
        return '?';
    }
    
    private void getTree(Node root){
        if (root == null) {
        } else {
            getTree(root.left);
            System.out.println(root);
            System.out.println(root.character + " " + root.subTreeSize);
            System.out.println(root.left + " " + root.right);
            getTree(root.right);
        }
    }
    
    @Override
    public synchronized void insert(int positionToInsert, char character){
        try {
            if (root != null && (positionToInsert > root.subTreeSize || positionToInsert < 0)) {
                throw new IndexOutOfBoundsException("Can't Insert at position " + positionToInsert +". IndexOutOfBound Exception.");
            } else {
                root = insert(root, positionToInsert, character);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace(System.err);
            System.err.println(toString());
        }
    }
    
    @Override
    public synchronized void erase(int positionToErase){
        try {
            if (root == null) {
                throw new IndexOutOfBoundsException("Can't erase at position " + positionToErase +". IndexOutOfBound Exception.");
            } else if (positionToErase >= root.subTreeSize || positionToErase < 0) {
                throw new IndexOutOfBoundsException("Can't erase at position " + positionToErase +". IndexOutOfBound Exception.");
            }
            root = erase(root, positionToErase);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace(System.out);
            System.err.println(toString());
        }
    }

    @Override
    public synchronized char charAt(int positionToSeek) {
        try {
            if (root == null) {
                    throw new IndexOutOfBoundsException("Can't seek position " + positionToSeek +". IndexOutOfBound Exception.");
            } else if (positionToSeek >= root.subTreeSize || positionToSeek < 0) {
                System.out.println(positionToSeek + " " + root.subTreeSize);
                    throw new IndexOutOfBoundsException("Can't seek position " + positionToSeek +". IndexOutOfBound Exception.");
            
            } else {
                return charAt(root, positionToSeek);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace(System.out);
            System.err.println(toString());
        }
        char invalidChar = '?';
        return invalidChar;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("");
        for (int i = 0; i < getSize(); i++) {
            s.append(charAt(i));
        }
        return s.toString();
    }
    
    @Override
    public void getTree() {
        getTree(root);
    }

    @Override
    public int getSize() {
        return (root == null) ? 0 : root.subTreeSize;
    }
}

class HashMap {
    Tree tree;

    public HashMap() {
        tree = new Tree();
    }

    public Integer find(int key) {
        Tree.Node node = tree.find(key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    public boolean push(int key, int value) {
        tree.insert(key, value);
        return true; // Assuming successful insertion
    }

    public boolean remove(int key) {
        Tree.Node node = tree.find(key);
        if (node != null) {
            tree.remove(key);
            return true;
        }
        return false;
    }
}

class Tree {
    Node root;

    class Node {
        int key;
        int value;
        Node left;
        Node right;
        boolean isRed; // true if the node is red, false if it is black

        Node(int key, int value) {
            this.key = key;
            this.value = value;
            isRed = true; // new node is always red
        }

        @Override
        public String toString() {
            return "Node(key=" + key + ", value=" + value + ", isRed=" + isRed + ")";
        }
    }

    public Node find(int key) {
        return find(root, key);
    }

    private Node find(Node node, int key) {
        if (node == null) {
            return null;
        }
        if (node.key == key) {
            return node;
        }
        if (node.key < key) {
            return find(node.right, key);
        } else {
            return find(node.left, key);
        }
    }

    public void insert(int key, int value) {
        root = insertWithBalance(root, key, value);
        root.isRed = false; // root must always be black
    }

    private Node insertWithBalance(Node node, int key, int value) {
        if (node == null) {
            return new Node(key, value);
        }

        if (key < node.key) {
            node.left = insertWithBalance(node.left, key, value);
        } else if (key > node.key) {
            node.right = insertWithBalance(node.right, key, value);
        } else {
            // Duplicate values are not allowed in the tree
            return node;
        }

        // Perform necessary rotations and color flips to balance the tree
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;
    }

    private boolean isRed(Node node) {
        if (node == null) {
            return false; // null nodes are considered black
        }
        return node.isRed;
    }

    private Node rotateLeft(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        newRoot.isRed = node.isRed;
        node.isRed = true;
        return newRoot;
    }

    private Node rotateRight(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        newRoot.isRed = node.isRed;
        node.isRed = true;
        return newRoot;
    }

    private void flipColors(Node node) {
        node.isRed = !node.isRed;
        node.left.isRed = !node.left.isRed;
        node.right.isRed = !node.right.isRed;
    }

    public void remove(int key) {
        root = remove(root, key);
        if (root != null) {
            root.isRed = false;
        }
    }

    private Node remove(Node node, int key) {
        if (node == null) {
            return null;
        }

        if (key < node.key) {
            node.left = remove(node.left, key);
        } else if (key > node.key) {
            node.right = remove(node.right, key);
        } else {
            // Node to be removed has been found
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            // Node to be removed has both left and right child
            Node successor = findMin(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node.right = removeMin(node.right);
        }

        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = removeMin(node.left);
        return node;
    }
}

class Main {
    public static void main(String[] args) {
        Tree tree = new Tree();

        tree.insert(1, 2);
        tree.insert(3, 4);

        System.out.println(tree.find(3));
        System.out.println(tree.find(2));

        tree.remove(3);
        tree.insert(2, 5);

        System.out.println(tree.find(3));
        System.out.println(tree.find(1));
    }
}

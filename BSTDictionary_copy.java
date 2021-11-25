public class BSTDictionary_copy<E, K extends Sortable> implements Dictionary<E, K> {
    private BSTNode<E, K> root;

    public BSTDictionary_copy(){
        root = null; //Root of the AVL Tree. (start point)
    }
    
    //Insert into the binary search tree dictionary from a given point
    public void insertFromGivenPoint(BSTNode<E,K> node, K key, E element){
        if(key.compareTo(node.getKey()) == 0) {} //Cant have the same key
        else if(key.compareTo(node.getKey()) < 0) { 
            if(node.getLeft() == null) {  //Value is less than current; goes to left
                node.setLeft(new BSTNode<E, K>(key, element, null, null));
            }
            else { //Left is occupied; check its child
                insertFromGivenPoint(node.getLeft(), key, element);
            }
        }
        else if (key.compareTo(node.getKey()) > 0) {
            if(node.getRight() == null) {  //Value is greater than the current value; goes to right side
                node.setRight(new BSTNode<E, K>(key, element, null, null));
            }
            else { //Right is occupied; check its child
                insertFromGivenPoint(node.getRight(), key, element);
            }
        }
        else {} //Error 1
    }
    
    //Checking the conditions of the binary search tree and then inserting
    public void insert(K key, E element){
        if(root == null) { 
            root = new BSTNode<E, K>(key, element, null, null);        
        }
        else {
            insertFromGivenPoint(root, key, element);
        }
    }
    
    //Code for deleting from the tree 
    public BSTNode<E,K> deleteWithTwoBranches(BSTNode<E,K> node, K key){
        if(key.compareTo(node.getKey()) == 0) { 
            if(node.getLeft() == null && node.getRight() == null) { // This node is a leaf and has no branches
                return null;
            }
            else if(node.getLeft() != null && node.getRight() == null) { // Only the left branch is occupied
                return node.getLeft();
            }
            else if(node.getLeft() == null && node.getRight() != null) { // Only the right branch is occupied
                return node.getRight();
            }
            else if((node.getLeft() != null) && (node.getRight() != null)) { // Both side branches off
                BSTNode<E,K> newNode = findMin(node.getRight());
                BSTNode<E,K> leftBranch = node.getLeft();
                newNode.setRight(findReplacement(node.getRight()));
                newNode.setLeft(leftBranch); // Reconnect the left branch
                return newNode;
            }
            else {} //Error 2
        }
        else if(key.compareTo(node.getKey()) < 0) {  //Go to the left side.
            if(node.getLeft() != null) {
                node.left = deleteWithTwoBranches(node.getLeft(), key);
            }
        }
        else if(key.compareTo(node.getKey()) > 0) {  //Go to the right side.
            if(node.getRight() != null) {
                node.right = deleteWithTwoBranches(node.getRight(), key);
            }
        }        
        else {} //Error 3     
        return node;
    }        
    
    //Starts at given point, goes to the left until it hits a null
    public BSTNode<E,K> findMin(BSTNode<E,K> node){
        while(node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }
    
    //Start at given point, goes to the left until it finds a right leaf
    public BSTNode<E,K> findReplacement(BSTNode<E,K> node){
        if(node.getLeft() == null) {
            return node.getRight();
        }
        else {
            node.setLeft(findReplacement(node.getLeft()));
        }
        return node;
    }
    
    //Deletes from the tree
    public void delete(K key){
        root = deleteWithTwoBranches(root, key);
    }
    
    //Finds the depth the tree from a given point
    public int depthFromGivenPoint(BSTNode<E,K> node, int currentDepth){
        if(node != null) { //Keeps calling itself until null is reached; increasing current each time
            return Math.max(depthFromGivenPoint(node.getLeft(), currentDepth++), depthFromGivenPoint(node.getRight(), currentDepth++));
        }
        else {
            return currentDepth;
        }
    }
    
    //Finds the overall depth of the tree
    public int depth(){
        return depthFromGivenPoint(root, 0);
    }
    
    //Search the tree for a specific key
    public E search(K key)
    {
        BSTNode<E,K> node;
        node = searchFromRoot(key);
        if(node == null) {
            return null;
        }
        else { //Gets element from given key
            return searchFromRoot(key).getElement();
        }
    }
    
    //Search for a specific key in the tree, starting from the root.
    public BSTNode<E,K> searchFromRoot(K key){
        if((key == null) || (root == null)) {return null;} // Makes sure it exists
        
        if(key.compareTo(root.getKey()) == 0) {return root;} // Key is equal to the root
        else if(key.compareTo(root.getKey()) < 0) { //Key is less than the root node; look on the left
            return searchFromSpecificNode(root.getLeft(), key);
        }
        else if(key.compareTo(root.getKey()) > 0) { //Key is greater than the root node; look on the right
            return searchFromSpecificNode(root.getRight(), key);
        }
        else {return null;}  //Error 4
    }
    
    //Search for key in the tree; starts at given node
    public BSTNode<E,K> searchFromSpecificNode(BSTNode<E,K> node, K key){
        if(node == null) { 
            return null;
        }
        if(key.compareTo(node.getKey()) == 0) { //Start is node needed
            return node;
        }
        else if(key.compareTo(node.getKey()) < 0) { //Less than the node; go left
            return searchFromSpecificNode(node.getLeft(), key);
        }
        else if(key.compareTo(node.getKey()) > 0) { //Greater than the node; go right
            return searchFromSpecificNode(node.getRight(), key);
        }
        else {return null;} //Error 5
    }
    
    //Prints tree from a given point.
    public void printFrom(BSTNode<E,K> node){
        if(node != null) {
            printFrom(node.getLeft()); //Prints left keys
            System.out.println("key: " + node.getKey().toString() + " element: " + node.getElement().toString());
            printFrom(node.getRight()); //Prints right keys
        }
    }
    
    //Prints tree.
    public void printTree(){
        System.out.println("Printing the Binary Search Tree Dictionary \n");
        printFrom(root);
    }
}

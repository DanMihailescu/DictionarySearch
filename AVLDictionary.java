public class AVLDictionary<E, K extends Sortable> implements Dictionary<E, K> {
  AVLNode<E, K> root; //Root of the AVL Tree. (start point)
  
  private static final int BALANCED = 2;
  private static final int MORELEFT = 1;
  private static final int MORERIGHT = 3;
  
  //Constructor with a default root node(null)
  public AVLDictionary() {
    this(null);
  }
  
  //Constructor with a non-default root node.
  public AVLDictionary(AVLNode<E, K> root) {
    this.root = root;
  }
  
  //Balances the node using rotations
  public AVLNode<E, K> balance(AVLNode<E, K> node) {
    int balanceFactor = node.getBalance();
    if(balanceFactor > MORERIGHT) {  //Needs to make a right rotation
      AVLNode<E, K> rightNode = copyNode(node.getRight());
      if(rightNode == null) return node;
      if(rightNode.getBalance() == MORELEFT) { //Right then left rotation needed
        node = rotateRIGHTLEFT(copyNode(node));
        node = rotateRIGHTRIGHT(copyNode(node));
        node.setBalance(2);
      }
      else if (rightNode.getBalance() >= MORERIGHT){ //Right then right rotation needed
        node = rotateRIGHTRIGHT(copyNode(node));
        node.setBalance(2); 
      }
    }
    else if(balanceFactor < MORELEFT) {  //Needs to make a left rotation
      AVLNode<E, K> leftNode = copyNode(node.getLeft());
      if(leftNode == null) return node;
      if (leftNode.getBalance() == MORERIGHT) { //Left then right rotation
        node = rotateLEFTRIGHT(copyNode(node));
        node = rotateLEFTLEFT(copyNode(node));
        node.setBalance(2);
      }
      else if(leftNode.getBalance() <= MORELEFT) {  //Left then left rotation
        node = rotateLEFTLEFT(copyNode(node));
        node.setBalance(2);
      }
    }
    return copyNode(node); //Returns a copy of the node  
  }
  
  //Copies given node to a new node.
  public AVLNode<E, K> copyNode(AVLNode<E, K>  node) {
    if(node != null) {
      return new AVLNode<E, K>(node.getKey(), node.getElement(), node.getLeft(), node.getRight(), node.getBalance());
    }
    else return null;
  }
  
  // Delete an entry with key passed as the parameter.
  public void delete(K key) {
    this.root = deleteRecursive(root, key);
  }
  
  // Recursive solution to deleting a double node. More details are prevalent beside where this method is called.
  public AVLNode<E, K> deleteDoubleNode(AVLNode<E, K> node) {
    if(node.getLeft() == null) {
      //at the bottom of the nodes.
      return node.getRight();
    }
    else {
      //set the left node as the right of the one we found at the bottom.
      node.setLeft(deleteDoubleNode(node.getLeft()));
    }
    return node;
  }
  
  // Recursive solution to deleting a node. Returns either a node without the  key attached to it (via referencing) 
  // or the original node due to a lack of finding the node with the key.
  public AVLNode<E, K> deleteRecursive(AVLNode<E,K> node, K key) {
    if(key.compareTo(node.getKey()) == 0) {  
      if((node.getLeft() == null) && (node.getRight() == null)) { //if its a leaf
        return null; 
      }
      else if((node.getLeft() == null) && (node.getRight() != null)) { //Node with one child on the right
        return node.getRight();
      }
      else if((node.getLeft() != null) && (node.getRight() == null)) { //Node with one child on the left
        return node.getLeft();
      }
      else if((node.getLeft() != null) && (node.getRight() != null)) { //Node with 2 children
        AVLNode<E, K> replacementNode = findMin(node.getRight()); 
        AVLNode<E, K> backupLeft = node.getLeft();
        replacementNode.setRight(deleteDoubleNode(node.getRight())); 
        replacementNode.setLeft(backupLeft); 
        return replacementNode; 
      }
    }
    else if(key.compareTo(node.getKey()) < 0) {  //Key is still less than the node
      if(node.getLeft() != null) {
        node.setLeft(copyNode(deleteRecursive(node.getLeft(), key))); // Looks to the left of the node
        node = balance(copyNode(node));
      }
    }
    else { //Key is still greater than the node
      if(node.getRight() != null) {
        node.setRight(copyNode(deleteRecursive(node.getRight(), key))); //Looks to the right of the node
        node = balance(copyNode(node));
      }
    }
    return node;
  }
  
  //Returns the depth of the tree
  public int depth() {
    return postorder_depth(root, 0);
  }
  
  //Finds the minimum value from a particular node.
  public AVLNode<E, K> findMin(AVLNode<E, K> node) {
    while(node.getLeft() != null) {
      node = node.getLeft();
    }
    return node;
  }
  
  //Prints out the tree in the order of "inorder" processing.
  public void inorder(AVLNode<E,K> node) {
    if(node != null) {
      inorder(node.getLeft()); 
      System.out.println("key: " + node.getKey().toString() + " element: " + node.getElement().toString());
      inorder(node.getRight()); 
    }
  }
  
  //Insert a key-element pair into the tree.
  public void insert(K key, E element) {
    if(root == null) {
      root = new AVLNode<E, K>(key, element, null, null, BALANCED);
    }
    else {
      root = copyNode(insertBelow(root, key, element));
    }
  }
  
  //Inserting a node below a specific node.
  public AVLNode<E, K> insertBelow(AVLNode<E, K> node, K key, E element) {
    if(node == null) {
      return new AVLNode<E, K>(key, element, null, null, 2);
    }
    else if(key.compareTo(node.getKey()) == 0) {
      return node;
    }
    else if(key.compareTo(node.getKey()) < 0) {
      //Key is less than
      if(node.getLeft() == null) {
        //There is no node to insert it into
        node.setLeft(new AVLNode<E, K>(key, element, null, null, 2));
      }
      else {
        //insert it into this node
        node.setLeft(copyNode(insertBelow(copyNode(node.getLeft()), key, element)));
      }
      //set the new balance to be more left
      node.setBalance(node.getBalance()-1);
    }
    else if(key.compareTo(node.getKey()) > 0) {
      //key is greater than
      if(node.getRight() == null) {
        //there is no node to insert it into
        node.setRight(new AVLNode<E, K>(key, element, null, null, 2));
      }
      else {
        node.setRight(copyNode(insertBelow(copyNode(node.getRight()), key, element)));
      }
      node.setBalance(node.getBalance()+1);
    }
    return copyNode(balance(node));
  }
  
  //Recursive counter to count the depth of the tree's node
  int postorder_depth(AVLNode<E,K> node, int current_depth) {
    if(node != null) {
      return Math.max(postorder_depth(node.getLeft(), current_depth+1), postorder_depth(node.getRight(), current_depth+1));
    }
    else return current_depth;
  }
  
  //Print the Dictionary in sorted order
  public void printTree() {
    System.out.println("\nPrinting the AVL Tree Dictionary\n");
    inorder(root);
  }
  
  //Rotate method for the LEFT-LEFT case.
  public AVLNode<E, K> rotateLEFTLEFT(AVLNode<E, K> node) {
    AVLNode<E, K> newCenter = copyNode(node.getLeft()); //new center node
    AVLNode<E, K> newRight = copyNode(node); //new right node to center node
    newRight.setLeft(copyNode(newCenter.getRight()));
    newRight.setBalance(2); 
    newCenter.setRight(copyNode(newRight)); 
    newCenter.setBalance(2); 
    return newCenter; 
  }
  
  // Rotate method for the LEFT-RIGHT case.
  public AVLNode<E, K> rotateLEFTRIGHT(AVLNode<E, K> node) {
    AVLNode<E, K> newTop = copyNode(node);
    AVLNode<E, K> newCenter = copyNode(node.getLeft().getRight());
    AVLNode<E, K> newBottom = copyNode(node.getLeft());
    if(newCenter == null) return node;
    newBottom.setRight(copyNode(newCenter.getLeft()));
    newBottom.setBalance(2); 
    newCenter.setLeft(copyNode(newBottom));
    newCenter.setBalance(1); 
    newTop.setLeft(copyNode(newCenter));
    newTop.setBalance(0);
    return newTop;
  }
  
  // Rotate method for the RIGHT-LEFT case.
  public AVLNode<E, K> rotateRIGHTLEFT(AVLNode<E, K> node) {
    AVLNode<E, K> newTop = copyNode(node);
    AVLNode<E, K> newCenter = copyNode(node.getRight().getLeft());
    AVLNode<E, K> newBottom = copyNode(node.getRight());
    if(newCenter == null) return node; 
    newBottom.setLeft(copyNode(newCenter.getRight()));
    newBottom.setBalance(2);
    newCenter.setRight(copyNode(newBottom));
    newCenter.setBalance(3); 
    newTop.setRight(copyNode(newCenter)); 
    newTop.setBalance(4); 
    return newTop; 
  }
  
  //Rotate method for the RIGHTRIGHT case.
  public AVLNode<E, K> rotateRIGHTRIGHT(AVLNode<E, K> node) {
    AVLNode<E, K> newCenter = copyNode(node.getRight());
    AVLNode<E, K> newLeft = copyNode(node);
    if(newCenter == null) return node;
    newLeft.setRight(copyNode(newCenter.getLeft()));
    newLeft.setBalance(2); 
    newCenter.setLeft(copyNode(newLeft)); 
    newCenter.setBalance(2);
    return newCenter; 
  }
  
  //Returns the element of the node with the key value of the specified value.
  public E search(K key) {
    AVLNode<E, K> nodeFound;
    nodeFound = searchNode(key);
    if(nodeFound == null) {
      return null; //not found
    }
    else return searchNode(key).getElement(); //call another helper method.
  }
  
  //Finds a specific key in the binary tree.
  public AVLNode<E,K> searchBelow(AVLNode<E, K> node, K key) {
    if(node == null) {
      return null;
    }
    if(key.compareTo(node.getKey()) == 0) {
      return node;
    }
    else if (key.compareTo(node.getKey()) > 0) {
      return searchBelow(node.getRight(), key);
    }
    else if(key.compareTo(node.getKey()) < 0) {
      return searchBelow(node.getLeft(), key);
    }
    else return null;
  }
  
  //Search for an entry with key KEY and return the node object
  public AVLNode<E, K> searchNode(K key) {
    if(key == null) {
      return null; 
    }
    if(root == null) {
      return null; 
    }
    if(key.compareTo(root.getKey()) == 0) {
      return root;
    }
    else if(key.compareTo(root.getKey()) < 0) {
      return searchBelow(root.getLeft(), key); 
    }
    else if(key.compareTo(root.getKey()) > 0) {
      return searchBelow(root.getRight(), key); 
    }
    else { //Error 1
      return null;
    }
  }
}
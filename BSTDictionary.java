
public class BSTDictionary<E, K extends Sortable> implements Dictionary<E, K>
{
 

 protected BSTNode<E, K> root;
 // search for an entry with key KEY and return the object
 public E search(K key)
 {
  return searchNode(root, key).getElement();
 }
 //Recursive method for 'search'
 public BSTNode<E, K> searchNode(BSTNode<E, K> current, K key)
 {
  if(key.compareTo(current.getKey()) == 0)
  {
   return current;
  }
  if(key.compareTo(current.getKey()) == -1)
  {
   return searchNode(current.getLeft(), key);
  }
  if(key.compareTo(current.getKey()) == 1)
  {
   return searchNode(current.getRight(), key);
  }
  return null;
 }

 
 // insert a key-value pair into the dictionary
 public void insert(K key, E element)
 {
  if(root==null)
  {
   root = new BSTNode<E, K>(key, element, null, null);
   return;
  }
  BSTNode<E, K> node = getInsert(root, key);
  if(key.compareTo(node.getKey()) == -1)
  {
   node.setLeft(new BSTNode<E, K>(key, element, null, null));
  }
  else
  {
   node.setRight(new BSTNode<E, K>(key, element, null, null));
  }
 }
 //Recursive method for 'insert'
 public BSTNode<E, K> getInsert(BSTNode<E, K> current, Sortable key)
 {
  if(key.compareTo(current.getKey()) == -1)
  {
   if(current.getLeft()!= null)
   {
    this.getInsert(current.getLeft(), key);
   }
  }
  else
  {
   if(current.getRight()!= null)
   {
    this.getInsert(current.getRight(), key);
   }
  }
  return current;
 }

 // delete an entry with key KEY
 public void delete(K key)
 {
  BSTNode<E, K> wacked = searchNode(root, key);
  wacked = checkBranch(wacked);
  
 }

 
 public BSTNode<E,K> checkBranch(BSTNode<E,K> node)
 {
  //Checks if it is leaf
  if(node.ge tLeft() == null && node.getRight() == null)
  {
   node = null;
   return node;
  }
  //Checks if branch on Right side
  else if(node.getLeft() != null && node.getRight() == null)
  {
   return node.getLeft();
  }
  //Check if branch on Left side
  else if(node.getLeft() == null && node.getRight() != null)
  {
   return node.getLeft();
  }
  //If none of above, both right and left much be branches
  return dealWithDouble(node);
 }
 
 public BSTNode<E, K> dealWithDouble(BSTNode<E,K> node)
 {
  BSTNode<E,K> newNode = node.getRight(); 
        BSTNode<E,K> leftBranch = node.getLeft(); 
        newNode.setRight(checkBranch((BSTNode<E, K>) node.getRight()));
        newNode.setLeft(leftBranch);
        return newNode;
 }

 // print the Dictionary in sorted order (as determined by the keys)
 public void printTree()
 {
  printNodes(root);
 }
 //Recursive method for 'printTree'
 public void printNodes(BSTNode<E,K> current)
 {
  if(current != null)
  {
   printNodes(current.getLeft());
   System.out.println("Keys: " + current.getKey().toString() + " Element: " + current.getElement().toString() + "\n");
   printNodes(current.getRight());
  }
 }

 // return the depth of the underlying tree
 public int depth()
 {
  return this.getDepth(root, 0);
 }
 //Recursive method for 'depth'
 public int getDepth(BSTNode<E, K> current, int currentDepth)
 {
  if(current != null)
  {
   return Math.max(this.getDepth(current.getLeft(), currentDepth++), this.getDepth(current.getRight(), currentDepth++));
  }
  return currentDepth;
  
 }
}

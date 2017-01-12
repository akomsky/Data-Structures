import java.util.*;

public class BinaryTreeClass {
	/**
	 * 
	 * @param input_arr
	 * @param tolerance
	 * @return
	 */
	public LinkedList<ArrayList<Integer> > Problem3(ArrayList<Integer> input_arr, int tolerance){
		// the official list that will be returned
		LinkedList<ArrayList<Integer> > ret = new LinkedList<>();
		// convert the array list into an array of Integers
		Integer [] array = input_arr.toArray(new Integer[input_arr.size()]);
		int n = array.length;
		BinaryTreeClass tree = new BinaryTreeClass();
		//find all possible binary trees will same inOrder traversal as given
		ArrayList<Node> allPossibleTrees = findBinaryTrees(array,0,n-1,tolerance);
		int length = allPossibleTrees.size();
		// for all of the possible trees, take the array and add its preOrder traversal to an ArrayList and add that to the ret list
		for(int i=0; i<length; i++) {
			// create a new arraylist to pass into the next method
			ArrayList<Integer> dataOfBinaryTree = new ArrayList<Integer>();
			tree.preOrder(dataOfBinaryTree,allPossibleTrees.get(i));
			// add this to a final list
			ret.add(dataOfBinaryTree);
		}
		return ret;
	}
	// ------ make nested Node class --------
	protected static class Node {
		int data;
		Node left;
		Node right;
		public Node(int item) {
			data = item;
			left = null;
			right = null;
		}
	}
	//----- end of nested node class -----
	/**
	 * 
	 * @param tree - pass in an arrayList
	 * @param node
	 * @return
	 */
	public static ArrayList<Integer> preOrder(ArrayList<Integer> tree,Node node) {
		if (node != null) {
			// add the data to the arraylist ("visit" first)
			tree.add(node.data);
			// go the left
			preOrder(tree,node.left);
			// go the right
			preOrder(tree,node.right);
		}
		return tree;
	}
	// find all possible inOrder traversals that have a specific tolerance
	/**
	 * 
	 * @param array
	 * @param start
	 * @param end
	 * @param tol
	 * @return
	 */
		public static  ArrayList<Node>findBinaryTrees(Integer[] array,int start,int end,int tol) {
			ArrayList<Node>trees= new ArrayList<Node>();
			// base case
			if (start>end) {
				trees.add(null);
				return trees;
			}
			// make each index a root
			for (int i=start; i<=end; i++) {
				// get all subtrees to the left
				ArrayList<Node>leftsubtrees = findBinaryTrees(array,start,i-1,tol);
				// get all subtrees to the right
				ArrayList<Node> rightsubtrees = findBinaryTrees(array,i+1,end,tol);
				// go through each node
				for (Node left : leftsubtrees) {
					for (Node right : rightsubtrees) {
						Node parent = new Node(array[i]);
						parent.left = left;
						parent.right = right;
						// check that this is a good binary tree before adding
						if (isBalanced(parent,tol))
							trees.add(parent);
						else 
							continue;
					}
				}
			}
			return trees;
		}
		/**
		 * 
		 * @param p
		 * @param tol
		 * @return
		 */
	public static boolean isBalanced(Node p, int tol) {
		// base case
		if (p == null)
			return true;
		// int representing the left subtree height
		int lht = height(p.left);
		// int representing the right subtree height
		int rht = height(p.right);
		// if the difference of the height is greater than the given tolerance,
		// then the tree is not balanced.  Use recursion to check if all of the subtrees 
		// are balanced too.
		if ((Math.abs(lht - rht) <= tol) && (isBalanced(p.left,tol)) && (isBalanced(p.right,tol)))
			return true;
		// if not, it is NOT balanced.
		return false;
	}
	/**
	 * method to find the height of a tree
	 * @param p - the given node
	 * @return
	 */
	public static int height(Node p) {
		// no more to go
		if (p== null)
			return 0;
		// use recursion to find the height
		// else 1 + the height of the larger subtree
		return 1 + Math.max(height(p.left), height(p.right));
	}
}

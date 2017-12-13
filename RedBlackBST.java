package facespace;

public class RedBlackBST<Key extends Comparable<Key>, Value> {

	private static final boolean RED = true;
	private static final boolean BLACK = false;
	
	private Node root;
	
	private class Node{
		Key username;
		String description;
		Node left;
		Node right;
		int size;
		boolean color;
		int numUsers;

		
		Node(Key username, String description, int size, boolean color){
			this.username = username;
			this.description = description;
			this.size = size;
			this.color = color;
			this.numUsers = numUsers;
		}
	}
	public String get(Key key){
		return(get(root, key));
	}
	private String get(Node n, Key key){
		if(n == null){
			return(null);
		}
		int cmp = key.compareTo(n.username);
		if(cmp < 0){
			return get(n.left, key);
		}
		else if(cmp > 0){
			return get(n.right, key);
		}
		else{
			return n.description;
		}
	}
	private int size(Node x){
		if(x == null){
			return 0;
		}
		else{
			return x.size;
		}
	}
	
	public int size() {
		return size(root);
	}
	
	private boolean isRed(Node x){
		if(x == null){
			return false;
		}
		else{
			return x.color == RED;
		}
	}
	
	private Node rotateLeft(Node h){
		Node x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = h.color;
		h.color = RED;
		x.size = h.size;
		h.size = 1 + size(h.left) + size(h.right);
		return(x);
	}
	
	private Node rotateRight(Node h){
		Node x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = h.color;
		h.color = RED;
		x.size = h.size;
		h.size = 1 + size(h.left) + size(h.right);
		return(x);
	}
	
	private void flipColors(Node h){
		h.color = RED;
		h.left.color = BLACK;
		h.right.color = BLACK;
	}
	
	public void put(Key username, String description){
		root = put(root, username, description);
		root.color = BLACK;
	}
	
	private Node put(Node h, Key username, String description){
		if(h == null){
			return new Node(username, description, 1, RED);
		}
		int cmp = username.compareTo(h.username);
		if(cmp < 0){
			h.left = put(h.left, username, description);
		}
		else if (cmp > 0){
			h.right = put(h.right, username, description);
		}
		else{
			h.description = description;
		}
		if(isRed(h.right) && !isRed(h.left)){
			h = rotateLeft(h);
		}
		if(isRed(h.left) && isRed(h.left.left)){
			h = rotateRight(h);
		}
		if(isRed(h.left) && (isRed(h.right))){
			flipColors(h);
		}
		h.size = size(h.left) + size(h.right) + 1;
		return(h);
	}
	
}

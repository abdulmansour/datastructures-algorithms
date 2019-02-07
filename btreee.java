package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class btreee {
	
	private static class Node {
		int [] items = new int [node_elements];
		Node [] childrens = new Node[childrens_count];
		boolean has_children = false;
		Node(int item) {
			items[0] = item;
			item_n++;
			for (int i = 1; i < items.length; i++) {
				items[i] = -1;
			}
		}
		Node() {
			for (int i = 0; i < items.length; i++) {
				items[i] = -1;
			}
		};
		private Node parent_node;
		private int item_n = 0;
		private int children_n = 0;
	}
	
	private Node root = new Node();
	private int new_root_value;
	private int new_sister_value;
	private int temp; //value to add back to make it into a b+ tree (also used new_root_value) have to perfectly differentiate between the two
	private static int node_elements = 3;
	private static int childrens_count = node_elements + 1;
	private boolean temp_exist = false;
	
	private boolean split = false;
	private boolean shift_parent_to_sister = false;
	private int sister_index;
	private int shift_parent_to_sister_count;
	
	public void add(int item) {
		Node leaf_node = traverse_tree(root, item); //start at root node and return leaf node
		if(leaf_node.item_n < node_elements) {
			add_item_to_node(leaf_node,item);
		}
		else {
			Node node_copy = copy_node(leaf_node);
			int nrv = new_root_value(leaf_node,item);
			add_item_to_parent(leaf_node, item);
			add_item_to_sister(leaf_node, node_copy, item); //issue here <<<
			add(nrv);//b+tree
			String node_content = "";
			
			for (int i = 0; i < root.item_n; i++) {
				node_content = node_content + root.items[i] + " ";
			}
			System.out.println("root: " + node_content);
		}
	}
	
	void add_item_to_parent(Node node, int item) {//when we pass item to parent, should we remove the passed value from the children? yes for btree
		for (int i = 0; i < node.items.length; i++) {
			System.out.println("Items contained in node: " + node.items[i]);
		}
		
		if(node.parent_node != null && node.parent_node.item_n >= node_elements) {
			int nrv = new_root_value((node),item);
			node.items[node_elements-1] = -1;
			node.item_n--;
			temp = new_root_value;
			temp_exist = true;
			Node node_copy = copy_node(node.parent_node);
			add_item_to_parent(node.parent_node,nrv); //objective is to now reach parent that has less than 3 items or initialize new root
			add_item_to_sister(node.parent_node, node_copy, nrv);
		}
		else if(node.parent_node == null) {
			new_root_value((node),item);
			node.parent_node = new Node(new_root_value);
			node.parent_node.childrens[node.parent_node.children_n++] = node;
			node.parent_node.has_children = true;
			node.items[node_elements-1] = -1;
			node.item_n--;
			if (node.parent_node.parent_node == null) {
				root = node.parent_node;
				root.item_n = node_item_count(root);
			}
			if(item<node.items[1]) {
				node.items[1] = item;
			}
		}
		else {
			new_root_value((node),item);
			add_item_to_node(node.parent_node,new_root_value);
			node.item_n--;
			node.items[node_elements-1] = -1;
			if(item<node.items[1]) {
				node.items[1] = item;
			}
		}
	}
	void add_item_to_sister(Node node, Node node_copy, int item) {
		new_sister_value(node_copy, item);
		Node new_sister = new Node(new_sister_value);
		if (shift_parent_to_sister) {
			shift_parent_to_sister_count++;
			new_sister.parent_node = node.parent_node.parent_node.childrens[sister_index];
			calculate_sister_index(new_sister, new_sister_value);
			new_sister.parent_node.childrens[new_sister.parent_node.children_n++] = new_sister;
			order_childrens_with_new_sister(new_sister);
			shift_parent_to_sister = false;
		}
		else {
			new_sister.parent_node = node.parent_node;
			//node.parent_node.childrens[node.parent_node.children_n++] = new_sister; // issue here << placement of the sister can be between other sister and not on edge (check children_n and node)
			calculate_sister_index(new_sister, item);
			if (sister_index == new_sister.parent_node.children_n) {
				new_sister.parent_node.childrens[new_sister.parent_node.children_n++] = new_sister;
			}
			else {
				new_sister.parent_node.children_n++;
				order_childrens_with_new_sister(new_sister);
			}
		}
		
		if (node.has_children) {
			new_sister.childrens[new_sister.children_n++] = node.childrens[3]; //potential error
			new_sister.childrens[0].parent_node = new_sister;
			new_sister.has_children = true;
			node.childrens[3] = null; //<< current issue (track full traversal when adding 7, notice that the functions that take care of reorganizing the children pointers when a split occurs mid-node 
			node.children_n--; //<<
			shift_parent_to_sister  = true;
		}
	}
	
	private void calculate_sister_index(Node new_sister, int item) {
		sister_index = 0;
		for (int i = 0; i < new_sister.parent_node.children_n; i++) {
			if (item >= new_sister.parent_node.childrens[i].items[0]) {
				sister_index = i+1;
			}
		}
	}

	void order_childrens_with_new_sister(Node new_sister) {
		Node [] temp_childrens = new Node[childrens_count];
		for (int i = 0; i < new_sister.parent_node.children_n; i++) {
			temp_childrens[i] = new Node();
			temp_childrens[i] = new_sister.parent_node.childrens[i];
		}
		//new_sister.parent_node.children_n++;
		for (int i = 0; i < new_sister.parent_node.children_n; i++) {
			if (i == sister_index) {
				new_sister.parent_node.childrens[i] = new_sister;
			}
			else if (i > sister_index) {
				new_sister.parent_node.childrens[i] = temp_childrens[i-1];
			}
		}
	}
	
	void add_item_to_node(Node node, int item) {
		if (node.item_n >= node_elements) {
			add_item_to_parent(node, item);
		}
		else {
			node.items[node.item_n++] = item;
			node.items = sort(node.items);
		}
	}
	//need to create a function called traverse that reaches the leaf node and is done recursively
	Node traverse_tree(Node node, int item) {
		if (!node.has_children) {
			return node;
		}
		int traverse_to_children = 0;
		for (int i = 0; i < node.item_n; i++) {
			if (item < node.items[i]) {
				traverse_to_children = i;
				break;
			}
			else if (i == node.item_n-1) {
				traverse_to_children = i + 1;
			}
		}
		return traverse_tree(node.childrens[traverse_to_children],item);
	}
	
	public int[] sort (int [] array) {
		int count = 0;
		for (int i = 0; i < array.length-1; i++) {
			for (int j = i+1; j < array.length; j++) {
				if (array[i] > array[j]) {
					int temp = array[i];
					array[i] = array[j];
					array[j] = temp;
				}
			}
		}
		for (int i = 0; i < array.length; i++) {
			if (array[i] == -1) {
				count++;
			}
		}
		if(count == 0) {
			return array;
		}
		int [] n_array = new int[array.length];
		for (int i = 0; i < n_array.length; i++) {
			n_array[i] = -1;
		}
		for (int i = 0; i < n_array.length-count; i++) {
			int temp = n_array[i];
			n_array[i] = array[i+count];
			n_array[i+count] = temp;
		}
		return n_array;
	}
	
	public int new_root_value(Node node, int item) {
		int [] array = new int[childrens_count];
		for (int i = 0; i < node_elements; i++) {
			array[i] = node.items[i];
		}
		array[node_elements] = item;
		array = sort(array);
		System.out.println("Item to be passed to parent node/new parent node/root: " + array[node_elements-1]);
		new_root_value = array[node_elements-1];
		return new_root_value;
		
		//node.items[2] = -1;
		//node.item_n--;
	}
	
	public void new_sister_value(Node node, int item) {
		int [] array = new int[childrens_count];
		for (int i = 0; i < node_elements; i++) {
			array[i] = node.items[i];
		}
		array[node_elements] = item;
		array = sort(array);
		System.out.println("Item to be passed to new sister node: " + array[node_elements]);
		new_sister_value = array[node_elements];
	}
	
	public int node_item_count(Node node) {
		int count = 0;
		for(int i = 0; i < node.items.length; i++) {
			if (node.items[i] != -1) {
				count++;
			}
		}
		return count;
	}	
	
	public Node copy_node(Node node) {
		Node node_copy = new Node();
		for (int i = 0; i < node.item_n; i++) {
			node_copy.items[i] = node.items[i];
			node_copy.item_n++;
		}
		for (int i = 0; i < node.children_n; i++) {
			node_copy.childrens[i] = node.childrens[i];
			node_copy.children_n++;
		}
		return node_copy;
	}
	

	public static void main(String[] args) throws FileNotFoundException, IOException {
		btreee tree = new btreee();
		
		tree.add(1);
		tree.add(2);
		tree.add(3);
		tree.add(10);
		tree.add(11);
		tree.add(12);
		tree.add(13);
		tree.add(14);
		tree.add(15);
		tree.add(16);
		tree.add(17);
		tree.add(18);
		tree.add(4);
		tree.add(5);
		tree.add(6);
		tree.add(7);
		tree.add(8);
		tree.add(9);


//		tree.add(100);
//		tree.add(200);
//		tree.add(300);
//		tree.add(400);
//		tree.add(150);
//		tree.add(250);
//		tree.add(350);
//		tree.add(450);
//		tree.add(125);
//		tree.add(225);
//		tree.add(325);
//		tree.add(425);
//		tree.add(500);
		
	}

}

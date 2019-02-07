
public class app {
	
	public static class Queue<Item> {
		private class Node {
			Item item;
			Node next;
		}
		private Node first = null;
		private Node last = null;
		
		public boolean isEmpty() {
			return first == null;
		}
		
		public void enqueue(Item item) {
			Node old_last = last;
			last = new Node();
			last.next = old_last;
			last.item = item;
			if (first == null) {
				first = last;
			}
		}
	
		public Item dequeue() {
			Item item = first.item;
			first = first.next;
			return item;
		}
		
		public void print_queue() {
			Node current = last;
			while (current != null) {
				System.out.println(current.item);
				current = current.next;
			}
		}
	}
		
	public static void main(String[] args) {
		Queue queue = new Queue();
		queue.enqueue("Abdul");
		queue.enqueue(120.12);
		queue.enqueue('a');
		queue.enqueue(7);
		queue.dequeue();
		queue.print_queue();

	}

}

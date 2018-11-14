import data_structures.*;

public class Test {
	public static void main(String [] args) {
		int size = 10;
		BinaryHeapPriorityQueue<Integer> pq = new BinaryHeapPriorityQueue<Integer>(size);
		
		// Insertion 
		pq.insert(14);
		pq.insert(2);
		pq.insert(18);
		pq.insert(6);
		pq.insert(7);
		pq.insert(3);
		pq.insert(19);
		pq.insert(7);
		pq.insert(7);
//		for(int i = 10; i > 0; i--) {
//			pq.insert(i);
//		}
//		
//		// Original heap
//		System.out.print("Original: ");
//		pq.printHeap();
		
		// Removal
//		System.out.println("Remove Item: " + pq.remove());
//		System.out.println("Remove Item: " + pq.remove());
//		System.out.println("Remove Item: " + pq.remove());
//		System.out.println("Remove Item: " + pq.remove());
//		System.out.println("Remove Item: " + pq.remove());
//		System.out.println("Remove Item: " + pq.remove());
////		System.out.println("Remove Item: " + pq.remove());
//		for(int i = 0; i < 10; i++) {
//			System.out.println(pq.remove());
//		}
		
		// Deletion 
		pq.delete(7);
		
//		// Print heap
//		pq.printHeap();
	}
}	

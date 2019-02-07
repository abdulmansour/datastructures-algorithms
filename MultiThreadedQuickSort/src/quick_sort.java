import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class quick_sort {
	
	public static int t = 0;
	static FileReader fileReader;
	static BufferedReader bufferedReader;
	static String fileName = "input.txt";
	static int[] A;
	
	//Responsible for reading the textfile and populating the intial array that will be quicksorted
	public static void populate() {
		String line = null;
        int n;
        int i = 0;
      
        try {            
            FileReader fileReader = new FileReader(fileName);            
            BufferedReader bufferedReader = new BufferedReader(fileReader);  
            
            //read the first line: responsible for array size
            n = Integer.parseInt(bufferedReader.readLine()); 
            //create new array of size n
            A = new int[n];
            //populate array with rest of elements in the text file
            while((line = bufferedReader.readLine()) != null) {
                  A[i] = Integer.parseInt(line);
                  i++;
            }   
            bufferedReader.close();
        }
        
        catch(FileNotFoundException ex) {
            System.out.println(ex.getMessage());                
        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());                     
        }
	}
	//Sorts an array by Partitionning the array until a final element is left in the sub-array chain
	// or until the end index crosses the start index.
	public static void quicksort(int[] A, int start, int end) {
		if (start < end) {
			
			int p_index = Partition(A,start,end);
			
			//left-array: left side of partitioning index, in new thread
			Thread t1 = new Thread(new Runnable() {
				public synchronized void run() {
					t++; //tracking the creation of threads
					quicksort(A, start, p_index - 1); //left array passed to quicksort
				}
			});
			
			//right-array: right side of partitioning index, in new thread
			Thread t2 = new Thread(new Runnable() {
				public synchronized void run() {
					t++; //tracking the creation of threads
					quicksort(A, p_index, end); //right array passed to quicksort
				}
			});
			
			t1.start();
			t2.start();
			
			// join() occurs after both threads start(), to ensure complete concurrent execution
			try {
				t1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				t2.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	//Responsible for returning the pivot index seperating the sub-arrays (left & right)
	//All elements to the left of the pivot index are smaller than the pivot element
	//All elements to the right of the pivot index are larger than the pivot element
	private static int Partition(int[] A, int start, int end) {
		int pivot = A[end];
		int p_index = start;
		
		for (int i = start; i < end; i++) {
			if(A[i] <= pivot) {
				int temp = A[i];
				A[i] = A[p_index];
				A[p_index] = temp;
				p_index++;
			}
		}
		//Ensures that pivot element is at proper pivot index position, and not at end index
		int temp = A[end];
		A[end] = A[p_index];
		A[p_index] = temp;
		
		return p_index;
	}
	
	public static void print(int [] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}
	}
	
	public static void output(int [] a) throws IOException {
		PrintWriter writer = new PrintWriter(new FileWriter("output.txt"));
		for (int i = 0; i < a.length; i++) {
			writer.println(a[i]);
		}
		writer.close();
	}


	public static void main(String[] args) throws IOException {
		populate();
		System.out.println("Unsorted Array: ");
		//print(A);
		System.out.println("Sorted Array: ");
		long m1 = System.currentTimeMillis();
		quicksort(A, 0, A.length - 1);
		long elapsedTime = System.currentTimeMillis() - m1;
		print(A);
		System.out.println("Number of threads used: " + t);
		System.out.println("Elapsed Time: " + elapsedTime);
		output(A);
	}

}

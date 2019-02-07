import java.util.Random;

public class shuffle_srt {
	
	public static void populate(int [] a) {
		int N = a.length;
		Random rand1 = new Random();
		
		for (int i = 0; i < N; i++) {
			a[i] = (rand1.nextInt(1000000));
		}
	}
	public static void shell_sort(int [] a) {
		int N = a.length;
		
		int h = 1;
		while (h < N/3) {
			h = h*3 + 1; // if N == 1000, then h will leave the while loop with a value of 364
		}
		while (h >=1) {
			for (int i = h; i < N; i++) {
				for (int j = i; j>=h && a[j] < a[j-h]; j -= h) {
					int temp = a[j];
					a[j] = a[j-h];
					a[j-h] = temp;
				}
			}
			h = h/3; // if h starts at 364, then second run h will be 121
		}
	}
	
	public static void print(int [] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}
	}
	
	public static void shuffle(int [] a) {
		int N = a.length;
		float [] f = new float[N];
		Random rand = new Random();
		
		for (int i = 0; i < N; i++) {
			f[i] = rand.nextFloat();
		}
		
		int h = 1;
		while (h < N/3) {
			h = h*3 + 1; // if N == 1000, then h will leave the while loop with a value of 364
		}
		while (h >=1) {
			for (int i = h; i < N; i++) {
				for (int j = i; j>=h && f[j] < f[j-h]; j -= h) {
					float temp = f[j];
					int temp2 = a[j];
					f[j] = f[j-h];
					a[j] = a[j-h];
					f[j-h] = temp;
					a[j-h] = temp2;
				}
			}
			h = h/3; // if h starts at 364, then second run h will be 121
		}
		
		
		
	}

	public static void main(String[] args) {
		int [] array = new int [1000000];
		populate(array);
		shell_sort(array);
		System.out.println("Sorted Array: ");
		print(array);
		shuffle(array);
		System.out.println("Shuffled Array: ");
		print(array);
		

	}

}

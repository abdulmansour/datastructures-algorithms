import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Application {
	
	private static List<String> review;
	private static int review_count;
	
	private static List<Integer> rating = new ArrayList<Integer>();

	private static List<String> review_critical = new ArrayList<String>();
	private static int review_count_critical;
	
	private static List<String> review_good = new ArrayList<String>(); 
	private static int review_count_good;
	
	private static List<String> words_1 = new ArrayList<String>();
	private static List<Integer> counts_1 = new ArrayList<Integer>();
	private static List<String> words_2 = new ArrayList<String>();
	private static List<Integer> counts_2 = new ArrayList<Integer>();
	private static List<String> words_3 = new ArrayList<String>();
	private static List<Integer> counts_3 = new ArrayList<Integer>();
	private static List<String> words_4 = new ArrayList<String>();
	private static List<Integer> counts_4 = new ArrayList<Integer>();
	private static List<String> words_5 = new ArrayList<String>();
	private static List<Integer> counts_5 = new ArrayList<Integer>();
	private static List<String> words_6 = new ArrayList<String>();
	private static List<Integer> counts_6 = new ArrayList<Integer>();
	
	public static String word_to_check(String review,int n, int j) {
		String trim = review.trim();
		int length = trim.split("\\s+").length;
		if (trim.isEmpty() || n > length) {
			return null;
		}
		else {
			String[] words = review.split(" ");
			String newString = "";
			for (int i = j; i < n+j; i++) {
				newString = newString + " " + words[i];
			}
			newString = newString.trim();
			return newString;
		}
	}
	
	public static void calculate_count(List<String> review, int review_count) {
		for (int n = 1; n <= 6; n++) {
			String []p = null;
			for (int i = 0; i < review_count-1; i++) {
				String sentence = review.get(i);
				p = sentence.split("\\s*(=>|,|\\s)\\s*");
				int length = p.length;
				for (int j = 0; j < length+1-n; j++) {
					String word = word_to_check(review.get(i),n,j);
					if ((n == 1 && words_1.contains(word)) || (n == 2 && words_2.contains(word)) ||
							(n == 3 && words_3.contains(word)) || (n == 4 && words_4.contains(word)) ||
							(n == 5 && words_5.contains(word)) || (n == 6 && words_6.contains(word))) {
						continue;
					}
					else {
						if (word != null) {
							int count = 0;
							String []p1 = null;
							for (int i1 = 0; i1 < review_count-1; i1++) {
								String sentence1 = review.get(i1);
								
								p1 = sentence1.split("\\s*(=>|,|\\s)\\s*");
								for (int j1 = 0; j1 < p1.length - n + 1; j1++) {
									String temp = "";
									for (int k = j1; k < n+j1; k++) {
										temp = temp + p1[k] + " ";
									}
									temp = temp.trim();
						            if (word.equals(temp)) {
						                count++;
						            }
						        }
							}
							if (n == 1) {
								words_1.add(word);
								counts_1.add(count);
							}
							else if (n == 2) {
								words_2.add(word);
								counts_2.add(count);
							}
							else if (n == 3) {
								words_3.add(word);
								counts_3.add(count);
							}
							else if (n == 4) {
								words_4.add(word);
								counts_4.add(count);
							}
							else if (n == 5) {
								words_5.add(word);
								counts_5.add(count);
							}
							else if (n == 6) {
								words_6.add(word);
								counts_6.add(count);
							}
						}
						else {
							System.out.println("The sentence is too short for the length chosen...");
						}
					}
				}
			}
		}
	}
	public static void fetch_csv() throws IOException {
		String csvFile = "/Users/AbdulrahimMansour/Desktop/input_2.csv";
		
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		String line;
		review_count = 0;
		review = new ArrayList<String>();
		
		while ((line = br.readLine()) != null) {
			if (review_count == 0) {
			}
			else {
			    String[] cols = line.split(",");
			    String resultString = cols[5].replaceAll("[^\\pL\\pN\\s]", "").trim().toLowerCase().replaceAll(" +", " ");
			    int r;
			    try {
			        r = Integer.parseInt(cols[6]);
			    } 
			    catch (NumberFormatException e) {
			        r = 5;
			    }
			    System.out.println("Review " + review_count + " (" + r + " stars): " + resultString );
			    review.add(resultString);
			    rating.add(r);
			}
			review_count++;
		}
	}
	
	public static void output(List<String> list_word, List<Integer> list_count) {
		for (int i = 0; i < list_word.size(); i++) {
			System.out.println(list_word.get(i) + ": " + list_count.get(i));
		}
	}
	
	public static void output_to_csv(String file) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File(file));
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 6; i++) {
	        sb.append(i + "-Word-Phrase");
	        sb.append(',');
	        sb.append(i + "-Word-Count");
	        sb.append(',');
        }
        sb.append('\n');
        int count = 0;
        while(true) {
        	if (count < words_1.size()) {
	        	sb.append(words_1.get(count));
	        	sb.append(',');
	        	sb.append(counts_1.get(count));
	        	sb.append(',');
        	}
        	else {
        		sb.append(",,");
        	}
        	if (count < words_2.size()) {
	        	sb.append(words_2.get(count));
	        	sb.append(',');
	        	sb.append(counts_2.get(count));
	        	sb.append(',');
        	}
        	else {
        		sb.append(",,");
        	}
        	if (count < words_3.size()) {
	        	sb.append(words_3.get(count));
	        	sb.append(',');
	        	sb.append(counts_3.get(count));
	        	sb.append(',');
        	}
        	else {
        		sb.append(",,");
        	}
        	if (count < words_4.size()) {
	        	sb.append(words_4.get(count));
	        	sb.append(',');
	        	sb.append(counts_4.get(count));
	        	sb.append(',');
        	}
        	else {
        		sb.append(",,");
        	}
        	if (count < words_5.size()) {
	        	sb.append(words_5.get(count));
	        	sb.append(',');
	        	sb.append(counts_5.get(count));
	        	sb.append(',');
        	}
        	else {
        		sb.append(",,");
        	}
        	if (count < words_6.size()) {
	        	sb.append(words_6.get(count));
	        	sb.append(',');
	        	sb.append(counts_6.get(count));
	        	sb.append(',');
        	}
        	else {
        		sb.append(",,");
        	}
        	sb.append('\n');
        	count++;
        	if (count > words_1.size() && count > words_2.size() && count > words_3.size() &&
        			count > words_4.size() && count > words_5.size() && count > words_6.size()) {
        		break;
        	}
        }
        pw.write(sb.toString());
        pw.close();
        System.out.println("output csv ready!");
    }
	
	public static void create_critical_and_good_reviews() {
		for (int i = 0; i < review_count-1; i++) {
			if (rating.get(i) <= 3) {
				review_critical.add(review.get(i));
				review_count_critical++;
			}
			else {
				review_good.add(review.get(i));
				review_count_good++;
			}
		}
		
	}
	
	public static void clear() {
		counts_1.clear(); words_1.clear(); counts_2.clear(); words_2.clear(); counts_3.clear(); words_3.clear();
		counts_4.clear(); words_4.clear(); counts_5.clear(); words_5.clear(); counts_6.clear(); words_6.clear();
	}
	
	public static void sort(List<String> arr_words, List<Integer> arr) {
        for (int i = 1; i < arr.size(); i++) {
            int valueToSort = arr.get(i);
            String stringToSort = arr_words.get(i);
            int j = i;
            while (j > 0 && arr.get(j-1) < valueToSort) {
                arr.set(j, arr.get(j-1));
                arr_words.set(j, arr_words.get(j-1));
                j--;
            }
            arr.set(j, valueToSort);
            arr_words.set(j, stringToSort);
            
        }
	}
	public static void main(String[] args) throws IOException {
		fetch_csv();
		create_critical_and_good_reviews();
		
		calculate_count(review, review_count);
		sort(words_1, counts_1);
		sort(words_2, counts_2);
		sort(words_3, counts_3);
		sort(words_4, counts_4);
		sort(words_5, counts_5);
		sort(words_6, counts_6);
		output_to_csv("all_reviews.csv");
		
		clear();
		
		calculate_count(review_critical, review_count_critical);
		sort(words_1, counts_1);
		sort(words_2, counts_2);
		sort(words_3, counts_3);
		sort(words_4, counts_4);
		sort(words_5, counts_5);
		sort(words_6, counts_6);
		output_to_csv("critical_reviews.csv");
		
		clear();
		
		calculate_count(review_good, review_count_good);
		sort(words_1, counts_1);
		sort(words_2, counts_2);
		sort(words_3, counts_3);
		sort(words_4, counts_4);
		sort(words_5, counts_5);
		sort(words_6, counts_6);
		output_to_csv("good_reviews.csv");
	}
	
}

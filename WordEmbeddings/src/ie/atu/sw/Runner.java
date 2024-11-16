// Cian Dicker-Hughes
// G00415413@atu.ie

package ie.atu.sw;

import java.util.*;
import java.io.*;

/**
 * 
 * @author Cian Dicker-Hughes
 * @version 1.0
 * @since 1.8
 *
 *The Runner Class is the main body of this program.
 *It calls the wordEmbedding class to read the file 
 *and load it to a HashMap and check Similarity of the Word 
 *Embedding.
 *The Meun is called from MainMeunPAGE class and it's
 *loop until the user enter -1 to end the program
 *
 */

public class Runner {

	public static void main(String[] args) throws Exception {

		int choice, n = 0;
		char choiceFile;
		String words = "", outputPath;

		// open and load in default file
		WordEmbedding wordEmbedding = new WordEmbedding();
		wordEmbedding.load();

		Map<String, double[]> embeddings = new HashMap<>();
		List<String> similarWord = new ArrayList<>();

		choice = MainMeunPAGE.meun();

		Scanner input = new Scanner(System.in);

		// loop for user choice
		while (choice != -1) {
			switch (choice) {
			case 1: // get new file and loading it in
				System.out.println("Enter New Embedding File (leave empty to keep default)");
				String filePath = input.nextLine().trim();
				if (!filePath.isEmpty()) {
					wordEmbedding.setDictionaryFile(filePath);
				}
				wordEmbedding.load();
				break;

			case 2: // compare word similarity and print in to a file
				System.out.println("Enter a word:");
				words = input.nextLine();
				while (!wordEmbedding.containsWord(words)) {
					if (!wordEmbedding.containsWord(words)) {
						System.out.println("\nWord doesn't exist on the map\n");
					}
					System.out.println("Enter a word:");
					words = input.nextLine();
				}

				System.out.println("\nHow many similar words to find?");
				n = Integer.parseInt(input.nextLine());

				List<String> similarWords = wordEmbedding.findTopNSimilarWords(words, n);
				System.out.println("\nTop " + n + " similar words: " + similarWords);

				// save similarWords to a text file
				outputPath = "";
				System.out.println("\nEnter the path and filename to save the similar words (add '.txt' to the end)");
				outputPath = input.nextLine().trim();
				if (outputPath.isEmpty()) {
					outputPath = "oneSimilarityOut.txt"; // default save file
				}
				try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
					for (String word : similarWords) {
						double[] embedding = wordEmbedding.getWordEmbedding(word);
						writer.print(word + ", ");
						for (double value : embedding) {
							writer.print(value + ", ");
						}
						writer.println();
					}
					System.out.println("Similar words saved to: " + outputPath);
				} catch (IOException e) {
					System.out.println("Error writing to file: " + e.getMessage());
				}
				break;

			case 3: // compare word similarity & save to list
				System.out.println("Enter a word:");
				words = input.nextLine();
				while (!wordEmbedding.containsWord(words)) {
					if (!wordEmbedding.containsWord(words)) {
						System.out.println("\nWord doesn't exist on the map\n");
					}
					System.out.println("Enter a word:");
					words = input.nextLine();
				}

				System.out.println("\nHow many similar words to find?");
				n = Integer.parseInt(input.nextLine());

				List<String> currentSimilarWords = wordEmbedding.findTopNSimilarWords(words, n);
				similarWord.addAll(currentSimilarWords);

				System.out.println("\nTop " + n + " similar words: " + currentSimilarWords);
				break;

			case 4: // save similarity search map to file
				outputPath = "";
				System.out.println("Enter the path and filename to save the similar words (add '.txt' to the end)");
				outputPath = input.nextLine().trim();
				if (outputPath.isEmpty()) {
					outputPath = "multipleSimilarityOut.txt"; // default save file
				}
				try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
					for (String word : similarWord) {
						double[] embedding = wordEmbedding.getWordEmbedding(word);
						writer.print(word + ", ");
						for (double value : embedding) {
							writer.print(value + ", ");
						}
						writer.println();
					}
					System.out.println("Similar words saved to: " + outputPath);
				} catch (IOException e) {
					System.out.println("Error writing to file: " + e.getMessage());
				}
				break;

			case 5: // get the word and display the the word and it's embedding
				System.out.println("Enter a word:");
				words = input.nextLine();
				if (wordEmbedding.containsWord(words)) {
					System.out.println(words + " found in embeddings.");
					double[] embedding1 = wordEmbedding.getWordEmbedding(words);
					System.out.println(Arrays.toString(embedding1));
				} else {
					System.out.println("Word not found in embeddings.");
				}
				break;

			case 6: // see which file is been used and display everything in the file
				System.out.println("You are using " + wordEmbedding.whichDictionaryFile());
				System.out.println("Size of embeddings map: " + wordEmbedding.getSizeEmbedding());
				System.out.println("Do you want to display the file? (y or n)");
				choiceFile = input.next().charAt(0);
				input.nextLine();
				if (choiceFile == 'y' || choiceFile == 'Y') {
					embeddings = wordEmbedding.getEmbeddings();
					for (Map.Entry<String, double[]> entry : embeddings.entrySet()) {
						String word = entry.getKey();
						double[] embedding = entry.getValue();
						System.out.print(word + ": [");
						for (double value : embedding) {
							System.out.print(value + ", ");
						}
						System.out.println("]");
					}
					System.out.println("Size of embeddings map: " + wordEmbedding.getSizeEmbedding());
				}
				break;

			case -1: // Exit Program
				System.out.println("Exit Program");
				return;
			default: // if user don't enter a option
				System.out.println("Please Enter Option\n");
				break;
			}
			choice = MainMeunPAGE.meun();
		}
		System.out.println("Exit Program");
	}
}
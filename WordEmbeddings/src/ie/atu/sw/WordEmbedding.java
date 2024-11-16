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
 *The WordEmbedding Class is where the file is load
 *in the HashMap and check Similarity of the Word 
 *Embedding of this program. 
 *
 */
public class WordEmbedding {
	private Map<String, double[]> embeddings = new HashMap<>();
	private String dictionaryFile = "";

	public WordEmbedding() {
		this.dictionaryFile = "word-embeddings.txt"; // default file path
	}

	// change the file path to a new file
	public void setDictionaryFile(String filePath) {
		this.dictionaryFile = filePath;
	}

	/**
	 *
	 * load() function that loads in the file and store the 
	 * contents to a HashMap.
	 * If it fail to load it will default to "word-embeddings.txt".
	 * If it can't find "word-embeddings.txt" it will 
	 * continue the program.
	 * 
	 */
	
	// load the file to the map
	public void load() {
	    embeddings.clear();
	    System.out.println("Loading embeddings from file: " + dictionaryFile);
	    try (var br = new BufferedReader(new InputStreamReader(new FileInputStream(dictionaryFile)))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            line = line.trim();
	            String[] parts = line.split(",\\s*");
	            String word = parts[0];
	            double[] embedding = Arrays.stream(parts, 1, parts.length).mapToDouble(Double::parseDouble).toArray();
	            embeddings.put(word, embedding);
	        }
	    } catch (IOException e) {
	        System.err.println("[ERROR] Encountered a problem reading the file: " + e.getMessage());
	        if (dictionaryFile.equals("word-embeddings.txt")) {
	            System.err.println("word-embeddings.txt file not found.");
	        } 
	        else {
	            System.out.println("Reverting to default file: word-embeddings.txt");
	            dictionaryFile = "word-embeddings.txt";
	            load(); // Reload using default file
	        }
	    }
	}

	/**
	 * 
	 * findTopNSimilarWords() function Compare the 
	 * Similarity of Words by using Word-Embedding.
	 * It uses the Dot Product for the Comparing 
	 * Vectors for Similarity.
	 * 
	 */
	
	// calculates the dot product between two vectors
	private double dotProduct(double[] vectorA, double[] vectorB) {
		double dotProduct = 0.0;
		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
		}
		return dotProduct;
	}

	// finds the top N similar words to the given word based on their embedding
	public List<String> findTopNSimilarWords(String word, int n) {
		double[] targetEmbedding = embeddings.get(word);
		if (targetEmbedding == null) {
			System.out.println("Word not found in embeddings.");
			return Collections.emptyList();
		}

		// use a priority queue to keep track of top N similar words
		PriorityQueue<Map.Entry<String, Double>> pq = new PriorityQueue<>(Map.Entry.comparingByValue());

		for (Map.Entry<String, double[]> entry : embeddings.entrySet()) {
			if (!entry.getKey().equals(word)) {
				double similarity = dotProduct(targetEmbedding, entry.getValue());
				pq.offer(new AbstractMap.SimpleEntry<>(entry.getKey(), similarity));
				if (pq.size() > n) {
					pq.poll();
				}
			}
		}

		// extract words from the priority queue
		List<String> topNWords = new ArrayList<>();
		while (!pq.isEmpty()) {
			topNWords.add(0, pq.poll().getKey());
		}

		return topNWords;
	}

	// is the word in the map/array
	public boolean containsWord(String word) {
		return embeddings.containsKey(word);
	}

	// get the word Numbers
	public double[] getWordEmbedding(String word) {
		return embeddings.get(word);
	}

	// get which file is being used
	public String whichDictionaryFile() {
		return dictionaryFile;
	}

	// return the full map/array
	public Map<String, double[]> getEmbeddings() {
		return this.embeddings;
	}

	// get size of map
	public int getSizeEmbedding() {
		return embeddings.size();
	}
}
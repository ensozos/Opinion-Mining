import java.io.*;
import java.util.*;

public class InvertedIndex {
    private TreeMap<String, List<DocumentEntry<Integer, Integer>>> catalog;
    private ReviewsReader rr;

    public InvertedIndex(ReviewsReader rr) {
        catalog = new TreeMap<String, List<DocumentEntry<Integer, Integer>>>();
        this.rr = rr;

        createInvertedIndex(rr);
    }

    private void createInvertedIndex(ReviewsReader rr) {
        while (rr.hasNext()) {
            ReviewsReader.Review review = rr.next();
            String[] words = review.getWords();
            int documentId = review.getUniqueIndex();
            int maxFreq = 1;

            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                List<DocumentEntry<Integer, Integer>> docList;

                if (!catalog.containsKey(word)) {
                    docList = new ArrayList<DocumentEntry<Integer, Integer>>();
                    DocumentEntry<Integer, Integer> entry = new DocumentEntry<Integer, Integer>(documentId, 1);
                    docList.add(entry);

                    catalog.put(word, docList);
                } else {
                    docList = catalog.get(word);
                    int pos = getDocumentPosition(docList, documentId);

                    if (pos < 0) {
                        docList.add(new DocumentEntry<Integer, Integer>(documentId, 1));
                    } else {
                        int freq = docList.get(pos).getValue() + 1;
                        docList.get(pos).setValue(freq);

                        if (freq > maxFreq)
                            maxFreq = freq;
                    }
                }
            }

            review.setWordMaxFreq(maxFreq);
        }
    }

    /**
     * Returns the position of a document in the list
     *
     * @param list the list to search
     * @param documentId the id of the document
     */
    private int getDocumentPosition(List<DocumentEntry<Integer, Integer>> list, int documentId) {
        // TODO BINARY
        for (int i=0; i<list.size(); i++)
            if (list.get(i).getKey() == documentId)
                return i;

        return -1;
    }

    /**
     * Returns the frequency of a word in the catalog
     *
     * @param word the word to look for
     */
    public int getWordFreqInCatalog(String word) {
        return catalog.get(word).size();
    }

    /**
     * Returns the frequency of a word in a document
     *
     * @param word the word to look for
     * @param id the id of the document
     */
    public int getWordFreqInDoc(String word, int id) {
        ArrayList<DocumentEntry<Integer, Integer>> list = (ArrayList) catalog.get(word);
        int pos = getDocumentPosition(list, id);

        if (pos < 0)
            return 0;

        return list.get(pos).getValue();
    }

    /**
     * Returns the max number of occurences of any word in a specific document
     *
     * @param id the id of the document
     */
    public int getWordMaxFreqInDoc(int id) {
        return rr.getReview(id).getWordMaxFreq();
    }

    /**
     * Method to print the inverted index
     */
    public void printCatalog() {
        for (String key : catalog.keySet()) {
            System.out.print(key + " : ");
            for (DocumentEntry<Integer, Integer> entry : catalog.get(key)) {
                System.out.print(" <" + "(" + entry.getKey() + ")" + entry.getValue() + "> ");
            }
            System.out.println();
        }

        System.out.println("Number of words: " + getWordsNumber());
    }

    public TreeMap<String, List<DocumentEntry<Integer, Integer>>> getCatalog() {
        return catalog;
    }

    /**
     * Returns the number of the words in the catalog
     */
    public int getWordsNumber() {
        return catalog.keySet().size();
    }

    public boolean hasWord(String word) {
        return catalog.keySet().contains(word);
    }
}

class DocComp implements Comparator<DocumentEntry> {

    @Override
    public int compare(DocumentEntry o1, DocumentEntry o2) {
        if ( o1.getKey().equals(o2.getKey())) return 0;
        if ((Integer) o1.getKey() < (Integer) o2.getKey()) return -1;
        return 1;
    }

}

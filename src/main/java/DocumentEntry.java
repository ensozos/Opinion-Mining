import java.util.Map;

public class DocumentEntry<K, V> implements Map.Entry<K, V>  {

    private final K documentID;
    private V wordFrequency;

    public DocumentEntry(K documentID, V wordFrequency) {
        this.documentID = documentID;
        this.wordFrequency = wordFrequency;
    }

    @Override
    public K getKey() {
        return documentID;
    }

    @Override
    public V getValue() {
        return wordFrequency;
    }

    @Override
    public V setValue(V value) {
        V old = this.wordFrequency;
        this.wordFrequency = value;
        return old;
    }

    @Override
    public String toString() {
        return documentID + " " + wordFrequency;
    }

    public boolean equals(DocumentEntry<K, V> entry) {
        return getKey().equals(entry.getKey());
    }
}

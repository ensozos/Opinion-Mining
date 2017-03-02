import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ReviewsReader {
    private File directory;
    private byte reviewType;
    private int index;
    private ArrayList<Review> reviews;

    public ReviewsReader(String dirname, byte type) {
        directory = new File(dirname);
        reviewType = type;

        reset();
    }

    public void reset() {
        reviews = new ArrayList<Review>();
        index = 0;

        File[] files = directory.listFiles();
        for (File file: files) {
            Review review = Review.createFromFile(file, reviewType);
            reviews.add(review.getUniqueIndex(), review);
        }
    }

    public void shuffle() {
        if (reviewType == Review.TYPE_TEST)
            return;

        Collections.shuffle(reviews, new Random(System.currentTimeMillis()));

        for (int i=0; i<reviews.size(); i++)
            reviews.get(i).setCid(i);
    }

    public void resetIndex() {
        index = 0;
    }

    public boolean hasNext() {
        return index < reviews.size();
    }

    public Review next() {
        return reviews.get(index++);
    }

    public int getReviewsNumber() {
        return reviews.size();
    }

    public int getIndex() {
        return index;
    }

    public Review getReview(int i) {
        return reviews.get(i);
    }

    public static class Review {
        public static final byte LABEL_POSITIVE = 2;
        public static final byte LABEL_NEGATIVE = 1;

        public static final String LABEL_POSITIVE_STR = "POSITIVE";
        public static final String LABEL_NEGATIVE_STR = "NEGATIVE";

        public static final byte TYPE_TRAIN = 2;
        public static final byte TYPE_TEST = 1;

        private static BufferedReader reader;

        private byte type;
        private int cid;
        private int oid;
        private String text;
        private byte label;
        private int rating;
        private int wordMaxFreq = 1;

        public Review(int oid, String text) {
            type = TYPE_TEST;

            this.oid = oid;
            this.text = text;
        }

        public Review(int cid, int oid, String text, byte label, int rating) {
            type = TYPE_TRAIN;

            this.cid = cid;
            this.oid = oid;
            this.text = text;
            this.label = label;
            this.rating = rating;
        }

        public static Review createFromFile(File file, byte type) {
            try {
                reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                String text = "";
                String line;
                while ((line = reader.readLine()) != null)
                    text += line + " ";

                text = text.replace("\n", " ").trim();

                String filename = file.getName();
                int search = filename.lastIndexOf(".");
                filename = search < 0 ? filename: filename.substring(0, search);
                String[] parts = filename.split("_");

                if (type == TYPE_TEST) {
                    int oid = Integer.parseInt(parts[0]);

                    return new Review(oid, text);
                } else {
                    int cid = Integer.parseInt(parts[0]);
                    int oid = Integer.parseInt(parts[1]);
                    byte label = parts[2].equals("pos") ? LABEL_POSITIVE: LABEL_NEGATIVE;
                    int rating = Integer.parseInt(parts[3]);

                    return new Review(cid, oid, text, label, rating);
                }
            } catch (FileNotFoundException exc) {
                exc.printStackTrace();
            } catch (IOException exc) {
                exc.printStackTrace();
            }

            return null;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public void setWordMaxFreq(int wordMaxFreq) {
            this.wordMaxFreq = wordMaxFreq;
        }

        public String[] getWords() {
            return text.split(" ");
        }

        public int getUniqueIndex() {
            return type == TYPE_TEST ? oid: cid;
        }

        public String getStringLabel() {
            return (label == LABEL_POSITIVE) ? LABEL_POSITIVE_STR: LABEL_NEGATIVE_STR;
        }

        public int getCid() {
            return cid;
        }

        public int getOid() {
            return oid;
        }

        public String getText() {
            return text;
        }

        public byte getLabel() {
            return label;
        }

        public int getRating() {
            return rating;
        }

        public int getWordMaxFreq() {
            return wordMaxFreq;
        }
    }
}

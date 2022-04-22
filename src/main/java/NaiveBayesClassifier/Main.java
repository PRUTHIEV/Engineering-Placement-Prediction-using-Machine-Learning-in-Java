package NaiveBayesClassifier;


import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Dataset dataset = new Dataset();
        dataset.readDataset();
        NaiveBayes naiveBayes = new NaiveBayes(dataset, dataset.getFeatures(), dataset.getOutcomes());
        naiveBayes.train();
    }
}

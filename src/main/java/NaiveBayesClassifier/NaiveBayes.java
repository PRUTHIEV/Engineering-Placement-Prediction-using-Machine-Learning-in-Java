package NaiveBayesClassifier;

import joinery.DataFrame;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class NaiveBayes {

    //It stores all the Features that needed to train the model
    DataFrame<Object> XTrain;

    //It stores the outcome that needed to train the model
    DataFrame<Object> YTrain;

    //It stores all the Features that needed to train the model
    DataFrame<Object> XTest;

    //It stores the outcome that needed to test the model
    DataFrame<Object> YTest;

    Dataset dataset;

    //It is the Naive Bayes Frequency Table that could find the conditional probability
    HashMap<String, HashMap<String, HashMap<String, Integer>>> naiveBayesModel = new HashMap<>();

    //It stores the distinct class value along with their frequencies
    HashMap<String, Integer> classCount = new HashMap<>();

    public NaiveBayes(Dataset dataset, DataFrame<Object> trainingFeatures, DataFrame<Object> trainingOutcomes, DataFrame<Object> testingFeatures, DataFrame<Object> testingOutcome) {
        this.XTrain = trainingFeatures;
        this.YTrain = trainingOutcomes;
        this.dataset = dataset;
    }

    public NaiveBayes(Dataset dataset, DataFrame<Object> trainingFeatures, DataFrame<Object> trainingOutcomes) {
        this.XTrain = trainingFeatures;
        this.YTrain = trainingOutcomes;
        this.dataset = dataset;
    }

    //It returns a set with distinct outcome class values
    public HashSet<String> getClassLabels() {
        HashSet<String> classLabels = new HashSet<>();
        int count = (Integer) YTrain.count().get(0, 0);
        for (int i = 0; i < count; i++) {
            classLabels.add(YTrain.get(i, 0).toString());
        }
        return (classLabels);
    }

    //Select the rows from the dataframe according to a particular condition
    public DataFrame<Object> getDataFrameOnCondition(String condition) {
        int count = (Integer) YTrain.count().get(0, 0);
        return YTrain.select(rows -> {
            for (Object value : rows) {
                return (value.toString().equals(condition));
            }
            return true;
        });
    }

    //Select the rows from the dataframe according the index values
    public DataFrame<Object> getDataFrameOnIndices(DataFrame<Object> condition, String classValue) {
        int count = (Integer) condition.count().get(0, 0);
        AtomicInteger iter = new AtomicInteger(-1);
        return (XTrain.select(rows -> {
            iter.set(iter.get() + 1);
            return iter.get() < count && condition.get(iter.get(), 0).toString().equals(classValue);
        }));
    }

    //For each feature return the distinct feature values along with their frequencies
    public HashMap<String, Integer> getFeatureFrequency(List<Object> feature) {
        HashMap<String, Integer> frequency = new HashMap<>();
        for (Object value : feature) {
            String key = value.toString();
            if (frequency.containsKey(key)) frequency.put(key, frequency.get(key) + 1);
            else frequency.put(key, 1);
        }
        System.out.println("frequency " + frequency);
        return frequency;
    }

    //Used to train the NaiveBayesModel
    public void train() {
        HashSet<String> classValues = getClassLabels(); // Stores the distinct class values which is to be the first level dictionary
        for (String classValue : classValues) {
            DataFrame<Object> YTrainClassValue = getDataFrameOnCondition(classValue); // This dataframe contains the outcome according to the class values
            DataFrame<Object> XTrainClassValue = getDataFrameOnIndices(YTrainClassValue, classValue); // This dataframe contains the features according to the class values
            classCount.put(classValue, (Integer) YTrainClassValue.count().get(0, 0)); // This stores the class value and its frequency
            HashMap<String, HashMap<String, Integer>> featureFrequency = new HashMap<>();
            System.out.println("Class " + classValue);
            for (int featureIndex = 0; featureIndex < XTrainClassValue.columns().size(); featureIndex++) {
                HashMap<String, Integer> frequency = getFeatureFrequency(XTrainClassValue.col(featureIndex)); //For each feature return the distinct feature values along with their frequencies which is to the third level dictionary
                featureFrequency.put(String.valueOf(featureIndex), frequency); // Feature index and feature values which is the second level dictionary
            }
            naiveBayesModel.put(classValue, featureFrequency);
        }
        System.out.println(classCount);
    }


}

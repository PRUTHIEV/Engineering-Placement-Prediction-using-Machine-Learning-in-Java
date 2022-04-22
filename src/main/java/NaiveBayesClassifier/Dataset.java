package NaiveBayesClassifier;

import joinery.DataFrame;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Dataset {
    //placementDataFrame to store the dataset as DataFrame Data Structure
    private DataFrame<Object> placementDataFrame;

    //Stores the number of rows
    private int numberOfRows;

    //Stores the number of cols
    private int numberOfCols;

    //This is used to return an arraylist which is used to set the row indices of the DataFrame instance
    public ArrayList<Integer> getRowIndices() {
        ArrayList<Integer> rowIndices = new ArrayList<>();
        for (int i = 0; i <= 2965; i++) rowIndices.add(i);
        return rowIndices;
    }

    // This is used to return an arraylist which is used to set the column indices of the DataFrame instance
    public ArrayList<Integer> getColumnIndices() {
        ArrayList<Integer> columnIndices = new ArrayList<>();
        for (int i = 0; i <= 7; i++) columnIndices.add(i);
        return columnIndices;
    }

    //This constructor initializes the DataFrame Instance with the Integer row indices and colum indices
    public Dataset() {
        placementDataFrame = new DataFrame<>(getRowIndices(), getColumnIndices());
        numberOfCols = 8;
        numberOfRows = 2966;
    }

    //Used to print the dataset as like a DataFrame
    public void printDataset() {
        System.out.println(placementDataFrame);
    }

    //Return the number of rows in the DataFrame Instance
    public int getNumberOfRows() {
        return numberOfRows;
    }

    // Return the number of cols in the DataFrame Instance
    public int getNumberOfCols() {
        return numberOfCols;
    }

    // This returns the DataFrame which rows with features alone
    public DataFrame<Object> getFeatures() {
        return placementDataFrame.slice(0, numberOfRows, 0, numberOfCols - 1);
    }

    //This returns the Dataframe with Outcome Column alone
    public DataFrame<Object> getOutcomes() {
        return placementDataFrame.slice(0, numberOfRows, 7, numberOfCols);
    }

    public DataFrame<Object> getDataFrame(){
        return placementDataFrame;
    }
    //Reads the dataset using File Handling from a txt file
    public void readDataset() throws IOException {
        Path filePath = Paths.get("D:\\SRP\\src\\main\\resources\\PlacementDataset.txt");
        Scanner datasetReader = new Scanner(filePath);
        int data = -1;
        int row = -1;
        int col = 0;
        while (datasetReader.hasNext()) {
            data = datasetReader.nextInt();
            if (col == 0) {
                row = row + 1;
            }
            placementDataFrame.set(row, col, data);
            col = (col + 1) % 8;
        }
    }
}

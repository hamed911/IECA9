package net.internetengineering.utils;

import net.internetengineering.domain.Transaction;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Hamed Ara on 4/7/2016.
 */
public class CSVFileWriter {
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String fileName="backup.csv";
    public static void writeCsvFile(Transaction t) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName,true);
            fileWriter.append(t.buyer);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(t.seller);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(t.instrument);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(t.typeOfTrade);
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(t.quantity);
            fileWriter.append(COMMA_DELIMITER);
            // fileWriter.append(t.buyerRemainedMoney);
            // fileWriter.append(COMMA_DELIMITER);
            // fileWriter.append(t.sellerCurrentMoney);
            // fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(NEW_LINE_SEPARATOR);
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }
}
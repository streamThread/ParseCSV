import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, CsvValidationException {

        Parser.parseCSV("data/movementList.csv")
                .forEach(System.out::println);


    }
}

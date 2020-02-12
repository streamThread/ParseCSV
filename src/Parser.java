import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Parser {

    public static List<BankStatementOperations> parseCSV(String path) throws IOException, CsvValidationException {
        ArrayList<BankStatementOperations> bsoList = new ArrayList<>();
        Iterator<String[]> iter = new CSVIterator(new CSVReader(new BufferedReader(new FileReader(path))));
        iter.next();
        while (iter.hasNext()) {
            String[] tempLine = iter.next();
            if (tempLine.length != 8) {
                System.out.println("Wrong line: " + tempLine);
                continue;
            }
            for (int i = 0; i < 8; i++) {
                if (tempLine[i].contains(",")) {
                    tempLine[i] = tempLine[i].replace(',', '.');
                }
            }
            bsoList.add(new BankStatementOperations(
                    tempLine[3],
                    tempLine[5],
                    Double.parseDouble(tempLine[6]),
                    Double.parseDouble(tempLine[7])
            ));
        }
        return bsoList;
    }
}
//            Arrays.stream(tempLine)
//                    .filter(p -> p.contains(","))
//                    .forEach(k -> k = k.replace(',', '.'));


//            for (String gdfs : tempLine) {
//                System.out.println(gdfs);
//            }


//            Arrays.stream(tempLine).forEach(System.out::println);

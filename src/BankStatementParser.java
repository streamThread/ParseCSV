import com.opencsv.CSVIterator;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

class BankStatementParser {

    private static ArrayList<BankStatementOperations> bsoList = new ArrayList<>();
    private static HashMap<String, String> mccMap = new HashMap<>();

    public BankStatementParser(String pathMovementList, String pathMCC) throws IOException, CsvException {
        parseCSV(pathMovementList,pathMCC);
    }

    public static List<BankStatementOperations> parseCSV(String pathMovementList, String pathMCC) throws IOException, CsvException {
        parseMCCMap(pathMCC);
        Iterator<String[]> iter = new CSVIterator(new CSVReader(new BufferedReader(new FileReader(pathMovementList))));
        iter.next();
        while (iter.hasNext()) {
            String[] tempLine = iter.next();
            if (tempLine.length != 8) {
                System.out.println("Wrong line: " + tempLine);
                continue;
            }
            for (int i = 6; i < 8; i++) {
                if (tempLine[i].contains(",")) {
                    tempLine[i] = tempLine[i].replace(',', '.');
                }
            }
            bsoList.add(new BankStatementOperations(
                    tempLine[3],
                    tempLine[5],
                    Double.parseDouble(tempLine[6]),
                    Double.parseDouble(tempLine[7])));
        }
        return bsoList;
    }

    private static void parseMCCMap(String MCCpath) throws IOException, CsvException {
        CSVReader reader = new CSVReaderBuilder(new BufferedReader(new FileReader(MCCpath)))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build();
        List<String[]> tempFile = reader.readAll();
        for (int i = 0; i < tempFile.size(); i++) {
            if (tempFile.get(i).length == 2) {
                mccMap.put("MCC" + tempFile.get(i)[0], tempFile.get(i)[1]);
            } else {
                System.out.println("Неверная строка в файле MCC.csv: " + Arrays.toString(tempFile.get(i)));
            }
        }
        reader.close();
    }

    public static LocalDate getFirstDateINStatement(List<BankStatementOperations> bso) throws NullPointerException {
        LocalDate[] tempDates = bso.stream().map(BankStatementOperations::getDate).toArray(LocalDate[]::new);
        if (tempDates == null) throw new NullPointerException();
        return Arrays.stream(tempDates).min(Comparator.comparing(LocalDate::toEpochDay)).get();
    }

    public static LocalDate getLastDateInStatement(List<BankStatementOperations> bso) throws NullPointerException {
        LocalDate[] tempDates = bso.stream().map(BankStatementOperations::getDate).toArray(LocalDate[]::new);
        if (tempDates == null) throw new NullPointerException();
        return Arrays.stream(tempDates).max(Comparator.comparing(LocalDate::toEpochDay)).get();
    }

    public static double getIncomeSumRUR(List<BankStatementOperations> bsoList) {
        return bsoList.stream().filter(BankStatementOperations::isRUR)
                .mapToDouble(BankStatementOperations::getIncome).sum();
    }

    public static double getIncomeSumEUR(List<BankStatementOperations> bsoList) {
        return bsoList.stream().filter(BankStatementOperations::isEUR)
                .mapToDouble(BankStatementOperations::getIncome).sum();
    }

    public static double getIncomeSumUSD(List<BankStatementOperations> bsoList) {
        return bsoList.stream().filter(BankStatementOperations::isUSD)
                .mapToDouble(BankStatementOperations::getIncome).sum();
    }

    public static double getExpenseSumRUR(List<BankStatementOperations> bsoList) {
        return bsoList.stream().filter(BankStatementOperations::isRUR)
                .mapToDouble(BankStatementOperations::getExpense).sum();
    }

    public static double getExpenseSumEUR(List<BankStatementOperations> bsoList) {
        return bsoList.stream().filter(BankStatementOperations::isEUR)
                .mapToDouble(BankStatementOperations::getExpense).sum();
    }

    public static double getExpenseSumUSD(List<BankStatementOperations> bsoList) {
        return bsoList.stream().filter(BankStatementOperations::isUSD)
                .mapToDouble(BankStatementOperations::getExpense).sum();
    }

    public static HashMap<String, String> getMccMap() {
        return mccMap;
    }

    public static ArrayList<BankStatementOperations> getBsoList() {
        return bsoList;
    }
}



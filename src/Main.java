import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.TreeMap;

public class Main {

    static final String MOVEMENT_LIST_CSV = "data/movementList.csv";
    static final String MCC_CSV = "data/MCC.csv";

    static TreeMap<String, Double> expRUR = new TreeMap<>();
    static TreeMap<String, Double> expUSD = new TreeMap<>();
    static TreeMap<String, Double> expEUR = new TreeMap<>();

    public static void main(String[] args) {

        try {

            BankStatementParser.parseCSV(MOVEMENT_LIST_CSV, MCC_CSV);

            System.out.println(getHeaderToString());

            System.out.println(getAllMovementsToString());

            getExpenseByMCCToString();

        } catch (IOException | CsvException ex) {
            ex.printStackTrace();
        }
    }

    private static String getHeaderToString() {
        return String.format("\nАналитическая справка по операциям на основе банковской выписки с " +
                        "%1$td %1$th %1$tY по %2$td %2$th %2$tY",
                BankStatementParser.getFirstDateINStatement(BankStatementParser.getBsoList()),
                BankStatementParser.getLastDateInStatement(BankStatementParser.getBsoList()));
    }

    private static String getAllMovementsToString() {
        return String.format("\n%7$-75s: %.2f руб."
                        + "; %.2f EUR" + "; %.2f USD" +
                        "\n%8$-75s: %.2f руб." +
                        "; %.2f EUR" + "; %.2f USD",
                BankStatementParser.getExpenseSumRUR(BankStatementParser.getBsoList()),
                BankStatementParser.getExpenseSumEUR(BankStatementParser.getBsoList()),
                BankStatementParser.getExpenseSumUSD(BankStatementParser.getBsoList()),
                BankStatementParser.getIncomeSumRUR(BankStatementParser.getBsoList()),
                BankStatementParser.getIncomeSumEUR(BankStatementParser.getBsoList()),
                BankStatementParser.getIncomeSumUSD(BankStatementParser.getBsoList()),
                "Общая сумма расходов составила ", "Общая сумма пополнений счета составила: ");
    }

    private static void getExpenseByMCCToString() {

        getExpenseByMCC();

        System.out.printf("----------------------------------------------------------------------------------------------------%n" +
                "%60s", "Расходы по категориям:");
        for (String key : expRUR.keySet()) {
            System.out.printf("\n%-75s: %.2f руб.", key, expRUR.get(key));
        }
        System.out.printf("\n%60s", "В иностранной валюте:");
        for (String key : expEUR.keySet()) {
            if (expUSD.containsKey(key)) {
                System.out.printf("\n%-75s: %.2f EUR %.2f USD", key, expEUR.get(key), expUSD.get(key));
                expUSD.remove(key);
            } else {
                System.out.printf("\n%-75s: %.2f EUR", key, expEUR.get(key));
            }
        }
        for (String key : expUSD.keySet()) {
            System.out.printf("\n%-75s: %.2f USD", key, expUSD.get(key));
        }
    }

    private static void getExpenseByMCC() {

        for (BankStatementOperations bso : BankStatementParser.getBsoList()) {
            String bsoMCC = bso.getMcc();
            String keyInExp = BankStatementParser.getMccMap().get(bsoMCC);
            double expense = bso.getExpense();
            if (expense > 0 && bso.isRUR()) {
                if (expRUR.containsKey(keyInExp)) {
                    double expenseSum = expRUR.get(keyInExp) + expense;
                    expRUR.put(keyInExp, expenseSum);
                } else {
                    expRUR.put(keyInExp, expense);
                }
            } else if (expense > 0 && bso.isUSD()) {
                if (expUSD.containsKey(keyInExp)) {
                    double expenseSum = expUSD.get(keyInExp) + expense;
                    expUSD.put(keyInExp, expenseSum);
                } else {
                    expUSD.put(keyInExp, expense);
                }
            } else if (expense > 0 && bso.isEUR()) {
                if (expEUR.containsKey(keyInExp)) {
                    double expenseSum = expEUR.get(keyInExp) + expense;
                    expEUR.put(keyInExp, expenseSum);
                } else {
                    expEUR.put(keyInExp, expense);
                }

            }
        }
    }
}




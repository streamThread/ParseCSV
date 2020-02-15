import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.TreeMap;

public class Main {

    static final String MOVEMENT_LIST_CSV = "data/movementList.csv";
    static final String MCC_CSV = "data/MCC.csv";

    private static TreeMap<String, Double> expRURtemp = new TreeMap<>();
    private static TreeMap<String, Double> expUSDtemp = new TreeMap<>();
    private static TreeMap<String, Double> expEURtemp = new TreeMap<>();

    public static void main(String[] args) {

        try {

            BankStatementParser statementParser = new BankStatementParser(MOVEMENT_LIST_CSV, MCC_CSV);

            System.out.println(getHeaderToString(statementParser));

            System.out.println(getAllMovementsToString(statementParser));

            getExpenseByMCCToString(statementParser);

        } catch (IOException | CsvException ex) {
            ex.printStackTrace();
        }
    }

    private static String getHeaderToString(BankStatementParser parser) {
        return String.format("\nАналитическая справка по операциям на основе банковской выписки с " +
                        "%1$td %1$th %1$tY по %2$td %2$th %2$tY",
                parser.getFirstDateINStatement(parser.getBsoList()),
                parser.getLastDateInStatement(parser.getBsoList()));
    }

    private static String getAllMovementsToString(BankStatementParser parser) {
        return String.format("\n%7$-75s: %.2f руб."
                        + "; %.2f EUR" + "; %.2f USD" +
                        "\n%8$-75s: %.2f руб." +
                        "; %.2f EUR" + "; %.2f USD",
                parser.getExpenseSumRUR(parser.getBsoList()),
                parser.getExpenseSumEUR(parser.getBsoList()),
                parser.getExpenseSumUSD(parser.getBsoList()),
                parser.getIncomeSumRUR(parser.getBsoList()),
                parser.getIncomeSumEUR(parser.getBsoList()),
                parser.getIncomeSumUSD(parser.getBsoList()),
                "Общая сумма расходов составила ", "Общая сумма пополнений счета составила: ");
    }

    private static void getExpenseByMCCToString(BankStatementParser parser) {

        getExpenseByMCC(parser);

        System.out.printf("----------------------------------------------------------------------------------------------------%n" +
                "%60s", "Расходы по категориям:");
        for (String key : expRURtemp.keySet()) {
            System.out.printf("\n%-75s: %.2f руб.", key, expRURtemp.get(key));
        }
        System.out.printf("\n%60s", "В иностранной валюте:");
        for (String key : expEURtemp.keySet()) {
            if (expUSDtemp.containsKey(key)) {
                System.out.printf("\n%-75s: %.2f EUR %.2f USD", key, expEURtemp.get(key), expUSDtemp.get(key));
                expUSDtemp.remove(key);
            } else {
                System.out.printf("\n%-75s: %.2f EUR", key, expEURtemp.get(key));
            }
        }
        for (String key : expUSDtemp.keySet()) {
            System.out.printf("\n%-75s: %.2f USD", key, expUSDtemp.get(key));
        }
        expRURtemp.clear();
        expEURtemp.clear();
        expUSDtemp.clear();
    }

    private static void getExpenseByMCC(BankStatementParser parser) {

        for (BankStatementOperations bso : parser.getBsoList()) {
            String bsoMCC = bso.getMcc();
            String keyInExp = parser.getMccMap().get(bsoMCC);
            double expense = bso.getExpense();
            if (expense > 0 && bso.isRUR()) {
                if (expRURtemp.containsKey(keyInExp)) {
                    double expenseSum = expRURtemp.get(keyInExp) + expense;
                    expRURtemp.put(keyInExp, expenseSum);
                } else {
                    expRURtemp.put(keyInExp, expense);
                }
            } else if (expense > 0 && bso.isUSD()) {
                if (expUSDtemp.containsKey(keyInExp)) {
                    double expenseSum = expUSDtemp.get(keyInExp) + expense;
                    expUSDtemp.put(keyInExp, expenseSum);
                } else {
                    expUSDtemp.put(keyInExp, expense);
                }
            } else if (expense > 0 && bso.isEUR()) {
                if (expEURtemp.containsKey(keyInExp)) {
                    double expenseSum = expEURtemp.get(keyInExp) + expense;
                    expEURtemp.put(keyInExp, expenseSum);
                } else {
                    expEURtemp.put(keyInExp, expense);
                }

            }
        }
    }
}




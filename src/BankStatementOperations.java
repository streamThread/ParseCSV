import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankStatementOperations {

    private LocalDate date;
    private String name;
    private double income;
    private double expense;
    private String mcc;
    private boolean isUSD;
    private boolean isRUR;
    private boolean isEUR;

    public BankStatementOperations(String date, String name, double income, double expense) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yy"));
        this.name = name;
        this.income = income;
        this.expense = expense;
        mcc = setMCC(name);
        setCurrency(name);
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public double getIncome() {
        return income;
    }

    public double getExpense() {
        return expense;
    }

    public String getMcc() {
        return mcc;
    }

    public boolean isUSD() {
        return isUSD;
    }

    public boolean isRUR() {
        return isRUR;
    }

    public boolean isEUR() {
        return isEUR;
    }

    @Override
    public String toString() {
        return "Дата операции: " + date.format(DateTimeFormatter.ofPattern("dd.MM.yy")) +
                ", Наименование операции: " + name +
                ", Приход: " + income +
                ", Расход: " + expense +
                ", MCC: " + mcc;
    }

    private String setMCC(String operationName) {
        Pattern pattern = Pattern.compile("MCC\\d{4}");
        Matcher matcher = pattern.matcher(operationName);
        return matcher.find() ? matcher.group() : "Номер MCC не указан в выписке";
    }

    private void setCurrency(String operationName) {
        Pattern usd = Pattern.compile("USD{1}");
        Pattern rur = Pattern.compile("RUR{1}");
        Pattern eur = Pattern.compile("EUR{1}");
        Matcher matchUSD = usd.matcher(operationName);
        Matcher matchRUR = rur.matcher(operationName);
        Matcher matchEUR = eur.matcher(operationName);
        if (matchRUR.find()) {
            isRUR = true;
        } else if (matchEUR.find()) {
            isEUR = true;
        } else if (matchUSD.find()) isUSD = true;
    }
}
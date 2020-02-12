import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class BankStatementOperations {

    LocalDate date;
    String name;
    double income;
    double expense;

    public BankStatementOperations(String date, String name, double income, double expense) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yy"));
        this.name = name;
        this.income = income;
        this.expense = expense;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern(date));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    @Override
    public String toString() {
        return "Дата операции: " + date.format(DateTimeFormatter.ofPattern("dd.MM.yy")) +
                ", Наименование операции: " + name +
                ", Приход: " + income +
                ", Расход: " + expense;
    }
}
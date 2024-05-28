package MoneyMate;

public class Expense {
    private String user;
    private String date;
    private String category;
    private double amount;

    /**
     * Constructs a new Expense.
     * @param user       the User who created the expense.
     * @param date       the date of the expense.
     * @param category   the category of the expense.
     * @param amount     the amount of the expense.
     */
    public Expense(String user, String date, String category, double amount) {
        this.user = user;
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    /**
     * Gets the id of the expense.
     *
     * @return the id of the expense.
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets the date of the expense.
     *
     * @return the date of the expense.
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the category of the expense.
     *
     * @return the category of expense.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the amount of the expense.
     *
     * @return the amount of the expense.
     */
    public double getAmount() {
        return amount;
    }
}

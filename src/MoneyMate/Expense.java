package MoneyMate;

public class Expense {
    private int id;
    private String date;
    private String category;
    private double amount;

    /**
     * Constructs a new Expense.
     * @param date       the date of the expense.
     * @param category   the category of the expense.
     * @param amount     the amount of the expense.
     *
     * */
    public Expense(String date, String category, double amount) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    /**
     * Gets the id of the expense.
     *
     * @return the id of the expense.
     * */
    public int getId() {
        return id;
    }

    /**
     * Gets the date of the expense.
     *
     * @return the date of the expense.
     * */
    public String getDate() {
        return date;
    }

    /**
     * Gets the category of the expense.
     *
     * @return the category of expense.
     * */
    public String getCategory() {
        return category;
    }


    /**
     * Gets the amount of the expense.
     *
     * @return the amount of the expense.
     * */
    public double getAmount() {
        return amount;
    }
}


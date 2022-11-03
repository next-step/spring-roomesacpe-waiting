package roomescape.sales;

public class Sales {
    private Long id;
    private int amount;

    public Sales(int amount) {
        this(null, amount);
    }

    public Sales(Long id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }
}

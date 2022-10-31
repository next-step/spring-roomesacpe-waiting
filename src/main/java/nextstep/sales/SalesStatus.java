package nextstep.sales;

public enum SalesStatus {

    SALES("매출"),
    REFUND("환불");

    private final String description;

    SalesStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

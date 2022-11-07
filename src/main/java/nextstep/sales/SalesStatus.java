package nextstep.sales;

public enum SalesStatus {
    APPROVE("매출 승인"), CANCEL("매출 철회");

    private final String desc;

    SalesStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}

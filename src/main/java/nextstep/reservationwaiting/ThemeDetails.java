package nextstep.reservationwaiting;

public class ThemeDetails {

    private final Long id;
    private final String name;
    private final String desc;
    private final int price;

    public ThemeDetails(Long id, String name, String desc, int price) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrice() {
        return price;
    }
}

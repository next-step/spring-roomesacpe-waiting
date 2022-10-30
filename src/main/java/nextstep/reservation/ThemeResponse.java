package nextstep.reservation;

import nextstep.theme.Theme;

public class ThemeResponse {

    private final Long id;
    private final String name;
    private final String desc;
    private final int price;

    public ThemeResponse(Long id, String name, String desc, int price) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
            theme.getId(),
            theme.getName(),
            theme.getDesc(),
            theme.getPrice()
        );
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

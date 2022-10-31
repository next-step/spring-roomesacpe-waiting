package nextstep.revenue;

import java.time.LocalDate;

public class DailyRevenue {

    private Long id;
    private Long memberId;
    private Long profit;
    private LocalDate dailyAt;

    public DailyRevenue(Long id, Long memberId, Long profit, LocalDate dailyAt) {
        this.id = id;
        this.memberId = memberId;
        this.profit = profit;
        this.dailyAt = dailyAt;
    }

    public DailyRevenue(Long memberId, Long profit, LocalDate dailyAt) {
        this.memberId = memberId;
        this.profit = profit;
        this.dailyAt = dailyAt;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProfit() {
        return profit;
    }

    public LocalDate getDailyAt() {
        return dailyAt;
    }

    public void updateProfit(Long profit) {
        this.profit = profit;
    }
}

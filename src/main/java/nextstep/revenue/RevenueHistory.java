package nextstep.revenue;

import java.time.LocalDateTime;

public class RevenueHistory {
    private Long id;
    private Long dailyRevenueId;
    private Long originalProfit;
    private Long targetProfit;
    private String remark;
    private LocalDateTime createdAt;

    public RevenueHistory(Long dailyRevenueId, Long originalProfit, Long targetProfit, String remark, LocalDateTime createdAt) {
        this.dailyRevenueId = dailyRevenueId;
        this.originalProfit = originalProfit;
        this.targetProfit = targetProfit;
        this.remark = remark;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getDailyRevenueId() {
        return dailyRevenueId;
    }

    public Long getOriginalProfit() {
        return originalProfit;
    }

    public Long getTargetProfit() {
        return targetProfit;
    }

    public String getRemark() {
        return remark;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

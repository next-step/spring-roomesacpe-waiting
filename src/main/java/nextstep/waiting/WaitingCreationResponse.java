package nextstep.waiting;

public class WaitingCreationResponse {

    private final WaitingCreationResult result;
    private final Long principalId;

    public WaitingCreationResponse(WaitingCreationResult result, Long principalId) {
        this.result = result;
        this.principalId = principalId;
    }

    public static WaitingCreationResponse reserved(Long principalId) {
        return new WaitingCreationResponse(WaitingCreationResult.RESERVATION_CREATED, principalId);
    }

    public static WaitingCreationResponse waiting(Long principalId) {
        return new WaitingCreationResponse(WaitingCreationResult.WAITING_CREATED, principalId);
    }

    public boolean isReserved() {
        return result == WaitingCreationResult.RESERVATION_CREATED;
    }

    public WaitingCreationResult getResult() {
        return result;
    }

    public Long getPrincipalId() {
        return principalId;
    }
}


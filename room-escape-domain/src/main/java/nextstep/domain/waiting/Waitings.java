package nextstep.domain.waiting;

import java.util.List;

public class Waitings {
    private final List<Waiting> waitings;

    public Waitings(List<Waiting> waitings) {
        this.waitings = waitings;
    }

    public boolean hasWaiting() {
        return !waitings.isEmpty();
    }

    public int nextWaitingNumber() {
        return waitings.size() + 1;
    }

    public void moveUp() {
        waitings.forEach(Waiting::moveUp);
    }
}

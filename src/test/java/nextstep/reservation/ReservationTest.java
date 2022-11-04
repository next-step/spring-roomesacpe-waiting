package nextstep.reservation;

import static nextstep.reservation.ReservationStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @DisplayName("예약이 입금 대기 상태일 때 취소를 시도하면 예약 철회가 된다.")
    @Test
    void cancelFromWaitPayment() {
        Reservation reservation = new Reservation(1L, schedule(), member(), WAIT_PAYMENT);
        reservation.cancel();
        assertThat(reservation.getStatus()).isEqualTo(WITHDRAW);
    }

    @DisplayName("예약이 승인 상태일 때 취소를 시도하면 관리자 취소 대기가 된다.")
    @Test
    void cancelFromApproved() {
        Reservation reservation = new Reservation(1L, schedule(), member(), APPROVED);
        reservation.cancel();
        assertThat(reservation.getStatus()).isEqualTo(WAIT_ADMIN_CANCEL);
    }

    @DisplayName("예약이 입금 대기 상태일 때 관리자가 취소를 승인하면 예약이 취소된다.")
    @Test
    void cancelByAdminFromWaitPayment() {
        Reservation reservation = new Reservation(1L, schedule(), member(), WAIT_PAYMENT);
        reservation.cancelByAdmin();
        assertThat(reservation.getStatus()).isEqualTo(CANCEL);
    }

    @DisplayName("예약이 승인 상태일 때 관리자가 취소를 승인하면 예약이 취소된다.")
    @Test
    void cancelByAdminFromApproved() {
        Reservation reservation = new Reservation(1L, schedule(), member(), APPROVED);
        reservation.cancelByAdmin();
        assertThat(reservation.getStatus()).isEqualTo(CANCEL);
    }

    @DisplayName("예약이 관리자 취소 대기 상태일 때 관리자가 취소를 승인하면 예약이 철회된다.")
    @Test
    void cancelApproveByAdminFromWaitAdminCancel() {
        Reservation reservation = new Reservation(1L, schedule(), member(), WAIT_ADMIN_CANCEL);
        reservation.cancelApproveByAdmin();
        assertThat(reservation.getStatus()).isEqualTo(WITHDRAW);
    }

    private Member member() {
        return new Member("hyeon9mak", "1234", "최현구", "010-1234-5678", "USER");
    }

    private Schedule schedule() {
        return new Schedule(theme(), LocalDate.of(2022, 11, 1), LocalTime.of(12, 0));
    }

    private Theme theme() {
        return new Theme("테마", "테마 설명", 10000);
    }
}

package nextstep.account;

public class AccountMoney {

  private Long id;
  private Long reservationId;
  private Long memberId;
  private Long amount;

  public AccountMoney(Long reservationId, Long memberId, Long amount) {
    this(null, reservationId, memberId, amount);
  }

  public AccountMoney(Long id, Long reservationId, Long memberId, Long amount) {
    this.id = id;
    this.reservationId = reservationId;
    this.memberId = memberId;
    this.amount = amount;
  }

  public Long getId() {
    return id;
  }

  public Long getReservationId() {
    return reservationId;
  }

  public Long getMemberId() {
    return memberId;
  }

  public Long getAmount() {
    return amount;
  }

  public AccountMoney refund() {
    return new AccountMoney(reservationId, memberId, Math.negateExact(amount));
  }
}

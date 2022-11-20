package nextstep.account;

public class PurchaseMoney {

  private Long id;
  private Long reservationId;
  private Long memberId;
  private Long amount;

  public PurchaseMoney(Long reservationId, Long memberId, Long amount) {
    this(null, reservationId, memberId, amount);
  }

  public PurchaseMoney(Long id, Long reservationId, Long memberId, Long amount) {
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
}

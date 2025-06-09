package vos.hoteldemo.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private int voucherID;

    @Column(name = "code")
    private String code;

    @Column(name = "discount_percent")
    private int discountPercent;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL)
    private List<VoucherCustomerRoom> voucherCustomerRooms;

    public Voucher() {
    }

    public int getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(int voucherID) {
        this.voucherID = voucherID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<VoucherCustomerRoom> getVoucherCustomerRooms() {
        return voucherCustomerRooms;
    }

    public void setVoucherCustomerRooms(List<VoucherCustomerRoom> voucherCustomerRooms) {
        this.voucherCustomerRooms = voucherCustomerRooms;
    }
}

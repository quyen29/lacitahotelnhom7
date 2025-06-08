package vos.hoteldemo.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "customer_type")
public class CustomerType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_type_id")
    private int customerTypeID;

    @Column(name = "customer_type_name")
    private String customerTypeName;

    @OneToMany(mappedBy = "customerType", cascade = CascadeType.ALL)
    private List<VoucherCustomerRoom> voucherCustomerRooms;

    public CustomerType() {
    }

    public int getCustomerTypeID() {
        return customerTypeID;
    }

    public void setCustomerTypeID(int customerTypeID) {
        this.customerTypeID = customerTypeID;
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public List<VoucherCustomerRoom> getVoucherCustomerRooms() {
        return voucherCustomerRooms;
    }

    public void setVoucherCustomerRooms(List<VoucherCustomerRoom> voucherCustomerRooms) {
        this.voucherCustomerRooms = voucherCustomerRooms;
    }
}

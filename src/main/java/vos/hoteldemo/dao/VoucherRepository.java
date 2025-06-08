package vos.hoteldemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vos.hoteldemo.entity.Voucher;

import java.util.Date;
import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    @Query("SELECT v FROM Voucher v " +
            "JOIN VoucherCustomerRoom vcr ON v = vcr.voucher " +
            "WHERE v.startDate <= :today AND v.endDate >= :today " +
            "AND vcr.roomType.roomTypeID = :roomTypeId " +
            "AND vcr.customerType.customerTypeID = :customerTypeId")
    List<Voucher> findAvailableVouchers(@Param("today") Date today,
                                        @Param("roomTypeId") int roomTypeId,
                                        @Param("customerTypeId") int customerTypeId);
}
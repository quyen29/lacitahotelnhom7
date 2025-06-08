package vos.hoteldemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vos.hoteldemo.dao.BillRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillRepository billRepository;

    @Override
    public float getMonthlyRevenue(LocalDate date) {
        LocalDateTime startOfMonth = date.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);
        Float total = billRepository.sumTotalByInvoiceTimeBetween(startOfMonth, endOfMonth);
        return total != null ? total : 0f;
    }

    @Override
    public Map<String, Float> getRevenueLast13Months() {
        Map<String, Float> revenueMap = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        for (int i = 12; i >= 0; i--) {
            LocalDate targetDate = now.minusMonths(i);
            LocalDateTime start = targetDate.withDayOfMonth(1).atStartOfDay();
            LocalDateTime end = start.plusMonths(1);
            Float total = billRepository.sumTotalByInvoiceTimeBetween(start, end);
            revenueMap.put(targetDate.format(formatter), total != null ? total : 0f);
        }
        return revenueMap;
    }
}

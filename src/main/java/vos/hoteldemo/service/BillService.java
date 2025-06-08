package vos.hoteldemo.service;

import java.time.LocalDate;
import java.util.Map;

public interface BillService {
    float getMonthlyRevenue(LocalDate month);

    Map<String, Float> getRevenueLast13Months();
}

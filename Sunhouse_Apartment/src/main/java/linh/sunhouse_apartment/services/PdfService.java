package linh.sunhouse_apartment.services;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface PdfService {
    void exportStatisticsPdf(Map<String, Object> data, HttpServletResponse response) throws Exception;
}

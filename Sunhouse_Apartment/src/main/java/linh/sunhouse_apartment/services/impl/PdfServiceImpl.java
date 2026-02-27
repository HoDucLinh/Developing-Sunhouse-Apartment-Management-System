package linh.sunhouse_apartment.services.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.servlet.http.HttpServletResponse;
import linh.sunhouse_apartment.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Service
public class PdfServiceImpl implements PdfService {
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void exportStatisticsPdf(Map<String, Object> data,
                                    HttpServletResponse response) throws Exception {

        Context context = new Context();
        context.setVariables(data);

        String html = templateEngine.process("statistics-pdf", context);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=statistics_report.pdf");

        OutputStream os = response.getOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();

        // 🔴 QUAN TRỌNG: đăng ký font Unicode
        builder.useFont(
                () -> {
                    try {
                        return new ClassPathResource("fonts/NotoSans-Regular.ttf").getInputStream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                "NotoSans"
        );

        builder.withHtmlContent(html, null);
        builder.toStream(os);
        builder.run();

        os.close();
    }
}

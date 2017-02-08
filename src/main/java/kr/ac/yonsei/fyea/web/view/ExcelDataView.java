package kr.ac.yonsei.fyea.web.view;

import com.google.common.primitives.Doubles;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static kr.ac.yonsei.fyea.util.WorkbookUtils.getCellStyle;

@Component
public class ExcelDataView extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Sheet sheet = workbook.createSheet();
        updateSheet(sheet, (List<List<String>>) model.get("data"));
    }

    private static void updateSheet(Sheet sheet, List<List<String>> rows) {
        CellStyle numericStyle = getCellStyle(sheet, "0.00");

        for (int rownum = 0; rownum < rows.size(); rownum++) {
            Row row = sheet.createRow(rownum);
            List<String> cells = rows.get(rownum);

            for (int column = 0; column < cells.size(); column++) {
                Cell cell = row.createCell(column);
                String value = cells.get(column);
                cell.setCellValue(value);

                Double numericValue = Doubles.tryParse(value);
                if (numericValue != null) {
                    cell.setCellStyle(numericStyle);
                    cell.setCellValue(numericValue);
                }
            }
        }
    }
}

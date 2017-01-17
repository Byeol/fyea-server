package kr.ac.yonsei.fyea.web.view;

import kr.ac.yonsei.fyea.util.ExcelStatsUtils;
import kr.ac.yonsei.fyea.web.model.StatsQueryData;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
//@RequiredArgsConstructor
public class ExcelStatsView extends AbstractXlsxView {

//    private final StatisticsService statisticsService;

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Sheet sheet = workbook.createSheet();
        StatsQueryData queryData = (StatsQueryData) model.get("data");
        updateSheet(sheet, queryData);
    }

    private static void updateSheet(Sheet sheet, StatsQueryData queryData) {
        if (queryData.getSurveyAnswerMap() != null) {
            ExcelStatsUtils.updateSheet(sheet, queryData.getStudents(), queryData.getConditionAnswerMap(), queryData.getSurveyAnswerMap());
        } else {
            ExcelStatsUtils.updateSheet(sheet, queryData.getStudents(), queryData.getConditionAnswerMap(), queryData.getKeys(), queryData.getCodeMap());
        }
    }
}

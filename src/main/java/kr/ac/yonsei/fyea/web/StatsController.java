package kr.ac.yonsei.fyea.web;

import kr.ac.yonsei.fyea.service.StatsService;
import kr.ac.yonsei.fyea.web.model.ChartData;
import kr.ac.yonsei.fyea.web.model.StatsQueryModel;
import kr.ac.yonsei.fyea.web.view.ExcelDataView;
import kr.ac.yonsei.fyea.web.view.ExcelStatsView;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import static kr.ac.yonsei.fyea.util.StatsDataUtils.getStatistics;
import static kr.ac.yonsei.fyea.util.StatsDataUtils.toList;

@Log
@RequiredArgsConstructor
@RestController
public class StatsController {

    private final StatsService statsService;
    private final ExcelStatsView excelStatsView;
    private final ExcelDataView excelDataView;

    @PostMapping("/stats")
    public Object getStats(@RequestBody StatsQueryModel queryModel) {
        return getStatistics(statsService.getStatsQueryData(queryModel));
    }

    @PostMapping("/stats/export")
    public ModelAndView statsExport(@RequestBody StatsQueryModel queryModel) {
        ModelAndView mav = new ModelAndView(excelStatsView);
        mav.addObject("data", statsService.getStatsQueryData(queryModel));
        return mav;
    }

    @PostMapping("/stats/chart")
    public ModelAndView statsChart(@RequestBody ChartData chartData) {
        return new ModelAndView(excelDataView, "data", toList(chartData));
    }
}

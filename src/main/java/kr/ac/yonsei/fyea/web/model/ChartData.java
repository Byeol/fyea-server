package kr.ac.yonsei.fyea.web.model;

import lombok.Data;

import java.util.List;

@Data
public class ChartData {
    private List<String> labels;
    private List<ChartDataSet> datasets;
}

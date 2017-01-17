package kr.ac.yonsei.fyea.web.model;

import lombok.Data;

import java.util.List;

@Data
public class ChartDataSet {
    private String label;
    private List<Number> data;
}

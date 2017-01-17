package kr.ac.yonsei.fyea.web.model;

import kr.ac.yonsei.fyea.domain.Statistics;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class DescriptiveStatisticsData {

    private final Map<String, List<Statistics>> statisticsMap;
}

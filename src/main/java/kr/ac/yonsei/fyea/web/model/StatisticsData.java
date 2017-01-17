package kr.ac.yonsei.fyea.web.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@RequiredArgsConstructor
public class StatisticsData {

    private final String type;
    private final Map<String, Object> data;

    public Object get(String key) {
        return data.get(key);
    }
}

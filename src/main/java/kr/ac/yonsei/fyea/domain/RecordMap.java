package kr.ac.yonsei.fyea.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor(force = true)
public class RecordMap {

    private final Map<String, String> records = new HashMap<>();

    public void addRecord(String key, String value) {
        this.records.put(key, value);
    }
}

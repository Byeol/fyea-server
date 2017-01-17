package kr.ac.yonsei.fyea.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

@Data
public class Statistics {

    @JsonIgnore
    private DescriptiveStatistics statistics;

    public Statistics(DescriptiveStatistics statistics) {
        this.statistics = statistics;
    }

    public double getMean() {
        return getStatistics().getMean();
    }

    public double getStandardDeviation() {
        return getStatistics().getStandardDeviation();
    }

    public long getN() {
        return getStatistics().getN();
    }
}

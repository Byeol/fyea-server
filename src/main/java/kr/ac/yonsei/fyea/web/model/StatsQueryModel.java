package kr.ac.yonsei.fyea.web.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
public class StatsQueryModel {

    @NotEmpty
    private String condition;

    @NotEmpty
    private List<String> surveys;

    private String idStartsWith;
}

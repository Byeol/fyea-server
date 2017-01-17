package kr.ac.yonsei.fyea.web.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class DataQueryModel {

    @NotEmpty
    private String dataFile;

    @NotEmpty
    private String codeMapFile;

    private String password;
}

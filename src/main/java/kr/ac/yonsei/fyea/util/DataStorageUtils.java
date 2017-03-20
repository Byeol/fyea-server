package kr.ac.yonsei.fyea.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static kr.ac.yonsei.fyea.util.DataUtils.loadWorkbook;

@UtilityClass
public class DataStorageUtils {

    public static Reader readFile(InputStream file, String fileExt, String password) throws IOException {
        switch (fileExt) {
            case "xlsx":
                return loadWorkbook(WorkbookUtils.readFile(file, password));

            case "xls":
                return loadWorkbook(WorkbookUtils.readFile(file, password));

            case "csv":
                return new InputStreamReader(file);

            default:
                throw new RuntimeException("Not supported file!");
        }
    }

    public static Iterable<CSVRecord> readAsCSV(Reader in) throws IOException {
        return CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
    }
}

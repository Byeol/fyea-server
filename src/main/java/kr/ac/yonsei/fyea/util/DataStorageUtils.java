package kr.ac.yonsei.fyea.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.openxml4j.opc.OPCPackage;

import java.io.*;

import static kr.ac.yonsei.fyea.util.DataUtils.loadWorkbook;

@UtilityClass
public class DataStorageUtils {
    public static InputStream readAsCSV(InputStream in) throws IOException {
        File tempFile = File.createTempFile("ucds-", ".csv");
        PrintStream output = new PrintStream(tempFile);

        try (OPCPackage opcPackage = OPCPackage.open(in)) {
            ExcelToCSVConverter converter = new ExcelToCSVConverter(opcPackage, output, -1);
            converter.process();
        } catch (Exception e) {
            throw new RuntimeException("Could not read file as CSV");
        }

        return new FileInputStream(tempFile);
    }

    public static Reader readFile(InputStream file, String fileExt, String password) throws IOException {
        switch (fileExt) {
            case "xlsx":
                if (password == null || password.isEmpty()) {
                    return new InputStreamReader(readAsCSV(file));
                }
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

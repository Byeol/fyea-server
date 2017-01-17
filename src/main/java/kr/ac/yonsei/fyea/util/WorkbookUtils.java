package kr.ac.yonsei.fyea.util;

import lombok.experimental.UtilityClass;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Iterator;

@UtilityClass
public class WorkbookUtils {

    public static String convert(Sheet sheet) {
        StringBuilder output = new StringBuilder();
//        sheet.getWorkbook().setMissingCellPolicy(Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Integer lastCellNum = (int) row.getLastCellNum();

            for (Integer i = 0; i < lastCellNum; i++) {
                if (i != 0) {
                    output.append(",");
                }

                Cell cell = row.getCell(i);
                String value = getCellValue(cell);
                value = value.replace('\"', '\'');
                output.append("\"").append(value).append("\"");
            }

            if (rowIterator.hasNext()) {
                output.append("\n");
            }
        }

//        System.out.println(output.toString());
        return output.toString();
    }

    public static InputStream getDecryptedDataStream(POIFSFileSystem fs, String password) throws IOException {
        EncryptionInfo info = new EncryptionInfo(fs);
        Decryptor d = Decryptor.getInstance(info);

        try {
            if (!d.verifyPassword(password)) {
                throw new RuntimeException("Unable to process: document is encrypted");
            }

            return d.getDataStream(fs);
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException("Unable to process encrypted document", ex);
        }
    }

    public static Workbook readFile(InputStream stream, String password) throws IOException {
        if (password != null && !password.equals("")) {
            POIFSFileSystem fs = new POIFSFileSystem(stream);
            stream = getDecryptedDataStream(fs, password);
        }

        return readFile(stream);
    }

    public static Workbook readFileHSSF(InputStream stream, String password) throws IOException {
        if (password != null && !password.equals("")) {
            Biff8EncryptionKey.setCurrentUserPassword(password);
        }
        POIFSFileSystem fs = new POIFSFileSystem(stream);
        HSSFWorkbook workbook = new HSSFWorkbook(fs);
        Biff8EncryptionKey.setCurrentUserPassword(null);
        return workbook;
    }

    public static Workbook readFile(InputStream stream) throws IOException {
        return new XSSFWorkbook(stream);
    }

    public static String getCellValue(Cell cell) {
        DataFormatter dataFormatter = new DataFormatter();
        return dataFormatter.formatCellValue(cell);
    }

    public static CellStyle getCellStyle(Sheet sheet, String format) {
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat(format));
        return style;
    }
}

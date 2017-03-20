package kr.ac.yonsei.fyea.util;

import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;

@RequiredArgsConstructor
public class ExcelToCSVConverter {
    private final OPCPackage opcPackage;
    private final PrintStream output;
    private final int minColumns;

    public void process() throws IOException, OpenXML4JException, SAXException {
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.opcPackage);
        XSSFReader xssfReader = new XSSFReader(this.opcPackage);
        StylesTable styles = xssfReader.getStylesTable();
        Iterator<InputStream> sheetIterator = xssfReader.getSheetsData();

        while (sheetIterator.hasNext()) {
            try (InputStream stream = sheetIterator.next()) {
                processSheet(styles, strings, new SheetToCSV(), stream);
            }
        }
    }

    private static void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetHandler, InputStream sheetInputStream) throws IOException, SAXException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);

        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
        }
    }

    private class SheetToCSV implements SheetContentsHandler {
        private boolean firstCellOfRow = false;
        private int currentRow = -1;
        private int currentCol = -1;

        private void outputMissingRows(int number) {
            for (int i = 0; i < number; i++) {
                for (int j = 0; j < minColumns; j++) {
                    output.append(',');
                }
                output.append('\n');
            }
        }

        @Override
        public void startRow(int rowNum) {
            outputMissingRows(rowNum - currentRow - 1);
            firstCellOfRow = true;
            currentRow = rowNum;
            currentCol = -1;
        }

        @Override
        public void endRow(int rowNum) {
            for (int i = currentCol; i < minColumns; i++) {
                output.append(',');
            }
            output.append('\n');
        }

        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment comment) {
            if (firstCellOfRow) {
                firstCellOfRow = false;
            } else {
                output.append(',');
            }

            if (cellReference == null) {
                cellReference = new CellAddress(currentRow, currentCol).formatAsString();
            }

            int thisCol = (new CellReference(cellReference)).getCol();
            int missedCols = thisCol - currentCol - 1;
            for (int i = 0; i < missedCols; i++) {
                output.append(',');
            }
            currentCol = thisCol;

            try {
                Double.parseDouble(formattedValue);
                output.append(formattedValue);
            } catch (NumberFormatException e) {
                output.append('"');
                output.append(formattedValue);
                output.append('"');
            }
        }

        @Override
        public void headerFooter(String text, boolean isHeader, String tagName) {
        }
    }
}

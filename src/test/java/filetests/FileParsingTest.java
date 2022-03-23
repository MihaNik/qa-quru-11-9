package filetests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileParsingTest  {

    @Test
    void parsePdfTest() throws Exception {
        ZipFile zipFile = new ZipFile("src/test/resources/files.zip");
        ZipEntry zipEntry = zipFile.getEntry("pdf-sample.pdf");
        try (InputStream inputStream = zipFile.getInputStream(zipEntry)) {
            PDF pdf = new PDF(inputStream);
            Assertions.assertThat(pdf.author).isEqualTo("cdaily");
            Assertions.assertThat(pdf.title).isEqualTo("This is a test PDF file");
            Assertions.assertThat(pdf.numberOfPages).isEqualTo(1);
            Assertions.assertThat(pdf.text).contains("Portable Document Format (PDF)");
        }

    }


    @Test
    void parseXlsTest() throws IOException {
        ZipFile zipFile = new ZipFile("src/test/resources/files.zip");
        ZipEntry zipEntry = zipFile.getEntry("EventsExample.xlsx");
        try (InputStream inputStream = zipFile.getInputStream(zipEntry)) {
            XLS xls = new XLS(inputStream);
            String reportTitle = xls.excel
                    .getSheetAt(0)
                    .getRow(0)
                    .getCell(0)
                    .getStringCellValue();

            Assertions.assertThat(xls.excel.getSheetAt(0).getLastRowNum()).isEqualTo(41);
            Assertions.assertThat(reportTitle).contains("Отчет \"События по объектам\"");
        }
    }

    @Test
    void parseCsvTest() throws Exception {
        ZipFile zipFile = new ZipFile("src/test/resources/files.zip");
        ZipEntry zipEntry = zipFile.getEntry("username.csv");
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        try (InputStream inputStream = zipFile.getInputStream(zipEntry);
             CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream)).withCSVParser(parser).build()) {
            List<String[]> content = reader.readAll();
            Assertions.assertThat(content.size()).isEqualTo(6);
            Assertions.assertThat(content.get(0)).contains("Username", "Identifier", "First name", "Last name");
            Assertions.assertThat(content.get(5)).contains("Jamie", "Smith");
        }

    }
}

package filetests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileParsingTest {


    @Test
    void parsePdfTest() throws Exception {

        PDF pdf = new PDF(new File("src/test/resources/pdf-sample.pdf"));
        Assertions.assertThat(pdf.author).isEqualTo("cdaily");
        Assertions.assertThat(pdf.title).isEqualTo("This is a test PDF file");
        Assertions.assertThat(pdf.numberOfPages).isEqualTo(1);
        Assertions.assertThat(pdf.text).contains("Portable Document Format (PDF)");
    }


    @Test
    void parseXlsTest() {
        XLS xls = new XLS(new File("src/test/resources/EventsExample.xlsx"));
        String reportTitle = xls.excel
                .getSheetAt(0)
                .getRow(0)
                .getCell(0)
                .getStringCellValue();

        Assertions.assertThat(xls.excel.getSheetAt(0).getLastRowNum()).isEqualTo(41);
        Assertions.assertThat(reportTitle).contains("Отчет \"События по объектам\"");
    }

    @Test
    void parseCsvTest() throws Exception {
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        try (BufferedReader br = Files.newBufferedReader(Paths.get("src/test/resources/username.csv"), StandardCharsets.UTF_8);
             CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).build()) {

            List<String[]> content = reader.readAll();
            Assertions.assertThat(content.size()).isEqualTo(6);
            Assertions.assertThat(content.get(0)).contains("Username", "Identifier", "First name", "Last name");
            Assertions.assertThat(content.get(5)).contains("Jamie", "Smith");

        }
    }
}

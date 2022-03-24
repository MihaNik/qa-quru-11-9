package filetests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dictionary.Shop;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class JsonParsingTest {

    @Test
    void jsonCommonTest() throws IOException {
        String shopJson = new String(Files.readAllBytes(new File("src/test/resources/shop.json").toPath()), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(shopJson);

        Assertions.assertThat(rootNode.get("cashType").asText()).isEqualTo("pos");
        Assertions.assertThat(rootNode.get("shopInfo").get("shopNumber").asInt()).isEqualTo(8);
        Assertions.assertThat(rootNode.get("cashes").get(1).get("cashName").asText()).isEqualTo("касса 15");
        Assertions.assertThat(rootNode.get("cashes").get(1).get("cashNumber").asInt()).isEqualTo(15);
    }


    @Test
    void jsonTypeTest() throws IOException {
        String shopJson = new String(Files.readAllBytes(new File("src/test/resources/shop.json").toPath()), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Shop shop  = objectMapper.readValue(shopJson,Shop.class);

        Assertions.assertThat(shop.cashType).isEqualTo("pos");
        Assertions.assertThat(shop.shopInfo.shopNumber).isEqualTo(8);
        Assertions.assertThat(shop.shopInfo.shopName).isEqualTo("Магазин 8");
        Assertions.assertThat(shop.cashes.get(1).cashName).isEqualTo("касса 15");
        Assertions.assertThat(shop.cashes.get(1).cashNumber).isEqualTo(15);
    }
}

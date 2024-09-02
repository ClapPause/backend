package com.clap.pause.service.image;

import com.clap.pause.config.properties.ImageProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@SpringBootTest
@Transactional
class LocalStorageServiceTest {

    @Autowired
    private LocalStorageService localStorageService;
    @Autowired
    private ImageProperties imageProperties;

    @Test
    @DisplayName("정상적인 형식의 이미지를 업로드하면 저장된다.")
    void uploadImage_success() throws IOException {
        //given
        var image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        image.createGraphics()
                .drawImage(image, 0, 0, null);

        var fileName = UUID.randomUUID() + ".jpg";
        var filePath = Paths.get(imageProperties.uploadDir(), fileName);

        var convertedImage = new File(filePath.toString());
        ImageIO.write(image, "jpg", convertedImage);
        //when
        var result = localStorageService.uploadImage(convertedImage);
        //then
        Assertions.assertThat(result).isEqualTo("/images/" + fileName);
        Assertions.assertThat(Files.exists(filePath)).isTrue();

        Files.deleteIfExists(filePath);
    }
}

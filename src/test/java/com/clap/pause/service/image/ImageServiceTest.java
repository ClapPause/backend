package com.clap.pause.service.image;

import com.clap.pause.config.properties.ImageProperties;
import com.clap.pause.exception.ImageProcessingFailedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
@Transactional
public class ImageServiceTest {

    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageProperties imageProperties;
    byte[] imageBytes;

    @BeforeEach
    void setUp() throws IOException {
        var image = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_RGB);
        image.createGraphics().drawImage(image, 0, 0, null);

        var outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        imageBytes = outputStream.toByteArray();
    }

    @Test
    @DisplayName("이미지를 동시에 100개를 처리해도 동시성 이슈가 발생하지 않는다.")
    void saveImage_success_withoutConcurrencyIssue() throws IOException {
        //given
        var futures = new ArrayList<CompletableFuture<String>>();
        //when
        for (int i = 0; i < 100; i++) {
            var multipartFile = new MockMultipartFile("file", UUID.randomUUID() + ".jpg", "image/jpeg", new ByteArrayInputStream(imageBytes));
            var future = imageService.saveImage(multipartFile)
                    .thenApply(image -> image)
                    .exceptionally(e -> {
                        throw new ImageProcessingFailedException(e.getMessage());
                    });
            futures.add(future);
        }
        //then
        var allOfFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOfFutures.join();

        var result = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        Assertions.assertThat(result.size())
                .isEqualTo(100);

        for (var image : result) {
            Files.deleteIfExists(getPath(image));
        }
    }

    private Path getPath(String imageUrl) {
        var image = imageUrl.replaceAll("/images/", "");
        return Paths.get(imageProperties.uploadDir(), image);
    }
}

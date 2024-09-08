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
import java.util.Collections;
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
    @DisplayName("@Async 애노테이션이 있는 상황에서 결과를 리턴")
    void asyncSaveImageTest() throws IOException {
        //given
        var startTime = System.nanoTime();
        var futures = new ArrayList<CompletableFuture<String>>();
        var result = Collections.synchronizedList(new ArrayList<String>());
        //when
        for (int i = 0; i < 1000; i++) {
            var multipartFile = new MockMultipartFile("file", UUID.randomUUID() + ".jpg", "image/jpeg", new ByteArrayInputStream(imageBytes));
            var future = imageService.saveImage(multipartFile);
            future.thenApply(image -> {
                result.add(image);
                return image;
            }).exceptionally(e -> {
                throw new ImageProcessingFailedException(e.getMessage());
            });
            futures.add(future);
        }
        //then
        var allOfFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOfFutures.join();

        var endTime = System.nanoTime();
        var elapsedTime = endTime - startTime;
        System.out.println("작업 완료까지 걸린 시간 (나노초): " + elapsedTime + " ns");

        Assertions.assertThat(result.size())
                .isEqualTo(1000);

        for (var image : result) {
            Files.deleteIfExists(getPath(image));
        }
    }

    @Test
    @DisplayName("@Async 애노테이션이 없는 상황에서 결과를 리턴")
    void noAsyncSaveImageTest() throws IOException {
        //given
        var startTime = System.nanoTime();
        var result = new ArrayList<String>();
        //when
        for (int i = 0; i < 1000; i++) {
            var multipartFile = new MockMultipartFile("file", UUID.randomUUID() + ".jpg", "image/jpeg", new ByteArrayInputStream(imageBytes));
            var imageUrl = imageService.noAsyncSaveImage(multipartFile);
            result.add(imageUrl);
        }
        //then
        var endTime = System.nanoTime();
        var elapsedTime = endTime - startTime;
        System.out.println("작업 완료까지 걸린 시간 (나노초): " + elapsedTime + " ns");

        Assertions.assertThat(result.size())
                .isEqualTo(1000);

        for (var image : result) {
            Files.deleteIfExists(getPath(image));
        }
    }

    private Path getPath(String imageUrl) {
        var image = imageUrl.replaceAll("/images/", "");
        return Paths.get(imageProperties.uploadDir(), image);
    }
}

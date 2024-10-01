package com.clap.pause.service.image;

import com.clap.pause.config.properties.ImageProperties;
import com.clap.pause.exception.ImageProcessingFailedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    static byte[] imageBytes;
    static BufferedImage image;
    @InjectMocks
    private ImageService imageService;
    @Mock
    private StorageService storageService;
    @Mock
    private ImageProperties imageProperties;

    @BeforeAll
    static void setUp() throws IOException {
        image = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_RGB);
        image.createGraphics()
                .drawImage(image, 0, 0, null);

        var outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        imageBytes = outputStream.toByteArray();
    }

//    @Test
//    void saveImage_success() throws Exception {
//        //given
//        var multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", new ByteArrayInputStream(imageBytes));
//
//        when(storageService.uploadImage(any(File.class))).thenReturn("/images/test.jpg");
//        when(imageProperties.uploadDir()).thenReturn("/mock/upload/dir");
//        try (var mockedStatic = mockStatic(ImageIO.class)) {
//            mockedStatic.when(() -> ImageIO.read(any(InputStream.class)))
//                    .thenReturn(image);
//            mockedStatic.when(() -> ImageIO.write(any(BufferedImage.class), any(String.class), any(File.class)))
//                    .thenReturn(true);
//            // when
//            var result = imageService.saveImage(multipartFile).get();
//            // then
//            Assertions.assertThat(result)
//                    .isEqualTo("/images/test.jpg");
//        }
//    }

    @Test
    void saveImage_fail_imageReadWithIOException() throws Exception {
        //given
        var multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", new ByteArrayInputStream(imageBytes));

        try (var mockedStatic = mockStatic(ImageIO.class)) {
            mockedStatic.when(() -> ImageIO.read(any(InputStream.class)))
                    .thenThrow(new IOException("TEST IO Exception"));
            // when, then
            Assertions.assertThatThrownBy(() -> imageService.saveImage(multipartFile))
                    .isInstanceOf(ImageProcessingFailedException.class);
        }
    }

    @Test
    void saveImage_fail_imageWriteWithIOException() throws Exception {
        //given
        var multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", new ByteArrayInputStream(imageBytes));

        when(imageProperties.uploadDir()).thenReturn("/mock/upload/dir");
        try (var mockedStatic = mockStatic(ImageIO.class)) {
            mockedStatic.when(() -> ImageIO.read(any(InputStream.class)))
                    .thenReturn(image);
            mockedStatic.when(() -> ImageIO.write(any(BufferedImage.class), any(String.class), any(File.class)))
                    .thenThrow(new IOException("TEST IO Exception"));
            // when, then
            Assertions.assertThatThrownBy(() -> imageService.saveImage(multipartFile))
                    .isInstanceOf(ImageProcessingFailedException.class);
        }
    }

//    @Test
//    void saveImages_success() throws Exception {
//        //given
//        var multipartFile1 = new MockMultipartFile("file", "test1.jpg", "image/jpeg", new ByteArrayInputStream(imageBytes));
//        var multipartFile2 = new MockMultipartFile("file", "test2.jpg", "image/jpeg", new ByteArrayInputStream(imageBytes));
//        var multipartFiles = new ArrayList<MultipartFile>();
//        multipartFiles.add(multipartFile1);
//        multipartFiles.add(multipartFile2);
//
//        when(storageService.uploadImages(any())).thenReturn(List.of("/images/test1.jpg", "/images/test2.jpg"));
//        when(imageProperties.uploadDir()).thenReturn("/mock/upload/dir");
//        try (var mockedStatic = mockStatic(ImageIO.class)) {
//            mockedStatic.when(() -> ImageIO.read(any(InputStream.class)))
//                    .thenReturn(image);
//            mockedStatic.when(() -> ImageIO.write(any(BufferedImage.class), any(String.class), any(File.class)))
//                    .thenReturn(true);
//            // when
//            var result = imageService.saveImages(multipartFiles).get();
//            // then
//            Assertions.assertThat(result)
//                    .containsExactly("/images/test1.jpg", "/images/test2.jpg");
//        }
//    }
}

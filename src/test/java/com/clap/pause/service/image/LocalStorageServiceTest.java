package com.clap.pause.service.image;

import com.clap.pause.config.properties.ImageProperties;
import com.clap.pause.exception.ImageProcessingFailedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocalStorageServiceTest {

    @InjectMocks
    private LocalStorageService localStorageService;
    @Mock
    private ImageProperties imageProperties;

    @Test
    @DisplayName("정상적인 형식의 이미지를 업로드하면 저장된다.")
    void uploadImage_success() {
        //given
        var file = mock(File.class);
        var path = Paths.get("/mock/upload/dir/test.jpg");

        when(file.getName()).thenReturn("test.jpg");
        when(file.toPath()).thenReturn(path);
        when(imageProperties.uploadDir()).thenReturn("/mock/upload/dir");
        try (var mockedStatic = Mockito.mockStatic(Files.class)) {
            mockedStatic.when(() -> Files.copy(any(Path.class), any(Path.class)))
                    .thenAnswer(result -> null);
            //when
            var result = localStorageService.uploadImage(file);
            //then
            Assertions.assertThat(result)
                    .isEqualTo("/images/test.jpg");
        }
    }

    @Test
    @DisplayName("파일의 Path 가 null 이면 실패한다.")
    void uploadImage_fail_nullPath() {
        //given
        var file = mock(File.class);

        when(file.getName()).thenReturn("test.jpg");
        //when, then
        Assertions.assertThatThrownBy(() -> localStorageService.uploadImage(file))
                .isInstanceOf(ImageProcessingFailedException.class);
    }

    @Test
    @DisplayName("여러 개의 이미지를 업로드하면 각 이미지의 경로를 반환한다.")
    void uploadImages_success() {
        //given
        var files = new ArrayList<File>();
        for (int i = 0; i < 2; i++) {
            var file = mock(File.class);
            var path = Paths.get("/mock/upload/dir/test" + i + ".jpg");

            when(file.getName()).thenReturn("test" + i + ".jpg");
            when(file.toPath()).thenReturn(path);
            files.add(file);
        }

        when(imageProperties.uploadDir()).thenReturn("/mock/upload/dir");
        try (var mockedStatic = Mockito.mockStatic(Files.class)) {
            mockedStatic.when(() -> Files.copy(any(Path.class), any(Path.class)))
                    .thenAnswer(result -> null);
            //when
            var result = localStorageService.uploadImages(files);
            //then
            Assertions.assertThat(result)
                    .containsExactly("/images/test0.jpg", "/images/test1.jpg");
        }
    }
}

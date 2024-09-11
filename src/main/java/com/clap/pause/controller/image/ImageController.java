package com.clap.pause.controller.image;

import com.clap.pause.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService service;

    @GetMapping("/{image}")
    public ResponseEntity<byte[]> getImage(@PathVariable String image) {
        var imageBytes = service.getImage(image);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + image)
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    @PostMapping(value = "/save-image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveImages(@RequestPart MultipartFile file) {
        var image = service.saveImage(file);
        return ResponseEntity.ok(image);
    }

    @PostMapping(value = "/save-images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<String>> saveImages(@RequestPart List<MultipartFile> files) {
        var images = service.saveImages(files);
        return ResponseEntity.ok(images);
    }
}

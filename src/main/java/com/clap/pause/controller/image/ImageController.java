package com.clap.pause.controller.image;

import com.clap.pause.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

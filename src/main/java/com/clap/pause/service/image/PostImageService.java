package com.clap.pause.service.image;

import com.clap.pause.repository.PostImageRepository;
import com.clap.pause.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final ImageService imageService;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    public String saveImage(Long postId, MultipartFile file) {
        var image = imageService.saveImage(file);
        return image;
    }

    public List<String> saveImages(Long postId, List<MultipartFile> files) {
        var images = imageService.saveImages(files);
        return images;
    }
}

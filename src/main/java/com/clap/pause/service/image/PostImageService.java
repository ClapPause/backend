package com.clap.pause.service.image;

import com.clap.pause.exception.ImageProcessingFailedException;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.PostImage;
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

    public void savePostImage(Long postId, MultipartFile file) {
        var image = imageService.saveImage(file);
        image.thenApply(result -> {
            savePostImage(postId, result);
            return result;
        }).exceptionally(e -> {
            throw new ImageProcessingFailedException(e.getMessage());
        });
    }

    public void saveImages(Long postId, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            savePostImage(postId, file);
        }
    }

    private PostImage savePostImage(Long postId, String image) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundElementException(postId + "를 가진 게시글이 존재하지 않습니다."));
        var postImage = new PostImage(post, image);
        return postImageRepository.save(postImage);
    }
}

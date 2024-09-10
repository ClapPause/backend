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
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final ImageService imageService;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    public void savePostImage(Long postId, MultipartFile file) {
        try {
            var image = imageService.saveImage(file).get();
            savePostImageWithImage(postId, image);
        } catch (ExecutionException | InterruptedException exception) {
            throw new ImageProcessingFailedException(exception.getMessage());
        }
    }

    public void savePostImages(Long postId, List<MultipartFile> files) {
        try {
            var images = imageService.saveImages(files).get();
            savePostImagesWithImages(postId, images);
        } catch (ExecutionException | InterruptedException exception) {
            throw new ImageProcessingFailedException(exception.getMessage());
        }
    }

    public void deleteAllByPostId(Long postId) {
        postImageRepository.deleteAllByPostId(postId);
    }

    private void savePostImageWithImage(Long postId, String image) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundElementException(postId + "를 가진 게시글이 존재하지 않습니다."));
        var postImage = new PostImage(post, image);
        postImageRepository.save(postImage);
    }

    private void savePostImagesWithImages(Long postId, List<String> images) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundElementException(postId + "를 가진 게시글이 존재하지 않습니다."));

        for (var image : images) {
            var postImage = new PostImage(post, image);
            postImageRepository.save(postImage);
        }
    }
}

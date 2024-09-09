package com.clap.pause.service.image;

import com.clap.pause.exception.ImageProcessingFailedException;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.PostImage;
import com.clap.pause.repository.PostImageRepository;
import com.clap.pause.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final ImageService imageService;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    public void savePostImage(Long postId, MultipartFile file) {
        imageService.saveImage(file)
                .thenAccept(result -> savePostImageWithImage(postId, result))
                .exceptionally(e -> {
                    throw new ImageProcessingFailedException(e.getMessage());
                });
    }

    public void savePostImages(Long postId, List<MultipartFile> files) {
        var futures = createImageSavingFutures(files);
        waitAllFutures(futures);

        var result = futures.stream()
                .map(CompletableFuture::join)
                .toList();
        savePostImagesWithImages(postId, result);
    }

    private List<CompletableFuture<String>> createImageSavingFutures(List<MultipartFile> files) {
        var futures = new ArrayList<CompletableFuture<String>>();
        for (var file : files) {
            var future = imageService.saveImage(file)
                    .thenApply(result -> result)
                    .exceptionally(e -> {
                        throw new ImageProcessingFailedException(e.getMessage());
                    });
            futures.add(future);
        }
        return futures;
    }

    private void waitAllFutures(List<CompletableFuture<String>> futures) {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .join();
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

package com.clap.pause.service.image;

import com.clap.pause.exception.ImageProcessingFailedException;
import com.clap.pause.model.Post;
import com.clap.pause.model.PostImage;
import com.clap.pause.repository.PostImageRepository;
import com.clap.pause.repository.PostRepository;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final ImageService imageService;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    public void savePostImage(Post post, MultipartFile file) {
        try {
            var image = imageService.saveImage(file).get();
            savePostImageWithImage(post, image);
        } catch (ExecutionException | InterruptedException exception) {
            throw new ImageProcessingFailedException(exception.getMessage());
        }
    }

    public void savePostImages(Post post, List<MultipartFile> files) {
        try {
            var images = imageService.saveImages(files).get();
            savePostImagesWithImages(post, images);
        } catch (ExecutionException | InterruptedException exception) {
            throw new ImageProcessingFailedException(exception.getMessage());
        }
    }

    public List<byte[]> getImages(Post post) {
        var postImages = postImageRepository.findAllByPost(post);
        return postImages.stream()
                .map(postImage -> imageService.getImage(postImage.getImage()))
                .collect(Collectors.toList());
    }

    public void deleteAllByPostId(Long postId) {
        postImageRepository.deleteAllByPostId(postId);
    }

    private void savePostImageWithImage(Post post, String image) {
        var postImage = new PostImage(post, image);
        postImageRepository.save(postImage);
    }

    private void savePostImagesWithImages(Post post, List<String> images) {
        for (var image : images) {
            var postImage = new PostImage(post, image);
            postImageRepository.save(postImage);
        }
    }
}

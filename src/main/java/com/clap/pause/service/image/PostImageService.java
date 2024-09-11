package com.clap.pause.service.image;

import com.clap.pause.model.Post;
import com.clap.pause.model.PostImage;
import com.clap.pause.repository.PostImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostImageService {
    private final PostImageRepository postImageRepository;

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

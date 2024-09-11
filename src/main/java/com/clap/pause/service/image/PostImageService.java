package com.clap.pause.service.image;

import com.clap.pause.dto.postImage.MultiPostImageRequest;
import com.clap.pause.dto.postImage.MultiPostImageResponse;
import com.clap.pause.dto.postImage.PostImageRequest;
import com.clap.pause.dto.postImage.PostImageResponse;
import com.clap.pause.model.Post;
import com.clap.pause.model.PostImage;
import com.clap.pause.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final PostImageRepository postImageRepository;

    public PostImageResponse savePostImage(PostImageRequest postImageRequest) {
        var postImage = savePostImageWithPostImageRequest(postImageRequest);
        return getPostImageResponseWithPostImage(postImage);
    }

    public MultiPostImageResponse saveMultiPostImage(MultiPostImageRequest multiPostImageRequest) {
        var postImages = saveMultiPostImageWithMultiPostImageRequest(multiPostImageRequest);
        return getMultiPostImageResponseWithPostAndPostImages(multiPostImageRequest.post(), postImages);
    }

    private PostImage savePostImageWithPostImageRequest(PostImageRequest postImageRequest) {
        var postImage = new PostImage(postImageRequest.post(), postImageRequest.image());
        return postImageRepository.save(postImage);
    }

    private List<PostImage> saveMultiPostImageWithMultiPostImageRequest(MultiPostImageRequest postImagesRequest) {
        var result = new ArrayList<PostImage>();

        for (var image : postImagesRequest.images()) {
            var postImage = new PostImage(postImagesRequest.post(), image);
            var savedPostImage = postImageRepository.save(postImage);
            result.add(savedPostImage);
        }

        return result;
    }

    private PostImageResponse getPostImageResponseWithPostImage(PostImage postImage) {
        return PostImageResponse.of(postImage.getPost().getId(), postImage.getImage());
    }

    private MultiPostImageResponse getMultiPostImageResponseWithPostAndPostImages(Post post, List<PostImage> postImages) {
        var result = postImages.stream()
                .map(PostImage::getImage)
                .toList();
        return MultiPostImageResponse.of(post.getId(), result);
    }
}

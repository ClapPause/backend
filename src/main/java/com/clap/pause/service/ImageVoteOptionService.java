package com.clap.pause.service;

import com.clap.pause.dto.post.request.ImageVoteOptionRequest;
import com.clap.pause.dto.post.response.ImageVoteOptionResponse;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.ImageVoteOption;
import com.clap.pause.model.Post;
import com.clap.pause.repository.ImageVoteOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageVoteOptionService {
    private final ImageVoteOptionRepository imageVoteOptionRepository;

    /**
     * post로 이미지투표 선택지 리스트를 가져오는 메소드
     *
     * @param post
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ImageVoteOption> getImageVoteOptionList(Post post) {
        return imageVoteOptionRepository.findAllByPost(post);
    }

    /**
     * 이미지 투표 선택지 리스트를 삭제하는 메소드
     *
     * @param imageVoteOptions
     */
    public void deleteAll(List<ImageVoteOption> imageVoteOptions) {
        imageVoteOptionRepository.deleteAll(imageVoteOptions);
    }

    /**
     * 이미지 선택지 리스트 를 저장하는 메소드
     *
     * @param post
     * @param imageVoteOptionRequests
     * @return
     */
    public List<ImageVoteOptionResponse> saveImageVoteOptionList(Post post, List<ImageVoteOptionRequest> imageVoteOptionRequests) {
        return imageVoteOptionRequests.stream()
                .map(imageVoteOptionRequest -> saveImageVoteOption(post, imageVoteOptionRequest))
                .toList();
    }

    /**
     * 이미지 선택지를 저장하는 메소드
     *
     * @param post
     * @param imageVoteOptionRequest
     * @return
     */
    private ImageVoteOptionResponse saveImageVoteOption(Post post, ImageVoteOptionRequest imageVoteOptionRequest) {
        ImageVoteOption imageVoteOption = new ImageVoteOption(post, imageVoteOptionRequest.image(), imageVoteOptionRequest.description());
        ImageVoteOption saved = imageVoteOptionRepository.save(imageVoteOption);
        return new ImageVoteOptionResponse(saved.getId(), saved.getImage(), saved.getDescription());
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public ImageVoteOption getImageVoteOptionById(Long optionId) {
        return imageVoteOptionRepository.findById(optionId)
                .orElseThrow(() -> new NotFoundElementException("해당 이미지 투표 선택지가 존재하지 않습니다."));
    }
}

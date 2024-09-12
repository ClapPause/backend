package com.clap.pause.service;

import com.clap.pause.dto.vote.VoteOptionCount;
import com.clap.pause.dto.vote.VoteRequest;
import com.clap.pause.dto.vote.VoteResponse;
import com.clap.pause.exception.VoteNotAllowException;
import com.clap.pause.model.ImageVoteOption;
import com.clap.pause.model.Post;
import com.clap.pause.model.PostType;
import com.clap.pause.model.TextVoteOption;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VoteService {
    private final ImageVoteHistoryService imageVoteHistoryService;
    private final TextVoteHistoryService textVoteHistoryService;
    private final ImageVoteOptionService imageVoteOptionService;
    private final TextVoteOptionService textVoteOptionService;
    private final PostService postService;

    /**
     * 투표하는 메소드
     *
     * @param voteRequest
     * @param memberId
     */
    public void voteOption(VoteRequest voteRequest, Long memberId) {
        var post = postService.getPostById(voteRequest.postId());
        //텍스트 투표면
        if (post.getPostType().equals(PostType.TEXT_VOTE)) {
            voteTextOption(post, voteRequest.optionId(), memberId);
        }
        //이미지 투표면
        if (post.getPostType().equals(PostType.IMAGE_VOTE)) {
            voteImageOption(post, voteRequest.optionId(), memberId);
        }
    }

    private void voteTextOption(Post post, Long optionId, Long memberId) {
        var textVoteOption = textVoteOptionService.getTextVoteOptionById(optionId);
        if (!textVoteOption.getPost().equals(post)) {
            throw new VoteNotAllowException("해당 글에 맞는 선택지가 아니므로 투표가 불가합니다.");
        }
        textVoteHistoryService.saveTextVoteHistory(textVoteOption, memberId);
    }

    private void voteImageOption(Post post, Long optionId, Long memberId) {
        ImageVoteOption imageVoteOption = imageVoteOptionService.getImageVoteOptionById(optionId);
        if (!imageVoteOption.getPost().equals(post)) {
            throw new VoteNotAllowException("해당 글에 맞는 선택지가 아니므로 투표가 불가합니다.");
        }
        imageVoteHistoryService.saveImageVoteHistory(imageVoteOption, memberId);
    }

    /**
     * 재투표 하는 메소드
     *
     * @param voteRequest
     * @param memberId
     */
    public void revoteOption(VoteRequest voteRequest, Long memberId) {
        //post 조회
        var post = postService.getPostById(voteRequest.postId());
        //텍스트 투표면
        if (post.getPostType().equals(PostType.TEXT_VOTE)) {
            //투표 기록을 지우고
            textVoteHistoryService.deleteTextVoteHistory(post.getId(), memberId);
            //새로운 투표 기록을 만든다
            voteTextOption(post, voteRequest.optionId(), memberId);
        }
        //이미지 투표면
        if (post.getPostType().equals(PostType.IMAGE_VOTE)) {
            //투표 기록을 지우고
            imageVoteHistoryService.deleteImageVoteHistory(post.getId(), memberId);
            //새로운 투표 기록을 만든다
            voteImageOption(post, voteRequest.optionId(), memberId);
        }
    }

    @Transactional(readOnly = true)
    public VoteResponse getVoteResult(Long postId) {
        var post = postService.getPostById(postId);
        if (post.getPostType().equals(PostType.TEXT_VOTE)) {
            List<TextVoteOption> textVoteOptions = textVoteOptionService.getTextVoteOptionList(post);
            return getTextVoteResult(textVoteOptions);
        }
        if (post.getPostType().equals(PostType.IMAGE_VOTE)) {
            List<ImageVoteOption> imageVoteOptions = imageVoteOptionService.getImageVoteOptionList(post);
            return getImageVoteResult(imageVoteOptions);
        }
        throw new VoteNotAllowException("투표 글이 아니므로 투표 조회가 불가합니다");
    }

    private VoteResponse getTextVoteResult(List<TextVoteOption> textVoteOptions) {
        //textVoteOption 마다 voteOptionCount를 생성
        List<VoteOptionCount> voteOptionCounts = textVoteOptions.stream()
                .map(textVoteHistoryService::getCountOfVoteOption)
                .toList();
        //각 voteOptionCount 의 count 총합을 구함
        var totalCount = voteOptionCounts.stream()
                .mapToLong(VoteOptionCount::count)
                .sum();
        return VoteResponse.of(totalCount, voteOptionCounts);
    }

    private VoteResponse getImageVoteResult(List<ImageVoteOption> imageVoteOptions) {
        //imageVoteOption 마다 voteOptionCount를 생성
        List<VoteOptionCount> voteOptionCounts = imageVoteOptions.stream()
                .map(imageVoteHistoryService::getCountOfImageOption)
                .toList();
        //각 voteOptionCount 의 count 총합을 구함
        var totalCount = voteOptionCounts.stream()
                .mapToLong(VoteOptionCount::count)
                .sum();
        return VoteResponse.of(totalCount, voteOptionCounts);
    }
}

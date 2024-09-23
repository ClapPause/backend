package com.clap.pause.service;

import com.clap.pause.dto.vote.VoteOptionCount;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.exception.VoteNotAllowException;
import com.clap.pause.model.ImageVoteHistory;
import com.clap.pause.model.ImageVoteOption;
import com.clap.pause.model.Member;
import com.clap.pause.repository.ImageVoteHistoryRepository;
import com.clap.pause.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageVoteHistoryService {
    private final ImageVoteHistoryRepository imageVoteHistoryRepository;
    private final MemberRepository memberRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveImageVoteHistory(ImageVoteOption imageVoteOption, Long memberId) {
        var member = getMember(memberId);
        if (existsByMember(member)) {
            throw new VoteNotAllowException("이미 투표가 완료되었습니다. 재투표 기능을 이용해주세요.");
        }
        imageVoteHistoryRepository.save(new ImageVoteHistory(imageVoteOption, member));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteImageVoteHistory(Long postId, Long memberId) {
        var member = getMember(memberId);
        if (!existsByMember(member)) {
            throw new VoteNotAllowException("투표 기록이 없어 재투표가 불가합니다.");
        }
        var imageVoteHistories = imageVoteHistoryRepository.findAllByMember(member);
        var imageVoteHistory = findImageVoteHistoryByPostId(imageVoteHistories, postId);
        imageVoteHistoryRepository.deleteById(imageVoteHistory.getId());
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException("해당 멤버가 존재하지 않습니다."));
    }

    private ImageVoteHistory findImageVoteHistoryByPostId(List<ImageVoteHistory> imageVoteHistories, Long postId) {
        for (ImageVoteHistory imageVoteHistory : imageVoteHistories) {
            if (imageVoteHistory.getImageVoteOption().getPost().getId().equals(postId)) {
                return imageVoteHistory;
            }
        }
        throw new VoteNotAllowException("투표 기록이 없어 재투표가 불가합니다.");
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public VoteOptionCount getCountOfImageOption(ImageVoteOption ImageVoteOption) {
        var count = imageVoteHistoryRepository.countByImageVoteOption(ImageVoteOption);
        return VoteOptionCount.of(ImageVoteOption.getId(), count);
    }

    private boolean existsByMember(Member member) {
        return imageVoteHistoryRepository.existsByMember(member);
    }
}

package com.clap.pause.service;

import com.clap.pause.dto.vote.VoteOptionCount;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.exception.VoteNotAllowException;
import com.clap.pause.model.Member;
import com.clap.pause.model.TextVoteHistory;
import com.clap.pause.model.TextVoteOption;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.repository.TextVoteHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TextVoteHistoryService {
    private final TextVoteHistoryRepository textVoteHistoryRepository;
    private final MemberRepository memberRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveTextVoteHistory(TextVoteOption textVoteOption, Long memberId) {
        var member = getMember(memberId);
        if (textVoteHistoryRepository.existsByMember(member)) {
            throw new VoteNotAllowException("이미 투표가 완료되었습니다. 재투표 기능을 이용해주세요.");
        }
        textVoteHistoryRepository.save(new TextVoteHistory(textVoteOption, member));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteTextVoteHistory(Long postId, Long memberId) {
        var member = getMember(memberId);
        if (!existsByMember(member)) {
            throw new VoteNotAllowException("투표 기록이 없어 재투표가 불가합니다.");
        }
        List<TextVoteHistory> textVoteHistories = textVoteHistoryRepository.findAllByMember(member);
        TextVoteHistory textVoteHistory = findTextVoteHistoryByPostId(textVoteHistories, postId);
        textVoteHistoryRepository.deleteById(textVoteHistory.getId());
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException("해당 멤버가 존재하지 않습니다."));
    }

    private TextVoteHistory findTextVoteHistoryByPostId(List<TextVoteHistory> textVoteHistories, Long postId) {
        for (TextVoteHistory textVoteHistory : textVoteHistories) {
            if (textVoteHistory.getTextVoteOption().getPost().getId().equals(postId)) {
                return textVoteHistory;
            }
        }
        throw new VoteNotAllowException("투표 기록이 없어 재투표가 불가합니다.");
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public VoteOptionCount getCountOfVoteOption(TextVoteOption textVoteOption) {
        Long count = textVoteHistoryRepository.countByTextVoteOption(textVoteOption);
        return VoteOptionCount.of(textVoteOption.getId(), count);
    }

    private boolean existsByMember(Member member) {
        return textVoteHistoryRepository.existsByMember(member);
    }
}

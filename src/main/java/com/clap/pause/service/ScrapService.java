package com.clap.pause.service;

import com.clap.pause.dto.scrap.ScrapResponse;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.exception.ScrapFailException;
import com.clap.pause.model.Member;
import com.clap.pause.model.Post;
import com.clap.pause.model.Scrap;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.repository.PostRepository;
import com.clap.pause.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public void scarpPost(Long postId, Long memberId) {
        var post = getPost(postId);
        var member = getMember(memberId);
        if (checkScrap(post, member)) {
            throw new ScrapFailException("이미 스크랩이 되어있습니다");
        }
        scrapRepository.save(new Scrap(post, member));
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundElementException("해당 글이 존재하지 않습니다"));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException("해당 회원이 존재하지 않습니다"));
    }

    @Transactional(readOnly = true)
    public ScrapResponse getScrap(Long postId, Long memberId) {
        var post = getPost(postId);
        var member = getMember(memberId);
        if (!checkScrap(post, member)) {
            return ScrapResponse.of(false);
        }
        return ScrapResponse.of(true);
    }

    public void deleteScrap(Long postId, Long memberId) {
        var post = getPost(postId);
        var member = getMember(memberId);
        var scrap = scrapRepository.findByPostAndMember(post, member)
                .orElseThrow(() -> new NotFoundElementException("해당 스크랩 내역이 존재하지 않아 삭제가 불가능 합니다."));
        scrapRepository.delete(scrap);
    }

    private boolean checkScrap(Post post, Member member) {
        return scrapRepository.existsByPostAndMember(post, member);
    }
}

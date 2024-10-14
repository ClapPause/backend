package com.clap.pause.service;

import com.clap.pause.dto.scrap.ScrapResponse;
import com.clap.pause.exception.FailElementException;
import com.clap.pause.exception.NotFoundElementException;
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

    /**
     * 글을 스크랩 하는 기능
     *
     * @param postId
     * @param memberId
     */
    public void scarpPost(Long postId, Long memberId) {
        var post = getPost(postId);
        var member = getMember(memberId);
        if (checkScrap(post, member)) {
            throw new FailElementException("이미 스크랩이 되어있습니다");
        }
        scrapRepository.save(new Scrap(post, member));
    }

    /**
     * post 엔티티를 가져옴
     *
     * @param postId
     * @return
     */
    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundElementException("해당 글이 존재하지 않습니다"));
    }

    /**
     * member 엔티티를 가져옴
     *
     * @param memberId
     * @return
     */
    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException("해당 회원이 존재하지 않습니다"));
    }

    /**
     * 해당 멤버가 해당 글을 스크랩했지를 조회하는 메소드
     *
     * @param postId
     * @param memberId
     * @return
     */
    @Transactional(readOnly = true)
    public ScrapResponse getScrap(Long postId, Long memberId) {
        var post = getPost(postId);
        var member = getMember(memberId);
        if (!checkScrap(post, member)) {
            return ScrapResponse.of(false);
        }
        return ScrapResponse.of(true);
    }

    /**
     * 스크랩을 삭제하는 기능
     *
     * @param postId
     * @param memberId
     */
    public void deleteScrap(Long postId, Long memberId) {
        var post = getPost(postId);
        var member = getMember(memberId);
        var scrap = scrapRepository.findByPostAndMember(post, member)
                .orElseThrow(() -> new NotFoundElementException("해당 스크랩 내역이 존재하지 않아 삭제가 불가능 합니다."));
        scrapRepository.delete(scrap);
    }

    /**
     * post와 member를 가지고 해당 스크랩이 있는지를 체크하는 메소드
     *
     * @param post
     * @param member
     * @return
     */
    private boolean checkScrap(Post post, Member member) {
        return scrapRepository.existsByPostAndMember(post, member);
    }
}

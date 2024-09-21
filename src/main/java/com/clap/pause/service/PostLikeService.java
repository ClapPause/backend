package com.clap.pause.service;

import com.clap.pause.dto.postlike.PostLikeResponse;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.exception.PostLikeFailException;
import com.clap.pause.model.Member;
import com.clap.pause.model.Post;
import com.clap.pause.model.PostLike;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.repository.PostLikeRepository;
import com.clap.pause.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /**
     * 글에 좋아요를 하는 메소드
     *
     * @param postId
     * @param memberId
     */
    public void like(Long postId, Long memberId) {
        var post = getPost(postId);
        var member = getMember(memberId);
        //이미 좋아요 했는지 체크함
        if (checkPostLikeByMember(post, member)) {
            throw new PostLikeFailException("이미 해당 글을 좋아요한 상태입니다.");
        }
        //존재하지 않으면, postLike를 저장함
        postLikeRepository.save(new PostLike(post, member));
    }

    /**
     * 해당 글의 좋아요 갯수와 멤버가 좋아요 눌렀는지를 반환하는 메소드
     *
     * @param postId
     * @param memberId
     * @return
     */
    @Transactional(readOnly = true)
    public PostLikeResponse getLike(Long postId, Long memberId) {
        var post = getPost(postId);
        var member = getMember(memberId);
        int likeCount = postLikeRepository.countByPost(post);
        boolean liked = checkPostLikeByMember(post, member);
        return PostLikeResponse.of(likeCount, liked);
    }

    /**
     * 해당 멤버가 글에 좋아요가 되어있는지 체크하는 메소드
     *
     * @param post
     * @param member
     * @return
     */
    private boolean checkPostLikeByMember(Post post, Member member) {
        //해당 멤버가 글에 좋아요가 되어있으면 true, 아니면 false
        return postLikeRepository.existsByMemberAndPost(member, post);
    }

    /**
     * 글을 가져오는 메소드
     *
     * @param postId
     * @return
     */
    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundElementException("해당 글을 찾을 수 없습니다."));
    }

    /**
     * 회원을 가져오는 메소드
     *
     * @param memberId
     * @return
     */
    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException("해당 회원를 찾을 수 없습니다."));
    }

    public void dislike(Long postId, Long memberId) {
        var post = getPost(postId);
        var member = getMember(memberId);
        var postLike = postLikeRepository.findByPostAndMember(post, member)
                .orElseThrow(() -> new NotFoundElementException("해당 글과 회원에 해당하는 글 좋아요가 존재하지 않습니다."));
        postLikeRepository.delete(postLike);
    }

}

package com.clap.pause.service;

import com.clap.pause.dto.comment.CommentRequest;
import com.clap.pause.dto.comment.CommentResponse;
import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityInfo;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.Comment;
import com.clap.pause.repository.CommentRepository;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final MemberUniversityDepartmentService memberUniversityDepartmentService;
    private final PostRepository postRepository;

    // 댓글 생성
    public CommentResponse saveComment(Long memberId, CommentRequest commentRequest) {
        var comment = saveCommentWithCommentRequest(memberId, commentRequest);
        return getCommentResponse(memberId, comment);
    }
    // 댓글 수정 - 대댓글 여부 파악
    // 댓글 삭제 - 대댓글 여부 파악
    // 대댓글 생성
    // 대댓글 수정
    // 대댓글 삭제

    private Comment saveCommentWithCommentRequest(Long memberId, CommentRequest commentRequest) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        var post = postRepository.findById(commentRequest.postId())
                .orElseThrow(() -> new NotFoundElementException(commentRequest.postId() + "를 가진 게시글이 존재하지 않습니다."));
        var comment = new Comment(member, post, commentRequest.contents());
        return commentRepository.save(comment);
    }

    private CommentResponse getCommentResponse(Long memberId, Comment comment) {
        var departmentGroupId = comment.getPost()
                .getDepartmentGroup()
                .getId();
        var memberUniversityDepartment = memberUniversityDepartmentService.findProperMemberUniversityDepartment(memberId, departmentGroupId);
        var universityDepartment = memberUniversityDepartment.getUniversityDepartment();
        var memberUniversityInfo = new MemberUniversityInfo(memberUniversityDepartment.getId(), memberUniversityDepartment.getMember().getName(), universityDepartment.getUniversity(), universityDepartment.getDepartment());
        return new CommentResponse(comment.getId(), memberUniversityInfo, comment.getContents(), comment.getCreatedAt());
    }
}

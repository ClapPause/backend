package com.clap.pause.service;

import com.clap.pause.dto.post.DefaultPostRequest;
import com.clap.pause.dto.post.DefaultPostResponse;
import com.clap.pause.exception.InvalidPostTypeException;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.Post;
import com.clap.pause.model.PostType;
import com.clap.pause.repository.DepartmentGroupRepository;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final DepartmentGroupRepository departmentGroupRepository;

    public DefaultPostResponse saveDefaultPost(Long memberId, DefaultPostRequest defaultPostRequest) {
        var post = saveDefaultPostWithDefaultPostRequest(memberId, defaultPostRequest);
        return getDefaultPostResponse(post);
    }

    private Post saveDefaultPostWithDefaultPostRequest(Long memberId, DefaultPostRequest defaultPostRequest) {
        if (!defaultPostRequest.postType().equals(PostType.DEFAULT)) {
            throw new InvalidPostTypeException("잘못된 글 타입입니다.");
        }
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException("존재하지 않는 이용자입니다."));
        var departmentGroup = departmentGroupRepository.findById(defaultPostRequest.departmentGroupId())
                .orElseThrow(() -> new NotFoundElementException("존재하지 않는 학과그룹입니다."));
        var post = new Post(member, departmentGroup, defaultPostRequest.title(), defaultPostRequest.contents(), defaultPostRequest.postCategory(), defaultPostRequest.postType());
        return postRepository.save(post);
    }

    private DefaultPostResponse getDefaultPostResponse(Post post) {
        return DefaultPostResponse.of(post.getId(), post.getTitle(), post.getContents(), post.getPostCategory(), post.getCreatedAt());
    }
}

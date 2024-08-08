package com.clap.pause.service;

import com.clap.pause.dto.post.PostRequest;
import com.clap.pause.dto.post.PostResponse;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.Post;
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

    public PostResponse saveDefaultPost(Long memberId, PostRequest postRequest) {
        var post = savePostWithPostRequest(memberId, postRequest);
        return getPostResponse(post);
    }

    private Post savePostWithPostRequest(Long memberId, PostRequest postRequest) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException("존재하지 않는 이용자입니다."));
        var departmentGroup = departmentGroupRepository.findById(postRequest.departmentGroupId())
                .orElseThrow(() -> new NotFoundElementException("존재하지 않는 학과그룹입니다."));
        var post = new Post(member, departmentGroup, postRequest.title(), postRequest.contents(), postRequest.postCategory(), postRequest.postType());
        return postRepository.save(post);
    }

    private PostResponse getPostResponse(Post post) {
        return PostResponse.of(post.getId(), post.getTitle(), post.getContents(), post.getPostCategory(), post.getPostType(), post.getCreatedAt());
    }
}

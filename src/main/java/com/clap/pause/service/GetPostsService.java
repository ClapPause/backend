package com.clap.pause.service;

import com.clap.pause.dto.post.response.PostListResponse;
import com.clap.pause.exception.PostAccessException;
import com.clap.pause.model.Post;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GetPostsService {
    private final PostService postService;
    private final MemberUniversityDepartmentService memberUniversityDepartmentService;

    /**
     * 모든 게시글 조회 하는 메소드
     *
     * @param departmentGroupId
     * @return
     * @throws PostAccessException
     */
    public List<PostListResponse> getAllPosts(Long departmentGroupId) throws PostAccessException {
        var postList = postService.getAllPosts(departmentGroupId);
        List<PostListResponse> postListRespons = new ArrayList<>();
        for (Post post : postList) {
            var memberUniversityDepartmentResponses =
                    memberUniversityDepartmentService.getMemberUniversityDepartments(post.getMember().getId());
            var response = postService.getMemberInfo(post, memberUniversityDepartmentResponses);
            postListRespons.add(response);
        }
        return postListRespons;
    }
}

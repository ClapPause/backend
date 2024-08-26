package com.clap.pause.service;

import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityDepartmentResponse;
import com.clap.pause.dto.post.request.PostRequest;
import com.clap.pause.dto.post.response.PostListResponse;
import com.clap.pause.dto.post.response.PostResponse;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.exception.PostAccessException;
import com.clap.pause.model.Post;
import com.clap.pause.repository.DepartmentGroupRepository;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.repository.MemberUniversityDepartmentRepository;
import com.clap.pause.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final DepartmentGroupRepository departmentGroupRepository;
    private final MemberUniversityDepartmentRepository memberUniversityDepartmentRepository;

    /**
     * 게시글 저장하는 메소드
     *
     * @param memberId
     * @param postRequest
     * @return postResponse
     */
    public PostResponse saveDefaultPost(Long memberId, PostRequest postRequest, Long departmentGroupId) {
        var post = savePostWithPostRequest(memberId, postRequest, departmentGroupId);
        return getPostResponse(post);
    }

    /**
     * 기본 post 생성
     *
     * @param memberId
     * @param postRequest
     * @return
     */
    private Post savePostWithPostRequest(Long memberId, PostRequest postRequest, Long departmentGroupId) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException("존재하지 않는 이용자입니다."));
        var departmentGroup = departmentGroupRepository.findById(departmentGroupId)
                .orElseThrow(() -> new NotFoundElementException("존재하지 않는 학과그룹입니다."));
        var post = new Post(member, departmentGroup, postRequest.title(), postRequest.contents(),
                postRequest.postCategory(), postRequest.postType());
        return postRepository.save(post);
    }

    /**
     * postResponse 생성
     *
     * @param post
     * @return
     */
    private PostResponse getPostResponse(Post post) {
        return PostResponse.of(post.getId(), post.getTitle(), post.getContents(), post.getPostCategory(),
                post.getPostType(), post.getCreatedAt());
    }

    /**
     * List<Post> 가져옴
     *
     * @param departmentGroupId
     * @return postListResponse
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Post> getAllPosts(Long departmentGroupId) {
//        departmentId로 departmentGroup 조회
        var departmentGroup = departmentGroupRepository.findById(departmentGroupId)
                .orElseThrow(() -> new NotFoundElementException("학과 그룹이 존재하지 않습니다."));
        return postRepository.findByDepartmentGroupOrderByCreatedAtDesc(departmentGroup);
    }

    /**
     * post에 대한 멤버 정보를 불러옴
     *
     * @param post
     * @param responseList
     * @return
     * @throws PostAccessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public PostListResponse getMemberInfo(Post post, List<MemberUniversityDepartmentResponse> responseList)
            throws PostAccessException {
        for (MemberUniversityDepartmentResponse response : responseList) {
            //여러개의 전공 중 현재 게시판의 departmentGroup과 일치하면 PostListResponse를 생성
            if (response.departmentGroupResponse().id().equals(post.getDepartmentGroup().getId())) {
                return getPostResponse(post, response);
            }
        }
        // 일치하는 전공이 없다면 예외를 던짐
        throw new PostAccessException("회원이 해당 학과그룹에 권한이 없어 조회가 불가 합니다.");
    }

    /**
     * PostListResponse 생성
     *
     * @param post
     * @param response
     * @return
     */
    private PostListResponse getPostResponse(Post post, MemberUniversityDepartmentResponse response) {
        return PostListResponse.of(post.getId(), post.getDepartmentGroup().getId(), post.getTitle(), post.getContents(),
                post.getPostCategory(),
                post.getPostType(), post.getCreatedAt(), post.getMember().getName(), response.university(),
                response.department());
    }
}

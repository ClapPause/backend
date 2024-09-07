package com.clap.pause.service;

import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityDepartmentResponse;
import com.clap.pause.dto.post.request.ImageVoteRequest;
import com.clap.pause.dto.post.request.PostRequest;
import com.clap.pause.dto.post.request.TextVoteOptionRequest;
import com.clap.pause.dto.post.request.TextVoteRequest;
import com.clap.pause.dto.post.response.PostListResponse;
import com.clap.pause.dto.post.response.PostResponse;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.exception.PostAccessException;
import com.clap.pause.model.ImageVoteOption;
import com.clap.pause.model.Photo;
import com.clap.pause.model.Post;
import com.clap.pause.model.TextVoteOption;
import com.clap.pause.repository.DepartmentGroupRepository;
import com.clap.pause.repository.ImageVoteOptionRepository;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.repository.PhotoRepository;
import com.clap.pause.repository.PostRepository;
import com.clap.pause.repository.TextVoteOptionRepository;
import com.clap.pause.service.image.ImageService;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final DepartmentGroupRepository departmentGroupRepository;
    private final MemberUniversityDepartmentService memberUniversityDepartmentService;
    private final ImageService imageService;
    private final PhotoRepository photoRepository;
    private final TextVoteOptionRepository textVoteOptionRepository;
    private final ImageVoteOptionRepository imageVoteOptionRepository;

    /**
     * @param memberId
     * @param postRequest
     * @param departmentGroupId
     * @return postResponse
     */
    public PostResponse saveDefaultPost(Long memberId, PostRequest postRequest, Long departmentGroupId, List<MultipartFile> imageFiles) {
        var post = savePostWithPostRequest(memberId, postRequest, departmentGroupId);
        //이미지들이 null이 아니면 이미지 저장
        if (Objects.nonNull(imageFiles)) {
            var imageStrings = imageService.saveImages(imageFiles);
            //이미지를 string으로 변환한 것을 photo 엔티티로 저장
            imageStrings.forEach(image -> savePostPhoto(post, image));
        }
        return getPostResponse(post);
    }

    public PostResponse saveTextVote(Long memberId, TextVoteRequest textVoteRequest, Long departmentGroupId, MultipartFile imageFile) {
        var postRequest = new PostRequest(textVoteRequest.title(), textVoteRequest.contents(), textVoteRequest.postCategory(), textVoteRequest.postType());
        var post = savePostWithPostRequest(memberId, postRequest, departmentGroupId);
        //이미지들이 null이 아니면 이미지 저장
        if (Objects.nonNull(imageFile)) {
            var imageString = imageService.saveImage(imageFile);
            savePostPhoto(post, imageString);
        }
        //텍스트 투표 선택지를 저장
        saveTextOption(post, textVoteRequest.options());
        return getPostResponse(post);
    }

    public PostResponse saveImageVote(Long memberId, ImageVoteRequest imageVoteRequest, Long departmentGroupId, List<MultipartFile> imageFiles) {
        var postRequest = new PostRequest(imageVoteRequest.title(), imageVoteRequest.contents(), imageVoteRequest.postCategory(), imageVoteRequest.postType());
        var post = savePostWithPostRequest(memberId, postRequest, departmentGroupId);
        //이미지들이 null이 아니면 이미지 저장
        if (Objects.nonNull(imageFiles)) {
            List<String> imageUrls = imageService.saveImages(imageFiles);
            saveImageOption(post, imageUrls, imageVoteRequest.descriptions());
        }
        return getPostResponse(post);
    }

    /**
     * 기본 post 생성
     *
     * @return post
     */
    private Post savePostWithPostRequest(Long memberId, PostRequest postRequest, Long departmentGroupId) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException("존재하지 않는 이용자입니다."));
        var departmentGroup = departmentGroupRepository.findById(departmentGroupId)
                .orElseThrow(() -> new NotFoundElementException("존재하지 않는 학과그룹입니다."));
        var post = new Post(member, departmentGroup, postRequest.title(), postRequest.contents(), postRequest.postCategory(), postRequest.postType());
        return postRepository.save(post);
    }

    private void saveTextOption(Post post, List<TextVoteOptionRequest> options) {
        options.forEach(option -> textVoteOptionRepository.save(new TextVoteOption(post, option.textOption())));
    }

    private void saveImageOption(Post post, List<String> imageUrls, List<String> descriptions) {
        IntStream.range(0, imageUrls.size())
                .forEach(i -> imageVoteOptionRepository.save(new ImageVoteOption(post, imageUrls.get(i), descriptions.get(i))));
    }

    /**
     * postResponse 생성
     *
     * @param post
     * @return postResponse
     */
    private PostResponse getPostResponse(Post post) {
        return PostResponse.of(post.getId(), post.getTitle(), post.getContents(), post.getPostCategory(),
                post.getPostType(), post.getCreatedAt());
    }

    /**
     * List<Post> 가져옴
     *
     * @return postListResponse
     */
    public List<PostListResponse> getAllPosts(Long departmentGroupId) throws PostAccessException {
//        departmentId로 departmentGroup 조회
        var departmentGroup = departmentGroupRepository.findById(departmentGroupId)
                .orElseThrow(() -> new NotFoundElementException("학과 그룹이 존재하지 않습니다."));
        var postList = postRepository.findByDepartmentGroupOrderByCreatedAtDesc(departmentGroup);
        //각 post에 대한 멤버정보를 가져옴
        return postList.stream()
                .map(post -> {
                    var memberUniversityDepartmentResponses = memberUniversityDepartmentService.getMemberUniversityDepartments(post.getMember()
                            .getId());
                    try {
                        return getMemberInfo(post, memberUniversityDepartmentResponses);
                    } catch (PostAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

    }

    /**
     * post에 대한 멤버 정보를 불러옴
     *
     * @param post
     * @param responseList
     * @return postListResponse
     * @throws PostAccessException
     */
    private PostListResponse getMemberInfo(Post post, List<MemberUniversityDepartmentResponse> responseList) throws PostAccessException {
        for (MemberUniversityDepartmentResponse response : responseList) {
            //여러개의 전공 중 현재 게시판의 departmentGroup과 일치하면 PostListResponse를 생성
            if (response.departmentGroupResponse().id().equals(post.getDepartmentGroup().getId())) {
                return getPostListResponse(post, response);
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
     * @return postListResponse
     */
    private PostListResponse getPostListResponse(Post post, MemberUniversityDepartmentResponse response) {
        return PostListResponse.of(post.getId(), post.getDepartmentGroup()
                .getId(), post.getTitle(), post.getContents(), post.getPostCategory(), post.getPostType(), post.getCreatedAt(), post.getMember()
                .getName(), response.university(), response.department());
    }

    /**
     * postId로 postResponse 생성하는 메소드
     *
     * @param postId
     * @return postListResponse
     */
    public PostListResponse getPostResponse(Long postId) throws PostAccessException {
        var post = getPost(postId);
        var memberUniversityDepartments = memberUniversityDepartmentService.getMemberUniversityDepartments(post.getMember()
                .getId());
        return getMemberInfo(post, memberUniversityDepartments);
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundElementException("글이 존재하지 않습니다."));
    }

    /**
     * post 수정 하는 메소드 - 제목과 내용만 바꿀 수 있음
     *
     * @param postId
     * @param postRequest
     */
    public void updatePost(Long postId, PostRequest postRequest) {
        var post = getPost(postId);
        post.updatePost(postRequest.title(), postRequest.contents());
        postRepository.save(post);
    }

    /**
     * post 삭제 하는 메소드
     *
     * @param postId
     */
    public void deletePost(Long postId) {
        var post = getPost(postId);
        postRepository.deleteById(post.getId());
    }

    public void savePostPhoto(Post post, String url) {
        photoRepository.save(new Photo(url, post));
    }
}

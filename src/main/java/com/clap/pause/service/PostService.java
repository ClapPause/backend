package com.clap.pause.service;

import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityDepartmentResponse;
import com.clap.pause.dto.post.request.ImageVoteRequest;
import com.clap.pause.dto.post.request.PostRequest;
import com.clap.pause.dto.post.request.TextVoteOptionRequest;
import com.clap.pause.dto.post.request.TextVoteRequest;
import com.clap.pause.dto.post.response.ImageVoteOptionResponse;
import com.clap.pause.dto.post.response.PostIdResponse;
import com.clap.pause.dto.post.response.PostListResponse;
import com.clap.pause.dto.post.response.TextVoteOptionResponse;
import com.clap.pause.dto.postImage.MultiPostImageRequest;
import com.clap.pause.dto.postImage.PostImageRequest;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.exception.PostAccessException;
import com.clap.pause.model.ImageVoteOption;
import com.clap.pause.model.Post;
import com.clap.pause.model.PostCategory;
import com.clap.pause.model.PostType;
import com.clap.pause.model.TextVoteOption;
import com.clap.pause.repository.DepartmentGroupRepository;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.repository.PostRepository;
import com.clap.pause.service.image.PostImageService;
import java.util.List;
import java.util.Objects;
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
    private final MemberUniversityDepartmentService memberUniversityDepartmentService;
    private final ImageVoteOptionService imageVoteOptionService;
    private final TextVoteOptionService textVoteOptionService;
    private final PostImageService postImageService;

    /**
     * @param memberId
     * @param postRequest
     * @param departmentGroupId
     * @return postResponse
     */
    public PostIdResponse saveDefaultPost(Long memberId, PostRequest postRequest, Long departmentGroupId) {
        var post = savePostWithPostRequest(memberId, departmentGroupId, postRequest.title(), postRequest.contents(), postRequest.postCategory(), postRequest.postType());
        //이미지가 존재하면 postImage에 저장
        if (!postRequest.images().isEmpty()) {
            postImageService.saveMultiPostImage(new MultiPostImageRequest(post, postRequest.images()));
        }
        return getPostIdResponse(post);
    }

    /**
     * 텍스트 투표를 저장하는 메소드
     *
     * @param memberId
     * @param textVoteRequest
     * @param departmentGroupId
     * @return
     */
    public PostIdResponse saveTextVote(Long memberId, TextVoteRequest textVoteRequest, Long departmentGroupId) {
        var post = savePostWithPostRequest(memberId, departmentGroupId, textVoteRequest.title(), textVoteRequest.contents(), textVoteRequest.postCategory(), textVoteRequest.postType());
        //이미지들이 null이 아니면 이미지 저장
        if (Objects.nonNull(textVoteRequest.image())) {
            postImageService.savePostImage(new PostImageRequest(post, textVoteRequest.image()));
        }
        //텍스트 투표 선택지를 저장
        saveTextOption(post, textVoteRequest.options());
        return getPostIdResponse(post);
    }

    /**
     * 이미지 투표를 저장
     *
     * @param memberId
     * @param imageVoteRequest
     * @param departmentGroupId
     * @return
     */
    public PostIdResponse saveImageVote(Long memberId, ImageVoteRequest imageVoteRequest, Long departmentGroupId) {
        var post = savePostWithPostRequest(memberId, departmentGroupId, imageVoteRequest.title(), imageVoteRequest.contents(), imageVoteRequest.postCategory(), imageVoteRequest.postType());
        //이미지들이 null이 아니면 이미지 저장
        imageVoteOptionService.saveImageVoteOptionList(post, imageVoteRequest.imageVoteOptionRequests());
        return getPostIdResponse(post);
    }

    /**
     * 기본 post 생성
     *
     * @return post
     */
    private Post savePostWithPostRequest(Long memberId, Long departmentGroupId, String title, String contents, PostCategory postCategory, PostType postType) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException("존재하지 않는 이용자입니다."));
        var departmentGroup = departmentGroupRepository.findById(departmentGroupId)
                .orElseThrow(() -> new NotFoundElementException("존재하지 않는 학과그룹입니다."));
        var post = new Post(member, departmentGroup, title, contents, postCategory, postType);
        return postRepository.save(post);
    }

    /**
     * 텍스트 투표 선택지 저장하는 메소드
     *
     * @param post
     * @param options
     */
    private void saveTextOption(Post post, List<TextVoteOptionRequest> options) {
        options.forEach(option -> textVoteOptionService.save(new TextVoteOption(post, option.textOption())));
    }

    /**
     * postResponse 생성
     *
     * @param post
     * @return postResponse
     */
    private PostIdResponse getPostIdResponse(Post post) {
        return PostIdResponse.of(post.getId());
    }

    /**
     * List<Post> 가져옴
     *
     * @return postListResponse
     */
    @Transactional(readOnly = true)
    public List<PostListResponse> getAllPosts(Long departmentGroupId) {
//        departmentId로 departmentGroup 조회
        var departmentGroup = departmentGroupRepository.findById(departmentGroupId)
                .orElseThrow(() -> new NotFoundElementException("학과 그룹이 존재하지 않습니다."));
        var postList = postRepository.findByDepartmentGroupOrderByCreatedAtDesc(departmentGroup);
        //각 post에 대한 멤버정보를 가져옴
        return postList.stream()
                .map(post -> {
                    var memberUniversityDepartmentResponses = memberUniversityDepartmentService.getMemberUniversityDepartments(post.getMember()
                            .getId());
                    var memberInfo = getMemberInfo(post, memberUniversityDepartmentResponses);
                    return getPostResponseForType(post, memberInfo);
                })
                .toList();
    }

    /**
     * post에 대한 멤버 정보를 불러옴
     *
     * @param post
     * @param responseList
     * @return postListResponse
     * @throws PostAccessException
     */
    private MemberUniversityDepartmentResponse getMemberInfo(Post post, List<MemberUniversityDepartmentResponse> responseList) {
        for (MemberUniversityDepartmentResponse response : responseList) {
            //여러개의 전공 중 현재 게시판의 departmentGroup과 일치하면 PostListResponse를 생성
            if (response.departmentGroupResponse().id().equals(post.getDepartmentGroup().getId())) {
                return response;
            }
        }
        // 일치하는 전공이 없다면 예외를 던짐
        throw new PostAccessException("회원이 해당 학과그룹에 권한이 없어 조회가 불가 합니다.");
    }

    /**
     * 글 타입에 맞게 글 응답 생성
     *
     * @param post
     * @param memberInfo
     * @return
     */

    private PostListResponse getPostResponseForType(Post post, MemberUniversityDepartmentResponse memberInfo) {
        //글 타입이 텍스트 투표 타입이면
        if (post.getPostType().equals(PostType.TEXT_VOTE)) {
            return getTextVotePostResponse(post, memberInfo);
        }//글 타입이 이미지 투표 타입이면
        if (post.getPostType().equals(PostType.IMAGE_VOTE)) {
            return getImageVotePostResponse(post, memberInfo);
        }
        //글 타입이 디폴트이면
        return getDefaultPostResponse(post, memberInfo);
    }

    /**
     * 디폴트 PostListResponse 생성
     *
     * @param post
     * @param response
     * @return postListResponse
     */
    private PostListResponse getDefaultPostResponse(Post post, MemberUniversityDepartmentResponse response) {
        //post 로 이미지를 불러옴
        var images = postImageService.getPostImageWithPost(post);
        return PostListResponse.of(post.getId(), post.getDepartmentGroup().getId(), post.getTitle(), post.getContents(), post.getPostCategory(), post.getPostType(), post.getCreatedAt(), post.getMember().getName(), response.university(), response.department(), images, null, null);
    }

    /**
     * 텍스트 투표 타입의 PostListResponse 생성
     *
     * @param post
     * @param response
     * @return
     */
    private PostListResponse getTextVotePostResponse(Post post, MemberUniversityDepartmentResponse response) {
        List<TextVoteOption> textVoteOptions = textVoteOptionService.getTextVoteOptionList(post);
        //텍스트 투표 선택지를 불러옴
        List<TextVoteOptionResponse> textVoteOptionResponses = textVoteOptions.stream()
                .map(textVoteOption -> {
                    return TextVoteOptionResponse.of(textVoteOption.getId(), textVoteOption.getText());
                })
                .toList();
        //post로 이미지를 불러옴
        var image = postImageService.getPostImageWithPost(post);
        return PostListResponse.of(post.getId(), post.getDepartmentGroup().getId(), post.getTitle(), post.getContents(), post.getPostCategory(), post.getPostType(), post.getCreatedAt(), post.getMember().getName(), response.university(), response.department(), image, textVoteOptionResponses, null);
    }

    /**
     * 이미지 투표 타입의 PostListResponse 생성
     *
     * @param post
     * @param response
     * @return
     */
    private PostListResponse getImageVotePostResponse(Post post, MemberUniversityDepartmentResponse response) {
        List<ImageVoteOption> imageVoteOptions = imageVoteOptionService.getImageVoteOptionList(post);
        //이미지 투표 사진의 부연설명을 불러옴
        List<ImageVoteOptionResponse> imageVoteOptionResponses = imageVoteOptions.stream()
                .map(imageVoteOption -> ImageVoteOptionResponse.of(imageVoteOption.getId(), imageVoteOption.getImage(), imageVoteOption.getDescription()))
                .toList();
        return PostListResponse.of(post.getId(), post.getDepartmentGroup().getId(), post.getTitle(), post.getContents(), post.getPostCategory(), post.getPostType(), post.getCreatedAt(), post.getMember().getName(), response.university(), response.department(), null, null, imageVoteOptionResponses);
    }

    /**
     * postId로 postResponse 생성하는 메소드
     *
     * @param postId
     * @return postListResponse
     */
    @Transactional(readOnly = true)
    public PostListResponse getPost(Long postId) {
        var post = getPostById(postId);
        var memberUniversityDepartments = memberUniversityDepartmentService.getMemberUniversityDepartments(post.getMember().getId());
        var memberInfo = getMemberInfo(post, memberUniversityDepartments);
        return getPostResponseForType(post, memberInfo);
    }

    private Post getPostById(Long postId) {
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
        var post = getPostById(postId);
        post.updatePost(postRequest.title(), postRequest.contents());
        postRepository.save(post);
        //글에 연관된 postImage을 지우고 다시 저장
        postImageService.deleteAllByPost(post);
        postImageService.saveMultiPostImage(new MultiPostImageRequest(post, postRequest.images()));
    }

    /**
     * 텍스트 투표 글을 수정하는 메소드
     *
     * @param postId
     * @param textVoteRequest
     */
    public void updateTextVote(Long postId, TextVoteRequest textVoteRequest) {
        var post = getPostById(postId);
        post.updatePost(textVoteRequest.title(), textVoteRequest.contents());
        var textVoteOptions = textVoteOptionService.getTextVoteOptionList(post);
        //기존 선택지들을 지움
        textVoteOptionService.deleteTextVoteOptionList(textVoteOptions);
        //새로운 선택지들을 저장함
        saveTextOption(post, textVoteRequest.options());
        //글에 연관된 postImage을 지우고 다시 저장
        postImageService.deleteAllByPost(post);
        postImageService.savePostImage(new PostImageRequest(post, textVoteRequest.image()));
    }

    /**
     * 이미지 투표 글을 수정하는 메소드
     *
     * @param postId
     * @param imageVoteRequest
     */
    public void updateImageVote(Long postId, ImageVoteRequest imageVoteRequest) {
        var post = getPostById(postId);
        post.updatePost(imageVoteRequest.title(), imageVoteRequest.contents());
        List<ImageVoteOption> imageVoteOptions = imageVoteOptionService.getImageVoteOptionList(post);
        //추후 수정 필요
        //기존 선택지들을 지움
        imageVoteOptionService.deleteAll(imageVoteOptions);
        //새로운 선택지들을 저장함
        imageVoteOptionService.saveImageVoteOptionList(post, imageVoteRequest.imageVoteOptionRequests());
    }

    /**
     * post 삭제 하는 메소드
     *
     * @param postId
     */
    public void deletePost(Long postId) {
        //post 삭제
        var post = getPostById(postId);
        //글타입이 이미지 투표면 imageVoteOption을 삭제
        if (post.getPostType().equals(PostType.IMAGE_VOTE)) {
            deleteImageVoteOption(post);
        }
        //글타입이 텍스트 투표면 textVoteOption을 삭제
        if (post.getPostType().equals(PostType.TEXT_VOTE)) {
            deleteTextVoteOption(post);
        }
        //글의 이미지를 삭제
        postImageService.deleteAllByPost(post);
        postRepository.deleteById(post.getId());
    }

    /**
     * 이미지 투표 선택지를 삭제하는 메소드
     *
     * @param post
     */
    private void deleteImageVoteOption(Post post) {
        List<ImageVoteOption> imageVoteOptions = imageVoteOptionService.getImageVoteOptionList(post);
        imageVoteOptionService.deleteAll(imageVoteOptions);
    }

    /**
     * 텍스트 투표 선택지를 삭제하는 메소드
     *
     * @param post
     */
    private void deleteTextVoteOption(Post post) {
        List<TextVoteOption> textVoteOptions = textVoteOptionService.getTextVoteOptionList(post);
        textVoteOptionService.deleteTextVoteOptionList(textVoteOptions);
    }
}

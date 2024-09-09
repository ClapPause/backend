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
import com.clap.pause.model.PostType;
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

    /**
     * 텍스트 투표를 저장하는 메소드
     *
     * @param memberId
     * @param textVoteRequest
     * @param departmentGroupId
     * @param imageFile
     * @return
     */
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

    /**
     * 이미지 투표를 저장
     *
     * @param memberId
     * @param imageVoteRequest
     * @param departmentGroupId
     * @param imageFiles
     * @return
     */
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

    /**
     * 텍스트 투표 선택지 저장하는 메소드
     *
     * @param post
     * @param options
     */
    private void saveTextOption(Post post, List<TextVoteOptionRequest> options) {
        options.forEach(option -> textVoteOptionRepository.save(new TextVoteOption(post, option.textOption())));
    }

    /**
     * 이미지 투표 선택지 저장하는 메소드
     *
     * @param post
     * @param imageUrls
     * @param descriptions
     */
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
        return PostListResponse.of(post.getId(), post.getDepartmentGroup()
                .getId(), post.getTitle(), post.getContents(), post.getPostCategory(), post.getPostType(), post.getCreatedAt(), post.getMember()
                .getName(), response.university(), response.department(), null);
    }

    /**
     * 텍스트 투표 타입의 PostListResponse 생성
     *
     * @param post
     * @param response
     * @return
     */
    private PostListResponse getTextVotePostResponse(Post post, MemberUniversityDepartmentResponse response) {
        var textVoteOptions = textVoteOptionRepository.findAllByPost(post)
                .orElseThrow(() -> new NotFoundElementException("해당 글의 텍스트 투표가 존재하지 않습니다."));
        List<String> texts = textVoteOptions.stream().
                map(TextVoteOption::getText)
                .collect(Collectors.toList());
        return PostListResponse.of(post.getId(), post.getDepartmentGroup()
                .getId(), post.getTitle(), post.getContents(), post.getPostCategory(), post.getPostType(), post.getCreatedAt(), post.getMember()
                .getName(), response.university(), response.department(), texts);
    }

    /**
     * 이미지 투표 타입의 PostListResponse 생성
     *
     * @param post
     * @param response
     * @return
     */
    private PostListResponse getImageVotePostResponse(Post post, MemberUniversityDepartmentResponse response) {
        List<ImageVoteOption> imageVoteOptions = imageVoteOptionRepository.findAllByPost(post)
                .orElseThrow(() -> new NotFoundElementException("해당 글의 이미지 투표가 존재하지 않습니다."));
        List<String> descriptions = imageVoteOptions.stream().
                map(ImageVoteOption::getDescription)
                .collect(Collectors.toList());
        return PostListResponse.of(post.getId(), post.getDepartmentGroup()
                .getId(), post.getTitle(), post.getContents(), post.getPostCategory(), post.getPostType(), post.getCreatedAt(), post.getMember()
                .getName(), response.university(), response.department(), descriptions);
    }

    /**
     * postId로 postResponse 생성하는 메소드
     *
     * @param postId
     * @return postListResponse
     */
    public PostListResponse getPost(Long postId) {
        var post = getPostById(postId);
        var memberUniversityDepartments = memberUniversityDepartmentService.getMemberUniversityDepartments(post.getMember()
                .getId());
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
        var textVoteOptions = textVoteOptionRepository.findAllByPost(post)
                .orElseThrow(() -> new NotFoundElementException("텍스트 투표 선택지가 존재하지 않습니다."));
        //기존 선택지들을 지움
        textVoteOptionRepository.deleteAll(textVoteOptions);
        //새로운 선택지들을 저장함
        saveTextOption(post, textVoteRequest.options());
    }

    /**
     * 이미지 투표 글을 수정하는 메소드
     *
     * @param postId
     * @param imageVoteRequest
     * @param imageFiles
     */
    public void updateImageVote(Long postId, ImageVoteRequest imageVoteRequest, List<MultipartFile> imageFiles) {
        var post = getPostById(postId);
        post.updatePost(imageVoteRequest.title(), imageVoteRequest.contents());
        var imageVoteOptions = imageVoteOptionRepository.findAllByPost(post)
                .orElseThrow(() -> new NotFoundElementException("이미지 투표 선택지가 존재하지 않습니다."));
        //기존 선택지 사진을 지움

        //새로운 선택지 사진을 저장함
        var images = imageService.saveImages(imageFiles);
        //기존 선택지들을 지움
        imageVoteOptionRepository.deleteAll(imageVoteOptions);
        //새로운 선택지들을 저장함
        saveImageOption(post, images, imageVoteRequest.descriptions());
    }

    /**
     * post 삭제 하는 메소드
     *
     * @param postId
     */
    public void deletePost(Long postId) {
        //post 삭제
        var post = getPostById(postId);
        postRepository.deleteById(post.getId());
        //글타입이 이미지 투표면 imageVoteOption을 삭제
        if (post.getPostType().equals(PostType.IMAGE_VOTE)) {
            deleteImageVoteOption(post);
        }
        //글타입이 텍스트 투표면 textVoteOption을 삭제
        if (post.getPostType().equals(PostType.TEXT_VOTE)) {
            deleteTextVoteOption(post);
        }
    }

    /**
     * photo를 저장하는 메소드
     *
     * @param post
     * @param url
     */
    private void savePostPhoto(Post post, String url) {
        photoRepository.save(new Photo(url, post));
    }

    /**
     * 이미지 투표 선택지를 삭제하는 메소드
     *
     * @param post
     */
    private void deleteImageVoteOption(Post post) {
        var imageVoteOptions = imageVoteOptionRepository.findAllByPost(post)
                .orElseThrow(() -> new NotFoundElementException("이미지 투표 선택지가 존재하지 않습니다."));
        imageVoteOptions.forEach(option -> imageVoteOptionRepository.deleteById(option.getId()));
    }

    /**
     * 텍스트 투표 선택지를 삭제하는 메소드
     *
     * @param post
     */
    private void deleteTextVoteOption(Post post) {
        var textVoteOptions = textVoteOptionRepository.findAllByPost(post)
                .orElseThrow(() -> new NotFoundElementException("텍스트 투표 선택지가 존재하지 않습니다."));
        textVoteOptions.forEach(option -> textVoteOptionRepository.deleteById(option.getId()));
    }
}

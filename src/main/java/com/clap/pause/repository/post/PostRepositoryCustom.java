package com.clap.pause.repository.post;

import com.clap.pause.dto.post.response.PostListResponse;
import java.util.List;

public interface PostRepositoryCustom {
    List<PostListResponse> getPostListsByDepartmentGroup(Long departmentGroupId);
}

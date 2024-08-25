package com.clap.pause.repository.post;

import com.clap.pause.model.Post;
import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getPostListsByDepartmentGroup(Long departmentGroupId);
}

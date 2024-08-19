package com.clap.pause.repository.post;

import static com.clap.pause.model.QDepartmentGroup.departmentGroup;
import static com.clap.pause.model.QMemberUniversityDepartment.memberUniversityDepartment;
import static com.clap.pause.model.QPost.post;
import static com.clap.pause.model.QUniversityDepartment.universityDepartment;

import com.clap.pause.dto.post.response.PostListResponse;
import com.clap.pause.model.PostType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    @Override
    public List<PostListResponse> getPostListsByDepartmentGroup(Long departmentGroupId) {
        return queryFactory.select(Projections.constructor(PostListResponse.class,
                        post.id,
                        post.title,
                        post.contents,
                        post.postCategory,
                        post.postType,
                        post.createdAt,
                        post.member.name,
                        universityDepartment.university))
                .from(post)
                .join(post.departmentGroup, departmentGroup)
                .join(post.member, memberUniversityDepartment.member)
                .join(universityDepartment, memberUniversityDepartment.universityDepartment)
                .where(departmentGroup.id.eq(departmentGroupId)
                        .and(post.postType.eq(PostType.DEFAULT)))
                .fetch();


    }
}

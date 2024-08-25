package com.clap.pause.repository.post;

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
                .join(memberUniversityDepartment).on(post.member.eq(memberUniversityDepartment.member))
                .join(universityDepartment).on(memberUniversityDepartment.universityDepartment.eq(universityDepartment))
                //post type이 default이고 deleted가 false 임
                .where(post.departmentGroup.id.eq(departmentGroupId)
                        .and(post.postType.eq(PostType.DEFAULT))
                        .and(post.deleted.eq(false)))
                //최신 시간 순
                .orderBy(post.createdAt.desc())
                .fetch();
    }
}

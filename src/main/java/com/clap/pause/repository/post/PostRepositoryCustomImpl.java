package com.clap.pause.repository.post;

import static com.clap.pause.model.QPost.post;

import com.clap.pause.model.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> getPostListsByDepartmentGroup(Long departmentGroupId) {
        return queryFactory.selectFrom(post)
                //post type이 default이고 deleted가 false 임
                .where(post.departmentGroup.id.eq(departmentGroupId)
                        .and(post.deleted.eq(false)))
                //최신 시간 순
                .orderBy(post.createdAt.desc())
                .fetch();


    }
}

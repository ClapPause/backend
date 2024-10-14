package com.clap.pause.repository.custom;

import com.clap.pause.model.Post;
import com.clap.pause.model.QPost;
import com.clap.pause.model.QPostLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getTop3HotPostByLike() {
        var post = QPost.post;
        var postLike = QPostLike.postLike;
        return jpaQueryFactory.selectFrom(post)
                .leftJoin(postLike).on(post.eq(postLike.post))
                .where(postLike.createdAt.between(LocalDateTime.now().minusDays(3), LocalDateTime.now()))
                .groupBy(post)
                .orderBy(postLike.count().desc(), post.createdAt.desc())
                .limit(3)
                .fetch();
    }

    @Override
    public List<Post> getHotPostsByCreatedAt() {
        QPost post = QPost.post;

        return jpaQueryFactory.selectFrom(post)
                .where(post.hotPosted.eq(true))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Post> getHotPostsByLike() {
        var post = QPost.post;
        var postLike = QPostLike.postLike;

        return jpaQueryFactory.selectFrom(post)
                .leftJoin(postLike).on(postLike.post.eq(post))
                .where(post.hotPosted.eq(true))
                .groupBy(post)
                .orderBy(postLike.count().desc(), post.createdAt.desc())
                .fetch();
    }

}

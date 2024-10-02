package com.clap.pause.repository.custom;

import com.clap.pause.model.Post;
import com.clap.pause.model.QBestPost;
import com.clap.pause.model.QPost;
import com.clap.pause.model.QPostLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BestPostRepositoryCustomImpl implements BestPostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getBestPostsByCreatedAt() {
        var bestPost = QBestPost.bestPost;
        var post = QPost.post;

        return jpaQueryFactory.select(post)
                .from(bestPost)
                .join(post).on(post.eq(bestPost.post))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Post> getBestPostsByLike() {
        var bestPost = QBestPost.bestPost;
        var post = QPost.post;
        var postLike = QPostLike.postLike;

        return jpaQueryFactory.select(post)
                .from(bestPost)
                .join(post).on(post.eq(bestPost.post))
                .leftJoin(postLike).on(postLike.post.eq(post))
                .groupBy(post)
                .orderBy(postLike.count().desc())
                .fetch();
    }

    @Override
    public Post getBestPostByCreatedAtDesc() {
        var bestPost = QBestPost.bestPost;
        var post = QPost.post;

        return jpaQueryFactory.select(post)
                .from(bestPost)
                .join(post).on(post.eq(bestPost.post))
                .orderBy(bestPost.createdAt.desc())
                .fetchFirst();
    }

}

package com.clap.pause.repository.custom;

import com.clap.pause.dto.commentLike.CommentLikeQueryResult;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.QCommentLike;
import com.clap.pause.model.QPost;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CommentLikeCustomRepositoryImpl implements CommentLikeCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentLikeQueryResult> getCommentLikeQueryResults(Long postId) {
        postValidation(postId);
        var commentLike = QCommentLike.commentLike;

        return queryFactory
                .select(Projections.constructor(
                        CommentLikeQueryResult.class,
                        commentLike.comment.id,
                        commentLike.comment.id.count()
                ))
                .from(commentLike)
                .where(commentLike.comment.post.id.eq(postId))
                .groupBy(commentLike.comment.id)
                .fetch();
    }

    private void postValidation(Long postId) {
        var post = QPost.post;
        var result = queryFactory.select()
                .from(post)
                .where(post.id.eq(postId))
                .fetch();
        if (result.isEmpty()) {
            throw new NotFoundElementException("존재하지 않는 게시글에 대한 요청입니다.");
        }
    }
}

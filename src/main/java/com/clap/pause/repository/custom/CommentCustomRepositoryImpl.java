package com.clap.pause.repository.custom;

import com.clap.pause.dto.comment.CommentQueryResult;
import com.clap.pause.model.QComment;
import com.clap.pause.model.QCommentLike;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentQueryResult> getCommentQueryResults(Long postId) {
        var comment = QComment.comment;
        var commentLike = QCommentLike.commentLike;

        return queryFactory
                .select(Projections.constructor(
                        CommentQueryResult.class,
                        comment,
                        JPAExpressions
                                .select(commentLike.id.count())
                                .from(commentLike)
                                .where(commentLike.comment.id.eq(comment.id))
                ))
                .from(comment)
                .where(comment.post.id.eq(postId))
                .orderBy(comment.createdAt.asc())
                .fetch();
    }
}

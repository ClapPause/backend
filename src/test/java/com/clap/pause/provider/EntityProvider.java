package com.clap.pause.provider;

import com.clap.pause.model.Comment;
import com.clap.pause.model.DepartmentGroup;
import com.clap.pause.model.DepartmentType;
import com.clap.pause.model.Gender;
import com.clap.pause.model.Member;
import com.clap.pause.model.MemberUniversityDepartment;
import com.clap.pause.model.Post;
import com.clap.pause.model.PostCategory;
import com.clap.pause.model.PostType;
import com.clap.pause.model.UniversityDepartment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

public class EntityProvider {

    private final static Long ID = 1000L;

    public static Member getMember(Long id) {
        var member = spy(new Member("이름", "010-1234-1234", "비밀번호", LocalDate.of(1999, 1, 16), Gender.MALE, "학생"));
        lenient().when(member.getId()).thenReturn(id);
        lenient().when(member.getCreatedAt()).thenReturn(LocalDateTime.now());
        lenient().when(member.getLastModifiedAt()).thenReturn(LocalDateTime.now());
        return member;
    }

    public static DepartmentGroup getDepartmentGroup(Long id) {
        var departmentGroup = spy(new DepartmentGroup("학과그룹"));
        lenient().when(departmentGroup.getId()).thenReturn(id);
        lenient().when(departmentGroup.getCreatedAt()).thenReturn(LocalDateTime.now());
        lenient().when(departmentGroup.getLastModifiedAt()).thenReturn(LocalDateTime.now());
        return departmentGroup;
    }

    public static UniversityDepartment getUniversityDepartment(Long id) {
        var departmentGroup = getDepartmentGroup(ID);
        var universityDepartment = spy(new UniversityDepartment(departmentGroup, "대학교", "학과"));
        lenient().when(universityDepartment.getId()).thenReturn(id);
        lenient().when(universityDepartment.getCreatedAt()).thenReturn(LocalDateTime.now());
        lenient().when(universityDepartment.getLastModifiedAt()).thenReturn(LocalDateTime.now());
        return universityDepartment;
    }

    public static MemberUniversityDepartment getMemberUniversityDepartment(Long id) {
        var member = getMember(ID);
        var universityDepartment = getUniversityDepartment(ID);
        var memberUniversityDepartment = spy(new MemberUniversityDepartment(member, universityDepartment, DepartmentType.MAJOR));
        lenient().when(memberUniversityDepartment.getId()).thenReturn(id);
        lenient().when(memberUniversityDepartment.getCreatedAt()).thenReturn(LocalDateTime.now());
        lenient().when(memberUniversityDepartment.getLastModifiedAt()).thenReturn(LocalDateTime.now());
        return memberUniversityDepartment;
    }


    public static Post getPost(Long id) {
        var member = getMember(ID);
        var departmentGroup = getDepartmentGroup(ID);
        var post = spy(new Post(member, departmentGroup, "제목", "내용", PostCategory.FREEDOM, PostType.DEFAULT));
        lenient().when(post.getId()).thenReturn(id);
        lenient().when(post.getCreatedAt()).thenReturn(LocalDateTime.now());
        lenient().when(post.getLastModifiedAt()).thenReturn(LocalDateTime.now());
        return post;
    }

    public static Comment getComment(Long id) {
        var member = getMember(ID);
        var post = getPost(ID);
        var comment = spy(new Comment(member, post, "댓글"));
        lenient().when(comment.getId()).thenReturn(id);
        lenient().when(comment.getCreatedAt()).thenReturn(LocalDateTime.now());
        lenient().when(comment.getLastModifiedAt()).thenReturn(LocalDateTime.now());
        return comment;
    }

    public static Comment getComment(Long id, Long parentCommentId) {
        var member = getMember(ID);
        var post = getPost(ID);
        var parentComment = getComment(parentCommentId);
        var comment = spy(new Comment(member, post, parentComment, "대댓글"));
        lenient().when(comment.getId()).thenReturn(id);
        lenient().when(comment.getCreatedAt()).thenReturn(LocalDateTime.now());
        lenient().when(comment.getLastModifiedAt()).thenReturn(LocalDateTime.now());
        return comment;
    }
}

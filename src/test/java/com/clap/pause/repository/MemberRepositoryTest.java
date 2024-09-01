package com.clap.pause.repository;

import com.clap.pause.model.Gender;
import com.clap.pause.model.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원을 저장하면 ID, CreatedAt 등이 함께 저장된다.")
    void save_success() {
        //given
        var member = getMember();
        //when
        var savedMember = memberRepository.save(member);
        // Then
        Assertions.assertThat(savedMember.getId())
                .isNotNull();
        Assertions.assertThat(savedMember.getCreatedAt())
                .isNotNull();
    }

    @Test
    @DisplayName("존재하는 이메일로 회원을 찾으면 성공한다.")
    void findByEmail_success() {
        //given
        var member = getMember();
        memberRepository.save(member);
        //when
        var foundMember = memberRepository.findByEmail("test@naver.com");
        // Then
        Assertions.assertThat(foundMember)
                .isPresent();
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 회원을 찾으면 실패한다.")
    void findByEmail_fail_notExists() {
        //given, when
        var foundMember = memberRepository.findByEmail("noExistEmail");
        // Then
        Assertions.assertThat(foundMember)
                .isNotPresent();
    }

    private Member getMember() {
        return new Member("테스트", "test@naver.com", "tessPassword", LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234-1234");
    }
}

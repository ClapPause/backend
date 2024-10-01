package com.clap.pause.service;

import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * Member 를 삭제하는 메서드
     *
     * @param memberId
     */
    public void deleteMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundElementException("존재하지 않는 이용자의 ID 입니다.");
        }
        memberRepository.deleteById(memberId);
    }

    /**
     * 중복된 이메일인지 확인하는 메서드
     *
     * @param email
     */
    public Boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    /**
     * 중복된 닉네임인지 확인하는 메서드
     *
     * @param name
     */
    public Boolean existsByName(String name) {
        return memberRepository.existsByName(name);
    }
}

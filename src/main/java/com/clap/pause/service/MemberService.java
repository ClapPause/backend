package com.clap.pause.service;

import com.clap.pause.exception.MemberNotFoundException;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.Member;
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
     * 멤버 id로 멤버 찾는 메소드
     *
     * @param memberId
     * @return member
     */
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException("해당 멤버가 존재하지 않습니다.")
        );
    }
}

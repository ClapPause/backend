package com.clap.pause.controller;

import com.clap.pause.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @DeleteMapping
    public ResponseEntity<Void> deleteMember() {
        memberService.deleteMember(getMemberId());
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/validate-phone-number")
    public ResponseEntity<Boolean> existsByPhoneNumber(@RequestParam String phoneNumber) {
        var result = memberService.existsByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/validate-name")
    public ResponseEntity<Boolean> existsByName(@RequestParam String name) {
        var result = memberService.existsByName(name);
        return ResponseEntity.ok(result);
    }

    private Long getMemberId() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}

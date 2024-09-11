package com.clap.pause.service;

import com.clap.pause.repository.TextVoteHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TextVoteHistoryService {
    private final TextVoteHistoryRepository textVoteHistoryRepository;
}

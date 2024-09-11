package com.clap.pause.service;

import com.clap.pause.repository.ImageVoteHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageVoteHistoryService {
    private final ImageVoteHistoryRepository imageVoteHistoryRepository;
}

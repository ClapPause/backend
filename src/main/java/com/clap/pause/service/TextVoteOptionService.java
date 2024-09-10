package com.clap.pause.service;

import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.Post;
import com.clap.pause.model.TextVoteOption;
import com.clap.pause.repository.TextVoteOptionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TextVoteOptionService {
    private final TextVoteOptionRepository textVoteOptionRepository;

    /**
     * 텍스트 투표 선택지를 저장하는 메소드
     *
     * @param textVoteOption
     */
    public void save(TextVoteOption textVoteOption) {
        textVoteOptionRepository.save(textVoteOption);
    }

    /**
     * 텍스트 투표 선택지 리스트를 가져오는 메소드
     *
     * @param post
     * @return
     */
    public List<TextVoteOption> getTextVoteOptionList(Post post) {
        return textVoteOptionRepository.findAllByPost(post)
                .orElseThrow(() -> new NotFoundElementException("해당 글의 텍스트 투표가 존재하지 않습니다."));
    }

    /**
     * 텍스트 투표 선택지 리스트를 삭제하는 메소드
     *
     * @param textVoteOptions
     */
    public void deleteTextVoteOptionList(List<TextVoteOption> textVoteOptions) {
        textVoteOptionRepository.deleteAll(textVoteOptions);
    }
}

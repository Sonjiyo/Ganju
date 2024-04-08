package kr.ganjuproject.service;

import kr.ganjuproject.entity.Board;
import kr.ganjuproject.entity.Category;
import kr.ganjuproject.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.ganjuproject.entity.RoleCategory.REPORT;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    public List<Board> getReortList(){ return boardRepository.findByBoardCategory(REPORT); }
}

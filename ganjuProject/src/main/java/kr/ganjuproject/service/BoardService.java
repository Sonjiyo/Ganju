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
    
    // 메인 메뉴에서 비동기로 공지사항만 가져갈때 쓰는 메서드
    public List<Board> noticeGetList() { return boardRepository.noticeGetList(); }
    public List<Board> getReortList(){ return boardRepository.findByBoardCategory(REPORT); }
}

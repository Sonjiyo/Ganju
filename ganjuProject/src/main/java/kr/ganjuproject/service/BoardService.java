package kr.ganjuproject.service;

import kr.ganjuproject.entity.Board;
import kr.ganjuproject.entity.Category;
import kr.ganjuproject.entity.Review;
import kr.ganjuproject.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public List<Board> noticeGetList(int page) {
        int num = 5;
        PageRequest pageRequest = PageRequest.of(page, num, Sort.by("id").descending());
        Page<Board> boards = boardRepository.findAll(pageRequest);
        return boards.getContent();
    }
    // 다 불러오기
    public List<Board> findAll(){
        return boardRepository.findAll();
    }
    public List<Board> getReortList(){ return boardRepository.findByBoardCategory(REPORT); }
}

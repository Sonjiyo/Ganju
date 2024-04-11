package kr.ganjuproject.service;

import kr.ganjuproject.dto.BoardDTO;
import kr.ganjuproject.entity.*;
import kr.ganjuproject.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.ganjuproject.entity.RoleCategory.REPORT;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    
    // 메인 메뉴에서 비동기로 공지사항만 가져갈때 쓰는 메서드
    public List<BoardDTO> noticeGetList(Long restaurantId, int page) {

        int num = 5;
        PageRequest pageRequest = PageRequest.of(page, num, Sort.by("regDate").descending());

        // NOTICE 카테고리에 해당하는 Board 엔티티만 조회
        Page<Board> boards = boardRepository.findByRestaurantIdAndBoardCategory(restaurantId,RoleCategory.NOTICE, pageRequest);

        // Board 엔티티 리스트를 BoardDTO 리스트로 변환
        return boards.getContent().stream().map(this::convertToDTO).collect(Collectors.toList());

    }
    // 해당 식당의 공지글 사이즈 가져가는
    public Long getNoticeCountForRestaurant(Long restaurantId) {
        return boardRepository.countByRestaurantIdAndBoardCategory(restaurantId, RoleCategory.NOTICE);
    }

    // 다 불러오기
    public List<Board> findAll(){
        return boardRepository.findAll();
    }
    public List<Board> getReortList(){
        List<Board> reportList = boardRepository.findByBoardCategory(REPORT);
        Collections.reverse(reportList); // 리스트를 역순으로 정렬
        return reportList;
    }

    public Board getOneBoard(Long id){
        return boardRepository.findById(id).orElse(null);
    }

    @Transactional
    public Board acceptReport(Board board){
        board.setTitle("accept");
        return boardRepository.save(board);
    }

    @Transactional
    public void deleteBoard(Long id){boardRepository.deleteById(id);}

    public int getReortAcceptList(Restaurant restaurant){
        return boardRepository.findByBoardCategoryAndTitleAndRestaurant(REPORT, "accept", restaurant).size();
    }

    private BoardDTO convertToDTO(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        boardDTO.setName(board.getName());
        boardDTO.setTitle(board.getTitle());
        boardDTO.setContent(board.getContent());
        boardDTO.setRegDate(board.getRegDate());
        // Enum 타입을 String으로 변환하여 설정
        boardDTO.setBoardCategory(board.getBoardCategory().name());
        return boardDTO;
    }

}

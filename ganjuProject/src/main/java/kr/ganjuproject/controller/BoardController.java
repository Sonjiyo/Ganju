package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.dto.BoardDTO;
import kr.ganjuproject.entity.Board;
import kr.ganjuproject.entity.Category;
import kr.ganjuproject.entity.Menu;
import kr.ganjuproject.entity.RoleCategory;
import kr.ganjuproject.service.BoardService;
import kr.ganjuproject.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final RestaurantService restaurantService;
    private final BoardService boardService;
    // 비동기로 데이터 보내기
    @GetMapping("/validateMenuBoard")
    @ResponseBody
    public ResponseEntity<Map<String, Object>>  validateBoard(@RequestParam(defaultValue = "0") int page) {
        System.out.println("비동기 공지");
        Map<String, Object> response = new HashMap<>();
        List<BoardDTO> boards = boardService.noticeGetList(1L, page);

        response.put("boards", boards);
        // 전체 게시글 수를 가져와서 응답에 포함시킵니다.
        response.put("size", boardService.getNoticeCountForRestaurant(1L)); // 이 부분이 수정되었습니다.
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{keyId}")
    public @ResponseBody String deleteMemberAjax(@PathVariable Long keyId){
        log.trace("keyId={}" , keyId);
        boardService.deleteBoard(keyId);
        return "ok";
    }

    @PutMapping("/report/{keyId}")
    public @ResponseBody String acceptReport(@PathVariable Long keyId){
        log.trace("keyId={}" , keyId);
        boardService.acceptReport(boardService.getOneBoard(keyId));
        return "ok";
    }

    @PostMapping("/validUserReport")
    @ResponseBody
    public ResponseEntity<?> saveReport(@RequestBody String content, HttpSession session) {
        // content를 사용하여 DB에 저장하는 로직 구현
        // 예를 들어, Report 엔티티를 생성하고 저장
        Board report = new Board();
        report.setName("신고");
        report.setContent(content);
        report.setRegDate(LocalDateTime.now());
        report.setBoardCategory(RoleCategory.REPORT);
        report.setRestaurant(restaurantService.findById((Long) session.getAttribute("restaurantId")).get());
        boardService.save(report);

        // 성공 응답 반환
        return ResponseEntity.ok().body(Map.of("message", "신고가 접수되었습니다."));
    }
}

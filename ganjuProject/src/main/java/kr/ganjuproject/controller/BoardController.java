package kr.ganjuproject.controller;

import kr.ganjuproject.dto.BoardDTO;
import kr.ganjuproject.entity.Board;
import kr.ganjuproject.entity.Category;
import kr.ganjuproject.entity.Menu;
import kr.ganjuproject.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
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
}

package kr.ganjuproject.controller;

import kr.ganjuproject.entity.Board;
import kr.ganjuproject.entity.Category;
import kr.ganjuproject.entity.Menu;
import kr.ganjuproject.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public ResponseEntity<Map<String, Object>>  validateBoard(Model model) {
        System.out.println("비동기 공지");
        Map<String, Object> response = new HashMap<>();
        List<Board> boards = boardService.noticeGetList();

        response.put("boards", boards);
        return ResponseEntity.ok(response);
    }
}

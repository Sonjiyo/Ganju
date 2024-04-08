package kr.ganjuproject.controller;

import kr.ganjuproject.entity.Board;
import kr.ganjuproject.entity.Review;
import kr.ganjuproject.service.BoardService;
import kr.ganjuproject.service.ReviewService;
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
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("main")
    public String review() {
        return "manager/review";
    }


    // 비동기로 데이터 보내기
    @GetMapping("/validateMenuBoard")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> validateBoard(Model model) {
        System.out.println("비동기 공지");
        Map<String, Object> response = new HashMap<>();
        List<Review> reviews = reviewService.reviewGetList();

        response.put("reviews", reviews);
        return ResponseEntity.ok(response);
    }
}

package kr.ganjuproject.controller;

import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.dto.ReviewDTO;
import kr.ganjuproject.entity.Board;
import kr.ganjuproject.entity.Review;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.service.BoardService;
import kr.ganjuproject.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@Slf4j
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("main")
    public String review(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();

        if (principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if (user.getLoginId().equals("admin")) return "redirect:/";

            List<Review> reviews = reviewService.findAll();
            double starAvg = reviewService.getAverageRating(user.getRestaurant().getId());
            long reviewCount = reviewService.countReviews();
            model.addAttribute("reviews", reviews);
            model.addAttribute("starAvg", starAvg);
            model.addAttribute("reviewCount", reviewCount);
        }
        return "manager/review";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok().body("리뷰(ID : " + id + ")가 삭제 되었습니다");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID 에 해당하는 리뷰가 없습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 삭제 중 오류가 발생했습니다 : " + e.getMessage());
        }
    }

    // 비동기로 데이터 보내기
    @GetMapping("/validateMenuReview")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> validateReview(@RequestParam(defaultValue = "0") int page) {
        System.out.println("비동기 리뷰");
        Map<String, Object> response = new HashMap<>();
        System.out.println("page = " + page);
        List<ReviewDTO> reviews = reviewService.reviewGetList(1L,page);

        response.put("reviews", reviews);
        // 더보기 했을때 페이지를 다 불러 왔으면 비활성화
        response.put("size", reviewService.findAll().size() );
        return ResponseEntity.ok(response);
    }
}

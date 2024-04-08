package kr.ganjuproject.service;

import kr.ganjuproject.entity.Board;
import kr.ganjuproject.entity.Review;
import kr.ganjuproject.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;


    // 메인 메뉴에서 비동기로 리뷰만 가져갈때 쓰는 메서드
    public List<Review> reviewGetList() { return reviewRepository.findAll(); }
}

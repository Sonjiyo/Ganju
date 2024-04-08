package kr.ganjuproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @ToString.Exclude
    private Restaurant restaurant;
    private String name;
    private String content;
    @CreatedDate // 나중에 service 쪽에서 정렬 기준으로 삼기 위한 메서드
    private LocalDateTime regDate;
    private int star;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    @ToString.Exclude
    private Orders order;
    private int secret;
}

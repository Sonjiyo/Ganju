package kr.ganjuproject.service;

import kr.ganjuproject.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
}

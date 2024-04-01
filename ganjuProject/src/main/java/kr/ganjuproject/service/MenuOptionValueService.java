package kr.ganjuproject.service;

import kr.ganjuproject.repository.MenuOptionValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuOptionValueService {
    private final MenuOptionValueRepository menuOptionValueRepository;
}

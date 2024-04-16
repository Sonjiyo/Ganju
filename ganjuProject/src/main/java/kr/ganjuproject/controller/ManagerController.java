package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.entity.*;
import kr.ganjuproject.dto.UserDTO;
import kr.ganjuproject.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Pattern;

@Controller
@Slf4j
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final ManagerService managerService;
    private final BoardService boardService;
    private final OrdersService ordersService;
    private final ReviewService reviewService;
    private final RestaurantService restaurantService;

    private static final String PHONE_REGEX = "^\\d{3}-\\d{4}-\\d{4}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    @GetMapping("")
    public String main(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();

        if (principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if (user.getLoginId().equals("admin")) return "redirect:/";

            double starAvg = reviewService.getAverageRating(user.getRestaurant().getId());
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime startTime = LocalDateTime.of(currentTime.toLocalDate(), LocalTime.MIN); // 오늘 날짜의 시작
            LocalDateTime endTime = LocalDateTime.of(currentTime.toLocalDate(), LocalTime.MAX); // 오늘 날짜의 끝

            List<Orders> list = ordersService.getRestaurantOrdersWithinTimeWithoutCall(user.getRestaurant(), startTime, endTime);

            Map<String, Object> total = ordersService.getRestaurantOrderData(list);
            model.addAttribute("user", user);
            model.addAttribute("orderCount", total.get("count"));
            model.addAttribute("orderPrice", total.get("price"));
            model.addAttribute("reportCount", boardService.getReortAcceptList(user.getRestaurant()));
            model.addAttribute("call", ordersService.getRestaurantOrdersDivision(user.getRestaurant(), RoleOrders.CALL).size());
            model.addAttribute("wait", ordersService.getRestaurantOrdersDivision(user.getRestaurant(), RoleOrders.WAIT).size());
            model.addAttribute("okay", ordersService.getRestaurantOrdersDivision(user.getRestaurant(), RoleOrders.OKAY).size());
            model.addAttribute("starAvg", starAvg);
        }

        return "manager/home";
    }

    @GetMapping("join")
    public String join() {
        return "manager/join";
    }

    @PostMapping("join")
    public @ResponseBody String insertUser(@RequestBody UserDTO userDTO) {
        Users user = new Users();
        user.setLoginId(userDTO.getLoginId());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setRole(RoleUsers.ROLE_MANAGER);
        managerService.insertUser(user);

        return "";
    }

    @GetMapping("join/{loginId}")
    public @ResponseBody String validUsernameCheck(@PathVariable(value = "loginId") String loginId) {
        return managerService.isVaildLoginId(loginId) ? "ok" : "no";
    }

    @GetMapping("myPage")
    public String myPage(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();
        if (principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if (user.getLoginId().equals("admin")) return "redirect:/";
            model.addAttribute("user", user);
        }
        return "/manager/myPage";
    }

    @GetMapping("myPageEdit")
    public String myPageEdit(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();
        if (principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if (user.getLoginId().equals("admin")) return "redirect:/";
            model.addAttribute("user", user);
        }
        return "/manager/myPageEdit";
    }

    @PostMapping("/update")
    public String update(@RequestBody Map<String, String> requestBody, Authentication authentication) {
        String inputPassword = requestBody.get("password");
        String inputPhone = requestBody.get("phone");
        if (authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();
        PrincipalDetails principalDetails = (PrincipalDetails) principal;
        Users user = principalDetails.getUser();
        // 입력된 비밀번호와 시큐리티로 해싱된 비밀번호를 비교
        if (passwordEncoder.matches(inputPassword, user.getPassword())) {
            // 비밀번호가 일치할 경우에만 전화번호 수정
            if (inputPhone != null && PHONE_PATTERN.matcher(inputPhone).matches()) {
                user.setPhone(inputPhone);
                managerService.updateUser(user);
                System.out.println("user = " + user);
                return "redirect:/manager/myPage";
            } else {
                throw new IllegalArgumentException("올바른 전화번호 형식이 아닙니다.");
            }
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @DeleteMapping("/{keyId}")
    public @ResponseBody String DeleteManager(@PathVariable Long keyId) {
        managerService.deleteUser(keyId);
        return "ok";
    }

    @GetMapping("/notice")
    public String noticeMain(Model model) {
        List<Board> boards = boardService.findAll();
        model.addAttribute("boards", boards);
        return "manager/notice";
    }

    @GetMapping("/addNotice")
    public String addNotice(Model model) {
        List<Board> boards = boardService.findAll();
        model.addAttribute("boards", boards);
        return "manager/addNotice";
    }

    @PostMapping("/addNotice")
    public ResponseEntity<String> addNotice(@RequestBody Map<String, String> noticeData, Authentication authentication) {
        try {
            System.out.println("notice = " + noticeData);
            String loggedInUsername = authentication.getName();
            Board board = new Board();
            board.setTitle(noticeData.get("title"));
            board.setContent(noticeData.get("contents"));
            Optional<Restaurant> restaurantOptional = restaurantService.findById(1L);
            if (restaurantOptional.isPresent()) {
                board.setRestaurant(restaurantOptional.get());
            } else {
                // Handle case when restaurant with ID 1 is not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("레스토랑을 찾을 수 없습니다");
            }
            board.setName(loggedInUsername);
            board.setBoardCategory(RoleCategory.NOTICE);
            boardService.save(board);
            // 성공적으로 등록된 경우
            return ResponseEntity.ok("공지사항 등록 성공");
        } catch (Exception e) {
            // 등록에 실패한 경우
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 등록 실패");
        }
    }

    @DeleteMapping("/notice/{id}")
    public ResponseEntity<String> deleteNotice(@PathVariable Long id) {
        try {
            boardService.deleteBoard(id);
            return ResponseEntity.ok().body("공지사항(ID : " + id + ")가 삭제 되었습니다");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID 에 해당하는 게시글이 없습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제 중 오류가 발생했습니다 : " + e.getMessage());
        }
    }
}

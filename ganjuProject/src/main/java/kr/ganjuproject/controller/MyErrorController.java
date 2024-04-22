package kr.ganjuproject.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "알 수 없는 오류가 발생했습니다.";
        int statusCode = 0; // 초기 상태 코드 값 설정

        if (status != null) {
            statusCode = Integer.valueOf(status.toString());

            switch (HttpStatus.valueOf(statusCode)) {
                case NOT_FOUND:
                    errorMessage = "페이지를 찾을 수 없습니다.";
                    break;
                case INTERNAL_SERVER_ERROR:
                    errorMessage = "서버 내부 오류가 발생했습니다.";
                    break;
                case FORBIDDEN:
                    errorMessage = "접근이 금지되었습니다.";
                    break;
                case UNAUTHORIZED:
                    errorMessage = "인증이 필요합니다.";
                    break;
                case BAD_REQUEST:
                    errorMessage = "잘못된 요청입니다.";
                    break;
                case METHOD_NOT_ALLOWED:
                    errorMessage = "허용되지 않는 메소드입니다.";
                    break;
                // 여기에 더 많은 오류 상태를 추가할 수 있습니다.
                default:
                    errorMessage = "예기치 못한 오류가 발생했습니다.";
            }
        }

        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);

        // 모든 오류를 'error/errorPage.html' 템플릿을 통해 처리
        return "error/errorPage";
    }
}

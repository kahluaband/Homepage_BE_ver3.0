package kahlua.KahluaProject.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import kahlua.KahluaProject.global.apipayload.ApiResponse;
import kahlua.KahluaProject.global.apipayload.code.ReasonDto;
import kahlua.KahluaProject.global.service.DiscordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RequiredArgsConstructor
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    private final DiscordService discordService;

    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity<?> onThrowException(GeneralException generalException, HttpServletRequest request) {
        ReasonDto errorReasonHttpStatus = generalException.getErrorStatus();
        return handleExceptionInternal(generalException, errorReasonHttpStatus, null, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ReasonDto reason, HttpHeaders headers, HttpServletRequest request) {

        ApiResponse<Object> body = ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null);

        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                reason.getHttpStatus(),
                webRequest
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ReasonDto>> handleAllExceptions(Exception ex, WebRequest webRequest) {

        discordService.sendInternalServerErrorNotification(ex, webRequest);

        ReasonDto reason = ReasonDto.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .isSuccess(false)
                .code("INTERNAL_SERVER_ERROR")          // 필요 시 ErrorStatus enum 사용
                .message("서버 내부 오류가 발생했습니다.")
                .build();

        ApiResponse<ReasonDto> body = ApiResponse.onFailure(
                reason.getCode(),
                reason.getMessage(),
                reason
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}

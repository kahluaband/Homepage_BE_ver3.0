package kahlua.KahluaProject.global.apipayload.code.status;

import kahlua.KahluaProject.global.apipayload.code.BaseCode;
import kahlua.KahluaProject.global.apipayload.code.ReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {

    // 요청 에러
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "COMMON429", "요청이 너무 많습니다. 잠시 후 다시 시도해 주세요."),

    // 에러 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 에러"),

    // 로그인 실패
    LOGIN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "LOGIN_FAIL", "아이디 또는 비밀번호를 확인하세요"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "금지된 접근입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "권한이 없습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.NOT_FOUND, "PASSWORD_NOT_MATCH", "비밀번호가 틀렸습니다."),
    PASSWORD_INVALID(HttpStatus.NOT_FOUND, "PASSWORD_INVALID", "유효하지 않은 password입니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "TOKEN_INVALID", "토큰이 유효하지 않습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "TOKEN_INVALID", "토큰을 찾을 수 없습니다."),
    ALREADY_EXIST_USER(HttpStatus.BAD_REQUEST, "ALREADY_EXIST_USER", "이미 존재하는 회원입니다."),
    REDIS_NOT_FOUND(HttpStatus.NOT_FOUND, "REDIS_NOT_FOUND", "Redis 설정에 오류가 발생했습니다."),

    //유저 에러
    INVALID_USER_TYPE(HttpStatus.UNAUTHORIZED, "INVALID_USER_TYPE", "요구한 사용자 타입이 아닙니다."),
    ALREADY_DELETED_PROFILE_IMAGE(HttpStatus.BAD_REQUEST, "ALREADY_DELETED_PROFILE_IMAGE" , "프로필이 존재하지 않습니다." ),

    // 세션 에러
    SESSION_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "유효하지 않은 세션입니다."),

    // 티켓 에러
    TICKET_NOT_FOUND(HttpStatus.NOT_FOUND, "TICKET_NOT_FOUND", "티켓을 찾을 수 없습니다."),
    TICKET_COLUMN_INVALID(HttpStatus.BAD_REQUEST, "TICKET_COLUMN_INVALID", "올바르지 않은 티켓 속성입니다."),
    ALREADY_EXIST_STUDENT_ID(HttpStatus.BAD_REQUEST, "ALREADY_EXIST_STUDENT_ID", "이미 존재하는 학번입니다."),

    //지원하기 에러
    ALREADY_EXIST_APPLICANT(HttpStatus.BAD_REQUEST, "ALREADY_EXIST_APPLICANT", "이미 존재하는 지원자입니다."),
    APPLICANT_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLICANT_NOT_FOUND", "존재하지 않는 지원자입니다."),
    APPLY_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLY_INFO_NOT_FOUND", "존재하지 않는 지원정보 입니다." ),

    // 웹소켓 에러
    WEBSOCKET_SESSION_UNAUTHORIZED(HttpStatus.BAD_REQUEST, "WEBSOCKET_ERROR", "웹소켓 연결 과정에서 에러가 발생했습니다."),

    // 게시판 에러
    IMAGE_NOT_UPLOAD(HttpStatus.BAD_REQUEST, "IMAGE_NOT_UPLOAD", "이미지 업로드 개수를 초과하였습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_NOT_FOUND", "존재하지 않는 게시글입니다."),

    // 댓글 에러
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_NOT_FOUND", "존재하지 않는 댓글입니다."),

    // 예약 에러
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "RESERVATION_NOT_FOUND", "예약내역을 찾을 수 없습니다."),

    //공연 관련 에러
    PERFORMANCE_NOT_FOUND(HttpStatus.NOT_FOUND,"PERFORMANCE_NOT_FOUND","공연 정보를 찾을 수 없습니다."),

    // 앨범/사진 에러
    ALBUM_NOT_FOUND(HttpStatus.NOT_FOUND, "ALBUM_NOT_FOUND", "존재하지 않는 앨범입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "CATEGORY_NOT_FOUND", "존재하지 않는 카테고리입니다."),
    PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "PHOTO_NOT_FOUND", "존재하지 않는 사진입니다."),
    IMAGE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "IMAGE_NOT_DELETED", "S3에서 이미지 삭제에 실패했습니다."),
    IMAGE_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "ALBUM500", "이미지를 다운로드하거나 압축하는 중 오류가 발생했습니다."),
    INVALID_IMAGE_PATH(HttpStatus.BAD_REQUEST, "IMAGE4002", "잘못되거나 권한이 없는 이미지 경로입니다."),
    FORBIDDEN_ALBUM_ACCESS(HttpStatus.FORBIDDEN, "ALBUM4031", "해당 기수 앨범에 접근할 권한이 없습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDto getReason() {
        return ReasonDto.builder()
                .isSuccess(false)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }

}
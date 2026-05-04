package kahlua.KahluaProject.global.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiscordService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final DateTimeFormatter KST_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul"));

    @Value("${logging.discord.webhook-url}")
    private String DISCORD_URL;

    public void sendInternalServerErrorNotification(Exception ex, WebRequest webRequest) {

        String message = formatErrorMessage(ex, webRequest);

        Map<String, String> content = Map.of("content", message);

        restTemplate.postForEntity(DISCORD_URL, content, String.class);
    }

    public String formatErrorMessage(Exception ex, WebRequest webRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append("🚨 ### 서버 에러 발생!\n");
        sb.append("**시간:** ").append(LocalDateTime.now()).append("\n");

        if (webRequest instanceof ServletWebRequest servletWebRequest) {
            HttpServletRequest request = servletWebRequest.getRequest();

            sb.append("**요청 URL:** ").append(request.getRequestURL()).append("\n");
            sb.append("**HTTP Method:** ").append(request.getMethod()).append("\n");
        } else {
            // 만약 서블릿 환경이 아닐 경우 fallback 처리
            sb.append("**요청 정보:** 서블릿 환경이 아님\n");
        }

        sb.append("**에러 메시지:** ").append(ex.getMessage()).append("\n");

        // 스택트레이스 앞부분만 출력
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String stackTrace = sw.toString();
        sb.append("**스택트레이스:**\n```\n")
                .append(stackTrace.substring(0, Math.min(stackTrace.length(), 1000)))
                .append("\n```");

        return sb.toString();
    }

}

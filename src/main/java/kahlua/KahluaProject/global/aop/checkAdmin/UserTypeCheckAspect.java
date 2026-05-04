package kahlua.KahluaProject.global.aop.checkAdmin;

import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.user.entity.UserType;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.global.exception.GeneralException;
import kahlua.KahluaProject.global.security.AuthDetails;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class UserTypeCheckAspect {

    @Before("@within(checkUserType) || @annotation(checkUserType)")
    public void checkUserType(JoinPoint joinPoint, CheckUserType checkUserType) {
        UserType[] requiredUserType = checkUserType.userType();

        for (Object arg : joinPoint.getArgs()) {

            if (arg instanceof AuthDetails authDetails) {
                UserType actualType = authDetails.user().getUserType();
                boolean match = Arrays.asList(requiredUserType).contains(actualType);
                if (!match) {
                    throw new GeneralException(ErrorStatus.INVALID_USER_TYPE);
                }
                return;
            }

            if (arg instanceof User user) {
                UserType actualType = user.getUserType();
                boolean match = Arrays.asList(requiredUserType).contains(actualType); // ← 핵심 수정
                if (!match) {
                    throw new GeneralException(ErrorStatus.INVALID_USER_TYPE);
                }
                return;
            }
        }

        throw new GeneralException(ErrorStatus.UNAUTHORIZED); // 인증 정보 없음
    }

}

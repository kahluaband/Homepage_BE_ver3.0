package kahlua.KahluaProject.global.aop.checkAdmin;

import kahlua.KahluaProject.domain.user.entity.UserType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUserType {
    public UserType[] userType();
}

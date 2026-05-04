package kahlua.KahluaProject.domain.admin.adminController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kahlua.KahluaProject.domain.user.entity.ApprovalFilter;
import kahlua.KahluaProject.domain.user.entity.UserType;
import kahlua.KahluaProject.domain.user.dto.response.UserListResponse;
import kahlua.KahluaProject.domain.user.dto.response.UserResponse;
import kahlua.KahluaProject.global.aop.checkAdmin.CheckUserType;
import kahlua.KahluaProject.global.apipayload.ApiResponse;
import kahlua.KahluaProject.global.security.AuthDetails;
import kahlua.KahluaProject.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자(멤버 관리)", description = "관리자(멤버 관리) 페이지 관련 API")
@RestController
@RequestMapping("/v1/admin/users")
@RequiredArgsConstructor
public class AdminApprovalController {
    private final UserService userService;

    @GetMapping
    @CheckUserType(userType = UserType.ADMIN)
    @Operation(summary = "깔루아 유저리스트 API", description = """
            깔루아 멤버들의 정보를 확인할 수 있는 리스트 api입니다.
            UserType이 ADMIN/KAHLUA일 경우 승인 상태는 APPROVED,
            PENDING일 경우에는 승인 상태는 PENDING입니다.
            ALL을 입력할 경우 전체 멤버 명단이 조회됩니다.
            """)
    @Parameter(name = "page", description = "페이지 번호, 0번이 1 페이지 입니다.")
    public ApiResponse<UserListResponse> getUsers(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestParam ApprovalFilter approvalFilter,
            @RequestParam(defaultValue = "0")    int page,
            @RequestParam(defaultValue = "10")   int size){

        UserListResponse userListResponse=userService.getUserList(approvalFilter,page,size);
        return ApiResponse.onSuccess(userListResponse);
    }

    @PatchMapping("/{userId}")
    @CheckUserType(userType = UserType.ADMIN)
    @Operation(summary = "유저 타입 변경 API", description = "유저 타입을 변경할 수 있는 API입니다.")
    public ApiResponse<UserResponse> changeUserType(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long userId,
            @RequestParam UserType userType){
        UserResponse userResponse=userService.changeUserType(userId,userType);

        return ApiResponse.onSuccess(userResponse);
    }

}

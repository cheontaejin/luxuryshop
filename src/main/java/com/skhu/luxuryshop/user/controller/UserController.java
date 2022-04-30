package com.skhu.luxuryshop.user.controller;

import com.skhu.luxuryshop.user.annotation.PreAuthorize;
import com.skhu.luxuryshop.user.dto.*;
import com.skhu.luxuryshop.user.entity.UserEntity;
import com.skhu.luxuryshop.user.jwt.AuthInterceptor;
import com.skhu.luxuryshop.user.jwt.TokenProvider;
import com.skhu.luxuryshop.user.service.UserManagementService;
import com.skhu.luxuryshop.user.service.UserSignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.skhu.luxuryshop.user.jwt.AuthInterceptor.TOKEN_HEADER;

/* Spring MVC Controller에 @RespinseBody가 추가된 것, @Controller와 달리 @RestController는 @ResponseBody를 추가할 필요가 없다. */
@RestController
/*
@NoArgsConstructor - 기본 생성자 생성
@AllArgsConstructor - 모든 필드를 초기화하는 생성자 생성
@RequiredArgsConstructor - 초기화 되지 않은 모든 final 필드 & @NonNull 마크가 있는 필드를 초기화하는 생성자 생성
*/
/* @Autowired를 사용하지않고 @RequiredArgsConstructor로 의존성 주입을 한다.(생성자 주입)
* 생성자 주입의 장점 : 순환 참조 방지, 테스트 코드 작성 용이, 코드 가독성이 좋음, 객체 변이 방지(final 가능) */
@RequiredArgsConstructor
/* URL을 컨트롤러의 메서드와 매핑할 때 사용하는 어노테이션(공통 url) */
@RequestMapping("/api/users")
public class UserController {
    private final UserSignupService userSignupService;
    private final UserManagementService userManagementService;
    private final TokenProvider tokenProvider;

    /* @RequestMapping의 GET 방식을 처리하고 싶으면 GET */
    @GetMapping("/emails/{email}/exists")
    /* @pathvariable URL 경로에 변수를 넣어주는 어노테이션 */
    /* ResponseEntity = HttpEntity 클래스를 상속받아 구현한 클래스 따라서 HttpStatus, HttpHeaders, HttpBody를 포함한다. */
    public ResponseEntity<Boolean> isDuplicatedEmail(@PathVariable String email) {
        /* HttpStatus.OK = http 상태코드 200번 전달 */
        return new ResponseEntity(userSignupService.validateDuplicatedEmail(email), HttpStatus.OK);
    }

    /* @RequestMapping의 POST 방식을 처리하고 싶으면 POST */
    @PostMapping
    /* @RequestBody 어노테이션 옆에 @Valid를 작성하면, RequestBody로 들어오는 객체에 대한 검증을 수행한다. 이 검증의 세부적인 사항은 객체 안에 정의를 해두어야 한다. */
    public ResponseEntity<String> signUp(@RequestBody @Valid UserSignupDto userSignupDto) throws Exception {
        UserResponseDto savedUser = userSignupService.save(userSignupDto);
        return ResponseEntity
                .created(URI.create("/" + savedUser.getId()))
                .build();
    }

    @GetMapping("/{id}/details")
    /* @PreAuthorize 권한 별로 접근을 제어한다. */
    @PreAuthorize(roles = {"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<UserResponseDto> details(@PathVariable Long id) {
        UserResponseDto userDetails = userManagementService.findById(id);
        return new ResponseEntity(userDetails, HttpStatus.OK);
    }

    /* @RequestMapping의 데이터를 삭제하고 싶다면 DELETE */
    @DeleteMapping("/{id}/delete")
    @PreAuthorize(roles = {"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userManagementService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /* @RequestMapping의 데이터 입력을 사용하고 싶다면 PUT */
    @PutMapping("/update")
    @PreAuthorize(roles = {"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Void> update(@RequestBody @Valid UserUpdateDto userUpdateDto) {
        userManagementService.update(userUpdateDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize(roles = {"ROLE_ADMIN"})
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<UserResponseDto> users = userManagementService.findAll();
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenDto> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        UserEntity user = userManagementService.login(userLoginDto);
        String jwt = tokenProvider.createToken(user);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AuthInterceptor.AUTHORIZATION_HEADER, TOKEN_HEADER + jwt);

        return new ResponseEntity<>(new UserTokenDto(user.getId(), jwt), httpHeaders, HttpStatus.OK);
    }
}
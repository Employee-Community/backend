package com.community.backend.domain.member.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.community.backend.common.dto.ApiResponse;
import com.community.backend.common.security.jwt.JwtBlackListService;
import com.community.backend.common.security.jwt.JwtPayload;
import com.community.backend.common.security.jwt.JwtTokenProvider;
import com.community.backend.domain.member.dto.request.MemberLogInDto;
import com.community.backend.domain.member.dto.request.MemberRegisterDto;
import com.community.backend.domain.member.dto.request.MembershipChangeDto;
import com.community.backend.domain.member.dto.response.MemberResponseDto;
import com.community.backend.domain.member.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member")
public class MemberController {

	private final MemberService memberService;
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtBlackListService jwtBlackListService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<Void>> registerMember(@RequestBody MemberRegisterDto request) {

		memberService.registerMember(request);
		return ResponseEntity.ok(ApiResponse.success("회원가입이 성공적으로 완료되었습니다.", null));
	}

	@PostMapping("/logIn")
	public ResponseEntity<ApiResponse<MemberResponseDto>> logInMember(@RequestBody MemberLogInDto request) {

		MemberResponseDto response = memberService.logInMember(request);
		String access = jwtTokenProvider.createToken(response.idx(), response.email(), response.nickname(),
			JwtTokenProvider.ACCESS_TOKEN_EXPIRATION_TIME);
		String refresh = jwtTokenProvider.createToken(response.idx(), response.email(), response.nickname(),
			JwtTokenProvider.REFRESH_TOKEN_EXPIRATION_TIME);

		return ResponseEntity.status(HttpStatus.OK)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + access)
			.header(HttpHeaders.SET_COOKIE, jwtTokenProvider.createRefreshTokenCookie(refresh).toString())
			.body(ApiResponse.success("로그인에 성공했습니다.", response));
	}

	@DeleteMapping("/{idx}")
	public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long idx, @RequestParam String password) {

		memberService.deleteMember(idx, password);
		return ResponseEntity.ok(ApiResponse.success("회원탈퇴가 성공적으로 완료되었습니다.", null));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<MemberResponseDto>> getMember(@AuthenticationPrincipal JwtPayload payload) {

		MemberResponseDto response = memberService.getMemberByIdx(payload.idx());
		return ResponseEntity.ok(ApiResponse.success("회원 조회가 성공적으로 완료되었습니다.", response));
	}

	@PostMapping("/logOut")
	public ResponseEntity<ApiResponse<Void>> logOutMember(@CookieValue(value = "refresh") String refreshToken) {

		jwtBlackListService.addBlackList(refreshToken);
		return ResponseEntity.ok(ApiResponse.success("로그아웃이 성공적으로 완료되었습니다", null));
	}

	@PostMapping("/reissue")
	public ResponseEntity<ApiResponse<Void>> reissueToken(HttpServletRequest request) {

		if (request.getCookies() == null)
			return null;

		String refreshToken = null;
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals("refresh")) {
				refreshToken = cookie.getValue();
			}
		}

		if(refreshToken == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail("Refresh 토큰이 존재하지 않습니다.", null));
		}

		if(jwtTokenProvider.parseToken(refreshToken) == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail("유효하지 않은 토큰입니다.", null));
		}

		if(jwtBlackListService.isBlackList(refreshToken)) {
			return ResponseEntity.badRequest().body(ApiResponse.fail("유효하지 않은 토큰입니다.", null));
		}

		String accessToken = jwtTokenProvider.createToken(jwtTokenProvider.getSubject(refreshToken), jwtTokenProvider.getEmail(refreshToken),
			jwtTokenProvider.getNickname(refreshToken),
			JwtTokenProvider.ACCESS_TOKEN_EXPIRATION_TIME);

		return ResponseEntity.status(HttpStatus.OK)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.body(ApiResponse.success("Access 토큰을 재발급했습니다.", null));
	}

	@PatchMapping("/role")
	public ResponseEntity<ApiResponse<Void>> changeMembership(
		@AuthenticationPrincipal JwtPayload payload,
		@RequestBody MembershipChangeDto request
	) {

		memberService.changeMembership(payload.idx(), request);
		return ResponseEntity.ok(ApiResponse.success("멤버쉽이 성공적으로 변경되었습니다.", null));
	}

	@GetMapping("/{idx}")
	public ResponseEntity<ApiResponse<MemberResponseDto>> getMember(@PathVariable Long idx) {

		MemberResponseDto response = memberService.getMemberByIdx(idx);
		return ResponseEntity.ok(ApiResponse.success("회원 조회가 성공적으로 완료되었습니다.", response));
	}

	// Member Idx 리스트를 통해 Member 리스트를 불러오는 API(내부 호출용)
	@GetMapping("/list")
	public ResponseEntity<ApiResponse<List<MemberResponseDto>>> getMembersByIdxs(
		@RequestParam String idxs
	) {
		List<MemberResponseDto> response = memberService.getMemberByIdxs(idxs);
		return ResponseEntity.ok(ApiResponse.success("멤버 목록이 성공적으로 조회되었습니다,", response));
	}
}

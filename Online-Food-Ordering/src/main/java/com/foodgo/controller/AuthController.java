package com.foodgo.controller;

import com.foodgo.Exception.UserException;
import com.foodgo.config.JwtProvider;
import com.foodgo.domain.USER_ROLE;
import com.foodgo.model.Cart;
import com.foodgo.model.PasswordResetToken;
import com.foodgo.model.User;
import com.foodgo.model.VerificationToken;
import com.foodgo.repository.CartRepository;
import com.foodgo.repository.UserRepository;
import com.foodgo.request.LoginRequest;
import com.foodgo.request.ResetPasswordRequest;
import com.foodgo.response.ApiResponse;
import com.foodgo.response.AuthResponse;
import com.foodgo.service.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final CustomeUserServiceImplementation customUserDetails;
	private final CartRepository cartRepository;
	private final PasswordResetTokenService passwordResetTokenService;
	private final UserService userService;
	private final VerificationTokenService verificationTokenService;
	private final EmailService emailService;

	public AuthController(UserRepository userRepository,
						  PasswordEncoder passwordEncoder,
						  JwtProvider jwtProvider,
						  CustomeUserServiceImplementation customUserDetails,
						  CartRepository cartRepository,
						  PasswordResetTokenService passwordResetTokenService,
						  UserService userService,
						  VerificationTokenService verificationTokenService,
						  EmailService emailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtProvider = jwtProvider;
		this.customUserDetails = customUserDetails;
		this.cartRepository = cartRepository;
		this.passwordResetTokenService = passwordResetTokenService;
		this.userService = userService;
		this.verificationTokenService = verificationTokenService;
		this.emailService = emailService;
	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody User user) throws UserException {
		String email = user.getEmail();
		String password = user.getPassword();
		String fullName = user.getFullName();
		USER_ROLE role = user.getRole();

		User isEmailExist = userRepository.findByEmail(email);
		if (isEmailExist != null) {
			throw new UserException("Email đã được sử dụng. Vui lòng nhập email khác.");
		}

		// tạo user
		User createdUser = new User();
		createdUser.setEmail(email);
		createdUser.setFullName(fullName);
		createdUser.setPassword(passwordEncoder.encode(password));
		createdUser.setRole(role);
		createdUser.setStatus("0"); // chưa xác thực
		User savedUser = userRepository.save(createdUser);

		// Gửi email xác thực
		VerificationToken verifyToken = verificationTokenService.createVerificationToken(savedUser);
		emailService.sendVerificationEmail(savedUser, verifyToken.getToken());

		// Tạo giỏ hàng mặc định
		Cart cart = new Cart();
		cart.setCustomer(savedUser);
		cartRepository.save(cart);

		// Không đăng nhập tự động vì chưa xác thực
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(null);
		authResponse.setMessage("Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản.");
		authResponse.setRole(savedUser.getRole());

		return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		Authentication authentication = authenticate(username, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtProvider.generateToken(authentication);
		AuthResponse authResponse = new AuthResponse();
		authResponse.setMessage("Đăng nhập thành công");
		authResponse.setJwt(token);

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
		authResponse.setRole(USER_ROLE.valueOf(roleName));

		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserDetails.loadUserByUsername(username);

		if (userDetails == null) {
			throw new BadCredentialsException("User vaf mật khẩu ko hợp lệ");
		}

		// Kiểm tra trạng thái xác thực
		User user = userRepository.findByEmail(username);
		if (user != null && "0".equals(user.getStatus())) {
			throw new BadCredentialsException("Tài khoản chưa được xác thực qua email.");
		}

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Sai thông tin đăng nhập, vui lòng kiểm tra lại.");
		}

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	@PostMapping("/reset-password")
	public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest req) throws UserException {
		PasswordResetToken resetToken = passwordResetTokenService.findByToken(req.getToken());

		if (resetToken == null || resetToken.isExpired()) {
			if (resetToken != null) {
				passwordResetTokenService.delete(resetToken);
			}
			throw new UserException("Token không hợp lệ hoặc đã hết hạn.");
		}

		User user = resetToken.getUser();
		userService.updatePassword(user, req.getPassword());
		passwordResetTokenService.delete(resetToken);

		ApiResponse res = new ApiResponse("Cập nhật mật khẩu thành công!", true);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/reset-password-request")
	public ResponseEntity<ApiResponse> resetPasswordRequest(@RequestParam("email") String email) throws UserException {
		User user = userService.findUserByEmail(email);
		if (user == null) {
			throw new UserException("Không tìm thấy người dùng");
		}

		userService.sendPasswordResetEmail(user);

		ApiResponse res = new ApiResponse("Email đặt lại mật khẩu đã được gửi.", true);
		return ResponseEntity.ok(res);
	}

	@GetMapping("/verify")
	public ResponseEntity<ApiResponse> verifyUser(@RequestParam("token") String tokenValue) throws UserException {
		VerificationToken token = verificationTokenService.findByToken(tokenValue);

		if (token == null || token.isExpired()) {
			throw new UserException("Token không hợp lệ hoặc đã hết hạn.");
		}

		User user = token.getUser();
		user.setStatus("1"); // Đánh dấu đã xác thực
		userRepository.save(user);
		verificationTokenService.delete(token);

		ApiResponse res = new ApiResponse("Xác thực tài khoản thành công!", true);
		return ResponseEntity.ok(res);
	}
}

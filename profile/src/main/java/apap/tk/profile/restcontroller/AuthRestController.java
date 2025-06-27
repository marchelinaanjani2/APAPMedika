package apap.tk.profile.restcontroller;

import apap.tk.profile.model.EndUser;
import apap.tk.profile.restdto.request.LoginJwtRequestDTO;
import apap.tk.profile.restdto.response.BaseResponseDTO;
import apap.tk.profile.restdto.response.EndUserResponseDTO;
import apap.tk.profile.restdto.response.LoginJwtResponseDTO;
import apap.tk.profile.restservice.EndUserRestService;
import apap.tk.profile.security.jwt.JwtUtils;
import apap.tk.profile.service.EndUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    @Autowired
    EndUserRestService userService;

    @Autowired
    EndUserService endUserService;

    @Autowired
    EndUserRestService endUserRestService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginJwtRequestDTO loginJwtRequestDTO) {
        var baseResponseDTO = new BaseResponseDTO<LoginJwtResponseDTO>();
        try {
            EndUser endUser = endUserService.getEndUserByEmail(loginJwtRequestDTO.getEmail());
            UserDetails userDetails = userDetailService.loadUserByUsername(endUser.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(endUser.getUsername(), loginJwtRequestDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            LoginJwtResponseDTO loginJwtResponseDTO = new LoginJwtResponseDTO();
            loginJwtResponseDTO.setToken(jwtUtils.generateJwtToken(userDetails.getUsername()));

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(loginJwtResponseDTO);
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setMessage(String.format("Login Berhasil"));
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.UNAUTHORIZED.value());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setMessage("Username atau Password salah!");
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUser() {
        var baseResponseDTO = new BaseResponseDTO<EndUserResponseDTO>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            EndUserResponseDTO endUser = endUserRestService.getEndUserByUsername(username);

            if (endUser == null) {
                throw new UsernameNotFoundException("User not found");
            }
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(endUser);
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setMessage("User login berhasil diambil");
            return ResponseEntity.ok(baseResponseDTO);

        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Error: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }

}

package com.trade.icesi_trade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.trade.icesi_trade.dtos.RegisterDto;

import jakarta.validation.Valid;

import com.trade.icesi_trade.Service.Impl.UserServiceImpl;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;

// import com.trade.icesi_trade.Service.Impl.JwtServiceImpl;
// import com.trade.icesi_trade.Service.Impl.UserServiceImpl;
// import com.trade.icesi_trade.dtos.LogInDto;
// import com.trade.icesi_trade.dtos.TokenDto;
// // import com.trade.icesi_trade.model.User;

// import jakarta.validation.Valid;

@Controller
// @RequestMapping("/public")
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/")
    public String redirectToUsers() {
        return "redirect:/users";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute("registerDto") RegisterDto dto,
            BindingResult br,
            Model model
    ) {
        // validación de coincidencia de contraseñas
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            br.rejectValue("confirmPassword", "error.confirmPassword", "Las contraseñas no coinciden");
        }

        if (br.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.register(dto);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("registrationError", ex.getMessage());
            return "auth/register";
        }

        return "redirect:/login?registered";
    }

    @GetMapping("/default")
    public String redirectByRole(Authentication auth) {
        for (GrantedAuthority authority : auth.getAuthorities()) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/users"; // Panel de admins
            }
        }
        return "redirect:/home"; // Panel para usuarios normales
    }
    

    
    // @Autowired
    // private UserServiceImpl userService;

//     @Autowired
//     private JwtServiceImpl jwtService;

//     @Autowired
//     private  AuthenticationManager authenticationManager;

//     @PostMapping("/login")
//     public ResponseEntity<TokenDto> login(@RequestBody LogInDto loginDto) {
        
//         Authentication authentication = authenticationManager.authenticate(
//                 new UsernamePasswordAuthenticationToken(
//                         loginDto.getEmail(),
//                         loginDto.getPassword()
//                 )
//         );

//         String token = jwtService.generateToken(authentication);
//         TokenDto tokenDto = new TokenDto(loginDto.getEmail(), token, authentication.getAuthorities().stream().map(a -> a.getAuthority()).toList());
        
//         return ResponseEntity.ok(tokenDto);
//     }    
}
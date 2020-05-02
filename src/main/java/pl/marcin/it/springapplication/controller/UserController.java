package pl.marcin.it.springapplication.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.marcin.it.springapplication.exception.TokenNotFoundException;
import pl.marcin.it.springapplication.model.Token;
import pl.marcin.it.springapplication.model.User;
import pl.marcin.it.springapplication.repository.TokenRepository;
import pl.marcin.it.springapplication.repository.UserRepository;
import pl.marcin.it.springapplication.service.UserService;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

@Controller
public class UserController {

    private UserService userService;
    private TokenRepository tokenRepository;
    private UserRepository userRepository;

    public UserController(UserService userService, TokenRepository tokenRepository, UserRepository userRepository) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/home")
    public String hello(Principal principal, Model model) {
        model.addAttribute("name", principal.getName());
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        model.addAttribute("authorities", authorities);
        model.addAttribute("details", details);
        return "welcome";
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("user", new User());
        return "sign-up";
    }

    @PostMapping("/register")
    public String register(User user) throws ServletException, MessagingException {
        userService.addUser(user);
        return "redirect:login";
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "access-denied";
    }

    @GetMapping("/token")
    public String token(HttpServletRequest request, @RequestParam String value) throws ServletException {
        Optional<Token> tokenByValue = Optional.ofNullable(tokenRepository.findByValue(value)
                .orElseThrow(() -> new TokenNotFoundException("Token not found!")));
        User user = tokenByValue.get().getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return "redirect:login";
    }
}

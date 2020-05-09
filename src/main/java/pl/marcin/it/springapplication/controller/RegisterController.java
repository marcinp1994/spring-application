package pl.marcin.it.springapplication.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.marcin.it.springapplication.exception.TokenNotFoundException;
import pl.marcin.it.springapplication.model.user.Token;
import pl.marcin.it.springapplication.model.user.User;
import pl.marcin.it.springapplication.repository.TokenRepository;
import pl.marcin.it.springapplication.repository.UserRepository;
import pl.marcin.it.springapplication.service.UserService;

import javax.mail.MessagingException;
import java.security.Principal;
import java.util.Optional;

@Controller
public class RegisterController {
    private static final Logger LOGGER = LogManager.getLogger(RegisterController.class);
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public RegisterController(UserService userService, TokenRepository tokenRepository, UserRepository userRepository) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/home")
    public String home(Principal principal, Model model) {
        model.addAttribute("name", principal.getName());
        LOGGER.info("User ['" + principal.getName() + "'] has successfully logged on to the application");
        return "home";
    }

    @GetMapping("/register")
    public String signUp(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(User user, RedirectAttributes redirectAttributes) throws MessagingException {
        userService.addUser(user);
        LOGGER.info("The user ['" + user.getUsername() + "']  was successfully created!");
        redirectAttributes.addFlashAttribute("registered", "success");
        return "redirect:login";
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "access-denied";
    }

    @GetMapping("/token")
    public String token(@RequestParam String value) {
        Optional<Token> tokenByValue = Optional.ofNullable(tokenRepository.findByValue(value)
                .orElseThrow(() -> new TokenNotFoundException("Token not found!")));
        User user = tokenByValue.get().getUser();
        user.setEnabled(true);
        userRepository.save(user);
        LOGGER.info("User ['" + user.getUsername() + "'] confirmed the account");
        return "redirect:login";
    }
}

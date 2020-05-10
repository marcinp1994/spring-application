package pl.marcin.it.springapplication.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.marcin.it.springapplication.exception.UserAlreadyExistException;
import pl.marcin.it.springapplication.exception.UserNotFoundException;
import pl.marcin.it.springapplication.model.user.Token;
import pl.marcin.it.springapplication.model.user.User;
import pl.marcin.it.springapplication.repository.TokenRepository;
import pl.marcin.it.springapplication.repository.UserRepository;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final MailService mailService;
    private final TokenRepository tokenRepository;

    public UserService(UserRepository userRepository, PasswordEncoder encoder, TokenRepository tokenRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.mailService = mailService;
        this.tokenRepository = tokenRepository;
    }

    public Optional<User> getUserByName(final String name){
        return userRepository.findUserByUsername(name);
    }

    public User addUser(User user) throws MessagingException {
        Optional<User> userByUsername = getUserByName(user.getUsername());
        if(userByUsername.isPresent()){
            LOGGER.error("Cannot add a User ['" + user.getUsername() + "'] to the database because such a user already exists!");
            throw new UserAlreadyExistException("User already exist!");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        LOGGER.info("New user ['" + user.getUsername() + "'] has been created in the database!");
        sendToken(user);
        return user;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void modifyUser(User user, Long id){
        Optional<User> userById = getUserById(id);
        if(!userById.isPresent()){
            LOGGER.error("Cannot modify a User ['" + user.getUsername() + "']  because such a user not exists!");
            throw new UserNotFoundException("User with id '" + id + "' not found");
        }
        user.setId(id);
        userRepository.save(user);
        LOGGER.info("User ['" + user.getUsername() + "'] has been modified!");
    }

    public void deleteUser(Long id) {
        User userById = getUserById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.deleteById(id);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    private void sendToken(User user) throws MessagingException {
        String tokenValue = UUID.randomUUID().toString();
        Token token = new Token();
        token.setValue(tokenValue);
        token.setUser(user);
        tokenRepository.save(token);
        LOGGER.info("Correctly created Token ['" + tokenValue + "'] for the User ['" + user.getUsername() + "']!");
        //TODO prepare url based on environments
        String url = "http://localhost:8080/token?value=" + tokenValue;
        mailService.sendMail(user.getEmail(), "Please confirm your registration", url, false);
    }
}

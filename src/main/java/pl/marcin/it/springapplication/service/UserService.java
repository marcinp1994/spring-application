package pl.marcin.it.springapplication.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.marcin.it.springapplication.exception.UserAlreadyExistException;
import pl.marcin.it.springapplication.exception.UserNotFoundException;
import pl.marcin.it.springapplication.model.Token;
import pl.marcin.it.springapplication.model.User;
import pl.marcin.it.springapplication.repository.TokenRepository;
import pl.marcin.it.springapplication.repository.UserRepository;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private MailService mailService;
    private TokenRepository tokenRepository;

    public UserService(UserRepository userRepository, PasswordEncoder encoder, TokenRepository tokenRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.mailService = mailService;
        this.tokenRepository = tokenRepository;
    }

    public Optional<User> getUserByName(final String name){
        return userRepository.findUserByUsername(name);
    }

    public void addUser(User user) throws MessagingException {
        Optional<User> userByUsername = getUserByName(user.getUsername());
        if(userByUsername.isPresent()){
            throw new UserAlreadyExistException("User already exist!");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        sendToken(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void modifyUser(User user, Long id){
        Optional<User> userById = getUserById(id);
        if(!userById.isPresent()){
            throw new UserNotFoundException("User with id '" + id + "' not found");
        }
        user.setId(id);
        userRepository.save(user);
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
        //TODO nie hardkoduj! co jesli wdrozymy na wyzsze srodowisko!
        String url = "http://localhost:8080/token?value=" + tokenValue;
        mailService.sendMail(user.getEmail(), "Please confirm your registration", url, false);
    }
}

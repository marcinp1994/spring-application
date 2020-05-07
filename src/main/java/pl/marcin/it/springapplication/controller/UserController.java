package pl.marcin.it.springapplication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.marcin.it.springapplication.exception.UserNotFoundException;
import pl.marcin.it.springapplication.model.user.User;
import pl.marcin.it.springapplication.model.user.UserList;
import pl.marcin.it.springapplication.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{name}")
    public User getUserByUsername(@PathVariable String name){
        Optional<User> userByName = userService.getUserByName(name);
        return userByName.orElseThrow(() -> new UserNotFoundException("User '" + name + "' not found"));
    }

    @GetMapping
    public UserList getUsers(){
        return new UserList(userService.getAllUsers());
    }


    @PutMapping("/{id}")
    public ResponseEntity modifyUser(@RequestBody @Valid User user, @PathVariable Long id){
        userService.modifyUser(user, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

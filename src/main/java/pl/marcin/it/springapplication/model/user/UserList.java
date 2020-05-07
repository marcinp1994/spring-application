package pl.marcin.it.springapplication.model.user;


import java.util.List;

public class UserList {
    private final List<User> users;

    public UserList(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
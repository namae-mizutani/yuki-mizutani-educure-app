package jp.mizutani.bookstore.service;

import java.util.List;
import jp.mizutani.bookstore.entity.User;

public interface UserService {
    List<User> selectAll();

    User selectById(int id);

    User login(String user_name, String password);
    
    void insert(User user);

    void delete(int id);

    void update(User user);
}

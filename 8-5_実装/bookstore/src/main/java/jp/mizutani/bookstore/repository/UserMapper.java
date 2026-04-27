package jp.mizutani.bookstore.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import jp.mizutani.bookstore.entity.User;

@Mapper
public interface UserMapper {
    List<User> selectAll();

    User selectById(@Param("id") int id);

    void insert(User user);

    void delete(@Param("id") int id);

    void update(User user);
}

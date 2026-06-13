package jp.mizutani.bookstore.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.mizutani.bookstore.entity.User;
import jp.mizutani.bookstore.repository.UserMapper;

@Service
public class LoginUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper; // 👈 水谷さんのMapperを使ってデータベースに接続！

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // 1. 画面に入力された名前（username）で、データベースからユーザーを検索
        // 💡 以前LoginControllerの43行目で使っていたメソッドと同じものです！
        User user = userMapper.selectByName(username); 
        
        // 2. もしデータベースにその名前のユーザーがいなければエラーを投げる（Securityの決まり）
        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + username);
        }

        // 3. 役職（一般、管理者など）を、Spring Securityが認識できる「権限（Authority）」の形に変換する
        // 💡 必ず「ROLE_」という文字を頭にくっつけるのがSpring Securityのルールです！
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        // 4. Spring Security専用の身分証明書（UserDetails）を作って返却する！
        // 💡 これを返すと、Securityが裏側で自動的に「パスワードが合致しているか」をチェックしてくれます！
        return new org.springframework.security.core.userdetails.User(
            user.getName(),      // ユーザー名
            user.getPassword(),  // データベースに入っている暗号化済みのパスワード
            authorities          // 認識させた役職（ロール）のリスト
        );
    }
}
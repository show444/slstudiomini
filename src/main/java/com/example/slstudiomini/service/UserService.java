package com.example.slstudiomini.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.slstudiomini.exception.MyUniqueConstraintViolationException;
import com.example.slstudiomini.model.Authority;
import com.example.slstudiomini.model.User;
import com.example.slstudiomini.repository.AuthorityRepository;
import com.example.slstudiomini.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> findAllUsers(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);

        // JOINでAuthorityと結合
        Join<User, Authority> authorityJoin = user.join("authorities");

        // 条件を設定
        Predicate userRolePredicate = cb.equal(authorityJoin.get("authority"), "ROLE_USER");
        Predicate enabledPredicate = cb.isTrue(user.get("enabled"));

        // 条件をANDで結合
        Predicate finalPredicate = cb.and(userRolePredicate, enabledPredicate);

        // クエリに条件を追加
        cq.select(user).where(finalPredicate);


        cq.select(user);
        
        return entityManager.createQuery(cq).getResultList();
    }

    public User addEnableStudentAndHashPassword(User user){

        addUser(user);
        //有効化
        user.setEnabled(true);
        //ハッシュ化するクラスの準備
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //ハッシュ化
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        //パスワードの詰め直し
        user.setPassword(hashedPassword);

        Authority authority = authorityRepository.findByAuthority("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Authority Not Found with name=USER"));

        user.setAuthorities(Set.of(authority));
        return userRepository.save(user);
    }

    public void addUser(User user) {

        // 最初にユニーク性違反のチェックを行う
        User uniqueUser = userRepository.findByUsername(user.getUsername());
        // 既に存在する場合は自作Exceptionをスロー
        if ( uniqueUser != null ) {
            throw new MyUniqueConstraintViolationException("既に存在するユーザーです");
        }

        // .... 続きの処理
    }
}

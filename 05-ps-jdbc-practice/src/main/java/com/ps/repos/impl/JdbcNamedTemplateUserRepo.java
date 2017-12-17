package com.ps.repos.impl;

import com.ps.ents.User;
import com.ps.repos.UserRepo;
import com.ps.repos.util.UserRowMapper;
import com.ps.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by iuliana.cosmina on 6/4/16.
 */
@Repository("userNamedTemplateRepo")
public class JdbcNamedTemplateUserRepo implements UserRepo {

    private RowMapper<User> rowMapper = new UserRowMapper();

    protected NamedParameterJdbcTemplate jdbcNamedTemplate;

    @Autowired
    public JdbcNamedTemplateUserRepo(NamedParameterJdbcTemplate jdbcNamedTemplate) {
        this.jdbcNamedTemplate = jdbcNamedTemplate;
    }

    @Override
    public Set<User> findAll() {
        String sql = "select id, username, email, password from p_user";
        return new HashSet<>(jdbcNamedTemplate.query(sql, rowMapper));
    }

    @Override
    public int createTable(String name) {
        String sql = "create table " + name + " (id long, name text)";
        jdbcNamedTemplate.getJdbcOperations().execute(sql);

        sql = "select count(*) from " + name;
        return jdbcNamedTemplate.getJdbcOperations().queryForObject(sql, Integer.class);
    }

    @Override
    public void htmlAllByName(String name) {
        String sql = "select * from " + name;
        List<Map<String, Object>> users = jdbcNamedTemplate.getJdbcOperations().queryForList(sql);

        for (Map<String, Object> user : users) {
            user.forEach((key, val) -> {
                System.out.println(key + ": " + val);
            });
            System.out.println("");
        }
    }

    @Override
    public Pair extractPair() {
        String sql = "select * from p_user limit 1";
        User user = jdbcNamedTemplate.getJdbcOperations().queryForObject(sql, new UserRowMapper());

        return Pair.of(user.getUsername(), user.getEmail());
    }

    @Override
    public int createUser(Long userId, String username, String password, String email) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", userId);
        params.put("un", username);
        params.put("pass", password);
        params.put("email", email);
        String query = "insert into p_user(ID, USERNAME, PASSWORD, EMAIL) values(:id, :un, :pass, :email)";

        jdbcNamedTemplate.getJdbcOperations().update(query);
        return 1;
    }

    @Override
    public int deleteById(Long userId) {
        String sql = "delete from p_user where id= :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", userId);

        jdbcNamedTemplate.query(sql, params, new UserRowMapper());
        return 0;
    }

    @Override
    public List<Map<String, Object>> findAllAsMaps() {
        throw new UnsupportedOperationException("Method not supported!");
    }

    @Override
    public User findById(Long id) {
        String sql = "select id, email, username,password from p_user where id= :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        List<User> users = jdbcNamedTemplate.query(sql, params, new UserRowMapper());

        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public Set<User> findAllByUserName(String username, boolean exactMatch) {
        String sql = "select id, username, email, password from p_user where ";
        if (exactMatch) {
            sql += "username= :un";
        } else {
            sql += "username like '%' || :un || '%'";
        }
        Map<String, Object> params = new HashMap<>();
        params.put("un", username);
        return new HashSet<>(jdbcNamedTemplate.query(sql, params, rowMapper));
    }

    @Override
    public int updatePassword(Long userId, String newPass) {
        String sql = "update p_user set password= :pass where ID = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("pass", newPass);
        params.put("id", userId);
        return jdbcNamedTemplate.update(sql, params);
    }

    @Override
    public int updateUsername(Long userId, String username) {
        String sql = "update p_user set username=:un where ID = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("un", username);
        params.put("id", userId);
        return jdbcNamedTemplate.update(sql, params);
    }

    @Override
    public String findUsernameById(Long id) {
        String sql = "select email from p_user where ID = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbcNamedTemplate.queryForObject(sql, params, String.class);
    }

    @Override
    public Map<String, Object> findByIdAsMap(Long id) {
        String sql = "select * from p_user where ID = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbcNamedTemplate.queryForMap(sql, params);
    }

    @Override
    public int countUsers() {
        String sql = "select count(*) from p_user";
        return jdbcNamedTemplate.queryForObject(sql, new HashMap<>(), Integer.class);
    }

}

package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public List<User> getAll() { return userMapper.findAll(); }

    @PostMapping
    public String add(@RequestBody User user) {
        userMapper.insert(user);
        return "插入成功，ID 为：" + user.getId();
    }

    @PutMapping
    public String update(@RequestBody User user) {
        userMapper.update(user);
        return "更新成功";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        userMapper.delete(id);
        return "删除成功";
    }
}
package com.ProyectoTingeso1.BackendProyecto1.Controllers;

import com.ProyectoTingeso1.BackendProyecto1.Entities.User;
import com.ProyectoTingeso1.BackendProyecto1.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User userNew = userService.saveUser(user);
        return ResponseEntity.ok(userNew);
    }
}

package com.revature.Project2.controllers;

import com.revature.Project2.models.JsonResponse;
import com.revature.Project2.models.User;
import com.revature.Project2.models.UserDTO;
import com.revature.Project2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "session")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class SessionController {
    private UserService userService;

    @Autowired
    public SessionController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<JsonResponse> login(HttpSession httpSession, @RequestBody User requestBody){

        User user = this.userService.validateCredentials(requestBody);

        if(user == null) {
            return ResponseEntity.ok(new JsonResponse("invalid username or password", null));
        }
        UserDTO userDTO = new UserDTO(user);
        httpSession.setAttribute("user-session", userDTO);

        //Tests will fail if the value is an object
        return ResponseEntity.ok(new JsonResponse("login successful", userDTO));

    }

    @GetMapping
    public JsonResponse checkSession(HttpSession httpSession){
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user-session");

        if(userDTO == null)
            return new JsonResponse("no session found", null);


        User user = this.userService.getOneUser(userDTO.getId());

        return new JsonResponse("session found", new UserDTO(user));

    }

    @DeleteMapping
    public JsonResponse logout(HttpSession httpSession){
        httpSession.invalidate();
        return new JsonResponse("you have been logged out", null);
    }
}


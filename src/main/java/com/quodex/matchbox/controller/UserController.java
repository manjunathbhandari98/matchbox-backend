package com.quodex.matchbox.controller;
import com.quodex.matchbox.dto.request.UpdatePasswordRequest;
import com.quodex.matchbox.dto.request.UserRequest;
import com.quodex.matchbox.dto.response.MemberResponse;
import com.quodex.matchbox.dto.response.UserResponse;
import com.quodex.matchbox.repository.UserRepository;
import com.quodex.matchbox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameAvailability(@RequestParam String username){
        Boolean exists = userRepository.existsByUsername(username);
        if (exists) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(false);
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String email, @RequestBody UserRequest request){
        return ResponseEntity.ok(userService.updateUser(email,request));
    }

    @PutMapping("/update-password/{email}")
    public ResponseEntity<String> updatePassword(@PathVariable String email,
                                            @RequestBody UpdatePasswordRequest request){
        return ResponseEntity.ok(userService.updatePassword(email,request));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MemberResponse>> searchUsers(
            @RequestParam("q") String query,
            @RequestParam("currentUserId") String currentUserId) {
        return ResponseEntity.ok(userService.searchUser(query, currentUserId));
    }

}

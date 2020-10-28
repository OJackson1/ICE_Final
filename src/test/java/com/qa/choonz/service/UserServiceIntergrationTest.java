package com.qa.choonz.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.choonz.persistence.domain.User;
import com.qa.choonz.persistence.repository.UserRepository;
import com.qa.choonz.rest.dto.UserDTO;

@SpringBootTest
public class UserServiceIntergrationTest {
	
    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repo;


    private  final List<UserDTO> userDTOList = new ArrayList<>();
    private User testUser;
    private User testUserWithId;

    private Long id;
    private UserDTO testUserDTO;

    private final String testUsername = "OJ";
    private final String testPassword = "password";


    private final String newUsername = "JJ";
    private final String newPassword = "abc";

    @Autowired
    private ModelMapper mapper;

    private UserDTO mapToDTO(User user) {
        return this.mapper.map(user, UserDTO.class);
    }

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.testUser = new User(this.testUsername,this.testPassword);
        this.testUserWithId = this.repo.save(this.testUser);
        this.testUserDTO = this.mapToDTO(testUserWithId);
        this.id = this.testUserWithId.getId();

    }

    @Test
    void testCreateUser() {
        assertThat(this.testUserDTO)
                .isEqualTo(this.service.create(this.testUser));
    }


    @Test
    void testReadByID() {
        assertThat(this.service.read(this.testUserWithId.getId()))
                .isEqualTo(this.mapToDTO(this.testUserWithId));
    }

    @Test
    void testReadUsers() {
        assertThat(this.service.read())
                .isEqualTo(Stream.of(this.mapToDTO(testUserWithId)).collect(Collectors.toList()));
    }

    @Test
    void testUpdateUser() {
        UserDTO newUser = new UserDTO(newUsername,newPassword);
        UserDTO updatedUser = new UserDTO(newUser.getUsername(), newUser.getPassword());
        updatedUser.setId(this.testUserWithId.getId());

        assertThat(this.service.update(newUser, this.testUserWithId.getId())).isEqualTo(updatedUser);
    }
    @Test
    void testDeleteUser() {
        assertThat(this.service.delete(this.testUserWithId.getId())).isTrue();
    }

}

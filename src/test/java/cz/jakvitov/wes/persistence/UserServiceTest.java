package cz.jakvitov.wes.persistence;

import cz.jakvitov.wes.persistence.service.UserService;
import org.hibernate.annotations.DialectOverride;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    public UserService userService;

    @Test
    @Disabled
    public void createUserTest(){
        userService.createUser("test@gmail.com", "Praha", "CZ", "DKJLJFD");
    }

    @Test
    @Disabled
    public void updateUserTest(){
        userService.updateUserCity("test@gmail.com", "Berlin", "GE");
    }

}

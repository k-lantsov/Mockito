package com.company.estore.service;

import com.company.estore.data.UserRepository;
import com.company.estore.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    EmailVerificationServiceImpl emailVerificationService;

    String firstname;
    String lastname;
    String email;
    String password;
    String repeatPassword;

    @BeforeEach
    void init() {
        firstname = "Kostia";
        lastname = "Lantsov";
        email = "k.lantsov@yahoo.com";
        password = "Jopamp113";
        repeatPassword = "Jopamp113";
    }

    @DisplayName("Create new user")
    @Test
    void testCreateUser_whenUserDetailsProvided_returnUserObject() {
        //Arrange
        Mockito.when(userRepository.save(any(User.class))).thenReturn(true);

        //Act
        User user = userService.createUser(firstname, lastname, email, password, repeatPassword);

        //Assert
        assertNotNull(user, "the createUser() should not have return null");
        assertNotNull(user.getId(), "User's id is missing");
        assertEquals(firstname, user.getFirstname(), "User's firstname is incorrect");
        assertEquals(lastname, user.getLastname(), "User's lastname is incorrect");
        assertEquals(email, user.getEmail(), "User's email is incorrect");
        verify(userRepository).save(any(User.class));
    }

    @DisplayName("Empty firstname causes correct exception")
    @Test
    void testCreateUser_whenFirstnameIsEmpty_throwsIllegalArgumentException() {
        //Arrange
        String firstname = "";
        String expectedExceptionMessage = "User's firstname is empty";

        //Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                        userService.createUser(firstname, lastname, email, password, repeatPassword),
                "Empty firstname should be caused an IllegalArgumentException");

        //Assert
        assertEquals(expectedExceptionMessage, thrown.getMessage(), "Exception error message is not correct");
    }

    @DisplayName("Empty lastname causes correct exception")
    @Test
    void testCreateUser_whenLastnameIsEmpty_throwsIllegalArgumentException() {
        //Arrange
        String lastname = "";
        String expectedExceptionMessage = "User's lastname is empty";

        //Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                        userService.createUser(firstname, lastname, email, password, repeatPassword),
                "Empty lastname should be caused an IllegalArgumentException");

        //Assert
        assertEquals(expectedExceptionMessage, thrown.getMessage(), "Exception error message is not correct");
    }

    @DisplayName("If save() causes RuntimeException, a UserServiceException is thrown")
    @Test
    void testCreateUser_whenSaveMethodThrowsException_thenThrowsUserServiceException() {
        //Arrange
        when(userRepository.save(any(User.class))).thenThrow(RuntimeException.class);

        //Act & Assert
        assertThrows(UserServiceException.class, () ->
                userService.createUser(firstname, lastname, email, password, repeatPassword),
                "Should have thrown UserServiceException instead");
    }

    @DisplayName("EmailNotificationServiceException is handled")
    @Test
    void testCreateUser_whenEmailNotificationExceptionThrown_thenThrowsUserServiceException() {
        when(userRepository.save(any(User.class))).thenReturn(true);
        doThrow(EmailNotificationServiceException.class)
                .when(emailVerificationService)
                .scheduleEmailConfirmation(any(User.class));

        assertThrows(UserServiceException.class,
                () -> userService.createUser(firstname, lastname, email, password, repeatPassword),
                "Should have thrown UserServiceException instead");

        verify(emailVerificationService).scheduleEmailConfirmation(any(User.class));
    }

    @DisplayName("Schedule Email Confirmation is executed")
    @Test
    void testCreateUser_whenUserCreated_schedulesEmailConfirmation() {
        //Arrange
        when(userRepository.save(any(User.class))).thenReturn(true);
        doCallRealMethod().when(emailVerificationService).scheduleEmailConfirmation(any(User.class));

        //Act
        userService.createUser(firstname, lastname, email, password, repeatPassword);

        //Assert
        verify(emailVerificationService).scheduleEmailConfirmation(any(User.class));
    }
}

package com.backend.boilerplate.repository;

import com.backend.boilerplate.AbstractIT;
import com.backend.boilerplate.entity.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.backend.boilerplate.entity.Status.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author sarvesh
 * @version 0.0.2
 * @since 0.0.1
 */
@ExtendWith(SpringExtension.class)
@Disabled
class UserRepositoryTest extends AbstractIT {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    private static final Long PERFORMED_BY = 1L;

    private User user;

    private Role role;

    private UserRole userRole;

    @BeforeEach
    public void setup() {
        /*********** Setup ************/
        Status status = CREATED;

        role = Role.builder()
            .name("Manager")
            .build();
        role = testEntityManager.persistAndFlush(role);
        assertNotNull(role.getId());

        UUID uuid = UUID.randomUUID();
        String email = "jack.sparrow@roche.com";
        String firstName = "jack";
        String lastName = "sparrow";
        user = User.builder()
            .uuid(uuid)
            .email(email)
            .firstName(firstName)
            .lastName(lastName)
            .status(status)
            .userRoles(new ArrayList<>())
            .build();

        userRole = new UserRole(user, role, PERFORMED_BY);

        user.getUserRoles().add(userRole);
    }

    @AfterEach
    public void cleanup() {
        user = null;
        role = null;
        userRole = null;
    }

    @Test
    public void saveUser_shouldPass_withUserRoleMapping() {
        /*********** Execute ************/
        user = userRepository.saveAndFlush(user);
        UserHistory userHistory = UserHistory.builder()
            .userId(user.getId())
            .uuid(user.getUuid())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .status(user.getStatus())
            .performedBy(PERFORMED_BY)
            .build();
        userHistory = userHistoryRepository.saveAndFlush(userHistory);
        /*********** Verify/Assertions ************/
        assertEquals(PERFORMED_BY, userHistory.getPerformedBy());

        assertNotNull(user.getId());
        assertEquals(CREATED, user.getStatus());
        assertEquals(PERFORMED_BY, userHistory.getPerformedBy());
        assertNotNull(user.getTimestamp());
        assertEquals(1, user.getUserRoles().size());
        assertEquals("Manager", user.getUserRoles().stream().findFirst().get().getRole().getName());

        assertNotNull(userHistory.getId());
        assertEquals(user.getId(), userHistory.getUserId());
    }

    private User setup_updateUser() {
        user = userRepository.saveAndFlush(user);
        user.getUserRoles().forEach(userRole -> {
            userRole.setUser(user);
            testEntityManager.persistAndFlush(userRole);
        });

        UserHistory userHistory = UserHistory.builder()
            .userId(user.getId())
            .uuid(user.getUuid())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .status(user.getStatus())
            .performedBy(PERFORMED_BY)
            .build();
        userHistory = userHistoryRepository.saveAndFlush(userHistory);

        assertNotNull(user.getId());
        assertEquals(CREATED, user.getStatus());

        String newEmail = "jack.sparrow@contacrtors.roche.com";
        user.setEmail(newEmail);
        user.setStatus(Status.UPDATED);
        return user;
    }

    @Test
    public void updateUser_shouldPass_withNewDetails() {
        /*********** Setup ************/
        user = setup_updateUser();
        /*********** Execute ************/
        user = userRepository.saveAndFlush(user);
        UserHistory userHistory = UserHistory.builder()
            .userId(user.getId())
            .uuid(user.getUuid())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .status(user.getStatus())
            .performedBy(PERFORMED_BY)
            .build();
        userHistory = userHistoryRepository.saveAndFlush(userHistory);
        /*********** Verify/Assertions ************/
        assertEquals(PERFORMED_BY, userHistory.getPerformedBy());

        assertNotNull(user.getId());
        assertEquals(UPDATED, user.getStatus());
        assertEquals(PERFORMED_BY, userHistory.getPerformedBy());
        assertNotNull(user.getTimestamp());
        assertEquals("jack.sparrow@contacrtors.roche.com", user.getEmail());

        assertNotNull(userHistory.getId());
        assertEquals(user.getId(), userHistory.getUserId());
        assertEquals(UPDATED, userHistory.getStatus());
        assertEquals("jack.sparrow@contacrtors.roche.com", userHistory.getEmail());
    }

    private User setup_deleteUser() {
        user = userRepository.saveAndFlush(user);
        UserHistory userHistory = UserHistory.builder()
            .userId(user.getId())
            .uuid(user.getUuid())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .status(user.getStatus())
            .performedBy(PERFORMED_BY)
            .build();
        userHistory = userHistoryRepository.saveAndFlush(userHistory);

        assertNotNull(user.getId());
        assertEquals(CREATED, user.getStatus());

        return user;
    }

    @Test
    public void deleteUser_shouldPass() {
        /*********** Setup ************/
        user = setup_deleteUser();
        /*********** Execute ************/
        userRepository.delete(user);
        user.setStatus(Status.DELETED);
        UserHistory userHistory = UserHistory.builder()
            .userId(user.getId())
            .uuid(user.getUuid())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .status(user.getStatus())
            .performedBy(PERFORMED_BY)
            .build();
        userHistory = userHistoryRepository.saveAndFlush(userHistory);
        /*********** Verify/Assertions ************/
        assertEquals(PERFORMED_BY, userHistory.getPerformedBy());

        assertTrue(userRepository.findById(user.getId()).isEmpty());

        assertNotNull(userHistory.getId());
        assertEquals(user.getId(), userHistory.getUserId());
        assertEquals(DELETED, userHistory.getStatus());
    }

    private User setup_getUserById() {
        user = userRepository.saveAndFlush(user);
        UserHistory userHistory = UserHistory.builder()
            .userId(user.getId())
            .uuid(user.getUuid())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .status(user.getStatus())
            .performedBy(PERFORMED_BY)
            .build();
        userHistory = userHistoryRepository.saveAndFlush(userHistory);
        return user;
    }

    @Test
    public void getUser_shouldPass_withGivenId() {
        /*********** Setup ************/
        user = setup_getUserById();
        /*********** Execute ************/
        Optional<User> userOptional = userRepository.findById(user.getId());
        /*********** Verify/Assertions ************/
        assertTrue(userOptional.isPresent());
        User userById = userOptional.get();
        assertNotNull(userById);
        assertEquals(1, userById.getUserRoles().size());
        assertEquals("Manager", userById.getUserRoles().stream().findFirst().get().getRole().getName());
    }

    private List<User> setup_getAllUsers() {
        UUID uuid1 = UUID.randomUUID();
        String email1 = "jack.sparrow@roche.com";
        String firstName1 = "jack";
        String lastName1 = "sparrow";
        User user1 = User.builder()
            .uuid(uuid1)
            .email(email1)
            .firstName(firstName1)
            .lastName(lastName1)
            .status(CREATED)
            .userRoles(new ArrayList<>())
            .build();
        UserRole userRole1 = new UserRole(user1, role, PERFORMED_BY);
        user1.getUserRoles().add(userRole1);

        UUID uuid2 = UUID.randomUUID();
        String email2 = "barbosa@roche.com";
        String firstName2 = "barbosa";
        String lastName2 = "blackpearl";
        User user2 = User.builder()
            .uuid(uuid2)
            .email(email2)
            .firstName(firstName2)
            .lastName(lastName2)
            .status(CREATED)
            .userRoles(new ArrayList<>())
            .build();
        UserRole userRole2 = new UserRole(user2, role, PERFORMED_BY);
        user2.getUserRoles().add(userRole2);

        List<User> users = List.of(user1, user2);

        userRepository.saveAll(users);
        userRepository.flush();
        users.forEach(u -> {
            UserHistory userHistory = UserHistory.builder()
                .userId(u.getId())
                .uuid(u.getUuid())
                .email(u.getEmail())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .status(u.getStatus())
                .performedBy(PERFORMED_BY)
                .build();
            userHistory = userHistoryRepository.saveAndFlush(userHistory);
        });
        return users;
    }

    @Test
    public void getAllUsers_shouldPass() {
        /*********** Setup ************/
        List<User> expectedUsers = setup_getAllUsers();
        /*********** Execute ************/
        List<User> actualUsers = userRepository.findAll();
        /*********** Verify/Assertions ************/
        assertNotNull(expectedUsers);
        assertEquals(2, expectedUsers.size());
        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertTrue(expectedUsers.containsAll(actualUsers));
    }

    private User setup_saveInvalidUser() {
        user.setEmail(null);
        return user;
    }

    @Test
    public void saveUser_shouldThrowSQLException_forConstraintViolations() {
        /*********** Setup ************/
        user = setup_saveInvalidUser();
        /*********** Execute and Verify/Assertions ************/
        DataIntegrityViolationException exception = Assertions.assertThrows(DataIntegrityViolationException.class, () ->
        {
            user = userRepository.saveAndFlush(user);
        });
    }
}

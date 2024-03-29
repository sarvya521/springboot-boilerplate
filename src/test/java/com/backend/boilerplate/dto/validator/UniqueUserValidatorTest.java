package com.backend.boilerplate.dto.validator;

import com.backend.boilerplate.AbstractIT;
import com.backend.boilerplate.dto.UpdateUserDto;
import com.backend.boilerplate.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.backend.boilerplate.entity.Status.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
@ExtendWith(SpringExtension.class)
@Disabled
public class UniqueUserValidatorTest extends AbstractIT {

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Autowired
    private TestEntityManager testEntityManager;

    private List<User> users = new ArrayList<>();

    @BeforeEach
    void setup() {
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setFirstName("John");
        user.setLastName("Snow");
        user.setEmail("johnsnow@mail.com");
        user.setStatus(CREATED);
        //user.setPerformedBy(1L);
        user = testEntityManager.persistAndFlush(user);

        User secondUser = new User();
        secondUser.setUuid(UUID.randomUUID());
        secondUser.setFirstName("Tyrion");
        secondUser.setLastName("Lannister");
        secondUser.setEmail("tyrionlannister@mail.com");
        secondUser.setStatus(CREATED);
        //secondUser.setPerformedBy(1L);
        secondUser = testEntityManager.persistAndFlush(secondUser);

        users.add(user);
        users.add(secondUser);
    }

    @Test
    void updateUser_shouldPass_noDuplicateEmail() {
        UUID uuid = users.get(0).getUuid();
        UpdateUserDto dto = updateUserDto(uuid, "khaldrogo@mail.com");
        Set<ConstraintViolation<UpdateUserDto>> constraintViolations = validator.validate(dto,
            ConstraintSequence.class);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    void updateUser_shouldThrow_userNotFound() {
        UUID uuid = UUID.randomUUID();
        UpdateUserDto dto = updateUserDto(uuid, "khaldrogo@mail.com");
        Set<ConstraintViolation<UpdateUserDto>> constraintViolations = validator.validate(dto,
            ConstraintSequence.class);
        assertFalse(constraintViolations.isEmpty());
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.stream().findFirst().get().getMessage()).isEqualTo("1004");
    }

    @Test
    void updateUser_shouldThrow_duplicateEmail() {
        UUID uuid = users.get(0).getUuid();
        UpdateUserDto dto = updateUserDto(uuid, "tyrionlannister@mail.com");
        Set<ConstraintViolation<UpdateUserDto>> constraintViolations = validator.validate(dto,
            ConstraintSequence.class);
        assertFalse(constraintViolations.isEmpty());
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.stream().findFirst().get().getMessage()).isEqualTo("1061");
    }

    private UpdateUserDto updateUserDto(UUID uuid, String email) {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setUuid(uuid);
        updateUserDto.setFirstName("Khal");
        updateUserDto.setLastName("Drogo");
        updateUserDto.setEmail(email);
        return updateUserDto;
    }
}
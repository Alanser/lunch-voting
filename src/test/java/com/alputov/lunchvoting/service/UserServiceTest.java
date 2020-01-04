package com.alputov.lunchvoting.service;

import com.alputov.lunchvoting.model.Role;
import com.alputov.lunchvoting.model.User;
import com.alputov.lunchvoting.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolationException;

import static com.alputov.lunchvoting.TestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest extends AbstractServiceTest {

    @Autowired
    protected UserService service;

    @Test
    void create() throws Exception {
        User newUser = getNewUser();
        User created = service.create(new User(newUser));
        Integer newId = created.getId();
        newUser.setId(newId);
        USER_MATCHERS.assertMatch(created, newUser);
        USER_MATCHERS.assertMatch(service.get(newId), newUser);
    }

    @Test
    void duplicateMailCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.ROLE_USER)));
    }

    @Test
    void delete() throws Exception {
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () ->
                service.delete(USER_ID));
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1000000));
    }

    @Test
    void get() throws Exception {
        User user = service.get(ADMIN_ID);
        USER_MATCHERS.assertMatch(user, ADMIN);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(1000000));
    }

    @Test
    void getByEmail() throws Exception {
        User user = service.getByEmail("admin@gmail.com");
        USER_MATCHERS.assertMatch(user, ADMIN);
    }

    @Test
    void update() throws Exception {
        User updated = getUpdatedUser();
        service.update(new User(updated));
        USER_MATCHERS.assertMatch(service.get(USER_ID), updated);
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "  ", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.ROLE_USER)), ConstraintViolationException.class);
    }

}
package com.alputov.lunchvoting.service;

import com.alputov.lunchvoting.AuthorizedUser;
import com.alputov.lunchvoting.model.User;
import com.alputov.lunchvoting.repository.UserRepository;
import com.alputov.lunchvoting.to.UserTo;
import com.alputov.lunchvoting.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.alputov.lunchvoting.util.UserUtil.prepareToSave;
import static com.alputov.lunchvoting.util.ValidationUtil.checkNotFound;
import static com.alputov.lunchvoting.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return prepareAndSave(user);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public User get(int id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        prepareAndSave(user);
    }

    @Transactional
    public void update(UserTo userTo) {
        User user = get(userTo.id());
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }

    private User prepareAndSave(User user) {
        return repository.save(prepareToSave(user, passwordEncoder));
    }
}
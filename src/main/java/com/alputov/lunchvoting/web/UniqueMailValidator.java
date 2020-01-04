package com.alputov.lunchvoting.web;


import com.alputov.lunchvoting.HasEmail;
import com.alputov.lunchvoting.model.User;
import com.alputov.lunchvoting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;


@Component
public class UniqueMailValidator implements org.springframework.validation.Validator {

    @Autowired
    private UserRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return HasEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasEmail user = ((HasEmail) target);
        User dbUser = repository.getByEmail(user.getEmail().toLowerCase());
        if (dbUser != null && !dbUser.getId().equals(user.getId())) {
            errors.rejectValue("email", ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL);
        }
    }
}

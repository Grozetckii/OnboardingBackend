package ru.grozetckii.SecurityApp.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.grozetckii.SecurityApp.models.Person;
import ru.grozetckii.SecurityApp.services.PersonDetailsService;
import ru.grozetckii.SecurityApp.util.exceptiions.PersonAlreadyExistsException;

@Component
public class PersonValidator implements Validator {
    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person)o;

        try {
            personDetailsService.loadUserByUsername(person.getUsername());
        } catch (UsernameNotFoundException e){
            return;
        }
        //errors.rejectValue("username", "user с именем пользователя'" + person.getUsername() + "' уже существует");
        throw new PersonAlreadyExistsException(person.getUsername());
    }
}

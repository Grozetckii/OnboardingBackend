package ru.grozetckii.SecurityApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.grozetckii.SecurityApp.models.Person;
import ru.grozetckii.SecurityApp.repositories.PeopleRepository;
import ru.grozetckii.SecurityApp.util.exceptiions.PersonAlreadyExistsException;

@Service
public class RegistrationService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person){
        if(peopleRepository.findByUsername(person.getUsername()).isPresent()){
            throw new PersonAlreadyExistsException(person.getUsername());
        }
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        peopleRepository.save(person);
    }
}

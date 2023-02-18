package ru.grozetckii.SecurityApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.grozetckii.SecurityApp.models.Person;
import ru.grozetckii.SecurityApp.repositories.PeopleRepository;
import ru.grozetckii.SecurityApp.security.PersonDetails;
import ru.grozetckii.SecurityApp.util.exceptiions.PersonNotFoundException;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(username);

        if(person.isEmpty())
            throw new PersonNotFoundException(username);

        return new PersonDetails(person.get());
    }
}

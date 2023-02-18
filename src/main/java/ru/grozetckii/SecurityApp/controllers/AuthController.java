package ru.grozetckii.SecurityApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.grozetckii.SecurityApp.dto.AuthenticationDTO;
import ru.grozetckii.SecurityApp.dto.PersonDTO;
import ru.grozetckii.SecurityApp.models.Person;
import ru.grozetckii.SecurityApp.security.JWTUtil;
import ru.grozetckii.SecurityApp.services.RegistrationService;
import ru.grozetckii.SecurityApp.util.ErrorResponse;
import ru.grozetckii.SecurityApp.util.PersonValidator;
import ru.grozetckii.SecurityApp.util.exceptiions.IncorrectPasswordException;
import ru.grozetckii.SecurityApp.util.exceptiions.PersonAlreadyExistsException;
import ru.grozetckii.SecurityApp.util.exceptiions.PersonNotFoundException;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService, JWTUtil jwtUtil,
                          ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid AuthenticationDTO authenticationDTO){
        System.out.println("зашли в метод login");
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());
        System.out.println("получили логин и пароль");

        try{
            authenticationManager.authenticate(authInputToken);
        }catch (BadCredentialsException e){
            throw new IncorrectPasswordException();
        }
        System.out.println("успешно авторизовались");
        String accessToken = jwtUtil.generateAccessToken(authenticationDTO.getUsername());
        System.out.println("токен сгенерирован");

        return new ResponseEntity<>(Map.of("accessToken", accessToken, "refreshToken", "test-refresh-token"), HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> registration(@RequestBody @Valid PersonDTO personDTO){
        Person person = convertToPerson(personDTO);

        //personValidator.validate(person, bindingResult);

        registrationService.register(person);

        String accessToken = jwtUtil.generateAccessToken(person.getUsername());
        return new ResponseEntity<>(Map.of("accessToken", accessToken, "refreshToken", "test-refresh-token"), HttpStatus.OK);
    }

    public Person convertToPerson(PersonDTO personDTO){
        return this.modelMapper.map(personDTO, Person.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlePersonNotFoundException(PersonNotFoundException e){
        ErrorResponse response = new ErrorResponse(
                "user '" + e.getUsername() + "' not found!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlePersonAlreadyExistsException(PersonAlreadyExistsException e){
        ErrorResponse response = new ErrorResponse(
                "user with username '" + e.getUsername() + "' already exists!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleIncorrectPasswordException(IncorrectPasswordException e){
        ErrorResponse response = new ErrorResponse(
                "incorrect password!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.main.beans.Person;
import com.timsanalytics.crc.main.services.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/person")
@Tag(name = "Person", description = "Person")
public class PersonController {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @ResponseBody
    @RequestMapping(value = "/relationship", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Student", tags = {"Student"}, description = "Create Student", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Long> createPerson_Relationship(@RequestBody Person person) {
        try {
            return ResponseEntity.ok()
                    .body(personService.createPerson_Relationship(person));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

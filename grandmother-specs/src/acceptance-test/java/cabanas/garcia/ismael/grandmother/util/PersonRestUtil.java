package cabanas.garcia.ismael.grandmother.util;

import cabanas.garcia.ismael.grandmother.model.Person;
import cabanas.garcia.ismael.grandmother.util.http.Response;

import java.util.HashMap;
import java.util.Map;

public final class PersonRestUtil {

    private static final String PERSONS_ENDPOINT = "http://localhost:8080/persons";

    private static final Class<Person> TARGET_CLASS = Person.class;

    private static Map<String, Person> persons = new HashMap<>();

    public static Person getPerson(String name) {
        if(persons.containsKey(name))
            return persons.get(name);

        Person newPerson = createPerson(name);
        persons.put(newPerson.getName(), newPerson);
        return newPerson;
    }

    private static Person createPerson(String name) {
        Person personPayload = Person.builder().name(name).build();

        Response postAndGetResponse = RestUtil.postAndGet(PERSONS_ENDPOINT, personPayload, TARGET_CLASS);

        return (Person) postAndGetResponse.getContent().get();
    }
}

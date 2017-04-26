package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.controller.request.PersonRequestBody
import cabanas.garcia.ismael.grandmother.domain.person.Person
import cabanas.garcia.ismael.grandmother.service.PersonService
import cabanas.garcia.ismael.grandmother.stubs.service.person.PersonServiceDefaultStub
import cabanas.garcia.ismael.grandmother.utils.test.PersonUtil
import groovy.json.JsonOutput
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class PersonControllerSpec extends Specification{

    def "should return status 200 when hits the URL for getting an existing person"(){
        given: "a given person identifier"
            Person person = getDefaultPersistedPerson()
        and: "person controller configured with stub service"
            PersonService personService = new PersonServiceDefaultStub(person)
            PersonController controller = new PersonController(personService: personService)
        when: "REST person get url is hit"
            def response = sendGet(controller, "/persons/$person.id")
        then:
            responseStatusCodeIsOk(response)
            responseContentTypeIsJson(response)
    }

    def "should return status 404 when hits the URL for getting an existing person and that person not exist"(){
        given: "a given person identifier that not exist"
            long personId = 1
        and: "person controller configured with stub service"
            PersonService personService = new PersonServiceDefaultStub()
            PersonController controller = new PersonController(personService: personService)
        when: "REST person get url is hit"
            def response = sendGet(controller, "/persons/$personId")
        then:
            responseStatusCodeIsNotFound(response)
    }

    def "should return status 201 when hits URL for creating a person"(){
        given: "person controller configured with his services"
            PersonService personService = new PersonServiceDefaultStub()
            PersonController controller = new PersonController(personService: personService)
        and: "a request body with person data"
            PersonRequestBody personRequestBody = PersonRequestBody.builder()
                .name(PersonUtil.DEFAULT_PERSON_NAME)
                .build()
        when: "REST person post url is hit"
            def response = sendPost(controller, "/persons", personRequestBody)
        then:
            response.status == HttpStatus.CREATED.value()
            response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
    }

    def "response should have location header with get URL for new resource when hits URL for creating a person"(){
        given: "person controller configured with his services"
            PersonService personService = new PersonServiceDefaultStub()
            PersonController controller = new PersonController(personService: personService)
        and: "a request body with person data"
            PersonRequestBody personRequestBody = PersonRequestBody.builder()
                .name(PersonUtil.DEFAULT_PERSON_NAME)
                .build()
        when: "REST person post url is hit"
            def response = sendPost(controller, "/persons", personRequestBody)
        then:
            response.getHeader("Location") == "http://localhost/persons/1"
    }

    def responseStatusCodeIsNotFound(MockHttpServletResponse response) {
        response.status == HttpStatus.NOT_FOUND.value()
    }

    def Person getDefaultPersistedPerson() {
        PersonUtil.defaultPersonPersisted
    }

    def sendGet(controller, path) {
        MockMvc mockMvc = standaloneSetup(controller).build()
        def response = mockMvc.perform(
                get(path)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)

        )
                .andDo(log())
                .andReturn().response

        return response
    }

    def sendPost(controller, path, body){
        MockMvc mockMvc = standaloneSetup(controller)
                .build()
        def response = mockMvc.perform(
                post(path)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJson(body))
        )
                .andDo(log())
                .andReturn().response

        return response
    }

    def toJson(Object object) {
        return JsonOutput.toJson(object)
    }

    def responseStatusCodeIsOk(MockHttpServletResponse response) {
        response.status == HttpStatus.OK.value()
    }

    def responseContentTypeIsJson(MockHttpServletResponse response) {
        response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
    }
}

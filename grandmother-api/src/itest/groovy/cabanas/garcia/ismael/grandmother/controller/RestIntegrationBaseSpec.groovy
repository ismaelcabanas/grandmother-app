package cabanas.garcia.ismael.grandmother.controller

import cabanas.garcia.ismael.grandmother.Profiles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * Created by XI317311 on 12/12/2016.
 */
@ContextConfiguration // not mentioned by docs, but had to include this for Spock to startup the Spring context
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(Profiles.INTEGRATION)
abstract class RestIntegrationBaseSpec extends Specification{

    @Value('${local.server.port}')
    int port

    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate()

    URI serviceURI(String path = "") {
        new URI("http://localhost:$port${path}")
    }

}

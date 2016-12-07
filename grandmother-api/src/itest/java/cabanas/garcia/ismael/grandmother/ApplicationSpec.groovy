package cabanas.garcia.ismael.grandmother

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

/**
 * Created by XI317311 on 07/12/2016.
 */
@ContextConfiguration // not mentioned by docs, but had to include this for Spock to startup the Spring context
@SpringBootTest
class ApplicationSpec extends Specification{

    @Autowired
    WebApplicationContext context

    def "should boot up without errors"() {
        expect: "web application context exists"
        context != null
    }
}

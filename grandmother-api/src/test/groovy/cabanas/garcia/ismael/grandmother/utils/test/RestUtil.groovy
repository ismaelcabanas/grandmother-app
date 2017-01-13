package cabanas.garcia.ismael.grandmother.utils.test

import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

/**
 * Created by XI317311 on 30/12/2016.
 */
final class RestUtil {

    static sendGet(controller, path) {
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

    static boolean responseStatusCodeIsOk(MockHttpServletResponse response) {
        response.status == HttpStatus.OK.value()
    }

    static boolean responseContentTypeIsJson(MockHttpServletResponse response) {
        response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
    }

    static boolean notExistDepositsInAccount(MockHttpServletResponse response) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        jsonResponse.deposits.size == 0
        jsonResponse.total == AccountUtil.ZERO
    }
}

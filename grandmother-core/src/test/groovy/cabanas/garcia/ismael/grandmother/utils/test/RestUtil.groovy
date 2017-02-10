package cabanas.garcia.ismael.grandmother.utils.test

import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc

/**
 * Created by XI317311 on 30/12/2016.
 */
final class RestUtil {

    static sendGet(controller, path) {
        MockMvc mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup(controller).build()
        def response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get(path)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)

        )
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.log())
                .andReturn().response

        return response
    }

    static void responseStatusCodeIs200(MockHttpServletResponse response) {
        assert response.status == HttpStatus.OK.value()
    }

    static void responseContentTypeIsJson(MockHttpServletResponse response) {
        assert response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
    }

    static boolean notExistDepositsInAccount(MockHttpServletResponse response) {
        def jsonResponse = new JsonSlurper().parseText(response.contentAsString)
        jsonResponse.deposits.size == 0
        jsonResponse.total == AmountUtil.ZERO
    }
}

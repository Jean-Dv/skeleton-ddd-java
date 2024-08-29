package dev.jean.apps;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ApplicationTestCase {
    @Autowired
    private MockMvc mockMvc;

    protected void assertResponse(String endpoint, Integer expectedStatusCode, String expectedResponse)
            throws Exception {
        ResultMatcher response = expectedResponse.isEmpty() ? content().string("") : content().json(expectedResponse);

        mockMvc.perform(get(endpoint)).andExpect(status().is(expectedStatusCode)).andExpect(response);
    }

    protected void assertResponse(String endpoint, Integer expectedStatusCode, String expectedResponse,
            HttpHeaders headers) throws Exception {
        ResultMatcher response = expectedResponse.isEmpty() ? content().string("") : content().json(expectedResponse);

        mockMvc.perform(get(endpoint).headers(headers)).andExpect(status().is(expectedStatusCode)).andExpect(response);
    }

    protected void assertRequestWithBody(String method, String endpoint, String body, Integer expectedStatusCode)
            throws Exception {
        mockMvc
                .perform(request(HttpMethod.valueOf(method), endpoint).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().string(""));
    }

    protected void assertRequest(String method, String endpoint, Integer expectedStatusCode) throws Exception {
        mockMvc
                .perform(request(HttpMethod.valueOf(method), endpoint))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().string(""));
    }
}

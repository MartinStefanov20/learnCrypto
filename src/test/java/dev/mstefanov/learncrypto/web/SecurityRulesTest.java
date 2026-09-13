package dev.mstefanov.learncrypto.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies the authorization rules of {@link dev.mstefanov.learncrypto.config.SecurityConfig}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityRulesTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void anonymousQuizRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/quiz"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    void staticAssetsArePublic() throws Exception {
        mockMvc.perform(get("/assets/css/style.css"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/css"));
    }

    @Test
    void landingAndLoginPagesArePublic() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(get("/users/login")).andExpect(status().isOk());
    }

    @Test
    void healthEndpointIsPublic() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void envEndpointIsNotReachableAnonymously() throws Exception {
        // Not exposed (management.endpoints.web.exposure.include=health,info) and behind the
        // authenticated() catch-all, so an anonymous caller is bounced to the login page.
        mockMvc.perform(get("/actuator/env"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser
    void envEndpointIsNotExposedEvenWhenAuthenticated() throws Exception {
        mockMvc.perform(get("/actuator/env"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void chartsPageIsAvailableToAuthenticatedUsers() throws Exception {
        mockMvc.perform(get("/charts"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/html"));
    }
}

package dev.mstefanov.learncrypto.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Form login against the seeded demo accounts (see {@code UserServiceImpl#initUsers()}).
 */
@SpringBootTest
@AutoConfigureMockMvc
class LoginFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void demoUserLogsInAndLandsOnHome() throws Exception {
        mockMvc.perform(formLogin("/users/login").user("demo").password("demo123"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/home"))
                .andExpect(authenticated().withUsername("demo").withRoles("USER"));
    }

    @Test
    void adminUserHasBothRoles() throws Exception {
        mockMvc.perform(formLogin("/users/login").user("admin").password("admin123"))
                .andExpect(redirectedUrl("/home"))
                .andExpect(authenticated().withUsername("admin").withRoles("ADMIN", "USER"));
    }

    @Test
    void wrongPasswordIsForwardedToLoginError() throws Exception {
        // SecurityConfig uses failureForwardUrl, so the failed POST is forwarded (not redirected)
        // to UserController#onLoginError, which re-renders the login page with an error flag.
        mockMvc.perform(formLogin("/users/login").user("demo").password("nope"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/users/login-error"))
                .andExpect(unauthenticated());
    }

    @Test
    void unknownUserIsRejected() throws Exception {
        mockMvc.perform(formLogin("/users/login").user("ghost").password("whatever"))
                .andExpect(forwardedUrl("/users/login-error"))
                .andExpect(unauthenticated());
    }

    @Test
    void loginWithoutCsrfTokenIsDenied() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/users/login").param("username", "demo").param("password", "demo123"))
                .andExpect(status().isForbidden())
                .andExpect(unauthenticated());
    }
}

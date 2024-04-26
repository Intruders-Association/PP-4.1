package com.itm.space.backendresources.controller;

import com.github.javafaker.Faker;
import com.itm.space.backendresources.BaseIntegrationTest;
import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends BaseIntegrationTest {

    @MockBean
    UserService userService;

    Faker faker = new Faker();
    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateUser() throws Exception {
        UserRequest userRequest = new UserRequest(faker.name().username(), faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName(), faker.name().lastName());
        mvc.perform(requestWithContent(post("/api/users"), userRequest)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateBlankName() throws Exception {
        UserRequest userRequest = new UserRequest("", faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName(), faker.name().lastName());
        mvc.perform(requestWithContent(post("/api/users"), userRequest)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateInvalidName() throws Exception {
        UserRequest userRequestSize = new UserRequest("a", faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName(), faker.name().lastName());
        mvc.perform(requestWithContent(post("/api/users"), userRequestSize)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateBlankEmail() throws Exception {
        UserRequest userRequest = new UserRequest(faker.name().username(), "", faker.internet().password(), faker.name().firstName(), faker.name().lastName());
        mvc.perform(requestWithContent(post("/api/users"), userRequest)).andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateInvalidEmail() throws Exception {
        UserRequest userRequest = new UserRequest(faker.name().username(), "invalid_email", faker.internet().password(), faker.name().firstName(), faker.name().lastName());
        mvc.perform(requestWithContent(post("/api/users"), userRequest)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateBlankPassword() throws Exception {
        UserRequest userRequest = new UserRequest(faker.name().username(), faker.internet().emailAddress(), "", faker.name().firstName(), faker.name().lastName());
        mvc.perform(requestWithContent(post("/api/users"), userRequest)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateInvalidPassword() throws Exception {
        UserRequest userRequest = new UserRequest(faker.name().username(), faker.internet().emailAddress(), "123", faker.name().firstName(), faker.name().lastName());
        mvc.perform(requestWithContent(post("/api/users"), userRequest)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateFirstNameBlank() throws Exception {
        UserRequest userRequest = new UserRequest(faker.name().username(), faker.internet().emailAddress(), faker.internet().password(), "", faker.name().lastName());
        mvc.perform(requestWithContent(post("/api/users"), userRequest)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateLastNameBlank() throws Exception {
        UserRequest userRequest = new UserRequest(faker.name().username(), faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName(), "");
        mvc.perform(requestWithContent(post("/api/users"), userRequest)).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCreateWithRolesUser() throws Exception {
        UserRequest userRequest = new UserRequest(faker.name().username(), faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName(), faker.name().lastName());
        mvc.perform(requestWithContent(post("/api/users"), userRequest)).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void getUserByIdTestModerator() throws Exception {
        UUID id = UUID.fromString("fe67cffe-d487-4b1a-bd57-3956a60fb2d6");
        mvc.perform(get("/api/users/" + id)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserByIdTestUser() throws Exception {
        UUID uuid = UUID.fromString("fe67cffe-d487-4b1a-bd57-3956a60fb2d6");
        mvc.perform(get("/api/users/" + uuid)).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void testNotFoundEndpoint() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/nonexistent"))
                .andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testHelloModerator() throws Exception {
        mvc.perform(get("/api/users/hello")).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testHelloUser() throws Exception {
        mvc.perform(get("/api/users/hello")).andDo(print())
                .andExpect(status().isForbidden());
    }

}

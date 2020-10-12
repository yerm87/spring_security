package com.example.SpringSecurityPractice.controllers;

import com.example.SpringSecurityPractice.models.User;
import com.example.SpringSecurityPractice.repo.UserRepository;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(value="/queries/clean_table_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestPropertySource(locations="/test-application.properties")
public class TemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    //@Autowired
    //private WebApplicationContext context;

    /*public void setup(){
        mockMvc=MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }*/

    @Test
    public void simpleTest(){
        int x = 1;
        int y = 2;
        int z = x+y;
        Assert.assertEquals(3, z);

    }

    @Test
    public void coursesPageTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        StringContains.containsString("Please")));
    }

    @Test
    public void studentApiTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/1")
                .with(user("roman").password("password").roles("STUDENT")))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value={"/queries/create_user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testLoginPage() throws Exception {
        mockMvc.perform(formLogin().user("mikhalych").password("pass"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void badCredentials() throws Exception {
        this.mockMvc.perform(post("/login").with(user("mikhalych").password("123")))
                .andExpect(status().isForbidden());
    }
}

package qoraa.net.modules.user.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import qoraa.net.AbstractWebMvcTest;
import qoraa.net.common.json.JsonWriter;
import qoraa.net.modules.user.controller.model.request.CreateUserRequest;
import qoraa.net.modules.user.mapper.UserMapper;
import qoraa.net.modules.user.model.User;
import qoraa.net.modules.user.service.UserService;

@WebMvcTest(WebUserController.class)
class WebUserControllerTest extends AbstractWebMvcTest {

    private static final String USERS_WEB_API_URL = "/webapi/v0/users";

    private final JsonWriter jsonWriter = JsonWriter.DEFAULT;

    @MockBean
    private MessageSource messageSource;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private UserService userService;

    @Test
    void createUser_ShouldSucceed() throws Exception {
	CreateUserRequest payload = new CreateUserRequest();
	payload.setUsername("testuser");
	payload.setEmail("test@qoraa.net");
	payload.setFirstName("Test");
	payload.setLastName("User");

	User mockUser = mock(User.class);
	when(userMapper.fromDto(payload)).thenReturn(mockUser);
	when(userService.findUsername(payload.getUsername())).thenReturn(Optional.empty());
	when(messageSource.getMessage("users.created.successfully", null, Locale.getDefault()))
		.thenReturn("User created successfully");

	mockMvc.perform(MockMvcRequestBuilders.post(USERS_WEB_API_URL).contentType(MediaType.APPLICATION_JSON)
		.content(jsonWriter.writeAsString(payload))
		.with(jwt().authorities(new SimpleGrantedAuthority("User.Create"),
			new SimpleGrantedAuthority("User.View"))))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.content().string("User created successfully"));
    }
}

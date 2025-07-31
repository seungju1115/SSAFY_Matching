package com.example.demo;

import com.example.demo.user.Enum.PersonalPrefEnum;
import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MockMvc mvc;

	@Test
	public void helloUnauthenticated() throws Exception{
		mvc.perform(get("/hello")).andExpect(status().is3xxRedirection());
	}

	@Test
	@WithMockUser
	public void helloAuthenticated() throws Exception{
		mvc.perform(get("/hello")).andExpect(content().string("Hello World"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "tester", roles = {"PRO"})
	public void getProfile() throws Exception{
		User user = new User();
		user.setUserName("홍길동");
		user.setRole("USER");
		user.setEmail("hong@example.com");
		user.setUserProfile("열정적인 개발자입니다.");
		user.setMajor("컴공");
		user.setLastClass(5);
		user.setWantedPosition("백엔드 개발자");
		user.setProjectPref(Set.of(ProjectPrefEnum.CHALLENGE, ProjectPrefEnum.STABLE));
		user.setPersonalPref(Set.of(PersonalPrefEnum.COMMUNICATE, PersonalPrefEnum.CONCENTRATE));
		user.setTechStack(Set.of(TechEnum.JPA, TechEnum.Spring));
		user.setProjectExp("3개의 사이드 프로젝트 경험");
		user.setQualification("정보처리기사");

		System.out.println("User: " + user);

		userRepository.save(user);

		assertTrue(true);
	}
}

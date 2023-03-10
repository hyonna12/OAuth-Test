package site.hobbyup.class_final_back.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import site.hobbyup.class_final_back.config.dummy.DummyEntity;
import site.hobbyup.class_final_back.domain.category.Category;
import site.hobbyup.class_final_back.domain.category.CategoryRepository;
import site.hobbyup.class_final_back.domain.expert.Expert;
import site.hobbyup.class_final_back.domain.expert.ExpertRepository;
import site.hobbyup.class_final_back.domain.lesson.Lesson;
import site.hobbyup.class_final_back.domain.lesson.LessonRepository;
import site.hobbyup.class_final_back.domain.profile.ProfileRepository;
import site.hobbyup.class_final_back.domain.subscribe.Subscribe;
import site.hobbyup.class_final_back.domain.subscribe.SubscribeRepository;
import site.hobbyup.class_final_back.domain.user.User;
import site.hobbyup.class_final_back.domain.user.UserRepository;
import site.hobbyup.class_final_back.dto.subscribe.SubscribeReqDto.SubscribeSaveReqDto;

@Sql("classpath:db/truncate.sql") // ?????? ?????? ?????? (auto_increment ????????? + ????????? ?????????)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class SubscribeApiControllerTest extends DummyEntity {

        private static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";
        private static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded; charset=utf-8";

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper om;

        @Autowired
        private SubscribeRepository subscribeRepository;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private ProfileRepository profileRepository;
        @Autowired
        private CategoryRepository categoryRepository;
        @Autowired
        private LessonRepository lessonRepository;
        @Autowired
        private ExpertRepository expertRepository;

        @BeforeEach
        public void setUp() throws IOException {
                User ssar = userRepository.save(newUser("ssar"));
                User cos = userRepository.save(newUser("cos"));
                User hong = userRepository.save(newUser("expert"));

                Expert expert1 = expertRepository.save(newExpert(hong));

                Category beauty = categoryRepository.save(newCategory("??????"));
                Category sports = categoryRepository.save(newCategory("?????????"));
                Category dance = categoryRepository.save(newCategory("??????"));
                Category music = categoryRepository.save(newCategory("??????"));
                Category art = categoryRepository.save(newCategory("??????"));
                Category crafts = categoryRepository.save(newCategory("??????"));
                Category game = categoryRepository.save(newCategory("??????"));
                Category others = categoryRepository.save(newCategory("??????"));

                Lesson lesson1 = lessonRepository.save(newLesson("??????1", 10000L, expert1, beauty));
                Lesson lesson2 = lessonRepository.save(newLesson("??????1", 10000L, expert1, beauty));

                Subscribe subscribe1 = subscribeRepository.save(newSubscribe(ssar, lesson1));
                Subscribe subscribe2 = subscribeRepository.save(newSubscribe(ssar, lesson2));
        }

        @WithUserDetails(value = "cos", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @Test
        public void saveSubscribe_test() throws Exception {
                // given
                SubscribeSaveReqDto subscribeSaveReqDto = new SubscribeSaveReqDto();
                subscribeSaveReqDto.setLessonId(1L);

                String requestBody = om.writeValueAsString(subscribeSaveReqDto);
                System.out.println("????????? : " + requestBody);

                // when
                ResultActions resultActions = mvc
                                .perform(post("/api/subscribe").content(requestBody)
                                                .contentType(APPLICATION_JSON_UTF8));
                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                System.out.println("????????? : " + responseBody);
                // then
                resultActions.andExpect(status().isCreated());
                resultActions.andExpect(jsonPath("$.data.id").value(3L));
        }

        @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @Test
        public void deleteSubscribe_test() throws Exception {
                // given
                Long subscribeId = 1L;

                // when
                ResultActions resultActions = mvc
                                .perform(delete("/api/subscribe/" + subscribeId));
                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                System.out.println("????????? : " + responseBody);

                // then
                resultActions.andExpect(status().isCreated());

        }

        @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @Test
        public void getSubscribeList_test() throws Exception {
                // given
                Long userId = 1L;

                // when
                ResultActions resultActions = mvc
                                .perform(get("/api/user/" + userId + "/subscribe"));

                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                System.out.println("????????? : " + responseBody);

                // then
                resultActions.andExpect(status().isOk());
                resultActions.andExpect(jsonPath("$.data.subscribes.length()").value(2));
        }

}

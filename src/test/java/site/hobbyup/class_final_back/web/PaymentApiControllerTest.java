package site.hobbyup.class_final_back.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;

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
import site.hobbyup.class_final_back.domain.coupon.Coupon;
import site.hobbyup.class_final_back.domain.coupon.CouponRepository;
import site.hobbyup.class_final_back.domain.expert.Expert;
import site.hobbyup.class_final_back.domain.expert.ExpertRepository;
import site.hobbyup.class_final_back.domain.lesson.Lesson;
import site.hobbyup.class_final_back.domain.lesson.LessonRepository;
import site.hobbyup.class_final_back.domain.payment.Payment;
import site.hobbyup.class_final_back.domain.payment.PaymentRepository;
import site.hobbyup.class_final_back.domain.paymentType.PaymentType;
import site.hobbyup.class_final_back.domain.paymentType.PaymentTypeRepository;
import site.hobbyup.class_final_back.domain.profile.Profile;
import site.hobbyup.class_final_back.domain.profile.ProfileRepository;
import site.hobbyup.class_final_back.domain.review.ReviewRepository;
import site.hobbyup.class_final_back.domain.subscribe.Subscribe;
import site.hobbyup.class_final_back.domain.subscribe.SubscribeRepository;
import site.hobbyup.class_final_back.domain.user.User;
import site.hobbyup.class_final_back.domain.user.UserRepository;
import site.hobbyup.class_final_back.dto.payment.PaymentReqDto.PaymentSaveReqDto;

@Sql("classpath:db/truncate.sql") // ?????? ?????? ?????? (auto_increment ????????? + ????????? ?????????)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class PaymentApiControllerTest extends DummyEntity {

  private static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper om;

  @Autowired
  private LessonRepository lessonRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private ProfileRepository profileRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private PaymentTypeRepository paymentTypeRepository;

  @Autowired
  private CouponRepository couponRepository;

  @Autowired
  private SubscribeRepository subscribeRepository;

  @Autowired
  private ExpertRepository expertRepository;

  @Autowired
  private EntityManager em;

  @BeforeEach
  public void setUp() {
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

    Profile ssarProfile = profileRepository
        .save(newProfile("", "??????????????? ???????????? ?????? ????????? ?????? ssar?????????.", "??????", "?????????", "5???", "?????? ????????? ????????? ?????? 10???", ssar));

    Lesson lesson1 = lessonRepository.save(newLesson("??????1", 10000L, expert1, beauty));
    Lesson lesson2 = lessonRepository.save(newLesson("??????2", 20000L, expert1, sports));
    Lesson lesson3 = lessonRepository.save(newLesson("??????3", 50000L, expert1, music));
    Lesson lesson4 = lessonRepository.save(newLesson("??????4", 34500L, expert1, music));
    Lesson lesson5 = lessonRepository.save(newLesson("??????5", 2400L, expert1, music));
    Lesson lesson6 = lessonRepository.save(newLesson("??????6", 98000000L, expert1, beauty));
    Lesson lesson7 = lessonRepository.save(newLesson("??????7", 30000L, expert1, sports));
    Lesson lesson8 = lessonRepository.save(newLesson("??????8", 40000L, expert1, sports));
    Lesson lesson9 = lessonRepository.save(newLesson("??????9", 50000L, expert1, sports));
    Lesson lesson10 = lessonRepository.save(newLesson("??????10", 70000L, expert1, sports));

    PaymentType card = paymentTypeRepository.save(newPaymentType("????????????"));
    PaymentType kakaoPay = paymentTypeRepository.save(newPaymentType("???????????????"));
    PaymentType vbank = paymentTypeRepository.save(newPaymentType("???????????????"));

    Subscribe subscribe1 = subscribeRepository.save(newSubscribe(ssar, lesson1));

    Coupon coupon1 = couponRepository.save(newCoupon("???????????? ??????", 1000L, "2022-12-22", ssar));

    Payment ssarPayment1 = paymentRepository.save(newPayment(ssar, lesson1, card, coupon1, 2));
    Payment ssarPayment2 = paymentRepository.save(newPayment(ssar, lesson2, kakaoPay, coupon1, 3));

  }

  @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  @Test
  public void savePayment_test() throws Exception {
    // given
    Long lessonId = 1L;
    PaymentSaveReqDto paymentSaveReqDto = new PaymentSaveReqDto();
    paymentSaveReqDto.setTotalCount(2);
    paymentSaveReqDto.setPaymentTypeId(1L);
    String requestBody = om.writeValueAsString(paymentSaveReqDto);

    // when
    ResultActions resultActions = mvc
        .perform(post("/api/lesson/" + lessonId + "/payment").content(requestBody)
            .contentType(APPLICATION_JSON_UTF8));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("????????? : " + responseBody);

    // then
    resultActions.andExpect(status().isCreated());
    resultActions.andExpect(jsonPath("$.data.finalPrice").value(18000L));
    resultActions.andExpect(jsonPath("$.data.paymentTypeName").value("????????????"));
  }

  @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  @Test
  public void getUserPaymentList_test() throws Exception {
    // given
    Long userId = 1L;

    // when
    ResultActions resultActions = mvc
        .perform(get("/api/user/" + userId + "/mypage/payment"));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("????????? : " + responseBody);

    // then
    resultActions.andExpect(status().isOk());
    resultActions.andExpect(jsonPath("$.data.paymentDtoList[0].lessonName").value("??????1"));
    resultActions.andExpect(jsonPath("$.data.paymentDtoList[0].paymentType").value("????????????"));
    resultActions.andExpect(jsonPath("$.data.paymentDtoList[0].finalPrice").value(18000L));
    resultActions.andExpect(jsonPath("$.data.paymentDtoList[1].lessonName").value("??????2"));
    resultActions.andExpect(jsonPath("$.data.paymentDtoList[1].paymentType").value("???????????????"));
    resultActions.andExpect(jsonPath("$.data.paymentDtoList[1].finalPrice").value(57000L));
  }
}

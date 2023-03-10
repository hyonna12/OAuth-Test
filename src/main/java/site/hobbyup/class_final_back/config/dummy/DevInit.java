package site.hobbyup.class_final_back.config.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import site.hobbyup.class_final_back.domain.category.Category;
import site.hobbyup.class_final_back.domain.category.CategoryRepository;
import site.hobbyup.class_final_back.domain.expert.Expert;
import site.hobbyup.class_final_back.domain.expert.ExpertRepository;
import site.hobbyup.class_final_back.domain.interest.InterestRepository;
import site.hobbyup.class_final_back.domain.lesson.Lesson;
import site.hobbyup.class_final_back.domain.lesson.LessonRepository;
import site.hobbyup.class_final_back.domain.profile.Profile;
import site.hobbyup.class_final_back.domain.profile.ProfileRepository;
import site.hobbyup.class_final_back.domain.review.Review;
import site.hobbyup.class_final_back.domain.review.ReviewRepository;
import site.hobbyup.class_final_back.domain.subscribe.Subscribe;
import site.hobbyup.class_final_back.domain.subscribe.SubscribeRepository;
import site.hobbyup.class_final_back.domain.user.User;
import site.hobbyup.class_final_back.domain.user.UserRepository;

@RequiredArgsConstructor
@Configuration
public class DevInit extends DummyEntity {

    @org.springframework.context.annotation.Profile("dev")
    @Bean
    public CommandLineRunner dataSetting(UserRepository userRepository, CategoryRepository categoryRepository,
            InterestRepository interestRepository, LessonRepository lessonRepository, ReviewRepository reviewRepository,
            SubscribeRepository subscribeRepository, ExpertRepository expertRepository,
            ProfileRepository profileRepository) {

        return (args) -> {
            User ssar = userRepository.save(newUser("ssar"));
            User cos = userRepository.save(newUser("cos"));
            User aa = userRepository.save(newUser("aa"));
            User hong = userRepository.save(newUser("expert"));

            Expert expert = expertRepository.save(newExpert(hong));

            Profile profile = profileRepository.save(newProfile("", "expert1??? ??????????????????.", "????????????", "expert1??? ??????????????????.",
                    "5???", "????????? ???????????? 10?????? ???????????? ??????", hong));

            Category beauty = categoryRepository.save(newCategory("??????"));
            Category sports = categoryRepository.save(newCategory("?????????"));
            Category dance = categoryRepository.save(newCategory("??????"));
            Category music = categoryRepository.save(newCategory("??????"));
            Category art = categoryRepository.save(newCategory("??????"));
            Category crafts = categoryRepository.save(newCategory("??????"));
            Category game = categoryRepository.save(newCategory("??????"));
            Category others = categoryRepository.save(newCategory("??????"));

            Lesson lesson1 = lessonRepository.save(newLesson("??????1", 10000L, expert, beauty));
            Lesson lesson2 = lessonRepository.save(newLesson("??????2", 20000L, expert, sports));
            Lesson lesson3 = lessonRepository.save(newLesson("??????3", 50000L, expert, music));
            Lesson lesson4 = lessonRepository.save(newLesson("??????4", 34500L, expert, music));
            Lesson lesson5 = lessonRepository.save(newLesson("??????5", 2400L, expert, music));
            Lesson lesson6 = lessonRepository.save(newLesson("??????6", 98000000L, expert, beauty));
            Lesson lesson7 = lessonRepository.save(newLesson("??????7", 30000L, expert, sports));
            Lesson lesson8 = lessonRepository.save(newLesson("??????8", 40000L, expert, sports));
            Lesson lesson9 = lessonRepository.save(newLesson("??????9", 50000L, expert, sports));
            Lesson lesson10 = lessonRepository.save(newLesson("??????10", 70000L, expert, sports));

            Review review1 = reviewRepository.save(newReivew("?????? ?????? ???????????????.", 4.5, ssar, lesson1));
            Review review2 = reviewRepository.save(newReivew("???????????? ????????? ??? ?????????!", 4.0, cos, lesson1));
            Review review3 = reviewRepository.save(newReivew("????????????", 3.0, ssar, lesson2));
            Review review4 = reviewRepository.save(newReivew("????????? ??? ?????? ????????? ????????? ???????", 2.5, ssar, lesson3));
            Review review5 = reviewRepository.save(newReivew("????????????", 2.0, cos, lesson2));
            Review review6 = reviewRepository.save(newReivew("????????? ???????????? ?????? ??? ?????????, ?????? ????????????...", 3.5, cos, lesson8));
            Review review7 = reviewRepository.save(newReivew("?????? ??????", 1.5, cos, lesson7));

            Subscribe subscribe1 = subscribeRepository.save(newSubscribe(ssar, lesson1));
            Subscribe subscribe2 = subscribeRepository.save(newSubscribe(ssar, lesson2));
            Subscribe subscribe3 = subscribeRepository.save(newSubscribe(cos, lesson3));
            Subscribe subscribe4 = subscribeRepository.save(newSubscribe(cos, lesson8));
            Subscribe subscribe5 = subscribeRepository.save(newSubscribe(cos, lesson9));
            Subscribe subscribe6 = subscribeRepository.save(newSubscribe(ssar, lesson10));

        };

    }
}

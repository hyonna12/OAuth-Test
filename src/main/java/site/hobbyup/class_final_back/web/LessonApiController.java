package site.hobbyup.class_final_back.web;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpsRedirectSpec;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.hobbyup.class_final_back.config.auth.LoginUser;
import site.hobbyup.class_final_back.domain.lesson.LessonRepository;
import site.hobbyup.class_final_back.domain.lesson.LessonRepository;
import site.hobbyup.class_final_back.dto.ResponseDto;
import site.hobbyup.class_final_back.dto.lesson.LessonCommonListDto;
import site.hobbyup.class_final_back.dto.lesson.LessonSortListRespDto;
import site.hobbyup.class_final_back.dto.lesson.LessonSubscribeListDto;
import site.hobbyup.class_final_back.dto.lesson.LessonReqDto.LessonSaveReqDto;
import site.hobbyup.class_final_back.dto.lesson.LessonReqDto.LessonUpdateReqDto;
import site.hobbyup.class_final_back.dto.lesson.LessonRespDto.LessonCategoryListRespDto;
import site.hobbyup.class_final_back.dto.lesson.LessonRespDto.LessonDetailRespDto;
import site.hobbyup.class_final_back.dto.lesson.LessonRespDto.LessonLatestListRespDto;
import site.hobbyup.class_final_back.dto.lesson.LessonRespDto.LessonSaveRespDto;
import site.hobbyup.class_final_back.dto.lesson.LessonRespDto.LessonUpdateRespDto;
import site.hobbyup.class_final_back.service.LessonService;

@RequiredArgsConstructor
@RestController
public class LessonApiController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final LessonService lessonService;

    // ?????? role ???????????? expert??? is_approval??? true??? ???????????? ???????????? ????????? ??? ?????? ?????? ??????.
    // lesson ??????
    @PostMapping("/api/lesson")
    public ResponseEntity<?> saveLesson(@RequestBody LessonSaveReqDto lessonSaveReqDto,
            @AuthenticationPrincipal LoginUser loginUser) throws IOException {
        log.debug("????????? : LessonApiController-saveLesson ?????????");
        LessonSaveRespDto lessonSaveRespDto = lessonService.saveLesson(lessonSaveReqDto, loginUser);
        return new ResponseEntity<>(new ResponseDto<>("????????? ?????? ??????", lessonSaveRespDto), HttpStatus.CREATED);
    }

    // lesson ????????? ??????(????????????????????? ????????? ???????????? ??????)(????????? ??????)
    @GetMapping("/api/category/{categoryId}/test")
    public ResponseEntity<?> getLessonCategoryList(@PathVariable Long categoryId,
            @RequestParam(name = "min_price") Long minPrice, @RequestParam(name = "max_price") Long maxPrice) {
        LessonCategoryListRespDto lessonCategoryListRespDto = lessonService.getLessonCategoryList(categoryId, minPrice,
                maxPrice);
        return new ResponseEntity<>(new ResponseDto<>("????????? ????????? ???????????? ??????", lessonCategoryListRespDto),
                HttpStatus.OK);
    }

    // lesson ????????????
    @GetMapping("/api/category/lesson/{lessonId}")
    public ResponseEntity<?> getLessonDetail(@PathVariable Long lessonId,
            @AuthenticationPrincipal LoginUser loginUser) {
        if (loginUser == null) {
            LessonDetailRespDto lessonDetailRespDto = lessonService.getLessonDetailNotLogin(lessonId);
            return new ResponseEntity<>(new ResponseDto<>("????????? ???????????? ??????", lessonDetailRespDto), HttpStatus.OK);
        }
        LessonDetailRespDto lessonDetailRespDto = lessonService.getLessonDetail(lessonId, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>("????????? ???????????? ??????", lessonDetailRespDto), HttpStatus.OK);
    }

    // ????????? ??????(?????? ??????)
    @GetMapping("/api/lesson/latest")
    public ResponseEntity<?> getLatestLessonList() {
        LessonLatestListRespDto lessonLatestListRespDto = lessonService.getLatestLessonList();
        return new ResponseEntity<>(new ResponseDto<>("????????? ??????????????? ??????", lessonLatestListRespDto), HttpStatus.OK);
    }

    // ?????? ?????????
    @GetMapping("/api/main")
    public ResponseEntity<?> getLessonCommonList(@AuthenticationPrincipal LoginUser loginUser) {
        if (loginUser == null) {
            List<LessonCommonListDto> lessonCommonListDtos = lessonService.getLessonCommonListNotLogin();
            return new ResponseEntity<>(new ResponseDto<>("?????? ????????? ?????? ??????", lessonCommonListDtos), HttpStatus.OK);
        }
        List<LessonCommonListDto> lessonCommonListDtos = lessonService.getLessonCommonList(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>("?????? ????????? ?????? ??????", lessonCommonListDtos), HttpStatus.OK);
    }

    // ?????? ????????????
    @PutMapping("/api/lesson/{id}")
    public ResponseEntity<?> updateLesson(@RequestBody LessonUpdateReqDto lessonUpdateReqDto, @PathVariable Long id,
            @AuthenticationPrincipal LoginUser loginUser) {
        log.debug("????????? : LessonApiController-updateLesson ?????????");
        LessonUpdateRespDto lessonUpdateRespDto = lessonService.updateLesson(lessonUpdateReqDto, id,
                loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(" ?????? ????????????", lessonUpdateRespDto), HttpStatus.OK);
    }

    // ????????? ??????(?????? ??? ???)
    @GetMapping("/api/lesson/subscribe")
    public ResponseEntity<?> getLessonSubscribeList(@AuthenticationPrincipal LoginUser loginUser) {
        if (loginUser == null) {
            List<LessonSubscribeListDto> lessonSubscribeListDtos = lessonService.getLessonSubscribeListNotLogin();
            return new ResponseEntity<>(new ResponseDto<>("????????? ??????????????? ??????", lessonSubscribeListDtos), HttpStatus.OK);
        }
        List<LessonSubscribeListDto> lessonSubscribeListDtos = lessonService
                .getLessonSubscribeList(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>("????????? ??????????????? ??????", lessonSubscribeListDtos), HttpStatus.OK);
    }

    // ??????????????? ?????????(?????????, ?????????, ????????? ?????? ?????? ???)
    @GetMapping("/api/category/{categoryId}")
    public ResponseEntity<?> getLessonListByCategoryWithSort(@AuthenticationPrincipal LoginUser loginUser,
            @PathVariable Long categoryId) {
        List<LessonSortListRespDto> lessonSortListRespDtoList = lessonService
                .getLessonListByCategoryWithSort(loginUser.getUser().getId(), categoryId);
        return new ResponseEntity<>(new ResponseDto<>("????????? ?????? ??????",
                lessonSortListRespDtoList),
                HttpStatus.OK);
    }

}

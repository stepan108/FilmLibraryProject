//package ru.stephen.filmlibrary.library.controllers.mvc;
//
//import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithAnonymousUser;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.web.multipart.MultipartFile;
//import ru.stephen.filmlibrary.library.dto.DirectorDTO;
//import ru.stephen.filmlibrary.library.dto.FilmDTO;
//import ru.stephen.filmlibrary.library.model.Genre;
//import ru.stephen.filmlibrary.library.service.FilmService;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@Slf4j
//@Transactional
//@Rollback(value = false)
//public class MVCFilmControllerTest
//        extends CommonTestMVC {
//
//    @Autowired
//    private FilmService filmService;
//
//    private final FilmDTO filmDTO = new FilmDTO("MVC_TestFilmTitle", LocalDate.now(), "Test Country",  1, "Test Description", Genre.DRAMA,"", new ArrayList<>());
//    private final FilmDTO filmDTOUpdated = new FilmDTO("MVC_TestFilmTitleUpdated", LocalDate.now(), "Test Country",  1, "Test Description", Genre.DRAMA,"", new ArrayList<>());
//
//
//    /**
//     * Метод, тестирующий просмотр всех фильмов через MVC-контроллер.
//     * Авторизуемся под пользователем admin (можно выбрать любого),
//     * создаем шаблон данных и вызываем MVC-контроллер с соответствующим маппингом и методом.
//     * flashAttr - используется, чтобы передать ModelAttribute в метод контроллера
//     * Ожидаем, что будет статус redirect (как у нас в контроллере) при успешном просмотре
//     *
//     * @throws Exception - любая ошибка
//     */
//    @Override
//    @Test
//    @DisplayName("Просмотр всех фильмов через MVC контроллер")
//    @Order(0)
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    protected void listAll() throws Exception {
//        log.info("Тест просмотра фильмов MVC начат!");
//        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/films")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(view().name("films/viewAllFilms"))
//                .andExpect(model().attributeExists("films"))
//                .andReturn();
//    }
//
//
//    /**
//     * Метод, тестирующий создание фильма через MVC-контроллер.
//     * Авторизуемся под пользователем admin (можно выбрать любого),
//     * создаем шаблон данных и вызываем MVC-контроллер с соответствующим маппингом и методом.
//     * flashAttr - используется, чтобы передать ModelAttribute в метод контроллера
//     * Ожидаем, что будет статус redirect (как у нас в контроллере) при успешном создании
//     *
//     * @throws Exception - любая ошибка
//     */
//    @Override
//    @Test
//    @Order(1)
//    @DisplayName("Создание фильма через MVC контроллер")
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    protected void createObject() throws Exception {
//        log.info("Тест по созданию фильма через MVC начат");
//        mvc.perform(MockMvcRequestBuilders.post("/films/add", new Object())
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .flashAttr("filmForm", filmDTO)
//                                .accept(MediaType.APPLICATION_JSON)
//                        // .with(csrf())
//                )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/films"))
//                .andExpect(redirectedUrlTemplate("/films"))
//                .andExpect(redirectedUrl("/films"));
//        log.info("Тест по созданию фильма через MVC закончен!");
//    }
//
//
//    @Order(2)
//    @Test
//    @DisplayName("Обновление фильма через MVC контроллер")
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    //@WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "andy_user")
//    protected void updateObject() throws Exception {
//        log.info("Тест по обновлению фильма через MVC начат успешно");
//        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "filmTitle"));
//        FilmDTO foundFIlmForUpdate = filmService.searchFilm(filmDTO.getFilmTitle(), pageRequest).getContent().get(0);
//        foundFIlmForUpdate.setFilmTitle(filmDTOUpdated.getFilmTitle());
//        mvc.perform(post("/films/update")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .flashAttr("filmForm", foundFIlmForUpdate)
//                        .accept(MediaType.APPLICATION_JSON_VALUE)
//                )
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/films"))
//                .andExpect(redirectedUrl("/films"));
//        log.info("Тест по обновлению фильма через MVC закончен успешно");
//    }
//
//    @Order(3)
//    @Test
//    @DisplayName("Софт удаление фильма через MVC контроллер, тестирование 'films/delete'")
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    @Override
//    protected void deleteObject() throws Exception {
//        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "filmTitle"));
//        FilmDTO foundFilmForDelete = filmService.searchFilm(filmDTOUpdated.getFilmTitle(), pageRequest).getContent().get(0);
//        foundFilmForDelete.setDeleted(true);
//        mvc.perform(get("/films/delete/{id}", foundFilmForDelete.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/films"))
//                .andExpect(redirectedUrl("/films"));
//
//        FilmDTO deletedFilm = filmService.getOne(foundFilmForDelete.getId());
//        assertTrue(deletedFilm.isDeleted());
//        log.info("Тест по soft удалению фильма через MVC закончен успешно!");
//    }
//}

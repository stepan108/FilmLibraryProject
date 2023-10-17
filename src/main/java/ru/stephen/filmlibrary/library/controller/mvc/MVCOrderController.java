package ru.stephen.filmlibrary.library.controller.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.stephen.filmlibrary.library.dto.OrderDTO;
//import ru.stephen.filmlibrary.library.exception.RepeatedRentException;
import ru.stephen.filmlibrary.library.service.FilmService;
import ru.stephen.filmlibrary.library.service.OrderService;
import ru.stephen.filmlibrary.library.service.userdetails.CustomUserDetails;

@Controller
@Slf4j
@RequestMapping("/rent")
public class MVCOrderController {
    private final OrderService orderService;
    private final FilmService filmService;

    public MVCOrderController(OrderService orderService, FilmService filmService) {
        this.orderService = orderService;
        this.filmService = filmService;
    }

    @GetMapping("/film/{filmId}")
    public String rentFilm(@PathVariable Long filmId,
                           Model model) {
        model.addAttribute("film", filmService.getOne(filmId));
        return "userFilms/rentFilm";
    }

    @PostMapping("/film")
    public String rentFilm(@ModelAttribute("order")OrderDTO orderDTO)
//            throws RepeatedRentException
    {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderDTO.setUserId(Long.valueOf(customUserDetails.getUserId()));
        orderService.rentFilm(orderDTO);
        return "redirect:/rent/user-films/" + customUserDetails.getUserId();
    }

    @GetMapping("/return-film/{id}")
    public String returnFilm(@PathVariable Long id) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.returnFilm(id);
        return "redirect:/rent/user-films/" + customUserDetails.getUserId();
    }

    @GetMapping("/user-films/{id}")
    public String userFilms(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "5") int pageSize,
                            @PathVariable Long id,
                            Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<OrderDTO> orderDTOPage = orderService.listUserRentFilms(id, pageRequest);
        model.addAttribute("rentFilms", orderDTOPage);
        model.addAttribute("userId", id);
        return "userFilms/viewAllUserFilms";
    }
}

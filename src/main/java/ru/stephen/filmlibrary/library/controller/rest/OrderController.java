package ru.stephen.filmlibrary.library.controller.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stephen.filmlibrary.library.dto.OrderDTO;
import ru.stephen.filmlibrary.library.model.Order;
import ru.stephen.filmlibrary.library.service.OrderService;

@RestController
@RequestMapping("/api/rest/rent/info")
@Tag(name = "Аренда фильмов",
        description = "Контроллер для работы с арендой/выдачей фильмов пользователям фильмотеки")
public class OrderController extends GenericController<Order, OrderDTO> {
    protected OrderController(OrderService orderService) {
        super(orderService);
    }
}

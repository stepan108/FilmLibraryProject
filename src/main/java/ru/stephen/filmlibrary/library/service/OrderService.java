package ru.stephen.filmlibrary.library.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.stephen.filmlibrary.library.constants.Errors;
import ru.stephen.filmlibrary.library.dto.FilmDTO;
import ru.stephen.filmlibrary.library.dto.OrderDTO;
//import ru.stephen.filmlibrary.library.exception.RepeatedRentException;
import ru.stephen.filmlibrary.library.mapper.OrderMapper;
import ru.stephen.filmlibrary.library.model.Order;
import ru.stephen.filmlibrary.library.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService
        extends GenericService<Order, OrderDTO> {
    private final FilmService filmService;

    public OrderService(OrderRepository repository,
                        OrderMapper mapper,
                        FilmService filmService) {
        super(repository, mapper);
        this.filmService = filmService;
    }

    public OrderDTO rentFilm(final OrderDTO orderDTO)
//            throws RepeatedRentException
    {
        FilmDTO filmDTO = filmService.getOne(orderDTO.getFilmId());
//        if (orderDTO.getFilmDTO() != null && !(orderDTO.getFilmDTO().equals(filmDTO))) {
            filmService.update(filmDTO);
            long rentPeriod = orderDTO.getRentPeriod() != null ? orderDTO.getRentPeriod() : 14L;
            orderDTO.setFilmDTO(filmDTO);
            orderDTO.setRentDate(LocalDateTime.now());
            orderDTO.setReturned(false);
            orderDTO.setRentPeriod((int) rentPeriod);
            orderDTO.setReturnDate(LocalDateTime.now().plusDays(rentPeriod));
            orderDTO.setCreatedWhen(LocalDateTime.now());
            orderDTO.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
//        } else {
//            throw new RepeatedRentException(Errors.Order.ORDER_RENT_ERROR);
//        }

        return mapper.toDTO(repository.save(mapper.toEntity(orderDTO)));
    }

    public Page<OrderDTO> listUserRentFilms(final Long id,
                                            final Pageable pageRequest) {
        Page<Order> objects = ((OrderRepository) repository).getOrderByUserId(id, pageRequest);
        List<OrderDTO> results = mapper.toDTOs(objects.getContent());
        return new PageImpl<>(results, pageRequest, objects.getTotalElements());
    }

    public void returnFilm(final Long id) {
        OrderDTO orderDTO = getOne(id);
        orderDTO.setFilmId(null);
        orderDTO.setReturned(true);
        orderDTO.setReturnDate(LocalDateTime.now());
        update(orderDTO);
    }

    public List<Long> getOrderIdsWithDelayedRentDate() {
        return ((OrderRepository) repository).getDelayedOrders();
    }
}

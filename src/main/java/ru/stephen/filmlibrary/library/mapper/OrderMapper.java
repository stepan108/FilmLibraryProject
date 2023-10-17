package ru.stephen.filmlibrary.library.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;
import ru.stephen.filmlibrary.library.dto.OrderDTO;
import ru.stephen.filmlibrary.library.model.Order;
import ru.stephen.filmlibrary.library.repository.FilmRepository;
import ru.stephen.filmlibrary.library.repository.UserRepository;
import ru.stephen.filmlibrary.library.service.FilmService;

import java.util.List;

@Component
public class OrderMapper
        extends GenericMapper<Order, OrderDTO> {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    private final FilmService filmService;

    public OrderMapper(ModelMapper modelMapper,
                       FilmRepository filmRepository,
                       UserRepository userRepository,
                       FilmService filmService) {
        super(Order.class, OrderDTO.class, modelMapper);
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
        this.filmService = filmService;
    }

    @PostConstruct
    public void setupMapper() {
        super.modelMapper.createTypeMap(Order.class, OrderDTO.class)
                .addMappings(mapper -> mapper.skip(OrderDTO::setUserId))
                .addMappings(mapper -> mapper.skip(OrderDTO::setFilmId))
                .addMappings(mapper -> mapper.skip(OrderDTO::setFilmDTO))
                .setPostConverter(toDTOConverter());

        super.modelMapper.createTypeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setUser))
                .addMappings(mapper -> mapper.skip(Order::setFilm))
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(OrderDTO source, Order destination) {
        destination.setFilm(filmRepository.findById(source.getFilmId()).orElseThrow(() -> new NotFoundException("Фильм не найден")));
        destination.setUser(userRepository.findById(source.getUserId()).orElseThrow(() -> new NotFoundException("Пользовател не найден")));
    }

    @Override
    protected void mapSpecificFields(Order source, OrderDTO destination) {
        destination.setUserId(source.getUser().getId());
        destination.setFilmId(source.getFilm().getId());
        destination.setFilmDTO(filmService.getOne(source.getFilm().getId()));
    }

    @Override
    protected List<Long> getIds(Order entity) {
        throw new UnsupportedOperationException("Метод недоступен");
    }
}
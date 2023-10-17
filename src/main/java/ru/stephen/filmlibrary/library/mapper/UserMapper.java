package ru.stephen.filmlibrary.library.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.stephen.filmlibrary.library.dto.UserDTO;
import ru.stephen.filmlibrary.library.model.GenericModel;
import ru.stephen.filmlibrary.library.model.User;
import ru.stephen.filmlibrary.library.repository.OrderRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserMapper
        extends GenericMapper<User, UserDTO> {
    private final OrderRepository orderRepository;

    public UserMapper(ModelMapper modelMapper,
                      OrderRepository orderRepository) {
        super(User.class, UserDTO.class, modelMapper);
        this.orderRepository = orderRepository;
    }

    @PostConstruct
    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(m -> m.skip(UserDTO::setUserOrders)).setPostConverter(toDTOConverter());
        modelMapper.createTypeMap(UserDTO.class, User.class)
                .addMappings(m -> m.skip(User::setOrders)).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(UserDTO source, User destination) {
        if (!Objects.isNull(source.getUserOrders())) {
            destination.setOrders(orderRepository.findAllById(source.getUserOrders()));
        }
        else {
            destination.setOrders(Collections.emptyList());
        }
    }

    @Override
    protected void mapSpecificFields(User source, UserDTO destination) {
        destination.setUserOrders(getIds(source));
    }

    @Override
    protected List<Long> getIds(User entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getOrders())
                ? null
                : entity.getOrders().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toList());
    }
}

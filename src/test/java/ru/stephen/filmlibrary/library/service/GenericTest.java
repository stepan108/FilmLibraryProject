//package ru.stephen.filmlibrary.library.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import ru.stephen.filmlibrary.library.dto.GenericDTO;
//import ru.stephen.filmlibrary.library.mapper.GenericMapper;
//import ru.stephen.filmlibrary.library.model.GenericModel;
//import ru.stephen.filmlibrary.library.repository.GenericRepository;
//import ru.stephen.filmlibrary.library.service.userdetails.CustomUserDetails;
//
//public abstract class GenericTest<E extends GenericModel, D extends GenericDTO> {
//    protected GenericService<E, D> service;
//    protected GenericRepository<E> repository;
//    protected GenericMapper<E, D> mapper;
//
//    @BeforeEach
//    void init() {
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                CustomUserDetails.builder()
//                        .username("USER"),
//                null,
//                null);
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//
//    protected abstract void getAll();
//    protected abstract void getOne();
//    protected abstract void create();
//    protected abstract void update();
//    protected abstract void delete();
//    protected abstract void restore();
//    protected abstract void getAllNotDeleted();
//}

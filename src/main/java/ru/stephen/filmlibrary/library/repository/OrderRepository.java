package ru.stephen.filmlibrary.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.stephen.filmlibrary.library.model.Order;

import java.util.List;

@Repository
public interface OrderRepository
        extends GenericRepository<Order> {
    Page<Order> getOrderByUserId(Long id,
                                 Pageable pageRequest);

    @Query(nativeQuery = true,
            value = """
                    select distinct id
                    from orders o 
                    where o.return_date < now()
                    and o.returned = false
                                """)
    List<Long> getDelayedOrders();
}

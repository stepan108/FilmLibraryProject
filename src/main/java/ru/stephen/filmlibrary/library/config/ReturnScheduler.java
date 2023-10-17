package ru.stephen.filmlibrary.library.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.stephen.filmlibrary.library.service.OrderService;

import java.util.List;

@Component
@Slf4j
public class ReturnScheduler {
    private final OrderService orderService;

    public ReturnScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

//https://crontab.cronhub.io/
    //https://crontab.guru/#15_14_1_*_*

    //Крон на каждую минуту: "0 0/1 * 1/1 * *"
    //"0 0 6 * * ?"
    @Scheduled(cron = "0 0/1 * 1/1 * *") // каждый день в 6 утра
    public void sentMailsToDebtors() {
        log.info("Запуск планировщика по проверки должников....");
        List<Long> ids = orderService.getOrderIdsWithDelayedRentDate();
        for (Long id : ids) {
            orderService.returnFilm(id);
        }
    }
}

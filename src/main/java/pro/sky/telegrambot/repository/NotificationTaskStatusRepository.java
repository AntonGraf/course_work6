package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.NotificationTaskStatus;

public interface NotificationTaskStatusRepository extends JpaRepository<NotificationTaskStatus,Long> {
    NotificationTaskStatus findNotificationTaskStatusByStatus(String status);
}

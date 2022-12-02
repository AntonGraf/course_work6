package pro.sky.telegrambot.service.scheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.repository.NotificationTaskStatusRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Service
public class NotificationTaskScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationTaskScheduler.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private NotificationTaskRepository repository;
    @Autowired
    private NotificationTaskStatusRepository statusRepository;

    @Scheduled(cron = "0 * * * * *")
    public void sendNotification() {
        Collection<NotificationTask> notificationTasks = getNotificationTaskByNowTime(LocalDateTime.now().
                truncatedTo(ChronoUnit.MINUTES));

        notificationTasks.stream()
                .forEach(task -> {

                    SendMessage sendMessage = new SendMessage(task.getChatId(), task.getText());
                    SendResponse response = telegramBot.execute(sendMessage);
                    task.setStatus(statusRepository.findNotificationTaskStatusByStatus("отправлено"));

                    if(response.isOk()) {
                        LOGGER.info("Напоминание: " + task.getText() + " - отправлено");
                        task.setStatus(statusRepository.findNotificationTaskStatusByStatus("доставлено"));
                    } else {
                        LOGGER.info("Напоминание: " + task.getText() + " - не доставлено");
                        task.setStatus(statusRepository.findNotificationTaskStatusByStatus("ошибка"));
                    }

                    repository.save(task);

                });

    }

    private Collection<NotificationTask> getNotificationTaskByNowTime(LocalDateTime localDateTime) {
        LOGGER.info("Поиск напоминаний на: " + localDateTime);
        return repository.findNotificationTaskByDateTime(localDateTime);
    }


}

package pro.sky.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.repository.NotificationTaskStatusRepository;
import pro.sky.telegrambot.service.notificationCommands.NotificationTaskCreate;

import java.util.regex.Pattern;

@Service
public class CommandsParser {

    @Autowired
    private NotificationTaskRepository notificationTaskRepository;
    @Autowired
    private NotificationTaskStatusRepository statusRepository;
    //Формат сообщения с заданием для напоминания
    private final String NOTIFICATION_TASK_PATTERN = "([0-9.:\\s]{16})(\\s)([\\W+]+)";

    /**
     * Определяет, что за команда указана в тексте, и возвращает соответсвующий команде сервис
     * @param text  - текст с командой
     * @return      - сервис, который работает с указанной командой
     */
    public CommandService getCommandService(String text) {

        if (text.startsWith("/start")) {
            return new StartCommand();
        }

        if (isNotificationTaskCreateCommand(text)) {
            return new NotificationTaskCreate(notificationTaskRepository, statusRepository);
        }

        return new DefaultCommand();
    }

    /**
     * Проверяет, является ли текст командой для напоминания
     * @param text  - текст с предполагаемой командой
     * @return      - true, если текст - команда для напоминания
     */
    private boolean isNotificationTaskCreateCommand(String text) {
        Pattern pattern = Pattern.compile(NOTIFICATION_TASK_PATTERN);
        return pattern.matcher(text).find();
    }


}

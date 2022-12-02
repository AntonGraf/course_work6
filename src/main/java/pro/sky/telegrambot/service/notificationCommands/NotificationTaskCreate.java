package pro.sky.telegrambot.service.notificationCommands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.NotificationTaskFormatException;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.repository.NotificationTaskStatusRepository;
import pro.sky.telegrambot.service.CommandService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class NotificationTaskCreate implements CommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationTaskCreate.class);

    private final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm";
    private final String TASK_FORMAT_EXAMPLE = "01.01.2022 20:00 Сделать домашнюю работу";

    private final String INCORRECT_REQUEST_ANSWER = "Не корректный формат команды";

    private final String SUCCESS_COMMAND = "Команда выполнена";

    private final NotificationTaskRepository repository;
    private final NotificationTaskStatusRepository statusRepository;

    public NotificationTaskCreate(NotificationTaskRepository repository, NotificationTaskStatusRepository statusRepository) {
        this.repository = repository;
        this.statusRepository = statusRepository;
    }

    /**
     * Формирует сообщение для отправки
     * @param message   - входящее сообщение с командой для напоминания
     * @return          - ответное сообщение с текстом об успешном/неуспешном выполнении команды
     */
    @Override
    public SendMessage executeCommand(Message message) {

        try {

            NotificationTask task = parse(message);
            repository.save(task);
            return new SendMessage(task.getChatId(), SUCCESS_COMMAND);

        } catch (NotificationTaskFormatException e) {
            return new SendMessage(message.chat().id(), INCORRECT_REQUEST_ANSWER);
        }

    }

    /**
     * Распознает из сообщения задание для напоминания
     * @param message   - входящее сообщение с заданием
     * @return          - задание для напоминания
     * @throws NotificationTaskFormatException  - в случае некорректного формата сообщения с заданием
     */
    private NotificationTask parse(Message message) throws NotificationTaskFormatException {

        NotificationTask notificationTask = new NotificationTask();
        notificationTask.setChatId(message.chat().id());

        String dateString = message.text().substring(0,16);

        notificationTask.setDateTime(getDateTime(dateString));
        notificationTask.setText(message.text().replace(dateString, "").trim());
        notificationTask.setStatus(statusRepository.findNotificationTaskStatusByStatus("добавлено"));

        return notificationTask;
    }

    /**
     * Распознает даты и время из текста
     * @param text  - текст с датой и временем
     * @return      - распознанные дата и время
     * @throws NotificationTaskFormatException  - в случае некорректного формата даты и времени
     */
    private LocalDateTime getDateTime(String text) throws NotificationTaskFormatException {

        LOGGER.debug("Распознается дата из строки: " + text);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

        try {
            return LocalDateTime.parse(text, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            LOGGER.warn("Не корректный формат сообщения. " +
                    "Сообщение должно иметь следующий формат:\t" + TASK_FORMAT_EXAMPLE);
            throw new NotificationTaskFormatException("Не корректный формат сообщения. " +
                    "Сообщение должно иметь следующий формат:\t" + TASK_FORMAT_EXAMPLE);
        }
    }
}

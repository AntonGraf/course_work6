package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

public interface CommandService {

    /**
     * Выполняет команду по указанному входящему сообщению и формирует результат в ответное сообщение
     * @param message   - входящее сообщение с командой
     * @return          - ответное сообщение с результатом выполнения команды
     */
    SendMessage executeCommand(Message message);
}

package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class DefaultCommand implements CommandService{
    @Override
    public SendMessage executeCommand(Message message) {
        return new SendMessage(message.chat().id(), "Неизвестная команда");
    }
}

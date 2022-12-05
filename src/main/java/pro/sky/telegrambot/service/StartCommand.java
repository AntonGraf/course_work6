package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements CommandService{
    @Override
    public SendMessage executeCommand(Message message) {
        return new SendMessage(message.chat().id(), message.chat().firstName() + "! Бот запущен");
    }
}

package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

public interface CommandService {

    SendMessage executeCommand(Message message);
}

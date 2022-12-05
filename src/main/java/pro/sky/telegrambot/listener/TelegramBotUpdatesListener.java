package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.CommandService;
import pro.sky.telegrambot.service.CommandsParser;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private CommandsParser parser;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {

        updates.forEach(update -> {

            logger.info("Processing update: {}", update);

            CommandService commandService = parser.getCommandService(update.message().text());
            SendMessage sendMessage = commandService.executeCommand(update.message());
            SendResponse response = telegramBot.execute(sendMessage);

            logger.info("Response status: {}", response.isOk());

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}

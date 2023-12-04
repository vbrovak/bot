package com.telegram.bot.handler;

import com.telegram.bot.action.Action;
import com.telegram.bot.button.Button;
import com.telegram.bot.data.entity.DontSmokeInfo;
import com.telegram.bot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdatesHandler extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;

    private Map<Long, DontSmokeInfo> dontSmokeMap;

    private Map<String, Action> actions;
    private Map<String, Button> buttons;

    private final MessageService messageService;

    @Autowired
    private void setBeans(Map<String, Action> actions, Map<String, Button> buttons) {
        this.actions = actions;
        this.buttons = buttons;
        this.dontSmokeMap = new HashMap<>();
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (Objects.nonNull(update.getMessage()) && update.getMessage().hasText()) {
             if (update.getMessage().getText().matches("^/\\S.*")) {
                if (update.getMessage().getText().equals("/reset")) {
                    Action action = actions.get("start");
                    action.handle(update);
                } else {
                    String stringAction = update.getMessage().getText().replace("/", "");
                    Action action = actions.get(stringAction);
                    action.handle(update);
                }
            } else {
                messageService.handleDontSmokeDataInput(update);
            }
        }

        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();

            if (callbackData.equals("backToStart")) {
                Action action = actions.get("start");
                action.handle(update);
            } else
            if (callbackData.split(",").length > 1) {
                buttons.get(callbackData.split(",")[0]).handle(update);
            } else {
                Button button = buttons.get(callbackData);
                button.handle(update);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
    @Override
    public String getBotToken() {
        return botToken;
    }
}

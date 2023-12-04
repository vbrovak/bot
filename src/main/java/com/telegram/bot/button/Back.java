package com.telegram.bot.button;

import com.telegram.bot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component("back")
@RequiredArgsConstructor
public class Back implements Button {
    private final MessageService messageService;

    @Override
    public void handle(Update update) {
        String id = update.getCallbackQuery().getFrom().getId().toString();
        String typeNumber = update.getCallbackQuery().getData().split(",")[1];

        messageService.sendButtonsMessage(typeNumber, id);
        messageService.dropButtonsFromMessage(update);
        messageService.editMessageText(update);
    }
}

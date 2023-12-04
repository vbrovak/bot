package com.telegram.bot.button;

import com.telegram.bot.data.dto.ButtonTemplate;
import com.telegram.bot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component("res")
@RequiredArgsConstructor
public class Res implements Button {
    private final MessageService messageService;

    @Override
    public void handle(Update update) {
        String id = update.getCallbackQuery().getFrom().getId().toString();
        String typeNumber = update.getCallbackQuery().getData().split(",")[1];

        List<ButtonTemplate> buttonTemplateList = List.of(
                new ButtonTemplate("Назад", "back," + typeNumber)
        );

        messageService.sendMessageWithButtons(
                id,
                "res, type: [" + typeNumber + "]",
                buttonTemplateList,
                false);
        messageService.dropButtonsFromMessage(update);
        messageService.editMessageText(update);
    }
}

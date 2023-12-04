package com.telegram.bot.button;

import com.telegram.bot.data.dto.ButtonTemplate;
import com.telegram.bot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component("addLine")
@RequiredArgsConstructor
public class AddLine implements Button {
    private final MessageService messageService;

    @Override
    public void handle(Update update) {
        Long id = update.getCallbackQuery().getFrom().getId();
        int typeNumber = Integer.parseInt(update.getCallbackQuery().getData().split(",")[1]);

        messageService.handleAddButton(id, typeNumber);
        messageService.editMessageText(update);
        messageService.dropButtonsFromMessage(update);
    }
}

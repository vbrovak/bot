package com.telegram.bot.action;

import com.telegram.bot.data.dto.ButtonTemplate;
import com.telegram.bot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component("start")
@RequiredArgsConstructor
public class Start implements Action {
    public static final String HELLO_STRING =
            "*************************************\n" +
            "Привет, я DontSmokeBot и я запущен.\n" +
            "Я помогу тебе бросить курить. \n " +
            "Для перезапуска используй команду /reset \n";
    private final MessageService messageService;

    @Override
    public void handle(Update update) {
        String id;
        boolean backToStart = Objects.nonNull(update.getMessage());
        if (backToStart) {
            id = update.getMessage().getChatId().toString();
        } else {
            id = update.getCallbackQuery().getFrom().getId().toString();
        }

        List<ButtonTemplate> buttonTemplateList = List.of(
                new ButtonTemplate("Ссылка(1)", "type,1"),
                new ButtonTemplate("Цитата(2)", "type,2"),
                new ButtonTemplate("Поговорка(3)", "type,3"),
                new ButtonTemplate("Анекдот(4)", "type,4")
        );

        messageService.sendMessageWithButtons(id, HELLO_STRING, buttonTemplateList, false);
        if (!backToStart) {
            messageService.dropButtonsFromMessage(update);
            messageService.editMessageText(update);
        }
    }
}

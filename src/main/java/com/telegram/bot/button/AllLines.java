package com.telegram.bot.button;

import com.telegram.bot.data.entity.DontSmokeInfo;
import com.telegram.bot.service.DontSmokeService;
import com.telegram.bot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component("allLines")
@RequiredArgsConstructor
public class AllLines implements Button {
    private final MessageService messageService;
    private final DontSmokeService dontSmokeService;

    @Override
    public void handle(Update update) {
        String id = update.getCallbackQuery().getFrom().getId().toString();
        int typeNumber = Integer.parseInt(update.getCallbackQuery().getData().split(",")[1]);

        List<DontSmokeInfo> dontSmokeInfoList = dontSmokeService.getAllLineByType(typeNumber);
        if (Objects.nonNull(dontSmokeInfoList)) {
            dontSmokeInfoList.forEach(dontSmokeInfo -> {
                String messageText =
                        String.valueOf(dontSmokeInfoList.indexOf(dontSmokeInfo)+1)+
                        " из " + String.valueOf(dontSmokeInfoList.size()) + ".\n \uD83D\uDCA1 "+
                        "Источник/описание: " + dontSmokeInfo.getANNOTATION()
                        + "\n \uD83D\uDC49 '" + dontSmokeInfo.getINFO() + "'"
                        + "\nТип: " + dontSmokeInfo.getTYPE()
                        + "\n\n";
                messageService.sendMessage(id, messageText, false);
            });

            messageService.dropButtonsFromMessage(update);
            messageService.editMessageText(update);
            messageService.sendButtonsMessage(String.valueOf(typeNumber), id);
        } else {
            messageService.sendMessage(id, "Записей такого типа нет)", false);

            messageService.dropButtonsFromMessage(update);
            messageService.sendButtonsMessage(String.valueOf(typeNumber), id);
        }
    }
}

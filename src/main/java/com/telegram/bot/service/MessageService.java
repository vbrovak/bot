package com.telegram.bot.service;

import com.telegram.bot.data.dto.ButtonTemplate;
import com.telegram.bot.data.entity.DontSmokeInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MessageService {
    private final TelegramLongPollingBot bot;
    private final DontSmokeService dontSmokeService;
    private final Map<Long, DontSmokeInfo> dontSmokeInfoMap = new HashMap<>();

    public void sendMessage(String id, String text, boolean disableNotification) {
        try {
            bot.execute(
                    SendMessage.builder()
                            .chatId(id)
                            .text(text)
                            .disableNotification(disableNotification)
                            .build()
            );
        }
        catch (TelegramApiException e) {
            log.info("{}, USER ID: {}", e.getMessage(), id);
        }
    }

    public void sendMessage(SendMessage msg) {
        try {
            bot.execute(msg);
        } catch (TelegramApiException e) {
            log.info(e.getMessage());
        }
    }

    public void sendMessageWithButtons(String id,
                                       String text,
                                       List<ButtonTemplate> buttonTemplateList,
                                       boolean disableNotification) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        for (ButtonTemplate buttonTemplate : buttonTemplateList) {
            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(buttonTemplate.getText());
            button.setCallbackData(buttonTemplate.getData());
            if (button.getText().equals("Добавить запись") || button.getText().equals("Назад")) {
                keyboard.add(List.of(button));
            } else {
                buttons.add(button);
            }
        }

        keyboard.add(buttons);
        inlineKeyboardMarkup.setKeyboard(keyboard);

        try {
            bot.execute(
                    SendMessage.builder()
                            .chatId(id)
                            .text(text)
                            .disableNotification(disableNotification)
                            .replyMarkup(inlineKeyboardMarkup)
                            .build()
            );
        }
        catch (TelegramApiException e) {
            log.info("{}, USER ID: {}", e.getMessage(), id);
        }

    }

    public static String printbotmodename(String typeNumber){
        String[] names = new String[]{"Ссылка", "Цитата", "Поговорка","Анекдот"};
        return names[Integer.valueOf(typeNumber)-1];
    }

    public void sendButtonsMessage(String typeNumber, String id) {
        String[] names = new String[]{"Ссылка", "Цитата", "Поговорка","Анекдот"};
        List<ButtonTemplate> buttonTemplateList = List.of(
                new ButtonTemplate("Одна запись", "singleLine," + typeNumber),
                new ButtonTemplate("Все записи", "allLines," + typeNumber),
                new ButtonTemplate("Резерв", "res," + typeNumber),
                new ButtonTemplate("Добавить запись", "addLine," + typeNumber),
                new ButtonTemplate("Назад", "backToStart")
        );


        sendMessageWithButtons(id, "Выбери кнопку для режима \uD83D\uDC49 "+
                printbotmodename(typeNumber), buttonTemplateList, false);
    }

    public void dropButtonsFromMessage(Update update) {
        try {
            Message message = update.getCallbackQuery().getMessage();
            EditMessageReplyMarkup msg = EditMessageReplyMarkup.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .build();

            bot.execute(msg);
        } catch (TelegramApiException e) {
            log.info("{}.", e.getMessage());
        }
    }

    public void editMessageText(Update update) {
        try {
            Message message = update.getCallbackQuery().getMessage();
            String data = update.getCallbackQuery().getData();

            data = data.replace("type,", "Тип ");
            data = data.replace("singleLine,", "Одна запись, тип: ");
            data = data.replace("allLines,", "Все записи, тип: ");
            data = data.replace("res,", "Резерв, тип: ");
            data = data.replace("addLine,", "Добавить запись, тип: ");

            if (data.split(",")[0].equals("back")) {
                data = data.replace("back,", "Назад, тип: ");
            } else {
                data = data.replace("backToStart", "Назад");

            }

            try
            {data = " " + printbotmodename(data.substring(data.length()-1,data.length())) +", " + data;
            } catch (NumberFormatException e) {
                data = " "+ data;
            }

            EditMessageText msg = EditMessageText.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text(message.getText() + "\n Эхо действий : ✅Выбрано:" + data)
                    .build();
            bot.execute(msg);
        } catch (TelegramApiException e) {
            log.info("{}.", e.getMessage());
        }
    }

    public void handleAddButton(Long chatId, int typeNumber) {
        // Создание новой сущности dontSmokeInfo и сохранение ее в dontSmokeInfoMap
        DontSmokeInfo dontSmokeInfo = new DontSmokeInfo();
        dontSmokeInfo.setTYPE(typeNumber);
        dontSmokeInfoMap.put(chatId, dontSmokeInfo);

        // Отправка сообщения "Введите краткое описание"
        SendMessage message = new SendMessage(chatId.toString(), "Введите Краткое описание");
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleDontSmokeDataInput(Update update) {
        // Получение сущности dontSmokeInfo из dontSmokeInfoMap
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        DontSmokeInfo dontSmokeInfo = dontSmokeInfoMap.get(chatId);
        int typeNumber = dontSmokeInfo.getTYPE();


        if (Objects.nonNull(dontSmokeInfo)) {
            if (Objects.isNull(dontSmokeInfo.getANNOTATION())) {
                // Установка значения краткого описания
                dontSmokeInfo.setANNOTATION(text);

                // Отправка сообщения "Введите полное описание"
                SendMessage message = new SendMessage(chatId.toString(), "Введите полное описание");
                try {
                    bot.execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            else if (Objects.isNull(dontSmokeInfo.getINFO())) {
                // Установка значения INFO
                dontSmokeInfo.setINFO(text);

                try {

                    dontSmokeService.saveDontSmokeInfo(dontSmokeInfo);
                    dontSmokeInfoMap.remove(chatId);
                    // Отправка сообщения "Сохранено"
                    SendMessage message = new SendMessage(chatId.toString(),
                            "Сохранено: \n" + "Краткое описание: " + dontSmokeInfo.getANNOTATION()
                                    + "\nИнформация: " + dontSmokeInfo.getINFO()
                                    + "\nНаименование типа: " + printbotmodename(String.valueOf(dontSmokeInfo.getTYPE()))
                                    +" (" + dontSmokeInfo.getTYPE()+")");
                    log.info("dontSmoke saved: [ {} ]", dontSmokeInfo);
                    try {
                        bot.execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    sendButtonsMessage(
                            String.valueOf(typeNumber),
                            String.valueOf(chatId)
                    );
                }
                catch (Exception e) {
                // Ошибка сохранения
                String errorMessageText = "Ошибка! Данные должны быть уникальными";
                SendMessage errorMessage = new SendMessage(chatId.toString(), errorMessageText);
                sendMessage(errorMessage);
            }
            }
        }
    }
}

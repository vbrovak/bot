package com.telegram.bot.button;

import org.telegram.telegrambots.meta.api.objects.Update;

// Интерфейс для управления кнопками
public interface Button {

    void handle(Update update);
}

package com.telegram.bot.action;

import org.telegram.telegrambots.meta.api.objects.Update;

// Интерфейс для управления командами
public interface Action {
    void handle(Update update);
}

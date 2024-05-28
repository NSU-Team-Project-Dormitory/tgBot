package org.tgbot.testtgbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.tgbot.testtgbot.entities.*;
import org.tgbot.testtgbot.model.*;
import org.tgbot.testtgbot.services.CampusService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CampusService campusService;

    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Запустить диалог"));
        listOfCommands.add(new BotCommand("/help", "Просмотр списка команд"));
        listOfCommands.add(new BotCommand("/stop", "Пересатать пользоваться ботом"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            System.out.println("Set command exception");
            log.error(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            System.out.println(message);
            log.info(message);
            String[] params = message.split(" ");
            Long chatId = update.getMessage().getChatId();

            switch (params[0]) {
                case "/start":
                    registerUser(update.getMessage());
                    sendHelpMessage(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/stop":
                    removeUser(chatId);
                    break;
                case "/help":
                    sendHelpMessage(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/rooms":
                    if (params.length != 2) {
                        sendFailedMessage(chatId);
                        break;
                    }
                    sendListAvailableRooms(chatId, params[1]);
                    break;
                case "/residents":
                    if (params.length != 3) {
                        sendFailedMessage(chatId);
                        break;
                    }
                    sendResidentsByDormAndRoomNumber(chatId, params[1], params[2]);
                    break;
                case "/dormitories":
                    sendDormitoryList(update.getMessage());
                    break;
                default:
                    sendMessage(chatId, "Неизвестная команда.\nИспользуй /help для просмотра списка команд");
            }
        }
    }

    private void sendResidentsByDormAndRoomNumber(Long chatId, String dormName, String roomName) {
        String answer = campusService.getResidentsByDormAndRoomNumber(dormName, roomName);
        sendMessage(chatId, answer);
    }


    private void sendFailedMessage(Long chatId) {
        sendMessage(chatId, "Неправильное использование команды. /help для просмотра информации");
    }

    private void sendListAvailableRooms(Long chatId, String dormNumber) {
        String answer = campusService.getAvailableRoomsByDormName(dormNumber);
        sendMessage(chatId, answer);
    }

    private void removeUser(Long chatId) {
        var user = userRepository.findById(chatId);
        user.ifPresent(value -> {
            userRepository.delete(value);
            log.info("User " + user.get() + "removed");
            sendMessage(chatId, "Информация о пользователе удалена");
        });
    }

    private void registerUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()) {
            Long chatId = message.getChatId();
            Chat chat = message.getChat();
            User user = new User();
            user.setChatId(chatId);
            user.setUsername(chat.getUserName());
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setRegisteredTime(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("Saved user" + user);
        }
    }

    private void sendDormitoryList(Message message) {
        List<Dormitory> dormitories = campusService.findAllDormitories();
        StringBuilder answer = new StringBuilder();
        for (Dormitory d : dormitories) {
            answer.append(d.getName());
            answer.append(", этажей: ");
            answer.append(d.getFloorCount());
            answer.append("\n");
        }
        sendMessage(message.getChatId(), answer.toString());
    }

    private void sendHelpMessage(Long chatId, String firstName) {
        String answer = "Привет, " + firstName + "!\n\n" +
                "Добро пожаловать в сервис общежитий!\n" +
                "Возможности:\n" +
                "/dormitories - показать список доступных общежитий\n" +
                "/rooms {номер общежития} - вывести список комнат со свободными местами в общежитии\n" +
                "/residents {номер общежития} {номер комнататы} - вывести список жителей комнаты" +
                "/stop - удалить все данные о себе у бота\n" +
                "/help - показать эту информацию снова";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Failed to send message");
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}

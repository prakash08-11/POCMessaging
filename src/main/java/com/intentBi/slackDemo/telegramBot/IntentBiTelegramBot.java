package com.intentBi.slackDemo.telegramBot;

import com.intentBi.slackDemo.common.IntenBiServices;
import com.intentBi.slackDemo.entity.request.ReportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class IntentBiTelegramBot extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(IntentBiTelegramBot.class);
    private static final IntenBiServices intenBiServices = new IntenBiServices();

    @Override // Bot username while creating the bot in #BotFather
    public String getBotUsername() {
        return "intentBi_bot";
    }

    @Override // BotFather will generate the token for your bot
    public String getBotToken() {
        return "5187362339:AAE1pRD7HlsH860GhqlL2C-Ym0fFVPT0-F8";
    }

    @Override //Any update is received here to this method
    public void onUpdateReceived(Update update) {

         List<String> listOfCommands= checkIfMessageEntitiesHasCommands(update.getMessage());
         List<String> queryString = checkIfListHasQueryCommand(listOfCommands,update);
        if (listOfCommands.isEmpty() || queryString.isEmpty()){
            String errorInvalidCommand = "Invalid input Input Details" + update.getMessage().getText();
            log.warn(errorInvalidCommand);
            SendMessage sendMessage = SendMessage.builder()
                    .text("Please use command /query to get the reports images from IntentBi below is the example " +
                            "\n Example query: \"/query data set details,query details\"")
                    .chatId(update.getMessage().getChatId().toString())
                    .build();
            try {
                this.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }else {
            if (queryString.size()!= 2){
                String errorInvalidInput = "Invalid input Input Details query and data set details" +update.getMessage().getText();
                log.warn(errorInvalidInput);
                SendMessage sendMessage = SendMessage.builder()
                        .text("invalid Query parameters please check and retry \n Example query: \"/query data set details,query details\"")
                        .chatId(update.getMessage().getChatId().toString())
                        .build();
                try {
                    this.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                ReportRequest request = ReportRequest.builder()
                        .dataSetName(queryString.get(0))
                        .queryDetails(queryString.get(1))
                        .uniqueId(update.getUpdateId().toString()+update.getMessage().getMessageId().toString()).build();

                byte[] bArray = intenBiServices.getImage(request);

                InputFile inputFile = new InputFile(new ByteArrayInputStream(bArray),"test.png");

                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(update.getMessage().getChatId().toString());
                sendPhoto.setPhoto(inputFile);
                sendPhoto.setCaption("Data Set#"+queryString.get(0)+" Query Details #"+ queryString.get(1));
                try {
                    this.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<String> checkIfListHasQueryCommand(List<String> listOfCommands, Update update) {
        if(listOfCommands.contains("/query")){
            String parameters= formatCommand(update.getMessage().getText());
            return intenBiServices.preprocessParameters(parameters);
        }else {
            return Collections.emptyList();
        }
    }

    private String formatCommand(String text) {
        text= text.replace("/query","");
        text = text.trim();
        return text;
    }

    private List<String> checkIfMessageEntitiesHasCommands(Message message) {
           if(Objects.nonNull(message.getEntities())&&!message.getEntities().isEmpty() ){
                return message.getEntities().stream().filter(messageEntity ->
                       messageEntity.getType().equals("bot_command")
               ).map(MessageEntity::getText
               ).collect(Collectors.toList());
           }
           return Collections.emptyList();
    }

}

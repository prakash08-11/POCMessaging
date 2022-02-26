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

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class IntentBiTelegramBot extends TelegramLongPollingBot {

    private static final String QUERY = "query";
    private static final String GET_DATA_SETS = "getdatasets";
    private static final String GET_COLUMNS = "getcolumns";
    private static final String EXAMPLE = "Please below command " +
            "\n/query to get the reports images from IntentBi below is the example " +
            "\n Example command : \"/query data set details,query details\"" +
            "\n/getdatasets to get data Sets from IntentBi below is the example" +
            "\n Example command: \"/getdatasets <Email Address>\"" +
            "\n /getcolumns to get data Sets from IntentBi below is the example" +
            "\n Example Command : \" /getcolumns <Data Set Name>";


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
        SendMessage sendMessage = SendMessage.builder()
                .text(EXAMPLE)
                .chatId(update.getMessage().getChatId().toString())
                .build();
         String command = checkIfMessageEntitiesHasCommands(update.getMessage());

        if(command.isEmpty()){
            String errorInvalidCommand = "Invalid Input Details\n" + update.getMessage().getText();
            log.warn(errorInvalidCommand);
            sendMessageToTelgram(sendMessage);
        } else if(!processTheCommand(command,update)){
            String errorInvalidCommand = "Invalid request\n" + update.getMessage().getText();
            log.warn(errorInvalidCommand);
            sendMessageToTelgram(sendMessage);
        }
    }

    private void sendMessageToTelgram(SendMessage sendMessage) {
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean processTheCommand(String command, Update update) {
        SendMessage sendMessage = SendMessage.builder()
                .text("")
                .chatId(update.getMessage().getChatId().toString())
                .build();
        String parameters = "";
        switch (command) {

            case QUERY:

                parameters = formatCommand(QUERY, update.getMessage().getText());
                List<String> processedParameters = intenBiServices.preprocessParameters(parameters);

                if(processedParameters.size()!= 2) {

                    String errorInvalidInput = "Invalid Input Details query and data set details" + update.getMessage().getText();
                    log.warn(errorInvalidInput);
                    sendMessage.setText("Invalid Query parameters please check and retry \n Example query: \"/query data set details,query details\"");
                    sendMessageToTelgram(sendMessage);

                } else {

                    ReportRequest request = ReportRequest.builder()
                            .dataSetName(processedParameters.get(0))
                            .queryDetails(processedParameters.get(1))
                            .uniqueId(update.getUpdateId().toString()+update.getMessage().getMessageId().toString()).build();

                    byte[] bArray = intenBiServices.getImage(request);

                    InputFile inputFile = new InputFile(new ByteArrayInputStream(bArray),"test.png");

                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(update.getMessage().getChatId().toString());
                    sendPhoto.setPhoto(inputFile);
                    sendPhoto.setCaption("Data Set#"+ processedParameters.get(0)+" Query Details #"+ processedParameters.get(1));
                    SendPhotoToTelegram(sendPhoto);

                }
                return true;

            case GET_DATA_SETS:

                parameters = formatCommand(GET_DATA_SETS, update.getMessage().getText());

                if(!parameters.isEmpty()) {

                    String dataSetNames = intenBiServices.getDatasetsAndDetails(parameters);

                    if(!dataSetNames.isEmpty()){
                        sendMessage.setText(dataSetNames);
                    } else {
                        sendMessage.setText("Invalid EmailAddress or no data sets available for the email address kindly check");
                    }

                } else {
                    sendMessage.setText("Invalid  parameters please check and retry \n" +
                            "\n Example command: \"/getdatasets <Email Address>\"");
                }

                sendMessageToTelgram(sendMessage);
                return true;
            case GET_COLUMNS:
                parameters = formatCommand(GET_COLUMNS, update.getMessage().getText());
                if(!parameters.isEmpty()) {
                    String columnNames = intenBiServices.getColumnsOfDataSets(parameters);
                    if (!columnNames.isEmpty()) {
                        sendMessage.setText(columnNames);
                    } else {
                        sendMessage.setText("Invalid EmailAddress or no data sets available for the email address kindly check");
                    }
                } else {
                    sendMessage.setText("Invalid  parameters please check and retry \n" +
                    "\n Example command: \"/getdatasets <Email Address>\"");
        }
                sendMessageToTelgram(sendMessage);
                return true;
            default:
                return false;
        }
    }

    private void SendPhotoToTelegram(SendPhoto sendPhoto) {
        try {
            this.execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String formatCommand(String replaceText,String text) {
        text= text.replace("/"+replaceText,"");
        text = text.trim();
        return text;
    }

    private String checkIfMessageEntitiesHasCommands(Message message) {
           if(Objects.nonNull(message.getEntities())&&!message.getEntities().isEmpty() ){
                return message.getEntities().stream().filter(messageEntity ->
                       messageEntity.getType().equals("bot_command")).map(MessageEntity::getText)
                        .collect(Collectors.toList())
                        .stream()
                        .findFirst().orElse("/")
                        .replace("/","");
           }
           return "";
    }

}

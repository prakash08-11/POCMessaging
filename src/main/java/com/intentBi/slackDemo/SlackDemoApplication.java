package com.intentBi.slackDemo;

import com.intentBi.slackDemo.telegramBot.IntentBiTelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class SlackDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlackDemoApplication.class, args);

		try {
			//Register the Telegram####
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot(new IntentBiTelegramBot());

		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}

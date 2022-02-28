# Slack and Telegram IntentBI Integration
## Slack Configurations
### Creations of Slack bot  

Follow the below steps to create slack bot and add the slack commands
- Create a slack workspace or in the existing workspace create a [Bot](https://api.slack.com/apps).
- Once bot is ready it has all the basic bot details which consists:
  - **Application ID** 
  - **Client ID**
  - **Client Secret** 
  - **Signing Secret**
  - **Verification Token** : This deprecated Verification Token can still be used to verify that requests come from Slack, but we strongly recommend using the above, more secure, signing secret instead.
- **Add features and functionality** : Incoming Webhooks, Slash Commands, Bots, Permissions. 
- Create Slash Commands with below details [Slash Documentations](https://api.slack.com/interactivity/slash-commands) 
  - Provide **Command** Example: "/query"
  - Request URL: Once Commands is triggered Slack sends post request to given request URL.
  - Short Descriptions
  - Usage Hint 

### Important Documentations 
- Building messages to send to slack bot, [messageBuilder](https://app.slack.com/block-kit-builder/T02U7GSSQEA)
- Webhooks [docs](https://api.slack.com/messaging/webhooks)
- Token Related [Info](https://api.slack.com/authentication/token-types)
- Authentication [Info](https://api.slack.com/authentication/basics)
- Web API methods [Info](https://api.slack.com/methods)

### Important notes
- Tokens are valid for 12 hours valid. [Token Rotation](https://api.slack.com/authentication/rotation)
  - (Post Method)https://slack.com/api/oauth.v2.access :: (parameters client id, client secret, grant type, refresh token)
  - Given token is taken for sending messages back to the Slack bot application.

## Telegram Configurations
- Create a telegram bot with the help of the [fatherBot](https://t.me/BotFather)
- [Documentation](https://core.telegram.org/bots)
- Once the bot is created it. Token and BotUserName are used in the code to link the code with the bot.

## WebexDocumentations 
- Documentation [Link](https://developer.webex.com/docs/platform-introduction)
- Webex SDK python websockets [link](https://github.com/fbradyirl/webex_bot)
- Sample Code [Link](https://github.com/0x2142/example-scripts/tree/master/simple-webex-chatbot)
- Added the ability to enable sending titles on join/quit [updated config.yml](https://github.com/ryderbelserion/ChatterBox/blob/75564fe08d576ca7c7d3f6d68498ed544f765363/plugin/src/main/resources/config.yml#L17)
  - This will not send the message in chat.
- Added /chatterbox motd
- Disconnect messages now show in chat.
- `output` in join-message/quit-message now supports being a String List, or a String
```yml
output: "your_message" 
```
or
```yml
output:
  - "line 1"
  - "line 2"
```
- Multi-locale support is setup internally, We need more translated files!
  - It would be much appreciated if people would offer translations for the [messages.yml](https://github.com/ryderbelserion/ChatterBox/blob/main/plugin/src/main/resources/messages.yml)
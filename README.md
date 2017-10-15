# Awww - A twitch bot fot cute pictures

The initial idea was to send out cute images on demand (!cutes), but over time some other things popped into mind, for example notifying if new tweets arrived on twitter or if the streamer (well itshafu in this case) tweeted about the stream it would notify via pushbullet.
The meat is still serving cute images.

What it does basically boils down to this:
 - Every day, it pulls new images from the current top of /r/aww
 - When a user issues `!cutes` it will dispense an image from the locally stored ones
 - Using `!cutes <imagelink>` trusted users can add images to the store
 - After receiving an image, if people don't like it, trusted users can use `!blacklist` to blacklist the last image.
 - If needed, admin users can user `!shutdown` to kill this bot
 - When itshafu sends out a tweet, it will post that tweet in the chat
 - When itshafu sends out a tweet that she went live, it will push a notification to pushbullet
 - Some random fun stuff.
 
This is probably not suitable to be used anywhere else, but I tried to make things configurable enough so it could theoretically be used by someone else. Just look at `src/main/resources/application.yml.example` to see what can be configured.

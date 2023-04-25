#  telegram-2-mastodon-receiver
This is an CLI tool that transports the history of the telegram channel to the mastodon account.

## Usage

1) Export your data from Telegram. How to do it you can check here: https://telegram.org/blog/export-and-more

2) Set up the config file:
```agsl
ACCESS_TOKEN=<Your access token>
BASE_URL=https://yourinstance.social
CHARACTER_LIMITATION=500
INTERVAL=60
PATH_TO_EXPORTED_DATA=Exported_Data_Dir/
```

How to get your ``ACCESS_TOKEN``:
- Open a new browser tab and login in to your account at your Mastodon instance.
- Click the gear icon (Preferences) icon to access your Mastodon preferences.
- Click Development in the left menu.
- Click the New Application button.
- Enter an Application Name of your choice.
- Uncheck all the Scopes check boxes, and then check only the following check boxes: write: statuses, write: media.

``BASE_URL`` This is your Mastodon instance. E.g.: https://mastodon.social

``CHARACTER_LIMITATION`` The default character limit is 500 characters on most instances.

``INTERVAL`` The value in seconds that sets the pause between each request. Zero is also valid. Many servers have sensitive settings for API requests by default. See more: https://docs.joinmastodon.org/api/rate-limits/

``PATH_TO_EXPORTED_DATA`` Path to directory with your exported Telegram data.

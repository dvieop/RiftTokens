# Token Plugin

## Overview

A fully configurable token plugin with an API.

## Placeholders
- `%tokens_balance%`: Displays the current token balance.

## Commands

### Player Commands
- `/tokens balance`
  - **Permission**: `tokens.balance`
  - **Description**: Check your current token balance.
  
- `/tokens top`
  - **Permission**: `tokens.top`
  - **Description**: Display the top players with the highest token balances.

### Admin Commands
- `/rtokens addtokens <player> <amount>`
  - **Permission**: `tokens.addtokens`
  - **Description**: Add a specific amount of tokens to a player's balance.

- `/rtokens removetokens <player> <amount>`
  - **Permission**: `tokens.removetokens`
  - **Description**: Remove a specific amount of tokens from a player's balance.

- `/rtokens settokens <player> <amount>`
  - **Permission**: `tokens.settokens`
  - **Description**: Set a player's token balance to a specific amount.

- `/rtokens reset <player>`
  - **Permission**: `tokens.reset`
  - **Description**: Reset a player's token balance to zero.

- `/rtokens resetall`
  - **Permission**: `tokens.resetall`
  - **Description**: Reset all players' token balances to zero.

- `/rtokens reload`
  - **Permission**: `tokens.reload`
  - **Description**: Reload the plugin configuration.

## API


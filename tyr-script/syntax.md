## Statement structure
`namespace:function_name(parameter1, parameter2)[output_variable_name]`  
| code name            | required?                            | description                                                                                                  |
| -------------------- | ------------------------------------ | ------------------------------------------------------------------------------------------------------------ |
| namespace            | no; defaults to `tyr:`, if not given | Used to specify a certain users namespace which is used to get their respective actions from the marketplace |
| function_name        | yes                                  |                                                                                                              |
| parameter            |                                      |                                                                                                              |
| output_variable_name | no                                   |                                                                                                              |

## Action structure  
```
ON trigger_function[event_object]

EXPOSE [variable_name],
    (default_value)[variable_name]

function()
...
```
Example actions can be found [here](./src/test/resources/scripts/).

## Available triggers
| name               | description                                                                 | has return value? | return value type                                                                                                                 |
| ------------------ | --------------------------------------------------------------------------- | ----------------- | --------------------------------------------------------------------------------------------------------------------------------- |
| `message_received` | called when an irc message is received in the context of the active channel | yes               | [PrivMsgMessage](../twitch-data-models/src/main/java/com/github/tyrbot/twitchdatamodels/irc/messages/channel/PrivMsgMessage.java) |

## Standard functions

| type        | name              | parameters                                                                                                | has return value | return value type | description                                                                                                                                                                                           |
| ----------- | ----------------- | --------------------------------------------------------------------------------------------------------- | ---------------- | ----------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| flow        | `if`              | condition: `boolean`,<br>fulfilled_statement: `function`,<br>(optional) unfulfilled_statement: `function` | no               | -                 | Checks the value of the condition; if the condition is `true`, the second parameter `fulfilled_statement` is executed.<br>Otherwise the thrid parameter `unfulfilled_parameter` is executed if given. |
| flow        | `sequence`        | statment1: `function`,<br>...<br>statementN: `function`                                                   | no               | -                 | Takes a list of functions as parameters, which are sequentially executed.                                                                                                                             |
|             |                   |                                                                                                           |                  |                   |                                                                                                                                                                                                       |
| util        | `equals`          |                                                                                                           |                  |                   |                                                                                                                                                                                                       |
| util        | `get_index`       |                                                                                                           |                  |                   |                                                                                                                                                                                                       |
| util        | `combine`         |                                                                                                           |                  |                   |                                                                                                                                                                                                       |
|             |                   |                                                                                                           |                  |                   |                                                                                                                                                                                                       |
| twitch      | `api_request`     |                                                                                                           |                  |                   |                                                                                                                                                                                                       |
| twitch      | `send_message`    |                                                                                                           |                  |                   |                                                                                                                                                                                                       |
|             |                   |                                                                                                           |                  |                   |                                                                                                                                                                                                       |
| date & time | `time_difference` |                                                                                                           |                  |                   |                                                                                                                                                                                                       |
| date & time | `now`             |                                                                                                           |                  |                   |                                                                                                                                                                                                       |
| date & time | `format_time`     |                                                                                                           |                  |                   |                                                                                                                                                                                                       |
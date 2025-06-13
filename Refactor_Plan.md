# Auto Command Refactor

> priorities

1. `management of file loading`
2. `AutoCommand storage`
3. `modelisation in-code > acmd class` - `migration`
4. `CLI`
5. `EDITOR`
6. `next features`

## migration

copy/rename the old data (.old)

create the migration file

create the new file

## management of file loading

make it clean.
make it robust.

## modelisation in-code

### acmd class

improve loading

being robust on loading the commands for executions

event based system

### CLI

factorize a maximum

rename parameters

### EDITOR

rename parameters

for now is ok

## AutoCommand storage

> definition -> plugins/autocommands/commands.yml

```yml
string:
 display_name: string
 start_delay: int
 period: int
 repetitions: int
 minecraft_commands: list<string>
 per_player_commands: list<string>
 message: string
 daily_execution_time: <hh<H or :>mm or hh.mm <pm or am>>
 triggers: list<string>
```

> runtime -> plugins/autocommands/run/commands_runtime.yml

```yml
string:
 active: bool
 running: bool
```

logs -> plugins/autocommands/run/commands_logs.csv

# next features

- dry run the commands on demand (print for the user running the test)
- auto add execution permissions for acmd
- check permission level needed per command per player
- conditions on acmd (number of players, time...)

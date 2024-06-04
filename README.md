# AutoCommands 🤖

A minecraft (**java**) plugin that lets you **run repetitive tasks** with **delay** and a lot more ! Configurable by UI or chat ! learn more with the **links 🔗** bellow ! 

Already **990 downloads ⏬**, **THANK YOU**  ! 

## Spigot 🔗
>Download the ressource via spigot page :
https://www.spigotmc.org/resources/acmd-%E2%8F%B0-%E2%8F%B3-autocommands-1-13-1-20-4.100090/

## bStat 🔗

>https://bstats.org/plugin/bukkit/ACMD/21737

## wiki 🔗

>https://github.com/AutoPluginsDev/Documentation/wiki/AutoCommands-%5BACMD%5D

## Future Refactoring

Use two config files, one to adresse template commands, and one to adresse state of the command 

> (why not using a DB for the state of the command ?)

### config file

register the command data that are not changing, like the commands registered, for the acmd, the delay etc...


#### Example

**commandTemplates.yml**
```yml

example2:
  active: false # CA TEJ
  TaskParameters:
    name: AcmdExample2
    cycle: 6000 
    delay: 200 
    repetition: -1
    commands:
      - say Dont forget to join our Discord !
    message: ''
    running: false  # CA TEJ
  DailySchedulerParameters:
    time: ''
```

**commandStates.yml**
```yml
1:
  template: example2
  running: false
  active: false
  executor: server
  
```

## release workflow

1. First go to environment "env" and change the variable to the targeted tag/version 

2. Merge develop on master

3. Go edit the release created by the action, add just the name and a description. The binaries are already there.







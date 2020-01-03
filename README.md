# FunQuiz

## Features
- Base trivia functions (asking questions, accepting answers, giving rewards)
- Open Trivia DB implementation (gets 50 random questions from OTDB each 25 questions asked)

## Commands
### Questions
- **/questions** -> Shows this help dialogue  
**[funquiz.questions.help]**

- **/questions list** -> Lists the names of all questions  
**[funquiz.questions.list]**

- **/questions info <name>** -> Displays information about the given question (question, answers, rewards)   
**[funquiz.questions.info]**
  
- **/questions ask [name]** -> Asks a random question if the name is not given, otherwise asks the given question   
**[funquiz.questions.ask]**

- **/questions reload** -> Reloads all questions and refreshes OTDB questions (if OTDB is enabled)   
**[funquiz.questions.reload]**

## TODO
- Playing sounds on question asked/answered/ended
- Interactive question adding (Low Priority)
- Updater (Low-ish priority)
- Translation (Low Priority)

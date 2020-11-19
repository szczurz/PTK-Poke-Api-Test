# PTK Poke Api Test
Application shows list of pokemons and display they details when pokemon selected

## Guidelines:
Min. SDK 21 - DONE\
Use Kotlin language - DONE\
Use Clean Architecture (Repository pattern) and MVI (e.g. Uniflow lib) - DONE\
Use JetPack: (ViewModel, Room, Navigation) - DONE\
Use Koin (DI) - DONE\
Use Retrofit2 and OkHttp3 - DONE\
Use Coroutines + Flow - DONE\
Use Moshi and Glide - DONE\
Create pagination with Jetpack’s Paging library - DONE\
Publish code in a github public repository - DONE

## Bonus
working offline - DONE\
unit tests - Nope

## Comments and information
With Paging Library it is hard to implement "true" MVI becouse of innacesible internal paging library classes\
List of all pokemons provides only name and url for detail (id could be extracted from url)\
Pokemon detail could be downloaded when name or id is known\
List position is saved on rotation, it is important when user is browsing very long list\
Current solution is depending on the order of pokemons on the list call, there is no sort parameter in api\
Current solution do not have any way of clearing/updating data after saving it on room

## Next things to do
Unit Testing\
Apply some theme/style current one is android studio default\
Improve pokemon types (currenty stores in one join string)\
Polish improve and extend UI especially types on list and better ui in pokemon detail page\
Look to limit number of items/updates in memory, default PagerConfig value is Int.Max\
Pokemon searching on the list
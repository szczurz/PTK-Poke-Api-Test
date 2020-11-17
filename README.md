# PTK Poke Api Test
Application shows list of pokemons and display they details when pokemon selected

## Guidelines:
Min. SDK 21 - DONE
Use Kotlin language - DONE
Use Clean Architecture (Repository pattern) and MVI (e.g. Uniflow lib) - DONE
Use JetPack: (ViewModel, Room, Navigation) - DONE
Use Koin (DI) - DONE
Use Retrofit2 and OkHttp3 - DONE
Use Coroutines + Flow - DONE
Use Moshi and Glide - DONE
Create pagination with Jetpack’s Paging library - DONE
Publish code in a github public repository - DONE

Bonus - working offline - DONE

## Comments and information
With Paging Library it is hard to implement "true" MVI and would require some "hacks" to use internal classes
List of all pokemons provides only name and url for detail (id could be extracted from url)
Pokemon detail could be downloaded when name or id is known
Image of pokemon in a list is hardcoded (working for around 95% pokemons) and should be changed to update after downloading detail
List position is saved on rotation, it is important when user is browsing very long list
Current solution is depending on the order of pokemons on the list call, there is no sort parameter
Current solution do not have any way of clearing/updating data after saving it on room

## Next things to do
Change hardcoded image load to loading details of pokemons visible on the screen and then downloading image and add some other information on list (type ?).
Searching and downloading list in the background until fully downloaded
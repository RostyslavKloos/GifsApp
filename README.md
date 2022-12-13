# GifsApp

API: https://developers.giphy.com </br>

Functionality of the first screen:
1. Display gif image in the list
2. Use paging loading 
3. Search image when keyboard is entered
4. Offline mode - caching the image to internal storage and caching urls of the images to the local data base
5. Possibility to delete image from cache. And that image should not be shown on next loading </br>

Functionality of another screen:
1. Full screen display selected GIF
2. Possibility to review other images with a horizontal swipe across the screen

Stack of technologies:
1. Retrofit (Netrowk)
2. Hilt (di)
3. Compose (ui)
4. Navigation Component (navigation)
5. Room (cache)
6. Paging-compose (pagination)

Project sctucture: 
MVVM</br>
(Since there's no difficult business logic, no "interactor/use cases in domain layer" were used)</br>
(Since paging library were used the main project structure became simplified a bit)

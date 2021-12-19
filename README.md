# physical-browser

**Physical browser** представляет собой Android приложение-браузер, предоставляющий web-приложениям доступ к контексту смартфона.

В приложении реализованы поисковая строка, кнопки _Next_ и _Refresh_ и компонент _WebView_. Кнопка _Next_ отображают страницу, введенную в поисковую строку. Кнопка _Refresh_ обновляет текущую страницу.


К _WebView_ привязывается _androidInterface_ - объект класса _JavascriptInterfaceImpl_, через который web-приложения могут взаимодействовать с физическим окружением устройства. _Javascript_ код web-приложения может вызывать функции androidInterface как напрямую, так и через предлагаемое API, предназначенное для облегчение web-разработки.
Ссылка на _physical-browser-api_: [https://github.com/Anna-Sl/physical-browser-api](https://github.com/Anna-Sl/physical-browser-api).


Скриншоты браузера:

<p align="center">
  <img src="./app/src/main/res/drawable/google_screen_2.jpg" width="20%">
  <img src="./app/src/main/res/drawable/google_screen.jpg" width="20%">
</p>

При первом запуске приложение запрашивает "разрешение на доступ к данным о расположении устройства".
**Важно!** Физическая функциональность работает только при одновременно включенном Wi-Fi и GPS.

Cайты, не использующие _physical-browser-api_, в Physical browser работают. Проверено на google.com, vk.com, youtube.com, wildberries.ru.

Для демонстрации использования _physical-browser-api_ создан сайт “Faculty CMC checker” (Ссылка: [https://github.com/Anna-Sl/cmc-checker](https://github.com/Anna-Sl/cmc-checker)). 

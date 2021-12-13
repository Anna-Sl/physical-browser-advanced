# physical-browser


Реализовано тестовое приложение, имитирующее работу браузера. 

**Цель приложения** – показать идею создания браузера, позволяющего веб-странице с помощью _javascript_ кода взаимодействовать c физическим окружением Android устройства.

**Задача приложения** – отобразить на HTML странице список доступных Wi-Fi сетей и сеть к которому подключено устройство. 

Приложение содержит компонент _webView_. В компонент загружается тестовая HTML страница, содержащая _javascipt_ код. 
В приложении реализован класс _JavaScriptInterface_. У компонента _WebView_ вызывается метод _addJavascriptInterface(…)_ ,
позволяющий связать интерфейс класса _JavaScriptInterface_ с _javascipt_ кодом HTML страницы.

`webView.addJavascriptInterface(new JavaScriptInterface(this, webView), "androidInterface");`
	
Теперь _javascript_ код страницы сможет вызывать методы объекта _JavaScriptInterface_.  
Методы вызываются через обращение к объекту _androidInterface_. Выглядит в _javascript_ коде вот так:

`var ssid = androidInterface.getWiFiSSID();`

И наоборот, через _WebView_ можно вызвать _javascript_ методы. 
Это сделано на примере получения доступных в данной точке Wi-Fi сетей. 
Так как сканирование доступных Wi-Fi сетей – долгий процесс, требующий вызова Android намерения `SCAN_RESULTS_AVAILABLE_ACTION`,
то необходимо чтобы JavaScriptInterface дождался ответа системы и передал результат _javascript_ коду. 

`webView.loadUrl("javascript:showAvailableWifis('" + ssids + "');")`

В итоге получаем, что веб-ресурс и объект _JavaScriptInterface_ могут свободно обмениваться информацией. 

В HTML коде созданы две кнопки: “Get current Wi-Fi name” и “Scan available Wi-fis”. 
По нажатию первой можно получить название текущей Wi-Fi сети (название показывается ввиде объявления _Toast_), 
по нажатию второй выводятся все доступные Wi-Fi сети.

   


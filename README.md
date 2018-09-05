# biznes-finder-parser

Przykładowe wywołanie

1. Pobierze pierwszych 10 stron<br/>
java -jar parser-spring-boot.jar "https://www.biznesfinder.pl/Wrocław;+dolnośląskie" 10

2. Pobierze wszystkie strony
java -jar parser-spring-boot.jar "https://www.biznesfinder.pl/Wrocław;+dolnośląskie"

3. Pobierze strony z danego zakresu
java -jar parser-spring-boot.jar "https://www.biznesfinder.pl/Wrocław;+dolnośląskie" 10 12


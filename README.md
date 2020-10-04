# biznes-finder-parser web crawler

Instructions:

1. Upewnij sie że masz zainstalowaną javę.
```
java -version
```
2. Run 
Wejdź na stronę www.biznesfinder.pl wpisz szukaną frazę, kliknij szukaj a następnie skopiuj adres www.
3. Odpal w konsoli program z parametrami:
- adres strony
- numer strony od której program ma rozpocząć pobieranie danych (parametr opcjonalny)
- numer strony na której program na zakończyć pobieranie danych (parametr opcjonalny)

Przykłady
java -jar parser-spring-boot.jar "https://www.biznesfinder.pl/wroc%C5%82aw%2C%20dolno%C5%9Bl%C4%85skie" 2 20
java -jar parser-spring-boot.jar "https://www.biznesfinder.pl/wroc%C5%82aw%2C%20dolno%C5%9Bl%C4%85skie"
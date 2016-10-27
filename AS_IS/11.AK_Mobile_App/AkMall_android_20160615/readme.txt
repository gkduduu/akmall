[개발환경]
Source encoding : UTF-8
Android Target sdk : android-8

[Production & Staging 전환]
src/com/ak/android/akmall/common/Const.java
의 IS_PRODUCTION 변수값 변경으로 전환함.
운영 : true
개발 : false 
로 각각 설정

[keystore]
소스 루트의 keystore 디레토리 참조
key: /keystore/akplaza.keystore
password: /keystore/keystore_key.txt


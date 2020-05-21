# N5 Terminal

text file scanner (every second)

## Project Usage

- 於application.yml內調整相關參數
    - n5.request.folder : 目標掃描路徑
    - n5.socket.address : n5 terminal的區域IP Address(``區域網路``必須與 ``執行環境``相同)
    
    
- 調整後build出jar檔並執行


- Request Format
    - 檔名不得包含```response```
    - extension name 需為 ```.txt```
    - 內容必須包含 ```orderNo``` , ```amount``` 兩個參數
    - 參數之間使用換行來區隔 
    - 鍵與值之間使用半形冒號區隔 ( : )
    - 以下為 請求內容範例:

````
   orderNo: ORD0000001 
   amount: 0.1
````
   
- txt檔案經掃瞄後, 檔名將使用```-response```後綴表示
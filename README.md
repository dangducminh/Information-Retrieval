# Information-Retrieval
## Người thực hiện :
- Đặng Đức Minh 
- Trường Đại học Khoa học tự nhiên - Đại học Quốc Gia Hà Nội.
- Khoa: Toán - Cơ - Tin học.
## Người hướng dẫn :
- TS : Lê Hồng Phương
## Hướng dẫn setup : 
- git clone https://github.com/dangducminh/Information-Retrieval.git
- Project gồm 2 phần : Front-end , Back-end
### Front-end :
- cd information-retrieval-fe
- npm install react-router-dom
- npm start
- ![alt text](https://user-images.githubusercontent.com/85719218/236600175-b1c12343-f5dc-40c7-b0c8-caa5b1ba263f.png)
- ![alt text](https://user-images.githubusercontent.com/85719218/236600178-e8a5d84d-2567-4089-a309-3522069a0970.png)
- Để build vocab , bạn hãy dán đường dẫn vào ô PATH (đối với foldẻ nằm trong project ta chỉ cần dùng đường dẫn tương đối)
- ![alt text](https://user-images.githubusercontent.com/85719218/236600177-4810c5b1-0c1c-4947-97e2-da5d81ce7826.png)
### Back-end :
- Mở project bằng Intellij (recommend)
- Chắc chắn rằng SDK ver 11 trở lên 
- Vào Help\Change Memory Setings 
- set Maximum Heap Size 4096 MB trở lên để có thể build được toàn bộ 3000 doc trong folder data, nếu như muốn build từ điển
- Back-end sau khi khởi động thành công sẽ có log như sau : 
- - ![alt text](https://user-images.githubusercontent.com/85719218/236600180-2e91f597-a1f3-4073-a745-ea534c1d26c2.png)

# Hackathon De01 - E-commerce AI Chatbot (QuickMart)

Dự án này là hệ thống Backend cho Website Thương mại điện tử tích hợp AI Chatbot. Hệ thống kết hợp RAG (Truy xuất thế hệ tăng cường) để cung cấp thông tin về cửa hàng và Function Calling (AI Tools) để thực hiện nghiệp vụ đặt hàng trực tiếp qua Chatbot.

## Yêu cầu môi trường
- Java 17+ (hoặc 21 tuỳ cấu hình của bạn)
- PostgreSQL đang chạy ở `localhost:5432` với Database tên `mcp-db`.
- **Bắt buộc**: Cơ sở dữ liệu `mcp-db` cần được bật pgvector. Bạn hãy mở SQL Console cho database `mcp-db` và chạy lệnh sau (1 lần):
  ```sql
  CREATE EXTENSION IF NOT EXISTS vector;
  ```

## Hướng dẫn cấu hình
1. Sửa cấu hình cơ sở dữ liệu nếu cần trong `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/mcp-db
       username: root
       password: 12345678
   ```
2. Dự án đã thiết lập API Key của Google Gemini trong file `application.yml`. Nếu bạn muốn dùng API Key của bạn, hãy cập nhật `spring.ai.openai.api-key`.
3. Đảm bảo file PDF thông tin cửa hàng nằm đúng vị trí: `d:/PTIT/AI_Integrated_In_Action/hackthon/De01/materials/QuickMart_Store_Info.pdf` (được cấu hình cứng trong `StoreInfoService`).

## Khởi chạy ứng dụng
Chạy ứng dụng bằng lệnh Gradle:
```bash
./gradlew bootRun
```
Khi khởi động, ứng dụng sẽ tự động nạp dữ liệu mẫu từ `src/main/resources/data.sql` (các bảng `categories`, `products`, `customers`, v.v.).

## Danh sách API endpoints đã hoàn thành
1. **[Phần I] GET /api/products**: Trả về danh sách sản phẩm kèm tên danh mục.
2. **[Phần I] GET /api/products/{id}**: Trả về thông tin chi tiết của 1 sản phẩm.
3. **[Phần II] POST /api/v1/admin/ingest-store-info**: Đọc tài liệu PDF, thực hiện chunking và embedding để lưu vào PGVector. Bắt buộc gọi 1 lần trước khi dùng Chatbot để có dữ liệu RAG.
4. **[Phần IV] POST /api/v1/chat**: Endpoint giao tiếp Chatbot. Body JSON:
   ```json
   {
       "sessionId": "12345",
       "message": "Shop ở Đà Nẵng có địa chỉ nào không?"
   }
   ```

## Kịch bản demo hội thoại đầu-cuối (Phần V)
Sau khi ứng dụng chạy và ingest xong PDF (`POST /api/v1/admin/ingest-store-info`), bạn có thể dùng Postman/cURL gọi `POST /api/v1/chat`:

**Lượt 1: Hỏi thông tin RAG**
- **User**: "Shop ở Hà Nội có địa chỉ nào không?"
- **AI**: (Trả lời đúng địa chỉ dựa trên thông tin lấy từ PGVector).

**Lượt 2: Gọi Tool tìm sản phẩm**
- **User**: "Còn AirPods Pro 2 không, giá bao nhiêu?"
- **AI**: (Gọi tool `checkStock` & `searchProducts`, trả lời: "Sản phẩm AirPods Pro 2 có giá X, còn tồn kho Y").

**Lượt 3: Xác nhận đơn hàng**
- **User**: "Cho tôi đặt 2 cái, tên Nguyễn Văn A, số điện thoại 0909111222, giao về 20 Lý Thường Kiệt, Hà Nội"
- **AI**: (Tổng hợp thông tin, hiển thị tổng tiền và hỏi xác nhận: "Bạn có đồng ý tạo đơn không?").

**Lượt 4: Tạo đơn**
- **User**: "Xác nhận"
- **AI**: (Gọi tool `createOrder`, thông báo mã đơn hàng).

Bạn có thể kiểm tra DB, sẽ thấy đơn hàng trong bảng `orders`, chi tiết ở `order_items` và tồn kho `products` bị giảm đi 2.

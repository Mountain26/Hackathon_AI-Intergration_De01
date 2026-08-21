package org.example.hackathon_de01.tools;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_de01.dto.CreateOrderRequest;
import org.example.hackathon_de01.dto.CreateOrderResponse;
import org.example.hackathon_de01.dto.OrderItemRequest;
import org.example.hackathon_de01.dto.ProductLookupResponse;
import org.example.hackathon_de01.service.OrderService;
import org.example.hackathon_de01.repository.ProductRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreTools {

    private final ProductRepository productRepository;
    private final VectorStore vectorStore;
    private final OrderService orderService;

    @Tool(description = "Tra cứu sản phẩm theo tên hoặc từ khóa. Dùng khi người dùng hỏi rõ tên sản phẩm, model, hoặc keyword cụ thể.", returnDirect = true)
    public List<ProductLookupResponse> searchProductByName(@ToolParam(description = "Tu khoa hoac ten san pham") String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(product -> new ProductLookupResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock(),
                        product.getCategory().getName()
                ))
                .toList();
    }

    @Tool(description = "Tra cứu danh sách sản phẩm theo danh mục. Dùng khi người dùng hỏi loại sản phẩm như laptop, điện thoại, phụ kiện.", returnDirect = true)
    public List<ProductLookupResponse> searchProductByCategory(@ToolParam(description = "Ten danh muc san pham") String categoryName) {
        return productRepository.findByCategory_NameIgnoreCase(categoryName).stream()
                .map(product -> new ProductLookupResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock(),
                        product.getCategory().getName()
                ))
                .toList();
    }

    @Tool(description = "Tra cứu thông tin cửa hàng như địa chỉ, giờ mở cửa, đổi trả, bảo hành, thanh toán bằng semantic search trên tài liệu PDF.", returnDirect = true)
    public String getStoreInfo(@ToolParam(description = "Cau hoi cua khach hang ve cua hang") String question) {
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder().query(question).topK(3).build());
        return documents.stream()
                .map(Document::getText)
                .filter(text -> text != null && !text.isBlank())
                .reduce((left, right) -> left + "\n\n" + right)
                .orElse("Không tìm thấy thông tin phù hợp.");
    }

    @Tool(description = "Tạo đơn hàng mới, kiểm tra tồn kho, tạo customer nếu chưa có, trừ stock và trả về thông tin đơn hàng.", returnDirect = true)
    public CreateOrderResponse createOrder(
            @ToolParam(description = "So dien thoai khach hang") String customerPhone,
            @ToolParam(description = "Ten khach hang") String customerName,
            @ToolParam(description = "Dia chi giao hang") String address,
            @ToolParam(description = "Danh sach san pham va so luong") List<OrderItemRequest> items) {
        return orderService.createOrder(new CreateOrderRequest(customerPhone, customerName, address, items));
    }
}

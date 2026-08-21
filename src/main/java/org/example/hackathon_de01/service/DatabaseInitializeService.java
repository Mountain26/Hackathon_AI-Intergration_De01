package org.example.hackathon_de01.service;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_de01.model.entity.Category;
import org.example.hackathon_de01.model.entity.Customer;
import org.example.hackathon_de01.model.entity.Product;
import org.example.hackathon_de01.repository.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseInitializeService {
    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @jakarta.annotation.PostConstruct
    public void initializeDatabase() {
        if (categoryRepository.count() == 0) {
            Category c1 = new Category(null, "Điện thoại", "Điện thoại thông minh các hãng");
            Category c2 = new Category(null, "Laptop", "Laptop văn phòng và gaming");
            Category c3 = new Category(null, "Phụ kiện", "Tai nghe, sạc, ốp lưng, cáp");
            Category c4 = new Category(null, "Đồng hồ thông minh", "Smartwatch theo dõi sức khoẻ");
            categoryRepository.saveAll(java.util.List.of(c1, c2, c3, c4));
        }

        if (productRepository.count() == 0) {
            java.util.List<Category> cats = categoryRepository.findAll();
            Category c1 = cats.stream().filter(c -> c.getName().equals("Điện thoại")).findFirst().orElse(null);
            Category c2 = cats.stream().filter(c -> c.getName().equals("Laptop")).findFirst().orElse(null);
            Category c3 = cats.stream().filter(c -> c.getName().equals("Phụ kiện")).findFirst().orElse(null);
            Category c4 = cats.stream().filter(c -> c.getName().equals("Đồng hồ thông minh")).findFirst().orElse(null);

            productRepository.saveAll(java.util.List.of(
                new Product(null, "iPhone 15", "Điện thoại Apple iPhone 15 128GB", new java.math.BigDecimal("21990000"), 25, null, c1),
                new Product(null, "Samsung Galaxy S24", "Điện thoại Samsung Galaxy S24 256GB", new java.math.BigDecimal("19990000"), 30, null, c1),
                new Product(null, "Xiaomi Redmi Note 13", "Điện thoại Xiaomi tầm trung 128GB", new java.math.BigDecimal("5490000"), 50, null, c1),
                new Product(null, "MacBook Air M2", "Laptop Apple MacBook Air chip M2 8GB/256GB", new java.math.BigDecimal("26990000"), 15, null, c2),
                new Product(null, "Dell Inspiron 15", "Laptop văn phòng Dell Inspiron 15 i5/8GB/512GB", new java.math.BigDecimal("15990000"), 20, null, c2),
                new Product(null, "Asus TUF Gaming A15", "Laptop gaming Asus TUF A15 Ryzen 7/16GB/RTX3050", new java.math.BigDecimal("22990000"), 10, null, c2),
                new Product(null, "AirPods Pro 2", "Tai nghe không dây chống ồn Apple AirPods Pro thế hệ 2", new java.math.BigDecimal("5990000"), 40, null, c3),
                new Product(null, "Sạc nhanh 20W Anker", "Củ sạc nhanh Anker 20W chuẩn USB-C", new java.math.BigDecimal("390000"), 100, null, c3),
                new Product(null, "Ốp lưng silicon iPhone 15", "Ốp lưng chống sốc silicon cho iPhone 15", new java.math.BigDecimal("150000"), 200, null, c3),
                new Product(null, "Cáp sạc USB-C to USB-C 1m", "Cáp sạc nhanh 60W dài 1m", new java.math.BigDecimal("99000"), 150, null, c3),
                new Product(null, "Apple Watch Series 9", "Đồng hồ thông minh Apple Watch Series 9 GPS 41mm", new java.math.BigDecimal("9990000"), 18, null, c4),
                new Product(null, "Xiaomi Smart Band 8", "Vòng đeo tay thông minh Xiaomi Smart Band 8", new java.math.BigDecimal("590000"), 60, null, c4)
            ));
        }

        if (customerRepository.count() == 0) {
            customerRepository.saveAll(java.util.List.of(
                new Customer(null, "Nguyễn Văn An", "0901234567", "an.nguyen@example.com", "12 Nguyễn Trãi, Q.1, TP.HCM"),
                new Customer(null, "Trần Thị Bích", "0912345678", "bich.tran@example.com", "45 Lê Lợi, Q.Hải Châu, Đà Nẵng"),
                new Customer(null, "Lê Hoàng Nam", "0987654321", "nam.le@example.com", "78 Trần Duy Hưng, Cầu Giấy, Hà Nội")
            ));
        }
    }

}

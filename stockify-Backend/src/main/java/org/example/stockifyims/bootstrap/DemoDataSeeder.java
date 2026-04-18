//package org.example.stockifyims.bootstrap;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.stockifyims.entity.ProductVo;
//import org.example.stockifyims.entity.PurchaseVo;
//import org.example.stockifyims.entity.SalesVo;
//import org.example.stockifyims.entity.UserVo;
//import org.example.stockifyims.entity.WarehouseVo;
//import org.example.stockifyims.repository.product.ProductRepository;
//import org.example.stockifyims.repository.purchase.PurchaseRepository;
//import org.example.stockifyims.repository.sales.SalesRepository;
//import org.example.stockifyims.repository.user.UserRepository;
//import org.example.stockifyims.repository.warehouse.WarehouseRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Loads demo users and inventory when the DB has no products yet.
// * Idempotent: skips inventory if any product row exists; only creates missing demo users.
// */
//@Slf4j
//@Component
//@Order(100)
//@RequiredArgsConstructor
//public class DemoDataSeeder implements CommandLineRunner {
//
//    @Value("${stockify.seed-demo-data:false}")
//    private boolean seedDemoData;
//
//    private final UserRepository userRepository;
//    private final WarehouseRepository warehouseRepository;
//    private final ProductRepository productRepository;
//    private final PurchaseRepository purchaseRepository;
//    private final SalesRepository salesRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    @Transactional
//    public void run(String... args) {
//        if (!seedDemoData) {
//            return;
//        }
//        seedUsersIfMissing();
//        if (productRepository.count() > 0) {
//            log.info("Demo seed skipped: product table already has data.");
//            return;
//        }
//        seedInventory();
//        log.info("Demo data loaded. Login: admin / Admin@123 (admin) | clerk / User@123 (user)");
//    }
//
//    private void seedUsersIfMissing() {
//        if (userRepository.findByUsername("admin").isEmpty()) {
//            UserVo admin = new UserVo();
//            admin.setUsername("admin");
//            admin.setFullName("System Administrator");
//            admin.setEmail("admin@stockify.local");
//            admin.setPassword(passwordEncoder.encode("Admin@123"));
//            admin.setRole("ROLE_ADMIN");
//            userRepository.save(admin);
//            log.info("Created demo user: admin");
//        }
//        if (userRepository.findByUsername("clerk").isEmpty()) {
//            UserVo clerk = new UserVo();
//            clerk.setUsername("clerk");
//            clerk.setFullName("Inventory Clerk");
//            clerk.setEmail("clerk@stockify.local");
//            clerk.setPassword(passwordEncoder.encode("User@123"));
//            clerk.setRole("ROLE_USER");
//            userRepository.save(clerk);
//            log.info("Created demo user: clerk");
//        }
//    }
//
//    private void seedInventory() {
//        WarehouseVo w1 = warehouse("North Hub", "WH-N-01");
//        WarehouseVo w2 = warehouse("South Depot", "WH-S-02");
//        WarehouseVo w3 = warehouse("Central DC", "WH-C-03");
//        warehouseRepository.save(w1);
//        warehouseRepository.save(w2);
//        warehouseRepository.save(w3);
//
//        // Descriptions start with a "category" token for dashboard charts
//        ProductVo p1 = product("Wireless Mouse", "Electronics compact ergonomic mouse", 29.99, 120, w1);
//        ProductVo p2 = product("USB-C Cable", "Electronics 2m fast-charge cable", 12.50, 200, w1);
//        ProductVo p3 = product("Notebook A5", "Stationery ruled 200 pages", 4.25, 80, w2);
//        ProductVo p4 = product("Ballpoint Pens (10)", "Stationery blue ink multipack", 6.00, 150, w2);
//        ProductVo p5 = product("Rice 5kg", "Groceries basmati long grain", 18.99, 45, w3);
//        ProductVo p6 = product("Cooking Oil 1L", "Groceries sunflower refined", 8.50, 60, w3);
//        ProductVo p7 = product("Desk Lamp LED", "Furniture adjustable warm white", 42.00, 35, w1);
//        ProductVo p8 = product("Office Chair", "Furniture mesh back ergonomic", 189.00, 12, w2);
//        ProductVo p9 = product("AA Batteries (8)", "Electronics alkaline long life", 9.99, 90, w1);
//        ProductVo p10 = product("Stapler Heavy Duty", "Stationery metal 40 sheet", 11.49, 55, w2);
//
//        productRepository.save(p1);
//        productRepository.save(p2);
//        productRepository.save(p3);
//        productRepository.save(p4);
//        productRepository.save(p5);
//        productRepository.save(p6);
//        productRepository.save(p7);
//        productRepository.save(p8);
//        productRepository.save(p9);
//        productRepository.save(p10);
//
//        // Purchases increase on-hand stock (mirror API behavior)
//        recordPurchase(p1, 15, p1.getProductPrice());
//        recordPurchase(p5, 20, p5.getProductPrice());
//        recordPurchase(p8, 3, p8.getProductPrice());
//
//        // Sales decrease stock
//        recordSale(p1, 8, p1.getProductPrice());
//        recordSale(p3, 25, p3.getProductPrice());
//        recordSale(p6, 12, p6.getProductPrice());
//    }
//
//    private WarehouseVo warehouse(String name, String code) {
//        WarehouseVo w = new WarehouseVo();
//        w.setWarehouseName(name);
//        w.setWarehouseCode(code);
//        w.setDeleted(false);
//        return w;
//    }
//
//    private ProductVo product(String name, String description, double price, int qty, WarehouseVo w) {
//        ProductVo p = new ProductVo();
//        p.setProductName(name);
//        p.setProductDescription(description);
//        p.setProductPrice(price);
//        p.setQuantity(qty);
//        p.setDeleted(false);
//        p.setWarehouse(w);
//        return p;
//    }
//
//    private void recordPurchase(ProductVo product, double qty, double unitPrice) {
//        PurchaseVo purchase = new PurchaseVo();
//        purchase.setProduct(product);
//        purchase.setQuantity(qty);
//        purchase.setPrice(unitPrice);
//        purchase.setTotal(unitPrice * qty);
//        purchase.setDeleted(false);
//        purchaseRepository.save(purchase);
//        product.setQuantity((int) (product.getQuantity() + qty));
//        productRepository.save(product);
//    }
//
//    private void recordSale(ProductVo product, double qty, double unitPrice) {
//        SalesVo sale = new SalesVo();
//        sale.setProduct(product);
//        sale.setQuantity(qty);
//        sale.setPrice(unitPrice);
//        sale.setTotal(unitPrice * qty);
//        sale.setDeleted(false);
//        salesRepository.save(sale);
//        product.setQuantity((int) (product.getQuantity() - qty));
//        productRepository.save(product);
//    }
//}

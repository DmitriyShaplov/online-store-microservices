package ru.shaplovdv.product.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.common.model.exception.ResponseCodeException;
import ru.shaplovdv.product.model.Product;
import ru.shaplovdv.product.model.ProductParam;
import ru.shaplovdv.product.model.persistence.ProductEntity;
import ru.shaplovdv.product.repository.ProductRepository;
import ru.shaplovdv.product.service.ProductService;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final JdbcTemplate jdbcTemplate;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(JdbcTemplate jdbcTemplate,
                              ProductRepository productRepository,
                              ModelMapper modelMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @SneakyThrows
    @Transactional
    public void uploadProductsByFile(InputStream inputStream) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            CsvToBean<Product> csvToBean = new CsvToBeanBuilder<Product>(reader)
                    .withType(Product.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<Product> products = csvToBean.parse();
            jdbcTemplate.batchUpdate("""
                                INSERT INTO products (id, name, price, quantity) VALUES (?, ?, ?, ?)
                                ON CONFLICT (name) DO UPDATE
                                SET quantity = products.quantity + ?,
                                    price = ?,
                                    update_date = now()
                            """,
                    products,
                    100,
                    ((ps, product) -> {
                        ps.setObject(1, product.getId());
                        ps.setString(2, product.getName());
                        ps.setBigDecimal(3, product.getPrice());
                        ps.setInt(4, product.getQuantity());
                        ps.setInt(5, product.getQuantity());
                        ps.setBigDecimal(6, product.getPrice());
                    }));
        }
    }

    @Override
    public Product uploadProduct(Product product) {
        return jdbcTemplate.queryForObject("""
                            INSERT INTO products (id, name, price, quantity) VALUES (?, ?, ?, ?)
                                        ON CONFLICT (id) DO UPDATE
                                        SET quantity = products.quantity + ?,
                                            price = ?,
                                            update_date = now()
                                            RETURNING *
                        """, (rs, rowNum) -> new Product()
                        .setId((UUID) rs.getObject("id"))
                        .setPrice(rs.getBigDecimal("price"))
                        .setName(rs.getString("name"))
                        .setQuantity(rs.getInt("quantity"))
                        .setCreateDate(((Timestamp) rs.getObject("create_date")).toInstant().atOffset(ZoneOffset.UTC))
                        .setUpdateDate(((Timestamp) rs.getObject("update_date")).toInstant().atOffset(ZoneOffset.UTC)),
                product.getId(), product.getName(), product.getPrice(), product.getQuantity(),
                product.getQuantity(), product.getPrice());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProductsPage(Pageable pageable) {
        return modelMapper.map(productRepository.findAll(pageable), new TypeToken<Page<Product>>() {
        }.getType());
    }

    @Override
    public List<Product> getProductList(Sort sort) {
        return modelMapper.map(productRepository.findAll(sort), new TypeToken<List<Product>>() {
        }.getType());
    }

    @Override
    public Product getProduct(UUID uuid) {
        return productRepository.findById(uuid)
                .map(product -> modelMapper.map(product, Product.class))
                .orElse(null);
    }

    @Override
    public List<Product> searchProducts(ProductParam param) {
        List<ProductEntity> productEntities = productRepository.findAllById(param.getIdList());
        if (param.getIdList().size() != productEntities.size()) {
            List<UUID> uuids = new ArrayList<>(param.getIdList());
            uuids.removeAll(productEntities.stream().map(ProductEntity::getId)
                    .collect(Collectors.toSet()));
            throw new ResponseCodeException("No product with uuids: " + uuids, HttpStatus.BAD_REQUEST);
        }
        return modelMapper.map(productEntities, new TypeToken<List<Product>>() {
        }.getType());
    }
}

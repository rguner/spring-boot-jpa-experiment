package com.guner.service;

import com.guner.entity.Product;
import com.guner.repository.ProductRepository;
import jakarta.persistence.PessimisticLockException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    @Transactional
    public Product createProductTransactional() {
        Product product = new Product();
        product.setDescription("This is an example with runtime exception but caught.");
        product.setPrice(10);
        product.setTitle("First Product");
        return productRepository.save(product);
    }

    public Product createProductWithParam(Product product) {
        return productRepository.save(product);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProductById(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        return optionalProduct.get();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Product product) {
        Product existingProduct = productRepository.findById(product.getId()).get();
        existingProduct.setTitle(existingProduct.getTitle() + " updated");
        existingProduct.setPrice(existingProduct.getPrice()) ;
        existingProduct.setDescription(existingProduct.getDescription() + " updated");
        Product updatedProduct = productRepository.save(existingProduct);
        return updatedProduct;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Product updateProductWithLock(Product product) {
        Product existingProduct = productRepository.findByIdWithLocked(product.getId()).get();
        existingProduct.setTitle(existingProduct.getTitle() + " updated with lock");
        existingProduct.setPrice(existingProduct.getPrice());
        existingProduct.setDescription(existingProduct.getDescription() + " updated with lock");
        Product updatedProduct = productRepository.save(existingProduct); // transactional oldugu içib save demeye gerek yok, farketmez, fieldlara set edersen update olur
        return updatedProduct;
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW, rollbackOn = Exception.class)
    public Product updateProductWithLock2(Product product) {
        Product existingProduct = productRepository.findByIdWithLocked(product.getId()).get();
        existingProduct.setTitle(existingProduct.getTitle() + " updated with lock2");
        existingProduct.setPrice(existingProduct.getPrice());
        existingProduct.setDescription(existingProduct.getDescription() + " updated with lock2");
        Product updatedProduct = productRepository.save(existingProduct); // transactional oldugu içib save demeye gerek yok, farketmez, fieldlara set edersen update olur
        if ( 1==1) {
            throw new RuntimeException("Runtime Exception During Transaction...");
        }
        return updatedProduct;
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW, dontRollbackOn = Exception.class)
    public Product updateProductWithLock3(Product product) {
        Product existingProduct = productRepository.findByIdWithLocked(product.getId()).get();
        existingProduct.setTitle(existingProduct.getTitle() + " updated with lock3");
        existingProduct.setPrice(existingProduct.getPrice());
        existingProduct.setDescription(existingProduct.getDescription() + " updated with lock3");
        Product updatedProduct = productRepository.save(existingProduct); // transactional oldugu içib save demeye gerek yok, farketmez, fieldlara set edersen update olur
        if ( 1==1) {
            throw new RuntimeException("Runtime Exception During Transaction...");
        }
        return updatedProduct;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Product updateProductWithLockAndNoWait(Product product) {

        try {
            Product existingProduct = productRepository.findByIdWithLockedAndNoWait(product.getId()).get();
            existingProduct.setTitle(existingProduct.getTitle() + " updated with lock and nowait");
            existingProduct.setPrice(existingProduct.getPrice());
            existingProduct.setDescription(existingProduct.getDescription() + " updated with lock nowait");
            Product updatedProduct = productRepository.save(existingProduct); // transactional oldugu içib save demeye gerek yok, farketmez, fieldlara set edersen update olur
            return updatedProduct;
        } catch(PessimisticLockingFailureException pessimisticLockingFailureException) {
            throw new RuntimeException("Pessimistic Lock Exception During Transaction...");
        }

    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public Product getProductByTitleAndPrice(String title, int price) {
        return productRepository.findByTitleAndPrice(title, price).orElseThrow(() ->
                new RuntimeException("Not found getProductByTitleAndPrice with title and price = " + title + " " + price));
    }
}

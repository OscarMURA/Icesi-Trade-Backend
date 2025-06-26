package com.trade.icesi_trade.Service.Impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trade.icesi_trade.Service.Interface.ProductService;
import com.trade.icesi_trade.model.Product;
import com.trade.icesi_trade.repository.ProductRepository;
import com.trade.icesi_trade.repository.SaleRepository;
import com.trade.icesi_trade.repository.FavoriteProductRepository;
import com.trade.icesi_trade.repository.ReviewRepository;
import com.trade.icesi_trade.repository.ImageProductRepository;
import com.trade.icesi_trade.Service.blob.AzureBlobService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private FavoriteProductRepository favoriteProductRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ImageProductRepository imageProductRepository;

    @Autowired
    private AzureBlobService azureBlobService;

    /**
     * Creates a new product and saves it to the repository.
     *
     * @param product The product to be created. Must not be null and must have a
     *                non-empty title.
     * @return The saved product instance.
     * @throws IllegalArgumentException If the product is null or if the product's
     *                                  title is null or empty.
     */
    @Override
    public Product createProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        if (product.getTitle() == null || product.getTitle().isEmpty()) {
            throw new IllegalArgumentException("El producto debe tener un título.");
        }

        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    /**
     * Updates an existing product with the provided details.
     * If the image URL has changed, this method will delete the old images from
     * Azure Blob Storage.
     *
     * @param id      The ID of the product to be updated. Must not be null.
     * @param product The product object containing the updated details. Must not be
     *                null.
     * @return The updated product after saving it to the repository.
     * @throws IllegalArgumentException If the provided ID or product is null.
     * @throws NoSuchElementException   If no product is found with the given ID.
     */
    @Override
    @Transactional
    public Product updateProduct(Long id, Product product) {
        if (id == null || product == null) {
            throw new IllegalArgumentException("El ID del producto y los datos a actualizar no pueden ser nulos.");
        }

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado con el ID: " + id));

        // Verificar si las imágenes han cambiado
        String oldImageUrl = existingProduct.getImageUrl();
        String newImageUrl = product.getImageUrl();

        // Solo eliminar de Azure las imágenes que fueron removidas
        List<String> removedImages = getRemovedImageUrls(oldImageUrl, newImageUrl);
        for (String url : removedImages) {
            azureBlobService.deleteImageByUrl(url);
        }

        existingProduct.setTitle(product.getTitle());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setLocation(product.getLocation());
        existingProduct.setStatus(product.getStatus());
        existingProduct.setUpdatedAt(LocalDateTime.now());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setImageUrl(product.getImageUrl());

        return productRepository.save(existingProduct);
    }

    /**
     * Obtiene la lista de URLs de imágenes que estaban en oldImageUrl pero no en
     * newImageUrl.
     */
    private List<String> getRemovedImageUrls(String oldImageUrl, String newImageUrl) {
        List<String> oldList = oldImageUrl == null || oldImageUrl.isBlank() ? List.of()
                : List.of(oldImageUrl.split(","));
        List<String> newList = newImageUrl == null || newImageUrl.isBlank() ? List.of()
                : List.of(newImageUrl.split(","));
        return oldList.stream()
                .map(String::trim)
                .filter(url -> !url.isEmpty() && !newList.contains(url.trim()))
                .toList();
    }

    /**
     * Deletes a product by its ID.
     * This method also deletes associated images from Azure Blob Storage
     * and removes all related records (sales, favorites, reviews, images).
     *
     * @param id the ID of the product to be deleted; must not be null.
     * @return {@code true} if the product was successfully deleted, {@code false}
     *         if the product does not exist.
     * @throws IllegalArgumentException if the provided ID is null.
     */
    @Override
    @Transactional
    public boolean deleteProduct(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo.");
        }

        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return false;
        }

        // Eliminar imágenes de Azure Blob Storage
        deleteProductImagesFromAzure(product);

        // Eliminar registros relacionados
        saleRepository.deleteByProduct_Id(id);
        favoriteProductRepository.deleteByProduct_Id(id);
        reviewRepository.deleteByProduct_Id(id);
        imageProductRepository.deleteByProduct_Id(id);

        // Finalmente eliminar el producto
        productRepository.deleteById(id);
        return true;
    }

    /**
     * Deletes all images associated with a product from Azure Blob Storage.
     * This method handles both single images and multiple images separated by
     * commas.
     *
     * @param product the product whose images should be deleted
     */
    private void deleteProductImagesFromAzure(Product product) {
        if (product.getImageUrl() == null || product.getImageUrl().isEmpty()) {
            return;
        }

        try {
            // Dividir las URLs si hay múltiples imágenes (separadas por comas)
            String[] imageUrls = product.getImageUrl().split(",");

            for (String imageUrl : imageUrls) {
                String trimmedUrl = imageUrl.trim();
                if (!trimmedUrl.isEmpty()) {
                    azureBlobService.deleteImageByUrl(trimmedUrl);
                }
            }
        } catch (Exception e) {
            // Log the error but don't fail the deletion process
            // The product deletion should continue even if image deletion fails
            System.err
                    .println("Error deleting images from Azure for product " + product.getId() + ": " + e.getMessage());
        }
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the unique identifier of the product to retrieve; must not be null.
     * @return the product associated with the given ID.
     * @throws IllegalArgumentException if the provided ID is null.
     * @throws NoSuchElementException   if no product is found with the given ID.
     */
    @Override
    public Product getProductById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo.");
        }
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado con el ID: " + id));
    }

    /**
     * Retrieves a list of all products from the repository.
     *
     * @return a list containing all products.
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a paginated list of all products.
     *
     * @param pageable the pagination and sorting information
     * @return a page containing the products
     */
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> getProductsBySellerId(Long idSeller) {
        if (idSeller == null) {
            throw new IllegalArgumentException("El ID del vendedor no puede ser nulo.");
        }
        return productRepository.findBySeller_Id(idSeller);
    }

    @Override
    public Product markProductAsSold(Long idProduct) {
        if (idProduct == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo.");
        }

        Product product = productRepository.findById(idProduct)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado con el ID: " + idProduct));
        product.setIsSold(true);
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAvailableProductsExcludingSeller(Long sellerId) {
        if (sellerId == null) {
            throw new IllegalArgumentException("El ID del vendedor no puede ser nulo.");
        }
        return productRepository.findAvailableProductsExcludingSeller(sellerId);
    }
}
